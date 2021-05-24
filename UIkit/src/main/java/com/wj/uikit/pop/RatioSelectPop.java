package com.wj.uikit.pop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.xpopup.core.AttachPopupView;
import com.wj.uikit.R;
import com.wj.uikit.adapter.OnItemClickListener;

/**
 * FileName: SelectPop
 * Author: xiongxiang
 * Date: 2021/1/22
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class RatioSelectPop extends AttachPopupView {

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<String> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.wj_ratio_select_pop;
    }

    public RatioSelectPop(Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ViewGroup raito_ll = findViewById(R.id.raito_ll);
        int childCount = raito_ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView childAt = (TextView) raito_ll.getChildAt(i);
            int finalI = i;
            childAt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = childAt.getText().toString();
                    if (getOnItemClickListener() != null) {
                        getOnItemClickListener().onClick(str, finalI);
                    }
                    dismiss();
                }
            });
        }
    }
}
