package com.wj.camera.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by 金KingMgg  kingmgg
 * on 2023/7/25 16:07
 * 程序人员写程序，又拿程序换酒钱
 */
public class DeviceInfoListResponse {

    /**
     * MatchList : [{"Device":{"EhomeParams":{"EhomeID":"F87307358"},"ISAPIPortBound":false,"activeStatus":true,"devIndex":"04A94B24-981A-0341-AB2F-6A9C4918A6ED","devMode":"DS-2CD8E45F-W","devName":"F87307358","devStatus":"online","devType":"AccessControl","protocolType":"ehomeV5","videoChannelNum":1}}]
     * numOfMatches : 1
     * totalMatches : 1
     */

    private SearchResultBean SearchResult;

    public SearchResultBean getSearchResult() {
        return SearchResult;
    }

    public void setSearchResult(SearchResultBean SearchResult) {
        this.SearchResult = SearchResult;
    }

    public static class SearchResultBean {
        private long numOfMatches;
        private long totalMatches;
        /**
         * Device : {"EhomeParams":{"EhomeID":"F87307358"},"ISAPIPortBound":false,"activeStatus":true,"devIndex":"04A94B24-981A-0341-AB2F-6A9C4918A6ED","devMode":"DS-2CD8E45F-W","devName":"F87307358","devStatus":"online","devType":"AccessControl","protocolType":"ehomeV5","videoChannelNum":1}
         */

        private List<MatchListBean> MatchList=new ArrayList<>();

        public long getNumOfMatches() {
            return numOfMatches;
        }

        public void setNumOfMatches(long numOfMatches) {
            this.numOfMatches = numOfMatches;
        }

        public long getTotalMatches() {
            return totalMatches;
        }

        public void setTotalMatches(long totalMatches) {
            this.totalMatches = totalMatches;
        }

        public List<MatchListBean> getMatchList() {
            return MatchList;
        }

        public void setMatchList(List<MatchListBean> MatchList) {
            this.MatchList = MatchList;
        }

        public static class MatchListBean {
            /**
             * EhomeParams : {"EhomeID":"F87307358"}
             * ISAPIPortBound : false
             * activeStatus : true
             * devIndex : 04A94B24-981A-0341-AB2F-6A9C4918A6ED
             * devMode : DS-2CD8E45F-W
             * devName : F87307358
             * devStatus : online
             * devType : AccessControl
             * protocolType : ehomeV5
             * videoChannelNum : 1
             */

            private DeviceBean Device;

            public DeviceBean getDevice() {
                return Device;
            }

            public void setDevice(DeviceBean Device) {
                this.Device = Device;
            }

            public static class DeviceBean {
                /**
                 * EhomeID : F87307358
                 */

                private EhomeParamsBean EhomeParams;
                private boolean ISAPIPortBound;
                private boolean activeStatus;
                private String devIndex;
                private String devMode;
                private String devName;
                private String devStatus;
                private String devType;
                private String protocolType;
                private long videoChannelNum;

                public EhomeParamsBean getEhomeParams() {
                    return EhomeParams;
                }

                public void setEhomeParams(EhomeParamsBean EhomeParams) {
                    this.EhomeParams = EhomeParams;
                }

                public boolean isISAPIPortBound() {
                    return ISAPIPortBound;
                }

                public void setISAPIPortBound(boolean ISAPIPortBound) {
                    this.ISAPIPortBound = ISAPIPortBound;
                }

                public boolean isActiveStatus() {
                    return activeStatus;
                }

                public void setActiveStatus(boolean activeStatus) {
                    this.activeStatus = activeStatus;
                }

                public String getDevIndex() {
                    return devIndex;
                }

                public void setDevIndex(String devIndex) {
                    this.devIndex = devIndex;
                }

                public String getDevMode() {
                    return devMode;
                }

                public void setDevMode(String devMode) {
                    this.devMode = devMode;
                }

                public String getDevName() {
                    return devName;
                }

                public void setDevName(String devName) {
                    this.devName = devName;
                }

                public String getDevStatus() {
                    return devStatus;
                }

                public void setDevStatus(String devStatus) {
                    this.devStatus = devStatus;
                }

                public String getDevType() {
                    return devType;
                }

                public void setDevType(String devType) {
                    this.devType = devType;
                }

                public String getProtocolType() {
                    return protocolType;
                }

                public void setProtocolType(String protocolType) {
                    this.protocolType = protocolType;
                }

                public long getVideoChannelNum() {
                    return videoChannelNum;
                }

                public void setVideoChannelNum(long videoChannelNum) {
                    this.videoChannelNum = videoChannelNum;
                }

                public static class EhomeParamsBean {
                    private String EhomeID;

                    public String getEhomeID() {
                        return EhomeID;
                    }

                    public void setEhomeID(String EhomeID) {
                        this.EhomeID = EhomeID;
                    }
                }
            }
        }
    }
}
