package com.wj.camera.response;

/**
 * FileName: DeviceUpdateStatus
 * Author: xiongxiang
 * Date: 2021/5/6
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class DeviceUpdateStatus {
    int progress;
    int status;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
