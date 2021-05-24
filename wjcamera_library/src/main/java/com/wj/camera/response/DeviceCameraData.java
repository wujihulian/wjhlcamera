package com.wj.camera.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: DeviceCameraData
 * Author: xiongxiang
 * Date: 2021/1/28
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCameraData implements Serializable {
        /**
         * deviceSerial : 427734222
         * ipcSerial : 427734222
         * channelNo : 1
         * deviceName : My(427734222)427734222
         * channelName : My(427734222)427734222
         * status : 1
         * isShared : 0
         * picUrl : https://portal.ys7.com/assets/imgs/public/homeDevice.jpeg
         * isEncrypt : 0
         * videoLevel : 2
         * relatedIpc : false
         */

        @JsonProperty("deviceSerial")
        private String deviceSerial;
        @JsonProperty("ipcSerial")
        private String ipcSerial;
        @JsonProperty("channelNo")
        private Integer channelNo;
        @JsonProperty("deviceName")
        private String deviceName;
        @JsonProperty("channelName")
        private String channelName;
        @JsonProperty("status")
        private Integer status;
        @JsonProperty("isShared")
        private String isShared;
        @JsonProperty("picUrl")
        private String picUrl;
        @JsonProperty("isEncrypt")
        private Integer isEncrypt;
        @JsonProperty("videoLevel")
        private Integer videoLevel;
        @JsonProperty("relatedIpc")
        private Boolean relatedIpc;


    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getIpcSerial() {
        return ipcSerial;
    }

    public void setIpcSerial(String ipcSerial) {
        this.ipcSerial = ipcSerial;
    }

    public Integer getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(Integer channelNo) {
        this.channelNo = channelNo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(Integer isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public Integer getVideoLevel() {
        return videoLevel;
    }

    public void setVideoLevel(Integer videoLevel) {
        this.videoLevel = videoLevel;
    }

    public Boolean getRelatedIpc() {
        return relatedIpc;
    }

    public void setRelatedIpc(Boolean relatedIpc) {
        this.relatedIpc = relatedIpc;
    }
}
