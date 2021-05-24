package com.ap.ezviz.pub.http.bean;


import androidx.annotation.Keep;

import java.util.ArrayList;

/**
 * ap模式获取设备wifi列表信息以及加密字样
 */
@Keep
public class ApWifiListResp {
    public SecurityInfo SecurityInfo;
    public ArrayList<AccessPointList> AccessPointList;
    public int statusCode = 1;
    public String statusString; //状态描述， string类型 = 1成功
    public int errorCode;
    public String errorMsg;

    public static class SecurityInfo {
        public String challenge; //挑战串
        public int iterations; //迭代次数
        public String salt; //盐值
    }

    public static class AccessPointList {
        /**
         * 必填,SSID,string
         */
        public String ssid;
        /**
         * 可选,带宽（单位：Mbps）,int
         */
        public int speed;
        /**
         * 可选,信号强度,"0-100"
         */
        public int signalStrength;
        /**
         * 可选,安全模式:
         * "disable,WEP,WPA-personal,WPA2-personal,WPA-RADIUS,
         * WPA-enterprise,WPA2-enterprise",string
         */
        public String securityMode;
    }
}
