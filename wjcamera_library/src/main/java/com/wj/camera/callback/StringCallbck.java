package com.wj.camera.callback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * FileName: StringCallbck
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class StringCallbck extends ResponseCallback {


    public StringCallbck(JsonCallback jsonCallback) {
        super(jsonCallback);
    }

    @Override
    public String getJson(@NotNull Call call, @NotNull Response response) throws IOException {
        return response.body().string();
    }
}
