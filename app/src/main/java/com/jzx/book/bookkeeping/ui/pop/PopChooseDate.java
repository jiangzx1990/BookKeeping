package com.jzx.book.bookkeeping.ui.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.PopupWindow;

import com.jzx.book.bookkeeping.R;

/**
 * Created by Jzx on 2019/1/18
 */
public class PopChooseDate extends PopupWindow {
    public PopChooseDate(@NonNull Context context, final OnDateEnsureListener listener) {
        View root = LayoutInflater.from(context).inflate(R.layout.pop_choose_date,null);
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode ==  KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        final DatePicker picker = root.findViewById(R.id.datePicker);
        picker.setMaxDate(System.currentTimeMillis());
        root.findViewById(R.id.tvEnsure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    StringBuilder sb = new StringBuilder();
                    sb.append(picker.getYear());
                    sb.append("年");
                    int month = picker.getMonth() + 1;
                    if(month < 10){
                        sb.append(0);
                    }
                    sb.append(month);
                    sb.append("月");
                    int day = picker.getDayOfMonth();
                    if(day < 10){
                        sb.append("0");
                    }
                    sb.append(day);
                    sb.append("日");
                    listener.onDatePicked(sb.toString());
                }
            }
        });

        setContentView(root);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        setFocusable(true);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.BottomInOut);
    }

    public interface OnDateEnsureListener {
        void onDatePicked(String date);
    }

}
