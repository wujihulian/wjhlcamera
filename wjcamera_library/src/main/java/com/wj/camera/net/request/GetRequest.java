package com.wj.camera.net.request;

import okhttp3.Request;

/**
 * FileName: getRequest
 * Author: xiongxiang
 * Date: 2021/6/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class GetRequest extends BaseRequest<GetRequest>{
    public GetRequest(String url) {
        super(url);
    }

    @Override
    Request.Builder buildRequest() {
        return new Request.Builder().url(getFullUrl()).get();
    }


}
