package com.xx.wjcamera;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.wj.camera.net.OkHttpUtils;
import com.wj.uikit.uitl.WJUikit;


/**
 * FileName: App
 * Author: xiongxiang
 * Date: 2021/5/25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        WJUikit.init(this, "2390420812811299", "asdiubpfhaishfipaeqhf319842301");
        OkHttpUtils.getInstance(this);

    }
}
