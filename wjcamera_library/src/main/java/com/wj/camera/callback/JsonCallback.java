package com.wj.camera.callback;

/**
 * FileName: ResponseCallback
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public abstract class JsonCallback<T> {

    public abstract void onSuccess(T data);

    public void onError(int code, String msg) {


    }

}
