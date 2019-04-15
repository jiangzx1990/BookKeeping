package com.jzx.book.bookkeeping.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.AlarmManagerCompat;

import com.jzx.book.bookkeeping.helper.LogHelper;
import com.jzx.book.bookkeeping.sp.SPUtils;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SettingRingtoneService extends Service {
    public static final int PENDING_INTENT_REQUEST_CODE_I = 101;
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
        SPUtils.RingtoneSP.RingtoneInfo info = SPUtils.RingtoneSP.getRingtoneInfo();
        if(info.isEnable()){
            int type = AlarmManager.RTC_WAKEUP;
            Calendar start = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"),Locale.CHINESE);
            start.set(Calendar.HOUR_OF_DAY,info.getStartHour());
            start.set(Calendar.MINUTE,info.getStartMinute());
            start.set(Calendar.SECOND,0);
            Calendar end = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"),Locale.CHINESE);
            end.set(Calendar.HOUR_OF_DAY,info.getEndHour());
            end.set(Calendar.MINUTE,info.getEndMinute());
            end.set(Calendar.SECOND,0);
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"),Locale.CHINESE);
            long time;
            if (now.before(start)){
                time = start.getTimeInMillis();
            }else if (now.before(end)){
                time = end.getTimeInMillis();
            }else{
                start.add(Calendar.DAY_OF_WEEK,1);
                time = start.getTimeInMillis();
            }
            AlarmManagerCompat.setExact(alarm,type,time,pending);
        }
        LogHelper.d("已重新设置闹铃");
        return super.onStartCommand(intent, flags, startId);
    }
}
