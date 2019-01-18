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
public class AddContactDialog extends Dialog {
    private TextInputLayout tilContactName;
    private EditText etContactName;

    private TextInputLayout tilContactTell;
    private EditText etContactTell;

    public interface OnAddContactEnsure{
        void onEnsureClick(String name,String tell);
    }

    public AddContactDialog(@NonNull Context context, final OnAddContactEnsure listener) {
        super(context, R.style.CommonDialog);
        setContentView(R.layout.dialog_add_contact);

        tilContactName = findViewById(R.id.tilContactName);
        etContactName = tilContactName.findViewById(R.id.etContactName);

        tilContactTell = findViewById(R.id.tilContactTell);
        etContactTell = tilContactTell.findViewById(R.id.etContactTell);

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
                    listener.onEnsureClick(etContactName.getText().toString().trim(),
                            etContactTell.getText().toString().trim());
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
        tilContactName.setError(text);
        tilContactName.setErrorEnabled(true);
    }

    public void hideNameError(){
        tilContactName.setErrorEnabled(false);
    }

    public void showTellError(CharSequence text){
        tilContactTell.setError(text);
        tilContactTell.setErrorEnabled(true);
    }

    public void hideTellError(){
        tilContactTell.setErrorEnabled(false);
    }
}
