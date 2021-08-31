package com.wj.uikit.tx.bs;

/**
 * FileName: TXReceiverEventListener
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface TXReceiverEventListener {
    int startPlay();

    int startPlay(String var1);

    int stopPlay();

    int isPlaying();

    int pauseAudio();

    int resumeAudio();

    int pauseVideo();

    int resumeVideo();

    int setPlayoutVolume(int var1);

}
