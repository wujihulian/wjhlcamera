package com.ap.ezviz.pub.ap


object ApErrorCode {

    const val ACTIVATE_TIMEOUT = 10001
    const val ACTIVATE_IP_EMPTY = 10002

    const val ACTIVATE_SUCCESS = 1
    const val ACTIVATE_FAIL = 0
    const val ACTIVATE_FAIL_RISK_PASSWORD = 2020

    const val ERROR_WIFI_PARAMS = 30001
    const val ERROR_WIFI_SSID_EMPTY = 30002 // wifi ssid 为空
    const val ERROR_WIFI_DEVICE_ERROR = 30003 // 设备wifi搜索异常
    const val ERROR_WIFI_SEND_ERROR = 30004 // 发送wifi 账号和密码到设备失败
    const val ERROR_WIFI_VERCODE_EMPTY = 30005 // 验证码为空
    const val ERROR_WIFI_VERCODE_ERROR = 30006 // 验证码错误
    const val ERROR_WIFI_OTHER = 31000
    //有线配置相关错误码，暂未更新，请直接联系技术支持
}