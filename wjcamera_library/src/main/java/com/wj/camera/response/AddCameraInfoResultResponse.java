package com.wj.camera.response;

import java.util.List;

/**
 * Create by 金KingMgg  kingmgg
 * on 2023/9/19 10:56
 * 程序人员写程序，又拿程序换酒钱
 */
public class AddCameraInfoResultResponse {

    /**
     * Device : {"EhomeParams":{"EhomeID":"F87307358"},"devIndex":"E913A630-D9B5-0948-A8B0-EC11BFB8E212","devName":"F87307358","protocolType":"ehomeV5","status":"success"}
     */

    private List<DeviceOutListBean> DeviceOutList;

    public List<DeviceOutListBean> getDeviceOutList() {
        return DeviceOutList;
    }

    public void setDeviceOutList(List<DeviceOutListBean> DeviceOutList) {
        this.DeviceOutList = DeviceOutList;
    }

    public static class DeviceOutListBean {
        /**
         * EhomeParams : {"EhomeID":"F87307358"}
         * devIndex : E913A630-D9B5-0948-A8B0-EC11BFB8E212
         * devName : F87307358
         * protocolType : ehomeV5
         * status : success
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
            private String devIndex;
            private String devName;
            private String protocolType;
            private String status;

            public EhomeParamsBean getEhomeParams() {
                return EhomeParams;
            }

            public void setEhomeParams(EhomeParamsBean EhomeParams) {
                this.EhomeParams = EhomeParams;
            }

            public String getDevIndex() {
                return devIndex;
            }

            public void setDevIndex(String devIndex) {
                this.devIndex = devIndex;
            }

            public String getDevName() {
                return devName;
            }

            public void setDevName(String devName) {
                this.devName = devName;
            }

            public String getProtocolType() {
                return protocolType;
            }

            public void setProtocolType(String protocolType) {
                this.protocolType = protocolType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
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
