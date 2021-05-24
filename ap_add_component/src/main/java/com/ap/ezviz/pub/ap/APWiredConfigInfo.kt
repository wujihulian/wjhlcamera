package com.ap.ezviz.pub.ap

class APWiredConfigInfo {
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
     * wired的DHCP
     */
    var Type: String? = ""
    /**
     * option
     * wired 的IP
     */
    var IP: String? = ""
    /**
     * option
     * wired 的子网掩码
     */
    var SubNetMask: String = ""
    /**
     * option
     * DHCP 使能
     */
    var Dhcp: String = ""
    /**
     * option
     * DNS 使能
     */
    var bDNS: Boolean = true
    /**
     * option
     * wired 的默认网关
     */
    var DefaultGateway: String = ""
    /**
     * option
     * wired 的首选DNS
     */
    var DNS_Primary: String = ""
    /**
     * option
     * wired 的次选DNS
     */
    var DNS_Secondary: String = ""
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

    public class Builder{
        var apWiredConfigInfo:APWiredConfigInfo = APWiredConfigInfo()

        fun deviceSN(deviceId:String):Builder{
            this.apWiredConfigInfo.deviceSN = deviceId
            return this
        }
        fun activatePwd(password:String):Builder{
            this.apWiredConfigInfo.activatePwd = password
            return this
        }

        fun verifyCode(verifyCode:String):Builder{
            this.apWiredConfigInfo.verifyCode = verifyCode
            return this
        }

        fun dhcp(dhcp:String):Builder{
            this.apWiredConfigInfo.Dhcp = dhcp
            return this
        }

        fun ipAddress(ip:String, subnetMask:String, defaultgateway: String):Builder{
            this.apWiredConfigInfo.IP = ip
            this.apWiredConfigInfo.SubNetMask = subnetMask
            this.apWiredConfigInfo.DefaultGateway = defaultgateway
            return this
        }

        fun dns(dns_enable:String, dns_primary:String, dns_secondary:String):Builder{
            if(dns_enable == "true")
                this.apWiredConfigInfo.bDNS = true
            else
                this.apWiredConfigInfo.bDNS = false
            this.apWiredConfigInfo.DNS_Primary = dns_primary
            this.apWiredConfigInfo.DNS_Secondary = dns_secondary
            return this
        }

        fun fixedIP(fixedIP: FIXED_IP):Builder{
            this.apWiredConfigInfo.fixedIP = fixedIP
            if (fixedIP.getIpPorts().size>0){
                this.apWiredConfigInfo.ipPort = fixedIP.getIpPorts()[0]
            } else {
                this.apWiredConfigInfo.ipPort = null
            }
            return this
        }


        fun build():APWiredConfigInfo{
            return this.apWiredConfigInfo
        }
    }
}