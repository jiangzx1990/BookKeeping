package com.jzx.book.bookkeeping.helper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {
    private static Toast sToast;
    public static void show(@NonNull String msg){
        if(sToast == null){
            sToast = Toast.makeText(ContextProvider.getContext(),"",Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.setGravity(Gravity.CENTER,0,0);
        sToast.setDuration(Toast.LENGTH_SHORT);
        sToast.show();
    }
}
