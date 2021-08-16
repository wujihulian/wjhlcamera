package com.wj.camera.response;

/**
 * FileName: NetConfig
 * Author: xiongxiang
 * Date: 2021/7/12
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class NetConfig {


    /**
     * NetConfig : {"result":"no operation"}
     */

    private NetConfigDTO NetConfig;

    public NetConfigDTO getNetConfig() {
        return NetConfig;
    }

    public void setNetConfig(NetConfigDTO NetConfig) {
        this.NetConfig = NetConfig;
    }

    public static class NetConfigDTO {

        /**
         * result : unknown error
         * result =no operation 下面参数才存在
         * time : 2021-08-12T13:14:23+08:00
         * SSID : svnlan9
         * passwd : test123456789
         * WLANIP :
         * EZVIZADDRESS : litedev.ys7.com
         * EZVIZIP :
         * LINKstatus : false
         */

        private String result;
        private String time;
        private String SSID;
        private String passwd;
        private String WLANIP;
        private String EZVIZADDRESS;
        private String EZVIZIP;
        private String LINKstatus;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSSID() {
            return SSID;
        }

        public void setSSID(String SSID) {
            this.SSID = SSID;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getWLANIP() {
            return WLANIP;
        }

        public void setWLANIP(String WLANIP) {
            this.WLANIP = WLANIP;
        }

        public String getEZVIZADDRESS() {
            return EZVIZADDRESS;
        }

        public void setEZVIZADDRESS(String EZVIZADDRESS) {
            this.EZVIZADDRESS = EZVIZADDRESS;
        }

        public String getEZVIZIP() {
            return EZVIZIP;
        }

        public void setEZVIZIP(String EZVIZIP) {
            this.EZVIZIP = EZVIZIP;
        }

        public String getLINKstatus() {
            return LINKstatus;
        }

        public void setLINKstatus(String LINKstatus) {
            this.LINKstatus = LINKstatus;
        }
    }
}
