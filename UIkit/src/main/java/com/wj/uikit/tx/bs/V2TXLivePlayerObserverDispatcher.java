package com.wj.uikit.tx.bs;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePlayer;
import com.tencent.live2.V2TXLivePlayerObserver;
import com.wj.camera.uitl.WJLogUitl;

/**
 * FileName: V2TXLivePlayerObserverDispatcher
 * Author: xiongxiang
 * Date: 2021/8/25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class V2TXLivePlayerObserverDispatcher extends TXSingleLivePlayerObserver {
    private static final String TAG = "V2TXLivePlayerObserverD";
    public TXReceiverGroup mReceiverGroup;
    public V2TXLivePlayerObserverDispatcher(TXReceiverGroup txReceiverGroup) {
        this.mReceiverGroup = txReceiverGroup;
    }
    @Override
    public void onError(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
        super.onError(player, code, msg, extraInfo);
        WJLogUitl.i(TAG, "onError: " + code + "   " + msg);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onError(player, code, msg, extraInfo);
                }
            }
        });
    }

    @Override
    public void onWarning(V2TXLivePlayer player, int code, String msg, Bundle extraInfo) {
        super.onWarning(player, code, msg, extraInfo);
        WJLogUitl.i(TAG, "onWarning: " + code + "   " + msg);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onWarning(player, code, msg, extraInfo);
                }
            }
        });
    }

    @Override
    public void onAudioPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
        super.onAudioPlaying(player, firstPlay, extraInfo);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onAudioPlaying(player,firstPlay, extraInfo);
                }
            }
        });
    }

    @Override
    public void onAudioLoading(V2TXLivePlayer player, Bundle extraInfo) {
        super.onAudioLoading(player, extraInfo);

        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onAudioLoading(player, extraInfo);
                }
            }
        });

    }
    /*  @Override
    public void onAudioPlayStatusUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayStatus status, V2TXLiveDef.V2TXLiveStatusChangeReason reason, Bundle extraInfo) {
        super.onAudioPlayStatusUpdate(player, status, reason, extraInfo);
        WJLogUitl.i(TAG, "onAudioPlayStatusUpdate: " + status);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onAudioPlayStatusUpdate(player, status, reason, extraInfo);
                }
            }
        });
    }*/

    @Override
    public void onPlayoutVolumeUpdate(V2TXLivePlayer player, int volume) {
        super.onPlayoutVolumeUpdate(player, volume);
        WJLogUitl.i(TAG, "onPlayoutVolumeUpdate: " + volume);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onPlayoutVolumeUpdate(player, volume);
                }
            }
        });
    }

    @Override
    public void onRenderVideoFrame(V2TXLivePlayer player, V2TXLiveDef.V2TXLiveVideoFrame videoFrame) {
        super.onRenderVideoFrame(player, videoFrame);
        WJLogUitl.i(TAG, "onRenderVideoFrame: ");
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onRenderVideoFrame(player, videoFrame);
                }
            }
        });
    }

    @Override
    public void onStatisticsUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayerStatistics statistics) {
        super.onStatisticsUpdate(player, statistics);
        WJLogUitl.i(TAG, "onStatisticsUpdate: " + statistics.fps);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onStatisticsUpdate(player, statistics);
                }
            }
        });

    }

    @Override
    public void onVideoPlaying(V2TXLivePlayer player, boolean firstPlay, Bundle extraInfo) {
        super.onVideoPlaying(player, firstPlay, extraInfo);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onVideoPlaying(player,firstPlay, extraInfo);
                }
            }
        });
    }

    @Override
    public void onVideoLoading(V2TXLivePlayer player, Bundle extraInfo) {
        super.onVideoLoading(player, extraInfo);
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onVideoLoading(player, extraInfo);
                }
            }
        });

    }

/*    @Override
    public void onVideoPlayStatusUpdate(V2TXLivePlayer player, V2TXLiveDef.V2TXLivePlayStatus status, V2TXLiveDef.V2TXLiveStatusChangeReason reason, Bundle extraInfo) {
        super.onVideoPlayStatusUpdate(player, status, reason, extraInfo);

        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onVideoPlayStatusUpdate(player, status, reason, extraInfo);
                }
            }
        });
    }*/

    @Override
    public void onSnapshotComplete(V2TXLivePlayer player, Bitmap image) {
        super.onSnapshotComplete(player, image);
        WJLogUitl.i(TAG, "onSnapshotComplete: ");
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onSnapshotComplete(player, image);
                }
            }
        });
    }

    @Override
    public void onReceiveSeiMessage(V2TXLivePlayer player, int payloadType, byte[] data) {
        super.onReceiveSeiMessage(player, payloadType, data);
        WJLogUitl.i(TAG, "onReceiveSeiMessage: ");
        mReceiverGroup.forEach(new TXIReceiverGroup.OnLoopListener() {
            @Override
            public void onEach(TXIReceiver receiver) {
                if (receiver instanceof V2TXLivePlayerObserver) {
                    ((V2TXLivePlayerObserver) receiver).onReceiveSeiMessage(player, payloadType, data);
                }
            }
        });
    }
}
