package com.wj.camera.callback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * FileName: ResponseStatusCallback
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class XmlCallback extends ResponseCallback {
    public XmlCallback(JsonCallback jsonCallback) {
        super(jsonCallback);
    }

    @Override
    public String getJson(@NotNull Call call, @NotNull Response response) throws IOException {
        ResponseBody body = response.body();

        String xml = Objects.requireNonNull(body).string();
        System.out.println("请求数据--->"+xml);
        return new XmlToJson.Builder(xml).build().toString();
    }

}
