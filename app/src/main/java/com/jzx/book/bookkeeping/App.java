package com.jzx.book.bookkeeping;

import android.app.Application;

/**
 * Created by Jzx on 2019/1/17
 */
public class App extends Application {
    private static App context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static App getInstance(){
        return context;
    }
}
