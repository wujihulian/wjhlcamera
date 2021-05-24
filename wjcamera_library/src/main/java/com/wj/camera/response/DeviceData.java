package com.wj.camera.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * FileName: DeviceInfo
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceData implements Serializable {

    /**
     * deviceSerial : F15868201
     * deviceName : DS-2CD7T47DWD-IZSW(F15868201)
     * model : DS-2CD7T47DWD-IZSW
     * status : 1
     * defence : 0
     * isEncrypt : 0
     * alarmSoundMode : 2
     * offlineNotify : 0
     * category : UNKNOWN
     * parentCategory : COMMON
     * updateTime : 1609137940000
     * netType : null
     * signal : null
     */

    @JsonProperty("deviceSerial")
    private String deviceSerial;
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("model")
    private String model;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("defence")
    private Integer defence;
    @JsonProperty("isEncrypt")
    private Integer isEncrypt;
    @JsonProperty("alarmSoundMode")
    private Integer alarmSoundMode;
    @JsonProperty("offlineNotify")
    private Integer offlineNotify;
    @JsonProperty("category")
    private String category;
    @JsonProperty("parentCategory")
    private String parentCategory;
    @JsonProperty("updateTime")
    private Long updateTime;
    @JsonProperty("netType")
    private Object netType;
    @JsonProperty("signal")
    private Object signal;

    public String getDeviceSerial() {
        return deviceSerial;
    }

    public void setDeviceSerial(String deviceSerial) {
        this.deviceSerial = deviceSerial;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDefence() {
        return defence;
    }

    public void setDefence(Integer defence) {
        this.defence = defence;
    }

    public Integer getIsEncrypt() {
        return isEncrypt;
    }

    public void setIsEncrypt(Integer isEncrypt) {
        this.isEncrypt = isEncrypt;
    }

    public Integer getAlarmSoundMode() {
        return alarmSoundMode;
    }

    public void setAlarmSoundMode(Integer alarmSoundMode) {
        this.alarmSoundMode = alarmSoundMode;
    }

    public Integer getOfflineNotify() {
        return offlineNotify;
    }

    public void setOfflineNotify(Integer offlineNotify) {
        this.offlineNotify = offlineNotify;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Object getNetType() {
        return netType;
    }

    public void setNetType(Object netType) {
        this.netType = netType;
    }

    public Object getSignal() {
        return signal;
    }

    public void setSignal(Object signal) {
        this.signal = signal;
    }
}
