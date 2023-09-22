package com.wj.camera.request;

import java.util.List;

/**
 * Create by 金KingMgg  kingmgg
 * on 2023/7/28 10:07
 * 程序人员写程序，又拿程序换酒钱
 */
public class RequestAddDeviceInfo {

    /**
     * Device : {"protocolType":"ehomeV5","EhomeParams":{"EhomeID":"K1T642","EhomeKey":"Abc12345"},"devName":"test1","devType":"AccessControl"}
     */

    private List<DeviceInListBean> DeviceInList;

    public List<DeviceInListBean> getDeviceInList() {
        return DeviceInList;
    }

    public void setDeviceInList(List<DeviceInListBean> DeviceInList) {
        this.DeviceInList = DeviceInList;
    }

    public static class DeviceInListBean {
        /**
         * protocolType : ehomeV5
         * EhomeParams : {"EhomeID":"K1T642","EhomeKey":"Abc12345"}
         * devName : test1
         * devType : AccessControl
         */

        private DeviceBean Device;

        public DeviceBean getDevice() {
            return Device;
        }

        public void setDevice(DeviceBean Device) {
            this.Device = Device;
        }

        public static class DeviceBean {
            private String protocolType="ehomeV5";
            /**
             * EhomeID : K1T642
             * EhomeKey : Abc12345
             */

            private EhomeParamsBean EhomeParams;
            private String devName;
//            private String devType="AccessControl";
            private String devType="encodingDev";

            public String getProtocolType() {
                return protocolType;
            }

            public void setProtocolType(String protocolType) {
                this.protocolType = protocolType;
            }

            public EhomeParamsBean getEhomeParams() {
                return EhomeParams;
            }

            public void setEhomeParams(EhomeParamsBean EhomeParams) {
                this.EhomeParams = EhomeParams;
            }

            public String getDevName() {
                return devName;
            }

            public void setDevName(String devName) {
                this.devName = devName;
            }

            public String getDevType() {
                return devType;
            }

            public void setDevType(String devType) {
                this.devType = devType;
            }

            public static class EhomeParamsBean {
                private String EhomeID;
                private String EhomeKey;

                public String getEhomeID() {
                    return EhomeID;
                }

                public void setEhomeID(String EhomeID) {
                    this.EhomeID = EhomeID;
                }

                public String getEhomeKey() {
                    return EhomeKey;
                }

                public void setEhomeKey(String EhomeKey) {
                    this.EhomeKey = EhomeKey;
                }
            }
        }
    }
}
