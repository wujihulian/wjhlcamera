package com.wj.uikit.db;

import android.content.ContentValues;

import com.wj.camera.response.DeviceCameraData;
import com.wj.camera.response.DeviceData;
import com.wj.camera.response.RtmpConfig;

import java.io.Serializable;

/**
 * FileName: DeviceInfo
 * Author: xiongxiang
 * Date: 2021/1/18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DeviceInfo implements Serializable {
    public int id;
    public String device_serial;
    public String device_code;
    public String device_type;
    public String devIndex;

    public String networkMode;//有线或者无线 设备网络, 0无, 1有线, 2无线',
    public String ipAaddress;//ip地址
    public String ssid;
    //设备类型
    public String device_factory;

    public DeviceData deviceData;
    public RtmpConfig rtmpConfig;
    public DeviceCameraData deviceCamera;

    public String getDevIndex() {
        return devIndex;
    }

    public void setDevIndex(String devIndex) {
        this.devIndex = devIndex;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setIpAaddress(String ipAaddress) {
        this.ipAaddress = ipAaddress;
    }

    public String getIpAaddress() {
        return ipAaddress;
    }

    public String getDevice_serial() {
        return device_serial;
    }

    public String getDevice_code() {
        return device_code;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("device_serial", device_serial);
        contentValues.put("device_code", device_code);
        contentValues.put("device_type", device_type);
        contentValues.put("device_factory", device_factory);
        return contentValues;
    }
}
