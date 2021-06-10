package com.wj.camera.net.request;


import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * FileName: PostFromRequest
 * Author: xiongxiang
 * Date: 2021/6/10
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class PostJsonRequest extends BaseRequest {
    public PostJsonRequest(String url) {
        super(url);
    }
    @Override
    RequestBody createRequestBody() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), getJson());
        return body;
    }
    @Override
    Request.Builder buildRequest() {
        return new Request.Builder().url(getFullUrl()).post(createRequestBody());
    }
}
