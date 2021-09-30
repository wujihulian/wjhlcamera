package com.wj.uikit.tx.bs;

import android.os.Bundle;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileName: ReceiverGroup
 * Author: xiongxiang
 * Date: 2021/8/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class TXReceiverGroup  implements TXIReceiverGroup{
    private Map<String, TXIReceiver> mReceivers;
    private List<TXIReceiver> mReceiverArray;
    public TXReceiverGroup(){
        mReceivers = new ConcurrentHashMap<>(16);
        mReceiverArray = Collections.synchronizedList(new ArrayList<TXIReceiver>());
    }

    @Override
    public void addReceiver(String key, TXIReceiver receiver) {
        mReceivers.put(key,receiver);
        mReceiverArray.add(receiver);
    }
    @Override
    public void removeReceiver(String key) {
        TXIReceiver receiver = mReceivers.remove(key);
        mReceiverArray.remove(receiver);
    }



    @Override
    public TXIReceiver getTXIReceiver(String  key) {
        return mReceivers.get(key);
    }


    public void clear(){
        mReceivers.clear();
        mReceiverArray.clear();
        mReceivers=null;
        mReceiverArray=null;
    }


    @Override
    public void forEach(OnLoopListener onLoopListener) {
        if (mReceiverArray!=null) {
            for (TXIReceiver txiReceiver : mReceiverArray) {
                onLoopListener.onEach(txiReceiver);
            }
        }
    }

}
