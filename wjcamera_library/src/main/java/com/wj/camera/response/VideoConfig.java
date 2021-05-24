package com.wj.camera.response;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: VideoConfig
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class VideoConfig implements Serializable{

    /**
     * StreamingChannel : {"Audio":{"enabled":"true","audioCompressionType":"AAC","audioInputChannelID":"1"},"id":"101","xmlns":"http://www.hikvision.com/ver20/XMLSchema","Video":{"constantBitRate":"6144","videoCodecType":"H.264","SmartCodec":{"enabled":"false"},"maxFrameRate":"2500","smoothing":"50","videoInputChannelID":"1","enabled":"true","H265Profile":"Main","SVC":{"enabled":"false"},"videoResolutionWidth":"1280","snapShotImageType":"JPEG","videoScanType":"progressive","GovLength":"250","fixedQuality":"60","PacketType":[{"content":"PS"},{"content":"RTP"}],"H264Profile":"Main","keyFrameInterval":"10000","videoQualityControlType":"CBR","videoResolutionHeight":"720"},"version":"2.0","enabled":"true","channelName":"无极教育云","Transport":{"Security":{"enabled":"true","SecurityAlgorithm":{"algorithmType":"MD5"},"certificateType":"digest"},"ControlProtocolList":{"ControlProtocol":[{"streamingTransport":"RTSP"},{"streamingTransport":"HTTP"},{"streamingTransport":"SHTTP"}]},"Multicast":{"videoDestPortNo":"8860","audioDestPortNo":"8862","enabled":"true","FecInfo":{"fecDestPortNo":"9860","fecRatio":"0"},"destIPAddress":"0.0.0.0"},"Unicast":{"enabled":"true","rtpTransportType":"RTP/TCP"},"maxPacketSize":"1000"}}
     */

    @com.fasterxml.jackson.annotation.JsonProperty("StreamingChannel")
    private StreamingChannelDTO StreamingChannel;

    public StreamingChannelDTO getStreamingChannel() {
        return StreamingChannel;
    }

    public void setStreamingChannel(StreamingChannelDTO StreamingChannel) {
        this.StreamingChannel = StreamingChannel;
    }

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamingChannelDTO implements Serializable {
        /**
         * Audio : {"enabled":"true","audioCompressionType":"AAC","audioInputChannelID":"1"}
         * id : 101
         * xmlns : http://www.hikvision.com/ver20/XMLSchema
         * Video : {"constantBitRate":"6144","videoCodecType":"H.264","SmartCodec":{"enabled":"false"},"maxFrameRate":"2500","smoothing":"50","videoInputChannelID":"1","enabled":"true","H265Profile":"Main","SVC":{"enabled":"false"},"videoResolutionWidth":"1280","snapShotImageType":"JPEG","videoScanType":"progressive","GovLength":"250","fixedQuality":"60","PacketType":[{"content":"PS"},{"content":"RTP"}],"H264Profile":"Main","keyFrameInterval":"10000","videoQualityControlType":"CBR","videoResolutionHeight":"720"}
         * version : 2.0
         * enabled : true
         * channelName : 无极教育云
         * Transport : {"Security":{"enabled":"true","SecurityAlgorithm":{"algorithmType":"MD5"},"certificateType":"digest"},"ControlProtocolList":{"ControlProtocol":[{"streamingTransport":"RTSP"},{"streamingTransport":"HTTP"},{"streamingTransport":"SHTTP"}]},"Multicast":{"videoDestPortNo":"8860","audioDestPortNo":"8862","enabled":"true","FecInfo":{"fecDestPortNo":"9860","fecRatio":"0"},"destIPAddress":"0.0.0.0"},"Unicast":{"enabled":"true","rtpTransportType":"RTP/TCP"},"maxPacketSize":"1000"}
         */

        @com.fasterxml.jackson.annotation.JsonProperty("Audio")
        private AudioDTO Audio;
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        private String id;
        @com.fasterxml.jackson.annotation.JsonProperty("xmlns")
        private String xmlns;
        @com.fasterxml.jackson.annotation.JsonProperty("Video")
        private VideoDTO Video;
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        private String version;
        @com.fasterxml.jackson.annotation.JsonProperty("enabled")
        private String enabled;
        @com.fasterxml.jackson.annotation.JsonProperty("channelName")
        private String channelName;
        @com.fasterxml.jackson.annotation.JsonProperty("Transport")
        private TransportDTO Transport;

        public AudioDTO getAudio() {
            return Audio;
        }

        public void setAudio(AudioDTO Audio) {
            this.Audio = Audio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getXmlns() {
            return xmlns;
        }

        public void setXmlns(String xmlns) {
            this.xmlns = xmlns;
        }

        public VideoDTO getVideo() {
            return Video;
        }

        public void setVideo(VideoDTO Video) {
            this.Video = Video;
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

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public TransportDTO getTransport() {
            return Transport;
        }

        public void setTransport(TransportDTO Transport) {
            this.Transport = Transport;
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class AudioDTO implements Serializable {
            /**
             * enabled : true
             * audioCompressionType : AAC
             * audioInputChannelID : 1
             */

            @com.fasterxml.jackson.annotation.JsonProperty("enabled")
            private String enabled;
            @com.fasterxml.jackson.annotation.JsonProperty("audioCompressionType")
            private String audioCompressionType;
            @com.fasterxml.jackson.annotation.JsonProperty("audioInputChannelID")
            private String audioInputChannelID;

            public String getEnabled() {
                return enabled;
            }

            public void setEnabled(String enabled) {
                this.enabled = enabled;
            }

            public String getAudioCompressionType() {
                return audioCompressionType;
            }

            public void setAudioCompressionType(String audioCompressionType) {
                this.audioCompressionType = audioCompressionType;
            }

            public String getAudioInputChannelID() {
                return audioInputChannelID;
            }

            public void setAudioInputChannelID(String audioInputChannelID) {
                this.audioInputChannelID = audioInputChannelID;
            }
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class VideoDTO implements Serializable {
            /**
             * constantBitRate : 6144
             * videoCodecType : H.264
             * SmartCodec : {"enabled":"false"}
             * maxFrameRate : 2500
             * smoothing : 50
             * videoInputChannelID : 1
             * enabled : true
             * H265Profile : Main
             * SVC : {"enabled":"false"}
             * videoResolutionWidth : 1280
             * snapShotImageType : JPEG
             * videoScanType : progressive
             * GovLength : 250
             * fixedQuality : 60
             * PacketType : [{"content":"PS"},{"content":"RTP"}]
             * H264Profile : Main
             * keyFrameInterval : 10000
             * videoQualityControlType : CBR
             * videoResolutionHeight : 720
             */

            @com.fasterxml.jackson.annotation.JsonProperty("constantBitRate")
            private String constantBitRate;
            @com.fasterxml.jackson.annotation.JsonProperty("videoCodecType")
            private String videoCodecType;
            @com.fasterxml.jackson.annotation.JsonProperty("SmartCodec")
            private SmartCodecDTO SmartCodec;
            @com.fasterxml.jackson.annotation.JsonProperty("maxFrameRate")
            private String maxFrameRate;
            @com.fasterxml.jackson.annotation.JsonProperty("smoothing")
            private String smoothing;
            @com.fasterxml.jackson.annotation.JsonProperty("videoInputChannelID")
            private String videoInputChannelID;
            @com.fasterxml.jackson.annotation.JsonProperty("enabled")
            private String enabled;
            @com.fasterxml.jackson.annotation.JsonProperty("H265Profile")
            private String H265Profile;
            @com.fasterxml.jackson.annotation.JsonProperty("SVC")
            private SVCDTO SVC;
            @com.fasterxml.jackson.annotation.JsonProperty("videoResolutionWidth")
            private String videoResolutionWidth;
            @com.fasterxml.jackson.annotation.JsonProperty("snapShotImageType")
            private String snapShotImageType;
            @com.fasterxml.jackson.annotation.JsonProperty("videoScanType")
            private String videoScanType;
            @com.fasterxml.jackson.annotation.JsonProperty("GovLength")
            private String GovLength;
            @com.fasterxml.jackson.annotation.JsonProperty("fixedQuality")
            private String fixedQuality;
            @com.fasterxml.jackson.annotation.JsonProperty("H264Profile")
            private String H264Profile;
            @com.fasterxml.jackson.annotation.JsonProperty("keyFrameInterval")
            private String keyFrameInterval;
            @com.fasterxml.jackson.annotation.JsonProperty("videoQualityControlType")
            private String videoQualityControlType;
            @com.fasterxml.jackson.annotation.JsonProperty("videoResolutionHeight")
            private String videoResolutionHeight;
            @com.fasterxml.jackson.annotation.JsonProperty("PacketType")
            private List<PacketTypeDTO> PacketType;

            @com.fasterxml.jackson.annotation.JsonProperty("vbrUpperCap")
            private String vbrUpperCap;

            public String getVbrUpperCap() {
                return vbrUpperCap;
            }

            public void setVbrUpperCap(String vbrUpperCap) {
                this.vbrUpperCap = vbrUpperCap;
            }

            public String getConstantBitRate() {
                return constantBitRate;
            }

            public void setConstantBitRate(String constantBitRate) {
                this.constantBitRate = constantBitRate;
            }

            public String getVideoCodecType() {
                return videoCodecType;
            }

            public void setVideoCodecType(String videoCodecType) {
                this.videoCodecType = videoCodecType;
            }

            public SmartCodecDTO getSmartCodec() {
                return SmartCodec;
            }

            public void setSmartCodec(SmartCodecDTO SmartCodec) {
                this.SmartCodec = SmartCodec;
            }

            public String getMaxFrameRate() {
                return maxFrameRate;
            }

            public void setMaxFrameRate(String maxFrameRate) {
                this.maxFrameRate = maxFrameRate;
            }

            public String getSmoothing() {
                return smoothing;
            }

            public void setSmoothing(String smoothing) {
                this.smoothing = smoothing;
            }

            public String getVideoInputChannelID() {
                return videoInputChannelID;
            }

            public void setVideoInputChannelID(String videoInputChannelID) {
                this.videoInputChannelID = videoInputChannelID;
            }

            public String getEnabled() {
                return enabled;
            }

            public void setEnabled(String enabled) {
                this.enabled = enabled;
            }

            public String getH265Profile() {
                return H265Profile;
            }

            public void setH265Profile(String H265Profile) {
                this.H265Profile = H265Profile;
            }

            public SVCDTO getSVC() {
                return SVC;
            }

            public void setSVC(SVCDTO SVC) {
                this.SVC = SVC;
            }

            public String getVideoResolutionWidth() {
                return videoResolutionWidth;
            }

            public void setVideoResolutionWidth(String videoResolutionWidth) {
                this.videoResolutionWidth = videoResolutionWidth;
            }

            public String getSnapShotImageType() {
                return snapShotImageType;
            }

            public void setSnapShotImageType(String snapShotImageType) {
                this.snapShotImageType = snapShotImageType;
            }

            public String getVideoScanType() {
                return videoScanType;
            }

            public void setVideoScanType(String videoScanType) {
                this.videoScanType = videoScanType;
            }

            public String getGovLength() {
                return GovLength;
            }

            public void setGovLength(String GovLength) {
                this.GovLength = GovLength;
            }

            public String getFixedQuality() {
                return fixedQuality;
            }

            public void setFixedQuality(String fixedQuality) {
                this.fixedQuality = fixedQuality;
            }

            public String getH264Profile() {
                return H264Profile;
            }

            public void setH264Profile(String H264Profile) {
                this.H264Profile = H264Profile;
            }

            public String getKeyFrameInterval() {
                return keyFrameInterval;
            }

            public void setKeyFrameInterval(String keyFrameInterval) {
                this.keyFrameInterval = keyFrameInterval;
            }

            public String getVideoQualityControlType() {
                return videoQualityControlType;
            }

            public void setVideoQualityControlType(String videoQualityControlType) {
                this.videoQualityControlType = videoQualityControlType;
            }

            public String getVideoResolutionHeight() {
                return videoResolutionHeight;
            }

            public void setVideoResolutionHeight(String videoResolutionHeight) {
                this.videoResolutionHeight = videoResolutionHeight;
            }

            public List<PacketTypeDTO> getPacketType() {
                return PacketType;
            }

            public void setPacketType(List<PacketTypeDTO> PacketType) {
                this.PacketType = PacketType;
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class SmartCodecDTO implements Serializable {
                /**
                 * enabled : false
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("enabled")
                private String enabled;

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class SVCDTO implements Serializable {
                /**
                 * enabled : false
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("enabled")
                private String enabled;

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class PacketTypeDTO implements Serializable {
                /**
                 * content : PS
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("content")
                private String content;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }

        @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
        public static class TransportDTO implements Serializable {
            /**
             * Security : {"enabled":"true","SecurityAlgorithm":{"algorithmType":"MD5"},"certificateType":"digest"}
             * ControlProtocolList : {"ControlProtocol":[{"streamingTransport":"RTSP"},{"streamingTransport":"HTTP"},{"streamingTransport":"SHTTP"}]}
             * Multicast : {"videoDestPortNo":"8860","audioDestPortNo":"8862","enabled":"true","FecInfo":{"fecDestPortNo":"9860","fecRatio":"0"},"destIPAddress":"0.0.0.0"}
             * Unicast : {"enabled":"true","rtpTransportType":"RTP/TCP"}
             * maxPacketSize : 1000
             */

            @com.fasterxml.jackson.annotation.JsonProperty("Security")
            private SecurityDTO Security;
            @com.fasterxml.jackson.annotation.JsonProperty("ControlProtocolList")
            private ControlProtocolListDTO ControlProtocolList;
            @com.fasterxml.jackson.annotation.JsonProperty("Multicast")
            private MulticastDTO Multicast;
            @com.fasterxml.jackson.annotation.JsonProperty("Unicast")
            private UnicastDTO Unicast;
            @com.fasterxml.jackson.annotation.JsonProperty("maxPacketSize")
            private String maxPacketSize;

            public SecurityDTO getSecurity() {
                return Security;
            }

            public void setSecurity(SecurityDTO Security) {
                this.Security = Security;
            }

            public ControlProtocolListDTO getControlProtocolList() {
                return ControlProtocolList;
            }

            public void setControlProtocolList(ControlProtocolListDTO ControlProtocolList) {
                this.ControlProtocolList = ControlProtocolList;
            }

            public MulticastDTO getMulticast() {
                return Multicast;
            }

            public void setMulticast(MulticastDTO Multicast) {
                this.Multicast = Multicast;
            }

            public UnicastDTO getUnicast() {
                return Unicast;
            }

            public void setUnicast(UnicastDTO Unicast) {
                this.Unicast = Unicast;
            }

            public String getMaxPacketSize() {
                return maxPacketSize;
            }

            public void setMaxPacketSize(String maxPacketSize) {
                this.maxPacketSize = maxPacketSize;
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class SecurityDTO implements Serializable {
                /**
                 * enabled : true
                 * SecurityAlgorithm : {"algorithmType":"MD5"}
                 * certificateType : digest
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("enabled")
                private String enabled;
                @com.fasterxml.jackson.annotation.JsonProperty("SecurityAlgorithm")
                private SecurityAlgorithmDTO SecurityAlgorithm;
                @com.fasterxml.jackson.annotation.JsonProperty("certificateType")
                private String certificateType;

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }

                public SecurityAlgorithmDTO getSecurityAlgorithm() {
                    return SecurityAlgorithm;
                }

                public void setSecurityAlgorithm(SecurityAlgorithmDTO SecurityAlgorithm) {
                    this.SecurityAlgorithm = SecurityAlgorithm;
                }

                public String getCertificateType() {
                    return certificateType;
                }

                public void setCertificateType(String certificateType) {
                    this.certificateType = certificateType;
                }

                @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
                public static class SecurityAlgorithmDTO implements Serializable {
                    /**
                     * algorithmType : MD5
                     */

                    @com.fasterxml.jackson.annotation.JsonProperty("algorithmType")
                    private String algorithmType;

                    public String getAlgorithmType() {
                        return algorithmType;
                    }

                    public void setAlgorithmType(String algorithmType) {
                        this.algorithmType = algorithmType;
                    }
                }
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class ControlProtocolListDTO implements Serializable {
                @com.fasterxml.jackson.annotation.JsonProperty("ControlProtocol")
                private List<ControlProtocolDTO> ControlProtocol;

                public List<ControlProtocolDTO> getControlProtocol() {
                    return ControlProtocol;
                }

                public void setControlProtocol(List<ControlProtocolDTO> ControlProtocol) {
                    this.ControlProtocol = ControlProtocol;
                }

                @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
                public static class ControlProtocolDTO implements Serializable {
                    /**
                     * streamingTransport : RTSP
                     */

                    @com.fasterxml.jackson.annotation.JsonProperty("streamingTransport")
                    private String streamingTransport;

                    public String getStreamingTransport() {
                        return streamingTransport;
                    }

                    public void setStreamingTransport(String streamingTransport) {
                        this.streamingTransport = streamingTransport;
                    }
                }
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class MulticastDTO implements Serializable {
                /**
                 * videoDestPortNo : 8860
                 * audioDestPortNo : 8862
                 * enabled : true
                 * FecInfo : {"fecDestPortNo":"9860","fecRatio":"0"}
                 * destIPAddress : 0.0.0.0
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("videoDestPortNo")
                private String videoDestPortNo;
                @com.fasterxml.jackson.annotation.JsonProperty("audioDestPortNo")
                private String audioDestPortNo;
                @com.fasterxml.jackson.annotation.JsonProperty("enabled")
                private String enabled;
                @com.fasterxml.jackson.annotation.JsonProperty("FecInfo")
                private FecInfoDTO FecInfo;
                @com.fasterxml.jackson.annotation.JsonProperty("destIPAddress")
                private String destIPAddress;

                public String getVideoDestPortNo() {
                    return videoDestPortNo;
                }

                public void setVideoDestPortNo(String videoDestPortNo) {
                    this.videoDestPortNo = videoDestPortNo;
                }

                public String getAudioDestPortNo() {
                    return audioDestPortNo;
                }

                public void setAudioDestPortNo(String audioDestPortNo) {
                    this.audioDestPortNo = audioDestPortNo;
                }

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }

                public FecInfoDTO getFecInfo() {
                    return FecInfo;
                }

                public void setFecInfo(FecInfoDTO FecInfo) {
                    this.FecInfo = FecInfo;
                }

                public String getDestIPAddress() {
                    return destIPAddress;
                }

                public void setDestIPAddress(String destIPAddress) {
                    this.destIPAddress = destIPAddress;
                }

                @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
                public static class FecInfoDTO implements Serializable {
                    /**
                     * fecDestPortNo : 9860
                     * fecRatio : 0
                     */

                    @com.fasterxml.jackson.annotation.JsonProperty("fecDestPortNo")
                    private String fecDestPortNo;
                    @com.fasterxml.jackson.annotation.JsonProperty("fecRatio")
                    private String fecRatio;

                    public String getFecDestPortNo() {
                        return fecDestPortNo;
                    }

                    public void setFecDestPortNo(String fecDestPortNo) {
                        this.fecDestPortNo = fecDestPortNo;
                    }

                    public String getFecRatio() {
                        return fecRatio;
                    }

                    public void setFecRatio(String fecRatio) {
                        this.fecRatio = fecRatio;
                    }
                }
            }

            @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
            public static class UnicastDTO implements Serializable {
                /**
                 * enabled : true
                 * rtpTransportType : RTP/TCP
                 */

                @com.fasterxml.jackson.annotation.JsonProperty("enabled")
                private String enabled;
                @com.fasterxml.jackson.annotation.JsonProperty("rtpTransportType")
                private String rtpTransportType;

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }

                public String getRtpTransportType() {
                    return rtpTransportType;
                }

                public void setRtpTransportType(String rtpTransportType) {
                    this.rtpTransportType = rtpTransportType;
                }
            }
        }
    }
}
