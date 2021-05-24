package com.wj.uikit;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wj.uikit.status.StatusBarUtil;
import com.wj.uikit.uitl.WJActivityControl;


/**
 * FileName: BaseUikitActivity
 * Author: xiongxiang
 * Date: 2021/1/21
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class BaseUikitActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WJActivityControl.getInstance().register(this);
        initStatusBar();
    }

    public void initStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
        StatusBarUtil.setStatusBarColor(this, Color.WHITE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WJActivityControl.getInstance().unregister(this);
    }


}
