package com.wj.camera.net.request;


import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
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
public class PostFromRequest extends BaseRequest {
    public PostFromRequest(String url) {
        super(url);
    }
    @Override
    RequestBody createRequestBody() {
        FormBody.Builder build = new FormBody.Builder();
        HashMap<String,String> params = getParams();
        if (params!=null){
            for (Map.Entry<String, String> hasMap : params.entrySet()) {
                build.addEncoded(hasMap.getKey(),hasMap.getValue());
            }
        }
        return build.build();
    }
    @Override
    Request.Builder buildRequest() {
        return new Request.Builder().url(getFullUrl()).post(createRequestBody());
    }
}
