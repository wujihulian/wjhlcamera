package com.wj.uikit.player.event;

import android.text.TextUtils;

/**
 * FileName: ReconnectEventConfig
 * Author: xiongxiang
 * Date: 2021/6/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJReconnectEventConfig {
    public static String token;
    public static String host;
    public static boolean isWebRtc = false;


    public static String transformUrl(String url) {
        if (isWebRtc) {
            if (!TextUtils.isEmpty(url)) {
                return url.replace("https", "webrtc").replace(".flv", "");
            }
            return url;
        }
        return url;
    }
}
