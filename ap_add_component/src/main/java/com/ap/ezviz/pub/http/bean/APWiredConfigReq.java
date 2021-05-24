package com.ap.ezviz.pub.http.bean;

import androidx.annotation.Keep;
import java.util.ArrayList;

/**
 * ap配置网络，请求配网
 */
@Keep
public class APWiredConfigReq {
    public String authorization;
    public WiredInfo WiredNetwork;

    public static class WiredInfo {
        public IPV4 IPV4;
    }

    public static class IPV4 {
        public ipAddress ipAddress;
        public DNS DNS;
    }

    public static class ipAddress {
        public String type;
        public ArrayList<AddressList> AddressList;
    }

    public static class DNS {
        public boolean enabled;
        public String primary;
        public String secondary;
    }

    public static class AddressList{
        public String ipV4Address;
        public String subnetMask;
        public String defaultGateway;
    }
}
