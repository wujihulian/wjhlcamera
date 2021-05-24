package com.wj.camera.response;

/**
 * FileName: checkDevcieUpdate
 * Author: xiongxiang
 * Date: 2021/5/6
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class CheckDevcieUpdate {
    String latestVersion;
    String currentVersion;
    int isNeedUpgrade;
    int isUpgrading;
    public DeviceUpdateStatus mDeviceResponse;
    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getIsNeedUpgrade() {
        return isNeedUpgrade;
    }

    public void setIsNeedUpgrade(int isNeedUpgrade) {
        this.isNeedUpgrade = isNeedUpgrade;
    }

    public int getIsUpgrading() {
        return isUpgrading;
    }

    public void setIsUpgrading(int isUpgrading) {
        this.isUpgrading = isUpgrading;
    }
}
