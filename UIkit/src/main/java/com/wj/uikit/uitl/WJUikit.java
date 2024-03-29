package com.wj.uikit.uitl;

import android.app.Application;

import com.kk.taurus.exoplayer.ExoMediaPlayer;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.wj.camera.WJCamera;
import com.wj.camera.net.OkHttpUtils;
import com.wj.uikit.db.DBHelper;

/**
 * FileName: WJUikit
 * Author: xiongxiang
 * Date: 2021/5/27
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJUikit {


    public static void init(Application application,String appkey,String appId){
        WJCamera.getInstance().init(appkey,appId,application);
        DBHelper.init(application);
        PlayerLibrary.init(application);
        ExoMediaPlayer.init(application);
    }





}
