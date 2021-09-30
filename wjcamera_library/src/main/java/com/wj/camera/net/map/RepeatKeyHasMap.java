package com.wj.camera.net.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FileName: TagHasMap
 * Author: xiongxiang
 * Date: 2021/6/10
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class RepeatKeyHasMap<K, V> {
    HashMap<K, List<V>> mHashMap = new HashMap<>();

    public void put(K key, V vlaue) {
        List<V> vs = mHashMap.get(key);
        if (vs == null) {
            vs = new ArrayList<>();
            vs.add(vlaue);
        } else {
            vs.add(vlaue);
        }
    }

    public List<V> get(K key) {
        return mHashMap.get(key);
    }

    public void remove(K key) {
        mHashMap.remove(key);
    }

    public HashMap<K, List<V>> getHashMap() {
        return mHashMap;
    }
    public void cancel(){
        mHashMap.clear();
    }
}
