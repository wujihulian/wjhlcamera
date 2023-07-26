package com.wj.camera.requestEntity;

import java.util.List;

/**
 * Create by 金KingMgg  kingmgg
 * on 2023/7/21 10:23
 * 程序人员写程序，又拿程序换酒钱
 */
public class RequestDeviceListEntity {

    /**
     * position : 0
     * maxResult : 100
     * Filter : {"key":"","devType":"","protocolType":["ehomeV5"],"devStatus":["online","offline"]}
     */

    private SearchDescriptionBean SearchDescription;

    public SearchDescriptionBean getSearchDescription() {
        return SearchDescription;
    }

    public void setSearchDescription(SearchDescriptionBean SearchDescription) {
        this.SearchDescription = SearchDescription;
    }

    public static class SearchDescriptionBean {
        private int position=0;
        private int maxResult=100;
        /**
         * key :
         * devType :
         * protocolType : ["ehomeV5"]
         * devStatus : ["online","offline"]
         */

        private FilterBean Filter;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getMaxResult() {
            return maxResult;
        }

        public void setMaxResult(int maxResult) {
            this.maxResult = maxResult;
        }

        public FilterBean getFilter() {
            return Filter;
        }

        public void setFilter(FilterBean Filter) {
            this.Filter = Filter;
        }

        public static class FilterBean {
            private String key="";
            private String devType="";
            private List<String> protocolType;
            private List<String> devStatus;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getDevType() {
                return devType;
            }

            public void setDevType(String devType) {
                this.devType = devType;
            }

            public List<String> getProtocolType() {
                return protocolType;
            }

            public void setProtocolType(List<String> protocolType) {
                this.protocolType = protocolType;
            }

            public List<String> getDevStatus() {
                return devStatus;
            }

            public void setDevStatus(List<String> devStatus) {
                this.devStatus = devStatus;
            }
        }
    }
}
