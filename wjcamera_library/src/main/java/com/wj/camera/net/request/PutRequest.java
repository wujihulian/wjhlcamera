package com.wj.camera.net.request;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * FileName: getRequest
 * Author: xiongxiang
 * Date: 2021/6/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class PutRequest extends BaseRequest<PutRequest> {

    public PutRequest(String url) {
        super(url);
    }

    @Override
    RequestBody createRequestBody() {
        MediaType mediaType = MediaType.parse("application/xml;charset=utf-8");
        RequestBody requestBody = RequestBody.create(getJson()+"", mediaType);
        return requestBody;
    }

    @Override
    Request.Builder buildRequest() {
        return new Request.Builder().url(getFullUrl()).put(createRequestBody());
    }


}
