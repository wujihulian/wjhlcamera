package com.wj.uikit.tx.bs;

import android.os.Bundle;

/**
 * FileName: TXIReceiver
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public interface TXIReceiver {
    void onReceiverBind();
    void bindTXReceiverGroup(TXReceiverGroup receiverGroup);
    void event(Bundle build);
    void bindReceiverEventListener(TXReceiverEventListener observer);

}
