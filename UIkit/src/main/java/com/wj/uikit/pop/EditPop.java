package com.wj.uikit.pop;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.lxj.xpopup.core.CenterPopupView;
import com.wj.uikit.R;
import com.wj.uikit.adapter.OnItemClickListener;

/**
 * FileName: EditPop
 * Author: xiongxiang
 * Date: 2021/1/23
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class EditPop extends CenterPopupView {

    private OnItemClickListener<String> mOnConfirmListener;
    private String  mString;
    private int minLenght=-1;
    public void setOnConfirmListener(OnItemClickListener<String> onConfirmListener) {
        mOnConfirmListener = onConfirmListener;
    }
    public EditPop(Context context,String  title){
        super(context);
        this.mString=title;
    }
    public EditPop(Context context,String  title,int minLenght){
        super(context);
        this.mString=title;
        this.minLenght=minLenght;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.wj_edit_pop;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView title = findViewById(R.id.title);
        title.setText(mString);

        EditText content = findViewById(R.id.content);

        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView confirm = findViewById(R.id.confirm);
        if (minLenght>=1){
            confirm.setClickable(false);
            confirm.setTextColor(Color.parseColor("#999999"));

            content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!TextUtils.isEmpty(s)){
                            if (s.length()>=minLenght){
                                confirm.setClickable(true);
                                confirm.setTextColor(Color.parseColor("#FF108EE9"));
                            }else {
                                confirm.setClickable(false);
                                confirm.setTextColor(Color.parseColor("#999999"));
                            }
                        }else {
                            confirm.setClickable(false);
                            confirm.setTextColor(Color.parseColor("#999999"));
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }







        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmListener!=null){
                    mOnConfirmListener.onClick(content.getText().toString(),-1);
                }
                dismiss();
            }
        });

    }
}
