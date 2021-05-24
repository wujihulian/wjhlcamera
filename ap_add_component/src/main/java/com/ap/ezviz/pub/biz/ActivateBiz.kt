package com.ap.ezviz.pub.biz

import com.ap.ezviz.pub.ap.APWiredConfigInfo
import com.hikvision.ezviz.lib.encypt.Base64
import com.hikvision.ezviz.lib.lang.ByteArrayUtil
import com.ap.ezviz.pub.ap.ApWifiConfigInfo
import com.ap.ezviz.pub.ap.ApErrorCode
import com.ap.ezviz.pub.encrypt.AESUtils
import com.ap.ezviz.pub.encrypt.RSAUtils
import com.ap.ezviz.pub.http.APHttpClient
import com.ap.ezviz.pub.utils.IsapiSearchHelper
import com.ap.ezviz.pub.utils.IsapiSearchResultListener
import com.ap.ezviz.pub.utils.LogUtil
import com.ap.ezviz.pub.utils.XmlUtils
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 激活业务
 * @param isNeedCheckServer 是否需要检查萤石服务
 */
class ActivateBiz(private val isNeedCheckServer:Boolean) {
    companion object{
        const val TAG = "ActivateBiz"

        fun create(isNeedCheckServer:Boolean): ActivateBiz{
            return ActivateBiz(isNeedCheckServer)
        }
    }

    fun getActivateState(apWifiConfigInfo:ApWifiConfigInfo, callback: OnGetActivateCallback?){
        if (apWifiConfigInfo.fixedIP == null){
            callback?.onIpEmpty()
            return
        }
        apWifiConfigInfo.fixedIP!!.getIpPorts().forEach {
            LogUtil.d(TAG, "兼容：${it.ip}:${it.port}")
        }
        LogUtil.d(TAG, "apWifiConfigInfo=$apWifiConfigInfo")
        IsapiSearchHelper.create().getActivateState(apWifiConfigInfo.fixedIP!!, isNeedCheckServer, object : IsapiSearchResultListener {
            override fun retry() {
                callback?.retry()
            }

            override fun isCancel(): Boolean {
                return callback?.isCancel() == true
            }

            override fun onSearchSuccess(result: IsapiSearchHelper.ResultBean) {
                apWifiConfigInfo.ipPort = result.fixedIp
                apWifiConfigInfo.isActivated = result.isActivated == true
                apWifiConfigInfo.enableHCPlatform = result.enableHCPlatform == true
                apWifiConfigInfo.isOpenHCPlatform = result.isOpenHCPlatform == true
                callback?.onSuccess(apWifiConfigInfo)
            }

            override fun onSearchTimeOut() {
                callback?.onTimeOut()
            }
        })
    }

    /**
     * 激活业务
     * @param isNeedCheckServer 是否需要检查萤石服务
     */
    fun getActivateState_Wired(apWiredConfigInfo: APWiredConfigInfo, callback: OnGetActivateCallback_Wired?){
        if (apWiredConfigInfo.fixedIP == null){
            callback?.onIpEmpty()
            return
        }
        apWiredConfigInfo.fixedIP!!.getIpPorts().forEach {
            LogUtil.d(TAG, "兼容：${it.ip}:${it.port}")
        }
        LogUtil.d(TAG, "apWiredConfigInfo=$apWiredConfigInfo")
        IsapiSearchHelper.create().getActivateState(apWiredConfigInfo.fixedIP!!, isNeedCheckServer, object : IsapiSearchResultListener {
            override fun retry() {
                callback?.retry()
            }

            override fun isCancel(): Boolean {
                return callback?.isCancel() == true
            }

            override fun onSearchSuccess(result: IsapiSearchHelper.ResultBean) {
                apWiredConfigInfo.ipPort = result.fixedIp
                apWiredConfigInfo.isActivated = result.isActivated == true
                apWiredConfigInfo.enableHCPlatform = result.enableHCPlatform == true
                apWiredConfigInfo.isOpenHCPlatform = result.isOpenHCPlatform == true
                callback?.onSuccess(apWiredConfigInfo)
            }

            override fun onSearchTimeOut() {
                callback?.onTimeOut()
            }
        })
    }

    fun activateDevice(apWifiConfigInfo:ApWifiConfigInfo, password: String, callback: OnActivateCallback?) {
        LogUtil.d(TAG, "apWifiConfigInfo=$apWifiConfigInfo , password=$password")
        // 1. 生成 RSA 秘钥对
        // 2. 获取挑战串及设备随机码（用于设备加密）
        // 3. 组装数据
        // 4. 激活

        //尝试激活次数
        val atomicInteger = AtomicInteger(3)

        val observable = Observable.fromCallable {

            val rsaKeyPair = RSAUtils.genKeyPair()
            val publicKey = RSAUtils.getPublicKey(rsaKeyPair)
            val publicKeyBytes = RSAUtils.getPublicKeyBytes(rsaKeyPair)
            val privateKey = RSAUtils.getPrivateKey(rsaKeyPair)
            val publicKeyBase64 = Base64.encode(ByteArrayUtil.byte2Hex(publicKeyBytes).toByteArray())

            LogUtil.d(TAG, "privateKey:$privateKey")
            LogUtil.d(TAG, "publicKey:$publicKey")
            LogUtil.d(TAG, "publicKeyBase64:$publicKeyBase64")
            LogUtil.d(TAG, "-----获取挑战串-----")
            val responseState =
                APHttpClient.getActivateChallenge(apWifiConfigInfo.ipPort!!, publicKeyBase64)
            val contentState = responseState?.body()?.string()
            val key = XmlUtils.getStringValue(contentState!!, "key")
            val keyString = String(Base64.decode(key))
            LogUtil.d(TAG, "RSA 解密前的挑战串:$keyString")
            val randomBytes = RSAUtils.decryptByPrivateKey(ByteArrayUtil.hex2Byte(keyString), privateKey)

            LogUtil.d(TAG, "-----开始激活-----")
            LogUtil.d(TAG, "输入密码：$password")
            val passwordEncrypt = getEncryptPwd(randomBytes, password)

            LogUtil.d(TAG, "passwordEncrypt:$passwordEncrypt")

            val response = APHttpClient.activate(apWifiConfigInfo.ipPort!!, passwordEncrypt)
            val content = response?.body()?.string()

            val errorCodeStr = XmlUtils.getStringValue(content!!, "subStatusCode")

            return@fromCallable when {
                "ok" == errorCodeStr?.toLowerCase() -> ApErrorCode.ACTIVATE_SUCCESS
                "riskPassword" == errorCodeStr -> ApErrorCode.ACTIVATE_FAIL_RISK_PASSWORD
                else -> ApErrorCode.ACTIVATE_FAIL
            }
        }.retryWhen{
            it.flatMap { error->
                if (atomicInteger.decrementAndGet() > 0){
                    Observable.timer(2, TimeUnit.SECONDS)
                } else {
                    Observable.error(error)
                }
            }
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :Subscriber<Int>(){
            override fun onNext(code: Int) {
                if (code == ApErrorCode.ACTIVATE_SUCCESS){
                    callback?.onSuccess(apWifiConfigInfo, password)
                } else {
                    callback?.onFailed(code, null, apWifiConfigInfo, password)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                callback?.onFailed(ApErrorCode.ACTIVATE_FAIL, e, apWifiConfigInfo, password)
            }
        })
    }

    fun activateDevice_Wired(apWiredConfigInfo:APWiredConfigInfo, password: String, callback: OnActivateCallback_Wired?) {
        LogUtil.d(TAG, "apWiredConfigInfo=$apWiredConfigInfo , password=$password")
        // 1. 生成 RSA 秘钥对
        // 2. 获取挑战串及设备随机码（用于设备加密）
        // 3. 组装数据
        // 4. 激活

        //尝试激活次数
        val atomicInteger = AtomicInteger(3)

        val observable = Observable.fromCallable {

            val rsaKeyPair = RSAUtils.genKeyPair()
            val publicKey = RSAUtils.getPublicKey(rsaKeyPair)
            val publicKeyBytes = RSAUtils.getPublicKeyBytes(rsaKeyPair)
            val privateKey = RSAUtils.getPrivateKey(rsaKeyPair)
            val publicKeyBase64 = Base64.encode(ByteArrayUtil.byte2Hex(publicKeyBytes).toByteArray())

            LogUtil.d(TAG, "privateKey:$privateKey")
            LogUtil.d(TAG, "publicKey:$publicKey")
            LogUtil.d(TAG, "publicKeyBase64:$publicKeyBase64")
            LogUtil.d(TAG, "-----获取挑战串-----")
            val responseState =
                APHttpClient.getActivateChallenge(apWiredConfigInfo.ipPort!!, publicKeyBase64)
            val contentState = responseState?.body()?.string()
            val key = XmlUtils.getStringValue(contentState!!, "key")
            val keyString = String(Base64.decode(key))
            LogUtil.d(TAG, "RSA 解密前的挑战串:$keyString")
            val randomBytes = RSAUtils.decryptByPrivateKey(ByteArrayUtil.hex2Byte(keyString), privateKey)

            LogUtil.d(TAG, "-----开始激活-----")
            LogUtil.d(TAG, "输入密码：$password")
            val passwordEncrypt = getEncryptPwd(randomBytes, password)

            LogUtil.d(TAG, "passwordEncrypt:$passwordEncrypt")

            val response = APHttpClient.activate(apWiredConfigInfo.ipPort!!, passwordEncrypt)
            val content = response?.body()?.string()

            val errorCodeStr = XmlUtils.getStringValue(content!!, "subStatusCode")

            return@fromCallable when {
                "ok" == errorCodeStr?.toLowerCase() -> ApErrorCode.ACTIVATE_SUCCESS
                "riskPassword" == errorCodeStr -> ApErrorCode.ACTIVATE_FAIL_RISK_PASSWORD
                else -> ApErrorCode.ACTIVATE_FAIL
            }
        }.retryWhen{
            it.flatMap { error->
                if (atomicInteger.decrementAndGet() > 0){
                    Observable.timer(2, TimeUnit.SECONDS)
                } else {
                    Observable.error(error)
                }
            }
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object :Subscriber<Int>(){
                override fun onNext(code: Int) {
                    if (code == ApErrorCode.ACTIVATE_SUCCESS){
                        callback?.onSuccess(apWiredConfigInfo, password)
                    } else {
                        callback?.onFailed(code, null, apWiredConfigInfo, password)
                    }
                }

                override fun onCompleted() {}

                override fun onError(e: Throwable?) {
                    callback?.onFailed(ApErrorCode.ACTIVATE_FAIL, e, apWiredConfigInfo, password)
                }
            })
    }

    /**
     * 获取加密后的密码进行传输
     */
    private fun getEncryptPwd(randomBytes:ByteArray, inPassword:String):String{
        val randomHex= String(randomBytes)
        LogUtil.d(TAG, "randomHex:$randomHex")
        val randomTrueBytes= ByteArrayUtil.hex2Byte(randomHex)
        val prePassword = AESUtils.encryptTest(randomTrueBytes,randomBytes,"AES/CBC/NoPadding")
        val paddingPwd = ByteArray(16)
        for(i in 0..15){
            paddingPwd[i] = 0x00
        }
        System.arraycopy(inPassword.toByteArray(), 0, paddingPwd, 0, inPassword.length)

        val inPasswordEncrypt = AESUtils.encryptTest(randomTrueBytes,paddingPwd,"AES/CBC/NoPadding")
        val newPwd = ByteArray(inPasswordEncrypt.size + 16)
        System.arraycopy(prePassword, 0, newPwd, 0, 16)
        System.arraycopy(inPasswordEncrypt, 0, newPwd, 16, inPasswordEncrypt.size)
        val finalPwdHex = ByteArrayUtil.byte2Hex(newPwd)
        return Base64.encode(finalPwdHex.toByteArray())
    }


    /**
     * 设置开启萤石（HIK）服务，
     * ps：萤石目前是默认开启的
     * @param apDeviceInfo AP 配网信息
     * @param username 设备用户名
     * @param password 激活密码
     * @param operateCode 操作码（视频加密）
     */
    fun setServerState(apWifiConfigInfo: ApWifiConfigInfo, username: String, password:String, operateCode:String, callback: OnOpenServerCallback?){
        LogUtil.d(TAG, "apWifiConfigInfo=$apWifiConfigInfo , username=$username , password=$password , operateCode=$operateCode")
        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            try {
                val response = APHttpClient.setHikServerConfig(apWifiConfigInfo.ipPort!!, username,password, true, operateCode)
                val content = response?.body()?.string()
                subscriber?.onNext(content)
            }catch (e:Exception){
                subscriber?.onError(e)
            }
            subscriber?.onCompleted()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<String>() {

            override fun onCompleted() {
            }

            override fun onError(throwable: Throwable) {
                LogUtil.d(TAG, throwable.localizedMessage)
                callback?.onFailed(0, throwable, apWifiConfigInfo, operateCode)
            }

            override fun onNext(s: String) {
                LogUtil.d(TAG, "开启服务结果：")
                LogUtil.d(TAG, s)
                val subStatusCode = XmlUtils.getStringValue(s,"subStatusCode")
                val statusCode = XmlUtils.getStringValue(s,"statusCode")?.toIntOrNull()?:0
                if ("ok" == subStatusCode?.toLowerCase()){
                    callback?.onSuccess(apWifiConfigInfo, operateCode)
                } else {
                    callback?.onFailed(statusCode, null, apWifiConfigInfo, operateCode)
                }
            }
        })
    }

    /**
     * 获取设备激活状态回调
     */
    interface OnGetActivateCallback{
        fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo)
        fun onIpEmpty()
        fun onTimeOut()
        fun retry(){}
        fun isCancel(): Boolean{
            return false
        }
    }

    /**
     * 获取设备激活状态回调(有线)
     */
    interface OnGetActivateCallback_Wired{
        fun onSuccess(apWiredConfigInfo: APWiredConfigInfo)
        fun onIpEmpty()
        fun onTimeOut()
        fun retry(){}
        fun isCancel(): Boolean{
            return false
        }
    }

    /**
     * 激活回调
     */
    interface OnActivateCallback{
        fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo, password: String)
        fun onFailed(errorCode:Int, e: Throwable?, apWifiConfigInfo: ApWifiConfigInfo, password: String)
    }

    /**
     * 激活回调(有线)
     */
    interface OnActivateCallback_Wired{
        fun onSuccess(apWiredConfigInfo: APWiredConfigInfo, password: String)
        fun onFailed(errorCode:Int, e: Throwable?, apWiredConfigInfo: APWiredConfigInfo, password: String)
    }

    /**
     * 开启服务
     */
    interface OnOpenServerCallback{
        fun onSuccess(apWifiConfigInfo: ApWifiConfigInfo, operateCode: String)
        fun onFailed(errorCode:Int, e: Throwable?, apWifiConfigInfo: ApWifiConfigInfo, operateCode: String)
    }
}