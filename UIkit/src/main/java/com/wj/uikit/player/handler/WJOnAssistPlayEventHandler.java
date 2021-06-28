package com.wj.uikit.player.handler;

import android.os.Bundle;

import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.assist.OnAssistPlayEventHandler;
import com.wj.uikit.player.event.WJInterEvent;

/**
 * FileName: WJOnAssistPlayEventHandler
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description: 扩展设置音量
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJOnAssistPlayEventHandler extends OnAssistPlayEventHandler {

    @Override
    public void onAssistHandle(AssistPlay assist, int eventCode, Bundle bundle) {
        super.onAssistHandle(assist, eventCode, bundle);
        switch (eventCode) {
            case WJInterEvent.CODE_SET_VOLUME_0:
                assist.setVolume(0, 0);
                break;
            case WJInterEvent.CODE_SET_VOLUME_1:
                assist.setVolume(100, 100);
                break;
        }
    }
}
