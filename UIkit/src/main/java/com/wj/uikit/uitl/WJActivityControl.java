package com.wj.uikit.uitl;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: WJActivityControl
 * Author: xiongxiang
 * Date: 2021/3/24
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJActivityControl {
    static WJActivityControl control = new WJActivityControl();
    public List<Activity> mActivityList = new ArrayList<>();

    public static WJActivityControl getInstance() {
        return control;
    }

    public void finishActivity(Class<?> classz) {
        for (Activity activity : mActivityList) {
            if (activity.getClass().getSimpleName().equals(classz.getSimpleName())) {
                activity.finish();
                return;
            }
        }
    }

    public void register(Activity activity) {
        mActivityList.add(activity);
    }

    public void unregister(Activity activity) {
        mActivityList.remove(activity);
    }

}
