package com.ap.ezviz.pub.biz

import android.text.TextUtils
import com.ap.ezviz.pub.ap.APWiredConfigInfo
import com.ap.ezviz.pub.ap.ApWifiConfigInfo
import com.ap.ezviz.pub.ap.ApErrorCode
import com.ap.ezviz.pub.http.APHttpClient
import com.ap.ezviz.pub.http.bean.ApWifiConfigReq
import com.ap.ezviz.pub.http.bean.ApWifiConfigResp
import com.ap.ezviz.pub.http.bean.ApAccessPointListResp
import com.ap.ezviz.pub.utils.ApConfigUtil
import com.ap.ezviz.pub.utils.JsonUtils
import com.ap.ezviz.pub.utils.LogUtil
import okhttp3.Response
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 配置wifi业务
 */
class WiFiSettingBiz {
    companion object{
        const val TAG = "WiFiSettingBiz"

        fun create(): WiFiSettingBiz{
            return WiFiSettingBiz()
        }
    }

    fun configWifi(apWifiConfigInfo: ApWifiConfigInfo, callback: OnConfigWifiCallback?) {
        if (TextUtils.isEmpty(apWifiConfigInfo.wifiBSSID)){
            callback?.onFailed(ApErrorCode.ERROR_WIFI_SSID_EMPTY, "The wifi SSID can not be empty")
            return
        }
    /*    if (TextUtils.isEmpty(apWifiConfigInfo.wifiPwd)){
            callback?.onFailed(ApErrorCode.ERROR_WIFI_VERCODE_ERROR, "The WIFIPWD can not be empty")
            return
        }*/

        val observable = Observable.fromCallable {
            return@fromCallable APHttpClient.getWifiList(apWifiConfigInfo.ipPort!!)
        }.map {
            if(it == null) {
                throw Exception("has no response")
            }

            val content = it.body()?.string()
            if(content.isNullOrBlank()){
                throw Exception("the response's content is empty")
            }else{
                return@map JsonUtils.formJson(content,
                    ApAccessPointListResp::class.java)
            }
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<ApAccessPointListResp>() {
                override fun onNext(t: ApAccessPointListResp?) {
                    if (t?.SecurityInfo != null && t.statusCode == 1) {
                        configWifi(apWifiConfigInfo, t.SecurityInfo, callback)
                    } else {
                        onError(Exception("get device wifi error"))
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    callback?.onFailed(ApErrorCode.ERROR_WIFI_DEVICE_ERROR, "device is not in AP mode: ${e?.message}")
                }

            })
    }
    /**
     * 配置网络
     */
    private fun configWifi(apWifiConfigInfo: ApWifiConfigInfo, securityInfo: ApAccessPointListResp.SecurityInfo, callback: OnConfigWifiCallback?) {
        val observable = Observable.create(Observable.OnSubscribe<ApWifiConfigResp> { subscriber ->
            val any = putWifi(apWifiConfigInfo, securityInfo)
            if (any == null) {
                subscriber.onError(null)
            } else if (any is Exception) {
                subscriber.onError(any)
            } else if (any is Response) {
                val content = any.body()?.string()
                LogUtil.d(TAG, "configWifi() Response:$content")
                val resp = JsonUtils.formJson(content, ApWifiConfigResp::class.java)
                if (resp != null) {
                    subscriber.onNext(resp)
                } else {
                    subscriber.onError(null)
                }
            }
            subscriber.onCompleted()
        })
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<ApWifiConfigResp>() {
                override fun onNext(t: ApWifiConfigResp?) {
                    if (t?.statusCode == 1) {
                        callback?.onSuccess(apWifiConfigInfo)
                    } else if (t?.statusCode != null) {
                        if (t.statusCode == 4 && t.errorCode != 0x4000800B) {
                            callback?.onFailed(ApErrorCode.ERROR_WIFI_VERCODE_ERROR, "The verifyCode is error")
                        } else {
                            callback?.onFailed(ApErrorCode.ERROR_WIFI_SEND_ERROR, "send wifi ssid and password failed. statusCode: ${t.statusCode} , errorCode:${t.errorCode}")
                        }
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    if (e!=null){
                        LogUtil.e(TAG, "configWifi() error response", e)
                    } else {
                        LogUtil.e(TAG, "configWifi() error response")
                    }
                    callback?.onFailed(ApErrorCode.ERROR_WIFI_SEND_ERROR, "send wifi ssid and password failed. error message: ${e?.message}")
                }
            })
    }
    private fun putWifi(apWifiConfigInfo: ApWifiConfigInfo, securityInfo: ApAccessPointListResp.SecurityInfo): Any? {
        LogUtil.d(TAG, "apWifiConfigInfo=$apWifiConfigInfo , securityInfo=$securityInfo")
        val verifyCode = apWifiConfigInfo.verifyCode
        val req = ApWifiConfigReq()
        val info = ApConfigUtil.getAuthorizationAndPassword(verifyCode, securityInfo.salt, apWifiConfigInfo.wifiBSSID!!, apWifiConfigInfo.wifiPwd?:"", securityInfo.iterations, securityInfo.challenge)
            ?: return Exception()

        req.authorization = info.authorization
        req.WifiInfo = ApWifiConfigReq.WifiInfo()
        req.WifiInfo.ssid = apWifiConfigInfo.wifiBSSID
        req.WifiInfo.password = info.password

        return APHttpClient.setWifi(apWifiConfigInfo.ipPort!!, JsonUtils.toJson(req), info)
    }


    /**
     * 设备wifi配置
     */
    interface OnConfigWifiCallback{
        fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo)
        fun onFailed(code: Int, msg: String)
    }

}