package com.jzx.book.bookkeeping.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.AlarmManagerCompat;

import com.jzx.book.bookkeeping.sp.SPUtils;

public class SettingRingtoneService extends Service {
    public static final int PENDING_INTENT_REQUEST_CODE_I = 101;
    public static final long INTERVAL_TIME_MILLIS_L = 5*60*1000L;//5分钟
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent ringtoneIntent = new Intent(this, RingtoneService.class);
        PendingIntent pending =
                PendingIntent.getService(this,
                        PENDING_INTENT_REQUEST_CODE_I,
                        ringtoneIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pending);//先取消之前设置的
        if(SPUtils.RingtoneSP.getRingtoneInfo().isEnable()){
            int type = AlarmManager.RTC_WAKEUP;
            long time = System.currentTimeMillis() + INTERVAL_TIME_MILLIS_L;
            AlarmManagerCompat.setExact(alarm,type,time,pending);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
