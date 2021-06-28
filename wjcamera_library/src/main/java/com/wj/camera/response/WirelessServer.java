package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: WirelessServer
 * Author: xiongxiang
 * Date: 2021/6/25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WirelessServer implements Serializable {


    /**
     * WirelessServer : {"wifiApEnabled":"true","ssid":"HAP_F77553891","WirelessSecurity":{"securityMode":"WPA2-personal","WPA":{"algorithmType":"AES"}}}
     */

    private WirelessServerDTO WirelessServer;

    public WirelessServerDTO getWirelessServer() {
        return WirelessServer;
    }

    public void setWirelessServer(WirelessServerDTO WirelessServer) {
        this.WirelessServer = WirelessServer;
    }

    public static class WirelessServerDTO {
        /**
         * wifiApEnabled : true
         * ssid : HAP_F77553891
         * WirelessSecurity : {"securityMode":"WPA2-personal","WPA":{"algorithmType":"AES"}}
         */

        private String wifiApEnabled;
        private String ssid;
        private WirelessSecurityDTO WirelessSecurity;

        public String getWifiApEnabled() {
            return wifiApEnabled;
        }

        public void setWifiApEnabled(String wifiApEnabled) {
            this.wifiApEnabled = wifiApEnabled;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public WirelessSecurityDTO getWirelessSecurity() {
            return WirelessSecurity;
        }

        public void setWirelessSecurity(WirelessSecurityDTO WirelessSecurity) {
            this.WirelessSecurity = WirelessSecurity;
        }

        public static class WirelessSecurityDTO {
            /**
             * securityMode : WPA2-personal
             * WPA : {"algorithmType":"AES"}
             */

            private String securityMode;
            private WPADTO WPA;

            public String getSecurityMode() {
                return securityMode;
            }

            public void setSecurityMode(String securityMode) {
                this.securityMode = securityMode;
            }

            public WPADTO getWPA() {
                return WPA;
            }

            public void setWPA(WPADTO WPA) {
                this.WPA = WPA;
            }

            public static class WPADTO {
                /**
                 * algorithmType : AES
                 */

                private String algorithmType;

                public String getAlgorithmType() {
                    return algorithmType;
                }

                public void setAlgorithmType(String algorithmType) {
                    this.algorithmType = algorithmType;
                }
            }
        }
    }
}
