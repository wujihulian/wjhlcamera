package com.ap.ezviz.pub.utils

import com.hikvision.ezviz.lib.encypt.AESCBCEncry
import com.hikvision.ezviz.lib.encypt.HmacSHA256Encry
import com.hikvision.ezviz.lib.encypt.SHA256Encry
import com.ap.ezviz.pub.encrypt.AESUtils
import java.security.MessageDigest
import java.util.*

/**
 * App配置工具类
 */
object ApConfigUtil {

    const val TAG = "ApConfigUtil"

    /**
     * 获取Ap网络配置的认证信息
     * @param identify 萤石验证码
     * @param salt 盐值 由设备返回
     * @param wifiSSId 需要连接wifi的ssid
     * @param wifiPassword 需要连接wifi的password
     * @param iterations 迭代次数
     * @param challenge 挑战串
     */
    fun getAuthorizationAndPassword(identify: String, salt: String, wifiSSId: String, wifiPassword: String, iterations: Int, challenge: String): APEncryptInfo?{
        var authorization = ""
        //要求必须小写
        val iv = UUID.randomUUID().toString().replace("-","").toLowerCase()
        LogUtil.d(TAG,"iv:$iv,identify:$identify,salt:$salt,wifiSSId:$wifiSSId,wifiPassword:$wifiPassword,iterations:$iterations,challenge:$challenge")
        val key = iteratorEntryKey(identify + salt, iterations)
        val ivArray = hex2Byte(iv)

        //AES加密截取前16个字节，考虑设备的性能以及AES256出口限制
        val aesKey = ByteArray(16)
        System.arraycopy(key,0,aesKey,0,16)

        val data = wifiPassword.toByteArray(Charsets.UTF_8)
        val passwordArray = try {
            AESCBCEncry.encrypt(aesKey, ivArray, data)
        }catch (e:Exception){
            null
        } ?: return null


        val passwordEncypted = android.util.Base64.encodeToString(passwordArray as ByteArray?,android.util.Base64.NO_WRAP)?:""

        val stringToSign = "/PreNetwork/WifiConfig?format=json&iv=$iv$wifiSSId$passwordEncypted"
        LogUtil.d(TAG,"stringToSign:$stringToSign")
        val sinature = challenge + stringToSign
        val authorizationArray  = try {
            HmacSHA256Encry.encode(key, sinature)
        }catch (e:Exception){null} ?:return null

        authorization = android.util.Base64.encodeToString(authorizationArray as ByteArray?,android.util.Base64.NO_WRAP)
        LogUtil.d(TAG,"authorization:$authorization + passwordEncypted:$passwordEncypted")
        return APEncryptInfo(authorization, passwordEncypted, iv)
    }

    fun getWiredAuthorization(identify: String, salt: String, reqBody:String, iterations: Int, challenge: String): APEncryptInfoWired?{
        var authorization = ""
        //要求必须小写
        val iv = UUID.randomUUID().toString().replace("-","").toLowerCase()
        val key = iteratorEntryKey(identify + salt, iterations)

        //$StringToSign = $URL + $WiredNetwork 为根节点的全部报文内容
        val stringToSign = "/PreNetwork/WiredNetwork?format=json&iv=$iv$reqBody"
        LogUtil.d(TAG,"stringToSign:$stringToSign")

        //$signature = $challenge + UTF-8(StringToSign)
        val sinature = challenge + stringToSign

        val authorizationArray  = try {
            HmacSHA256Encry.encode(key, sinature)
        }catch (e:Exception){null} ?:return null

        //$authorization = Base64(HMAC_SHA256($key, $signature))
        authorization = android.util.Base64.encodeToString(authorizationArray as ByteArray?,android.util.Base64.NO_WRAP)
        LogUtil.d(TAG,"authorization:$authorization")

        return APEncryptInfoWired(authorization, iv)
    }


    /**
     * 获取明文认证信息
     * @param vercode 需要加密的操作码
     * @param iterations 迭代次数
     * @param salt 盐，固定值
     */
    fun getClearTextPassword(vercode: String,password: String, iterations: Int, salt: String = "AaBbCcDd1234!@#$"): APEncryptInfo?{
        //要求必须小写
        val iv = UUID.randomUUID().toString().replace("-","").toLowerCase()
        LogUtil.d(TAG,"iv:$iv,vercode:$vercode,iterations:$iterations,password:$password")
        val key = SHA256Encry.iteratorEntryKey(password + salt, iterations)
//        val key = iteratorEntryKey(password + salt, iterations)
        val ivArray = hex2Byte(iv)

        //AES加密截取前16个字节，考虑设备的性能以及AES256出口限制
        val aesKey = ByteArray(16)
        System.arraycopy(key,0,aesKey,0,16)

        val data = vercode.toByteArray(Charsets.UTF_8)
        val passwordArray = try {
            AESUtils.encrypt(aesKey, ivArray, android.util.Base64.encode(data,android.util.Base64.NO_WRAP), "AES/CBC/NoPadding")
        }catch (e:Exception){
            null
        } ?: return null
        val passwordEncypted = byte2Hex(passwordArray)
        LogUtil.d(TAG,"passwordEncypted:$passwordEncypted, iv=$iv")
        return APEncryptInfo("", passwordEncypted, byte2Hex(ivArray))
    }

    /**
     * Ap config request info(Wifi)
     */
    data class APEncryptInfo(val authorization:String,val password:String,val iv: String)

    /**
     * Ap config request info(Wired)
     */
    data class APEncryptInfoWired(val authorization:String,val iv: String)

    /**
     * 迭代加密生成key
     */
    private fun iteratorEntryKey(originKey: String,iterations: Int):ByteArray{
        var result = originKey
        val messageDigest = MessageDigest.getInstance("SHA-256")
        var hashArray:ByteArray = messageDigest.digest(result.toByteArray(Charsets.UTF_8))
        LogUtil.d(TAG,"初始化时字符串" + byte2Hex(hashArray))
        if (iterations - 1 > 0){
            for (i in 0 until iterations - 1){
                result = byte2Hex(hashArray)
                hashArray = messageDigest.digest(result.toByteArray(Charsets.UTF_8))
                LogUtil.d(TAG,"第$i 次迭代结果:${byte2Hex(hashArray)}")
            }
        }
        return hashArray
    }


    /**
     * 字节数组转为16进制字符串
     */
    private fun byte2Hex(bytes: ByteArray): String {
        val stringBuffer = StringBuffer()
        var temp: String? = null
        for (i in bytes.indices) {
            temp = Integer.toHexString(bytes[i].toInt() and 0xFF)
            if (temp!!.length == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0")
            }
            stringBuffer.append(temp)
        }
        return stringBuffer.toString()
    }

    /**
     * 16进制字符串转字节数组
     */
    private fun hex2Byte(hexStr: String): ByteArray{
        val resultArray = ByteArray(hexStr.length/2)
        for (i in resultArray.indices){
            resultArray[i] = hexStr.substring(2 * i,2*i +2).toInt(16).toByte()
        }
        return resultArray
    }


}