package com.wj.camera.response;

import java.io.Serializable;

/**
 * FileName: ResponseStatus
 * Author: xiongxiang
 * Date: 2021/1/4
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class ResponseStatus implements Serializable {
    public Status ResponseStatus;
    public class Status implements Serializable{
        public String requestURL;
        public String statusCode;
        public String statusString;
        public String subStatusCode;
    }
}
