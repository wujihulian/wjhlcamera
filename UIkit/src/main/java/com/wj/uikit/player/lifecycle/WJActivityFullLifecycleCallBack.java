package com.wj.uikit.player.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * FileName: WJActivityFullLifecycleCallBack
 * Author: xiongxiang
 * Date: 2021/6/29
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface WJActivityFullLifecycleCallBack {

    void onCrete(@Nullable Bundle savedInstanceState, FrameLayout frameLayout, AppCompatActivity context);

    void onResume();

    void onPause();

    void onStart();

    void onDestroy();

    void onStop();
}
