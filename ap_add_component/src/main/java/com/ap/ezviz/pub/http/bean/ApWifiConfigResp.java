package com.ap.ezviz.pub.http.bean;

import androidx.annotation.Keep;

/**
 * ap配置网络
 */
@Keep
public class ApWifiConfigResp {
    public String requestURL;
    public int statusCode;
    public String statusString; //状态描述， string类型 = 1成功
    public String subStatusCode;
    public int errorCode;
    public String errorMsg;
}
