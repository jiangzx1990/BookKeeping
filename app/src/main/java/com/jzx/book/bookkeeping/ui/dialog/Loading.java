package com.jzx.book.bookkeeping.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;

/**
 * Created by Jzx on 2019/1/16
 */
public class Loading extends Dialog {
    private TextView tvLoadingTips;
    public Loading(@NonNull Context context) {
        super(context, R.style.Loading);
        setContentView(R.layout.layout_loading_dialog);
        tvLoadingTips = findViewById(R.id.tvLoadingTips);
        setCanceledOnTouchOutside(false);
    }

    public void setLoadingText(CharSequence text){
        tvLoadingTips.setText(text);
    }
}
