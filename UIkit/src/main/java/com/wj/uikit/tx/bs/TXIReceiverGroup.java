package com.wj.uikit.tx.bs;

import android.os.Bundle;

/**
 * FileName: TXIReceiverGroup
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface TXIReceiverGroup {
    void addReceiver(String key, TXIReceiver receiver);
    void removeReceiver(String  key);
    TXIReceiver getTXIReceiver(String  key);
    void forEach(TXIReceiverGroup.OnLoopListener onLoopListener);
    interface OnLoopListener{
        void onEach(TXIReceiver receiver);
    }
}
