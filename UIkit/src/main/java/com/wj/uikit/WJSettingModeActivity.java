package com.wj.uikit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * FileName: WJSettingWiredActivity
 * Author: xiongxiang
 * Date: 2021/3/23
 * Description: 配网方式选择
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJSettingModeActivity extends BaseUikitActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wj_activity_setting_mode);
        initView();
    }

    private Bundle getBundle() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        return extras;
    }

    private void initView() {
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.fl_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WJSettingModeActivity.this, WJSettingWifiActivity.class);
                intent.putExtras(getBundle());
                startActivity(intent);
            }
        });
        findViewById(R.id.fl_wired).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WJSettingModeActivity.this, WJSettingWiredActivity.class);
                intent.putExtras(getBundle());
                startActivity(intent);
            }
        });


    }
}
