package com.jzx.book.bookkeeping.ui.pop;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.jzx.book.bookkeeping.R;

/**
 * Created by Jzx on 2019/1/18
 */
public class ChooseDateDialog extends PopupWindow {
    // TODO: 2019/1/18 调整日期选择样式 
    public ChooseDateDialog(@NonNull Context context, final OnDateEnsureListener listener) {
        View root = LayoutInflater.from(context).inflate(R.layout.pop_choose_date,null);
        final DatePicker picker = root.findViewById(R.id.datePicker);
        root.findViewById(R.id.tvEnsure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    int year = picker.getYear();
                    int month = picker.getMonth() + 1;
                    int day = picker.getDayOfMonth();
                    listener.onDatePicked(year + "年" + month + "月" + day + "日");
                }
            }
        });

        setContentView(root);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public interface OnDateEnsureListener {
        void onDatePicked(String date);
    }

}
