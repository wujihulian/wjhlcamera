package com.ap.ezviz.pub.http.bean;


import androidx.annotation.Keep;

/**
 * ap配置网络，请求配网
 */
@Keep
public class ApConfigReq {
    public String authorization;
    public WifiInfo WifiInfo;

    public static class WifiInfo {
        public String ssid;
        public String password;
    }
}
