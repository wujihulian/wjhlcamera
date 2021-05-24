package com.ap.ezviz.pub

import android.text.TextUtils
import com.ap.ezviz.pub.ap.APWiredConfigInfo
import com.ap.ezviz.pub.ap.ApWifiConfigInfo
import com.ap.ezviz.pub.ap.ApErrorCode
import com.ap.ezviz.pub.biz.ActivateBiz
import com.ap.ezviz.pub.biz.WiFiSettingBiz
import com.ap.ezviz.pub.biz.WiredSettingBiz
import com.ap.ezviz.pub.utils.LogUtil

object YsApManager {
    const val TAG = "YsApManager"
    private val activateBiz = ActivateBiz.create(false)
    private val wifiSettingBiz = WiFiSettingBiz.create()
    private val wiredSettingBiz = WiredSettingBiz.create()
    /**
     * 执行萤石激活流程
     */
    fun activateWifi(apWifiConfigInfo: ApWifiConfigInfo, callback: ApActivateCallback?){
        callback?.onStartSearch()

        if (TextUtils.isEmpty(apWifiConfigInfo.verifyCode)){
            callback?.onFailed(ApErrorCode.ERROR_WIFI_VERCODE_EMPTY, "The verifyCode can not be empty",null)
            return
        }

        activateBiz.getActivateState(apWifiConfigInfo, object :ActivateBiz.OnGetActivateCallback{
            override fun onIpEmpty() {
                callback?.onFailed(ApErrorCode.ACTIVATE_IP_EMPTY, "Fixed ip can not be empty", null)
            }

            override fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo) {
                startActivate(apWifiConfigInfo, callback)
            }

            override fun onTimeOut() {
                callback?.onFailed(ApErrorCode.ACTIVATE_TIMEOUT, "Search Device Timeout", null)
            }
        })
    }

    /**
     * 执行萤石激活流程(有线方式)
     */
    fun activateWired(apWiredConfigInfo: APWiredConfigInfo, callback: ApActivateCallback?){
        callback?.onStartSearch()

        if (TextUtils.isEmpty(apWiredConfigInfo.verifyCode)){
            callback?.onFailed(ApErrorCode.ERROR_WIFI_VERCODE_EMPTY, "The verifyCode can not be empty",null)
            return
        }

        activateBiz.getActivateState_Wired(apWiredConfigInfo, object :ActivateBiz.OnGetActivateCallback_Wired{
            override fun onIpEmpty() {
                callback?.onFailed(ApErrorCode.ACTIVATE_IP_EMPTY, "Fixed ip can not be empty", null)
            }

            override fun onSuccess(apWiredConfigInfo: APWiredConfigInfo) {
                startActivate(apWiredConfigInfo, callback)
            }

            override fun onTimeOut() {
                callback?.onFailed(ApErrorCode.ACTIVATE_TIMEOUT, "Search Device Timeout", null)
            }
        })
    }

    /**
     * 开始执行激活
     */
    private fun startActivate(apWifiConfigInfo: ApWifiConfigInfo, callback: ApActivateCallback?){
        callback?.onStartActivate()
        // 萤石设备是默认"Hik验证码"
        if (TextUtils.isEmpty(apWifiConfigInfo.activatePwd)){
            apWifiConfigInfo.activatePwd = "Hik${apWifiConfigInfo.verifyCode}"
        }

        if (apWifiConfigInfo.isActivated){
            LogUtil.d(TAG, "已经激活，不需要在执行激活过程，直接进入下一步")
            startConfigWifi(apWifiConfigInfo, callback)
            return
        }
        activateBiz.activateDevice(apWifiConfigInfo, apWifiConfigInfo.activatePwd!!, object: ActivateBiz.OnActivateCallback{
            override fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo, password: String) {
                startConfigWifi(apWifiConfigInfo, callback)
            }

            override fun onFailed(errorCode: Int, e: Throwable?, apWifiConfigInfo: ApWifiConfigInfo, password: String) {
                callback?.onFailed(errorCode, "The device is failed to activate" ,e)
            }
        })
    }

    /**
     * 开始执行激活(有线)
     */
    private fun startActivate(apWiredConfigInfo: APWiredConfigInfo, callback: ApActivateCallback?){
        callback?.onStartActivate()
        // 萤石设备是默认"Hik验证码"
        if (TextUtils.isEmpty(apWiredConfigInfo.activatePwd)){
            apWiredConfigInfo.activatePwd = "Hik${apWiredConfigInfo.verifyCode}"
        }

        if (apWiredConfigInfo.isActivated){
            LogUtil.d(TAG, "已经激活，不需要在执行激活过程，直接进入下一步")
            startConfigWired(apWiredConfigInfo, callback)
            return
        }
        activateBiz.activateDevice_Wired(apWiredConfigInfo, apWiredConfigInfo.activatePwd!!, object: ActivateBiz.OnActivateCallback_Wired{
            override fun onSuccess(apWiredConfigInfo: APWiredConfigInfo, password: String) {
                startConfigWired(apWiredConfigInfo, callback)
            }

            override fun onFailed(errorCode: Int, e: Throwable?, apWiredConfigInfo: APWiredConfigInfo, password: String) {
                callback?.onFailed(errorCode, "The device is failed to activate" ,e)
            }
        })
    }

    fun startConfigWifi(apWifiConfigInfo: ApWifiConfigInfo, callback: ApActivateCallback?){
        callback?.onStartConfigWifi()
        wifiSettingBiz.configWifi(apWifiConfigInfo, object : WiFiSettingBiz.OnConfigWifiCallback{
            override fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo) {
                callback?.onSuccess()
            }

            override fun onFailed(code: Int, msg: String) {
                callback?.onFailed(code, msg, null)
            }

        })
    }

    fun startConfigWired(apWiredConfigInfo: APWiredConfigInfo, callback: ApActivateCallback?){
        callback?.onStartConfigWifi()
        wiredSettingBiz.configWired(apWiredConfigInfo, object : WiredSettingBiz.OnConfigWiredCallback{
            override fun onSuccess(apWiredConfigInfo: APWiredConfigInfo) {
                callback?.onSuccess()
            }

            override fun onFailed(code: Int, msg: String) {
                callback?.onFailed(code, msg, null)
            }

        })
    }

    interface ApActivateCallback{
        fun onStartSearch()
        fun onStartActivate()
        fun onStartConfigWifi()
        fun onSuccess()
        fun onFailed(code:Int, msg:String, exception: Throwable?)
    }


}