package com.wj.camera.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * FileName: TwoWayAudio
 * Author: xiongxiang
 * Date: 2021/3/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwoWayAudio  implements Serializable {


    /**
     * TwoWayAudioChannel : {"speakerVolume":"50","noisereduce":"true","xmlns":"http://www.std-cgi.com/ver20/XMLSchema","audioSamplingRate":"32","id":"1","audioInputType":"MicIn","audioCompressionType":"AAC","audioBitRate":"32","version":"2.0","enabled":"false"}
     */

    private TwoWayAudioChannelDTO TwoWayAudioChannel;

    public TwoWayAudioChannelDTO getTwoWayAudioChannel() {
        return TwoWayAudioChannel;
    }

    public void setTwoWayAudioChannel(TwoWayAudioChannelDTO TwoWayAudioChannel) {
        this.TwoWayAudioChannel = TwoWayAudioChannel;
    }

    public static class TwoWayAudioChannelDTO {
        /**
         * speakerVolume : 50
         * noisereduce : true
         * xmlns : http://www.std-cgi.com/ver20/XMLSchema
         * audioSamplingRate : 32
         * id : 1
         * audioInputType : MicIn
         * audioCompressionType : AAC
         * audioBitRate : 32
         * version : 2.0
         * enabled : false
         */

        private String speakerVolume;
        private String noisereduce;
        private String xmlns;
        private String audioSamplingRate;
        private String id;
        private String audioInputType;
        private String audioCompressionType;
        private String audioBitRate;
        private String version;
        private String enabled;

        public String getSpeakerVolume() {
            return speakerVolume;
        }

        public void setSpeakerVolume(String speakerVolume) {
            this.speakerVolume = speakerVolume;
        }

        public String getNoisereduce() {
            return noisereduce;
        }

        public void setNoisereduce(String noisereduce) {
            this.noisereduce = noisereduce;
        }

        public String getXmlns() {
            return xmlns;
        }

        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }

        public String getAudioSamplingRate() {
            return audioSamplingRate;
        }

        public void setAudioSamplingRate(String audioSamplingRate) {
            this.audioSamplingRate = audioSamplingRate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAudioInputType() {
            return audioInputType;
        }

        public void setAudioInputType(String audioInputType) {
            this.audioInputType = audioInputType;
        }

        public String getAudioCompressionType() {
            return audioCompressionType;
        }

        public void setAudioCompressionType(String audioCompressionType) {
            this.audioCompressionType = audioCompressionType;
        }

        public String getAudioBitRate() {
            return audioBitRate;
        }

        public void setAudioBitRate(String audioBitRate) {
            this.audioBitRate = audioBitRate;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }
    }
}
