package com.wj.camera.config;

/**
 * FileName: WJRateTypeEnum
 * Author: xiongxiang
 * Date: 2021/7/6
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public enum WJRateTypeEnum {
    CBR("cbr", "固定码率"),//定码率
    VBR("vbr", "动态码率");//变码率
    private String rate;
    private String title;

    WJRateTypeEnum(String rate, String title) {
        this.rate = rate;
        this.title = title;
    }

    public String getRate() {
        return rate;
    }

    public String getTitle() {
        return title;
    }
}
