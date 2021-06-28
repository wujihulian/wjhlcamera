package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: CameraDeviceLiveUrlResponse
 * Author: xiongxiang
 * Date: 2021/6/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class CameraDeviceLiveUrlResponse implements Serializable {
    int code;
    String success;

    public CameraDeviceLiveUrlData data;

    public class CameraDeviceLiveUrlData {
        String docplay;
        String docpub;

        public String getDocplay() {
            return docplay;
        }

        public void setDocplay(String docplay) {
            this.docplay = docplay;
        }

        public String getDocpub() {
            return docpub;
        }

        public void setDocpub(String docpub) {
            this.docpub = docpub;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public CameraDeviceLiveUrlData getData() {
        return data;
    }

    public void setData(CameraDeviceLiveUrlData data) {
        this.data = data;
    }
}
