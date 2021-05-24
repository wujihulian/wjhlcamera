package com.wj.camera.net;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * FileName: SafeGuardInterceptor
 * Author: xiongxiang
 * Date: 2021/3/18
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class SafeGuardInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        try {
            return chain.proceed(chain.request());
        } catch (Throwable e) {
            throw new IOException("SafeGuarded when requesting ${chain.request().url}", e);
        }
    }
}
