package com.ap.ezviz.pub.ap

/**
 * 固定IP配置
 */
class FIXED_IP(vararg ips:IP_PORT) {
    companion object{
        val WIRELESS_IPC_HC = FIXED_IP(IP_PORT.IPC_1, IP_PORT.IPC_253)
        val WIRELESS_IPC_YS = FIXED_IP(IP_PORT.IPC_1, IP_PORT.IPC_253)
    }

    private val ipPorts: Array<out IP_PORT> = ips

    fun getIpPorts():ArrayList<IP_PORT>{
        val arrayList = ArrayList<IP_PORT>()
        ipPorts.forEach {
            arrayList.add(it)
        }
        return arrayList
    }

    fun createIpPort(ip:String, port:Int):IP_PORT{
        return IP_PORT(ip, port)
    }


    /**
     * 配置IP地址
     */
    class IP_PORT(val ip:String, val port:Int) {
        companion object{
            val IPC_1 = IP_PORT("192.168.8.1", 80)
            val IPC_253 = IP_PORT("192.168.8.253", 80)
        }

    }
}