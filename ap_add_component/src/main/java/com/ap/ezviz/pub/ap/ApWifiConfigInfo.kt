package com.ap.ezviz.pub.ap


class ApWifiConfigInfo {

    var isActivated: Boolean = false
    var enableHCPlatform: Boolean = true
    var isOpenHCPlatform: Boolean = false

    /**
     * 必填
     * 设备序列号
     */
    var deviceSN:String = ""

    /**
     * 可选
     * 设备验证码
     */
    var verifyCode: String = ""

    /**
     * option
     * wifi的bSSID
     */
    var wifiBSSID: String? = ""
    /**
     * option
     * wifi 的密码
     */
    var wifiPwd: String? = ""

    /**
     * apConfigType = HIK_PRIVATE_ISAPI 时  必填
     * ap配网固定IP及端口信息；可能存在多个IP的情况
     */
    var fixedIP: FIXED_IP? = null

    /**
     * apConfigType = HIK_PRIVATE_ISAPI 时  必填
     * AP 配网具体使用的IP
     */
    var ipPort: FIXED_IP.IP_PORT? = null

    /**
     * 走萤石云的加密时，需要自动创建
     */
    var activatePwd:String? = null

    override fun toString(): String {
        return "deviceSN=$deviceSN,verifyCode=$verifyCode,wifiBSSID=$wifiBSSID,wifiPwd=$wifiPwd,fixedIP=$fixedIP" +
                ",ipPort=$ipPort,isActivated=$isActivated,enableHCPlatform=$enableHCPlatform,isOpenHCPlatform=$isOpenHCPlatform"
    }


    public class Builder{
        var apWifiConfigInfo:ApWifiConfigInfo = ApWifiConfigInfo()

        fun deviceSN(deviceId:String):Builder{
            this.apWifiConfigInfo.deviceSN = deviceId
            return this
        }
        fun activatePwd(password:String):Builder{
            this.apWifiConfigInfo.activatePwd = password
            return this
        }

        fun verifyCode(verifyCode:String):Builder{
            this.apWifiConfigInfo.verifyCode = verifyCode
            return this
        }

        fun withWiFi(ssid: String?, password: String?):Builder{
            this.apWifiConfigInfo.wifiBSSID = ssid?:""
            this.apWifiConfigInfo.wifiPwd = password?:""
            return this
        }

        fun fixedIP(fixedIP: FIXED_IP):Builder{
            this.apWifiConfigInfo.fixedIP = fixedIP
            if (fixedIP.getIpPorts().size>0){
                this.apWifiConfigInfo.ipPort = fixedIP.getIpPorts()[0]
            } else {
                this.apWifiConfigInfo.ipPort = null
            }
            return this
        }


        fun build():ApWifiConfigInfo{
            return this.apWifiConfigInfo
        }
    }
}