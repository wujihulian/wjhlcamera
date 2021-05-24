package com.ap.ezviz.pub.biz

import android.text.TextUtils
import com.ap.ezviz.pub.ap.APWiredConfigInfo
import com.ap.ezviz.pub.ap.ApErrorCode
import com.ap.ezviz.pub.http.APHttpClient
import com.ap.ezviz.pub.http.bean.APWiredConfigReq
import com.ap.ezviz.pub.http.bean.APWiredConfigResp
import com.ap.ezviz.pub.http.bean.ApAccessPointListResp
import com.ap.ezviz.pub.utils.ApConfigUtil
import com.ap.ezviz.pub.utils.JsonUtils
import com.ap.ezviz.pub.utils.LogUtil
import okhttp3.Response
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class WiredSettingBiz {
    companion object{
        const val TAG = "WiredSettingBiz"

        fun create(): WiredSettingBiz{
            return WiredSettingBiz()
        }
    }

    fun configWired(apWireConfigInfo: APWiredConfigInfo, callback: WiredSettingBiz.OnConfigWiredCallback?) {
        val observable = Observable.fromCallable {
            return@fromCallable APHttpClient.getWifiList(apWireConfigInfo.ipPort!!)
        }.map {
            if(it == null) {
                throw Exception("has no response")
            }

            val content = it.body()?.string()
            if(content.isNullOrBlank()){
                throw Exception("the response's content is empty")
            }else{
                return@map JsonUtils.formJson(content, ApAccessPointListResp::class.java)
            }
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<ApAccessPointListResp>() {
                override fun onNext(t: ApAccessPointListResp?) {
                    if (t?.SecurityInfo != null && t.statusCode == 1) {
                        configWired(apWireConfigInfo, t.SecurityInfo, callback)
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
    private fun configWired(apWireConfigInfo: APWiredConfigInfo, securityInfo: ApAccessPointListResp.SecurityInfo, callback: WiredSettingBiz.OnConfigWiredCallback?) {
        val observable = Observable.create(Observable.OnSubscribe<APWiredConfigResp> { subscriber ->
            val any = putWired(apWireConfigInfo, securityInfo)
            if (any == null) {
                subscriber.onError(null)
            } else if (any is Exception) {
                subscriber.onError(any)
            } else if (any is Response) {
                val content = any.body()?.string()
                LogUtil.d(WiredSettingBiz.TAG, "configWired() Response:$content")
                val resp = JsonUtils.formJson(content, APWiredConfigResp::class.java)
                if (resp != null) {
                    subscriber.onNext(resp)
                } else {
                    subscriber.onError(null)
                }
            }
            subscriber.onCompleted()
        })
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<APWiredConfigResp>() {
                override fun onNext(t: APWiredConfigResp?) {
                    if (t?.statusCode == 1) {
                        callback?.onSuccess(apWireConfigInfo)
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
                        LogUtil.e(WiredSettingBiz.TAG, "configWired() error response", e)
                    } else {
                        LogUtil.e(WiredSettingBiz.TAG, "configWired() error response")
                    }
                    callback?.onFailed(ApErrorCode.ERROR_WIFI_SEND_ERROR, "send wifi ssid and password failed. error message: ${e?.message}")
                }
            })
    }
    private fun putWired(apWireConfigInfo: APWiredConfigInfo, securityInfo: ApAccessPointListResp.SecurityInfo): Any? {
        LogUtil.d(WiredSettingBiz.TAG, "apWireConfigInfo=$apWireConfigInfo , securityInfo=$securityInfo")

        //打包有线配置信息
        val verifyCode = apWireConfigInfo.verifyCode
        val req = APWiredConfigReq()
        req.WiredNetwork = APWiredConfigReq.WiredInfo()
        req.WiredNetwork.IPV4 = APWiredConfigReq.IPV4()
        req.WiredNetwork.IPV4.DNS = APWiredConfigReq.DNS()
        req.WiredNetwork.IPV4.ipAddress = APWiredConfigReq.ipAddress()
        req.WiredNetwork.IPV4.ipAddress.AddressList = ArrayList<APWiredConfigReq.AddressList>()
        var apIpv4Info = APWiredConfigReq.AddressList()
        apIpv4Info.ipV4Address = apWireConfigInfo.IP
        apIpv4Info.defaultGateway = apWireConfigInfo.DefaultGateway
        apIpv4Info.subnetMask = apWireConfigInfo.SubNetMask
        req.WiredNetwork.IPV4.ipAddress.AddressList.add(apIpv4Info)
        req.WiredNetwork.IPV4.ipAddress.type = apWireConfigInfo.Dhcp
        req.WiredNetwork.IPV4.DNS.enabled = apWireConfigInfo.bDNS
        req.WiredNetwork.IPV4.DNS.primary = apWireConfigInfo.DNS_Primary
        req.WiredNetwork.IPV4.DNS.secondary = apWireConfigInfo.DNS_Secondary

        val info = ApConfigUtil.getWiredAuthorization(verifyCode, securityInfo.salt, "\"WiredNetwork\": "+JsonUtils.toJson(req.WiredNetwork), securityInfo.iterations, securityInfo.challenge)
            ?: return Exception()

        req.authorization = info.authorization

        return APHttpClient.setWiredNetwork(apWireConfigInfo.ipPort!!, JsonUtils.toJson(req), info)
    }


    /**
     * 设备有线配置配置
     */
    interface OnConfigWiredCallback{
        fun onSuccess(apWireConfigInfo: APWiredConfigInfo)
        fun onFailed(code: Int, msg: String)
    }
}