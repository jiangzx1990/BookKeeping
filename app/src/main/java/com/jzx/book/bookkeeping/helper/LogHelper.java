package com.jzx.book.bookkeeping.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jzx.book.bookkeeping.BuildConfig;

public class LogHelper {
    private static final String TAG = "bookkeeping";
    public static void d(@NonNull String msg){
        if(!BuildConfig.BUILD_TYPE.equals("release")){
            Log.d(TAG,msg);
        }
    }

    public static void e(@NonNull String msg){
        if(!BuildConfig.BUILD_TYPE.equals("release")){
            Log.e(TAG,msg);
        }
    }
}
