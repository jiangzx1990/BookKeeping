package com.jzx.book.bookkeeping.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.jzx.book.bookkeeping.R;

/**
 * Created by Jzx on 2019/1/17
 */
public class AddPayWayDialog extends Dialog {
    private TextInputLayout tilPayName;
    private EditText etPayName;

    public interface OnAddPayWayEnsure {
        void onEnsureClick(String name);
    }

    public AddPayWayDialog(@NonNull Context context, final OnAddPayWayEnsure listener) {
        super(context, R.style.CommonDialog);
        setContentView(R.layout.dialog_add_pay_way);

        tilPayName = findViewById(R.id.tilPayName);
        etPayName = tilPayName.findViewById(R.id.etPayName);

        findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.tvEnsure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onEnsureClick(etPayName.getText().toString().trim());
                }
            }
        });

        Window window = getWindow();
        if(window != null){
            WindowManager.LayoutParams params = window.getAttributes();
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            params.width = (int) (metrics.widthPixels * 0.75f);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }

        setCanceledOnTouchOutside(false);

    }

    public void showNameError(CharSequence text){
        tilPayName.setError(text);
        tilPayName.setErrorEnabled(true);
    }

    public void hideNameError(){
        tilPayName.setErrorEnabled(false);
    }
}
