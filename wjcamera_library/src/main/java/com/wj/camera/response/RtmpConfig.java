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
    }
}
