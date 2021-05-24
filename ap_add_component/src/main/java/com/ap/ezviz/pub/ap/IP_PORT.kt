package com.ap.ezviz.pub.ap

/**
 * 配置IP地址
 */
enum class IP_PORT(val ip:String, val port:Int) {
    IPC_1("192.168.8.1", 80),
    IPC_253("192.168.8.253", 80);
    init {
    }
}