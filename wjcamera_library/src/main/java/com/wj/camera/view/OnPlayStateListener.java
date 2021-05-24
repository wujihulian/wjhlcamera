package com.wj.camera.view;

import android.os.Message;

/**
 * FileName: PlayStateListener
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface OnPlayStateListener {

    void playState(int state, Message data);
}
