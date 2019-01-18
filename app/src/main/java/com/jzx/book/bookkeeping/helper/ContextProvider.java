package com.jzx.book.bookkeeping.helper;

import android.content.Context;

import com.jzx.book.bookkeeping.App;

/**
 * Created by Jzx on 2019/1/17
 */
public class ContextProvider {
    public static Context getContext(){
        return App.getInstance();
    }
}
