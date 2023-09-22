package com.wj.camera.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * FileName: RtmpConfig
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RtmpConfig implements Serializable {

    /**
     * RTMP : {"enabled":"false","xmlns":"http://www.hikvision.com/ver20/XMLSchema","URL":"12213","version":"2.0"}
     */

    @JsonProperty("RTMP")
    private RTMPDTO RTMP;

    public RTMPDTO getRTMP() {
        return RTMP;
    }

    public void setRTMP(RTMPDTO RTMP) {
        this.RTMP = RTMP;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RTMPDTO implements Serializable {
        /**
         * enabled : false
         * xmlns : http://www.hikvision.com/ver20/XMLSchema
         * URL : 12213
         * version : 2.0
         */

        @JsonProperty("enabled")
        private String enabled;
        @JsonProperty("xmlns")
        private String xmlns;
        @JsonProperty("URL")
        private String URL;
        @JsonProperty("version")
        private String version;
        @JsonProperty("PrivatelyEnabled")
        private String PrivatelyEnabled;
        @JsonProperty("PrivatelyURL")
        private String PrivatelyURL;
        @JsonProperty("PlayURL1")
        private String PlayURL1;
        @JsonProperty("PlayURL2")
        private String PlayURL2;

        public String getPrivatelyEnabled() {
            return PrivatelyEnabled;
        }

        public void setPrivatelyEnabled(String privatelyEnabled) {
            PrivatelyEnabled = privatelyEnabled;
        }

        public String getPrivatelyURL() {
            return PrivatelyURL;
        }

        public void setPrivatelyURL(String privatelyURL) {
            PrivatelyURL = privatelyURL;
        }

        public String getPlayURL1() {
            return PlayURL1;
        }

        public void setPlayURL1(String playURL1) {
            PlayURL1 = playURL1;
        }

        public String getPlayURL2() {
            return PlayURL2;
        }

        public void setPlayURL2(String playURL2) {
            PlayURL2 = playURL2;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public String getXmlns() {
            return xmlns;
        }

        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "RTMPDTO{" +
                    "enabled='" + enabled + '\'' +
                    ", xmlns='" + xmlns + '\'' +
                    ", URL='" + URL + '\'' +
                    ", version='" + version + '\'' +
                    ", PrivatelyEnabled='" + PrivatelyEnabled + '\'' +
                    ", PrivatelyURL='" + PrivatelyURL + '\'' +
                    ", PlayURL1='" + PlayURL1 + '\'' +
                    ", PlayURL2='" + PlayURL2 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RtmpConfig{" +
                "RTMP=" + RTMP +
                '}';
    }
}
