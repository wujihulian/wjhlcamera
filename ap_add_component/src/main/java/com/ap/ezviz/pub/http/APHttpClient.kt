package com.ap.ezviz.pub.http

import com.ap.ezviz.pub.ap.FIXED_IP
import com.ap.ezviz.pub.http.digest.AuthenticationCacheInterceptor
import com.ap.ezviz.pub.http.digest.CachingAuthenticatorDecorator
import com.ap.ezviz.pub.http.digest.digest.CachingAuthenticator
import com.ap.ezviz.pub.http.digest.digest.Credentials
import com.ap.ezviz.pub.http.digest.digest.DigestAuthenticator
import com.ap.ezviz.pub.utils.ApConfigUtil
import com.ap.ezviz.pub.utils.LogUtil
import com.ap.ezviz.pub.utils.XmlUtils
import ap.apconnect.add.component.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


object APHttpClient {

    private const val DEFAULT_CONNECT_TIMEOUT = 5000L

    const val TAG = "APHttpClient"
    private val mHttpClient by lazy {
        val it = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            it.addInterceptor(httpLoggingInterceptor)
        }
        return@lazy it.build()
    }

    /**
     * 设置设备wifi
     */
    fun setWifi(ipPort: FIXED_IP.IP_PORT, reqJson:String, info: ApConfigUtil.APEncryptInfo): Any?{
        val body = RequestBody.create(MediaType.parse("application/json;utf-8"), reqJson)
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/PreNetwork/WifiConfig?format=json&iv=${info.iv}")
                .put(body).addHeader("Authorization", info.authorization).build()
        return mHttpClient.newCall(request).execute()
    }

    fun setWiredNetwork(ipPort: FIXED_IP.IP_PORT, reqJson:String, info: ApConfigUtil.APEncryptInfoWired): Any?{
        val body = RequestBody.create(MediaType.parse("application/json;utf-8"), reqJson)
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/PreNetwork/WiredNetwork?format=json&iv=${info.iv}")
            .put(body).addHeader("Authorization", info.authorization).build()
        return mHttpClient.newCall(request).execute()
    }

    /**
     * 获取Wifi列表页面
     */
    fun getWifiList(ipPort: FIXED_IP.IP_PORT): Response? {
        val request = Request.Builder()
                .url("http://${ipPort.ip}:${ipPort.port}/PreNetwork/SecurityAndAccessPoint?format=json")
                .build()
        return mHttpClient.newCall(request).execute()
    }

    /**
     * 获取设备激活状态
     */
    fun getActivateState(ipPort: FIXED_IP.IP_PORT): Response? {
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/SDK/activateStatus")
                .get().build()
        return mHttpClient.newCall(request).execute()
    }


    /**
     * 获取设备免摘要能力
     */
    fun getCapabilities(ipPort: FIXED_IP.IP_PORT): Response? {
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/SDK/capabilities")
                .get().build()
        return mHttpClient.newCall(request).execute()
    }


    /**
     * 获取设备激活状态
     */
    fun getActivateChallenge(ipPort: FIXED_IP.IP_PORT, publicKey: String): Response? {
        val xmlBody = "<PublicKey version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\"><key>$publicKey</key></PublicKey>"
        val body = RequestBody.create(MediaType.parse("application/xml;utf-8"), xmlBody)
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/ISAPI/Security/challenge")
                .post(body).build()
        return mHttpClient.newCall(request).execute()
    }

    /**
     * 激活设备
     * passwordEncrypt ：用base64解密随机串，再用私钥解密后，用随机串对密码用AES128加密后再base64加密，发给设备
     * 发给设备的密码数据是：随机串前16字符串+真实密码，
     * 例如真实密码是12345，那么加密前的数据是“aaaabbbbccccdddd12345”,其中”aaaabbbbccccdddd”表示设备发送的随机串前16字符串；
     */
    fun activate(ipPort: FIXED_IP.IP_PORT,passwordEncrypt:String): Response? {
        val xmlBody = "<ActivateInfo version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\"><password>$passwordEncrypt</password></ActivateInfo>"
        val body = RequestBody.create(MediaType.parse("application/xml;utf-8"), xmlBody)
        val request = Request.Builder().url("http://${ipPort.ip}:${ipPort.port}/ISAPI/System/activate")
                .put(body).build()
        return mHttpClient.newCall(request).execute()
    }


    /**
     * 获取设备萤石（海康）服务开启状态
     */
    fun getHikServerState(ipPort: FIXED_IP.IP_PORT,username:String, password:String): Response? {
        val url = "http://${ipPort.ip}:${ipPort.port}/ISAPI/System/Network/EZVIZ"
        val authenticator = DigestAuthenticator(Credentials(username, password))

        val authCache = ConcurrentHashMap<String, CachingAuthenticator>()
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .authenticator(CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(AuthenticationCacheInterceptor(authCache))
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        val client = clientBuilder.build()
        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute()
    }

    /**
     * 设置设备萤石（海康）服务、操作码（视频加密密码）
     * 萤石服务opt registerStatus:开启、关闭
     * 操作码string： verificationCode
     */
    fun setHikServerConfig(ipPort: FIXED_IP.IP_PORT,username:String, password:String, enabled:Boolean, verificationCode:String): Response? {
        // 是否需要加密（3.10.0 这个版本不需要，下个版本改成加密）
        val isNeedSecurity = false
        val response =if (isNeedSecurity){
            getSecurityCapabilities(ipPort, username, password)
        } else {
            null
        }
        val apEncryptInfo = if (response?.isSuccessful == true){
            val responseStr = response.body()?.string()?:""
            LogUtil.d(TAG,"responseStr:$responseStr")
            val keyIterateNum = XmlUtils.getStringValue(responseStr,"keyIterateNum")?.toIntOrNull()
            val securityVersion = XmlUtils.getAttrValue(responseStr,"securityVersion", "opt")
            if (keyIterateNum!=null && keyIterateNum > 0 && securityVersion!=null && "1" in securityVersion){
                LogUtil.d(TAG,"对明文进行加密")
                // 需要加密
                ApConfigUtil.getClearTextPassword(verificationCode, password, keyIterateNum)
            } else{
                null
            }
        } else {
            null
        }

        var url = "http://${ipPort.ip}:${ipPort.port}/ISAPI/System/Network/EZVIZ"

        val verCode = if (apEncryptInfo != null){
            url = "$url?security=1&iv=${apEncryptInfo.iv}"
            apEncryptInfo.password
        } else {
            verificationCode
        }

        val xmlBody = "<EZVIZ version=\"2.0\" xmlns=\"http://www.isapi.org/ver20/XMLSchema\"><enabled>$enabled</enabled>" +
                "<verificationCode>$verCode</verificationCode></EZVIZ>"
        val body = RequestBody.create(MediaType.parse("application/xml;utf-8"), xmlBody)

        val authenticator = DigestAuthenticator(Credentials(username, password))
        val authCache = ConcurrentHashMap<String, CachingAuthenticator>()
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .authenticator(CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(AuthenticationCacheInterceptor(authCache))
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        val client = clientBuilder.build()
        val request = Request.Builder().url(url).put(body).build()
        return client.newCall(request).execute()
    }

    /**
     * 获取设备安全能力
     */
    fun getSecurityCapabilities(ipPort: FIXED_IP.IP_PORT,username:String, password:String): Response? {
        val url = "http://${ipPort.ip}:${ipPort.port}/ISAPI/Security/capabilities?username=$username"
        val authenticator = DigestAuthenticator(Credentials(username, password))

        val authCache = ConcurrentHashMap<String, CachingAuthenticator>()
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .authenticator(CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(AuthenticationCacheInterceptor(authCache))
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        val client = clientBuilder.build()
        val request = Request.Builder().url(url).get().build()
        return client.newCall(request).execute()
    }
}