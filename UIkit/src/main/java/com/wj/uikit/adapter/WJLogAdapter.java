package com.wj.uikit.adapter;

import com.wj.uikit.R;

/**
 * FileName: WJLogAdapter
 * Author: xiongxiang
 * Date: 2022/4/28
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class WJLogAdapter extends BaseRecyclerViewAdapter<String> {
    @Override
    public int inflaterLayout(int viewType) {
        return R.layout.wj_log_item;
    }

    @Override
    public void convert(ViewHolder holder, String data, int position) {
        holder.setText(R.id.tv,data);
    }
}
