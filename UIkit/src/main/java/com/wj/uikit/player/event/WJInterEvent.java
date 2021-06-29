package com.wj.uikit.player.event;

import com.kk.taurus.playerbase.assist.InterEvent;

/**
 * FileName: WJInterEvent
 * Author: xiongxiang
 * Date: 2021/6/22
 * Description: 新增设置音量事件
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface WJInterEvent extends InterEvent {

    int CODE_SET_VOLUME_0 = -66017;
    int CODE_SET_VOLUME_1 = -66018;
    int CODE_PLAY = -66019;
}
