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

import java.text.SimpleDateFormat;
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
        startService(new Intent(getBaseContext(),RingtoneService.class));
        SPUtils.RingtoneSP.RingtoneInfo info = SPUtils.RingtoneSP.getRingtoneInfo();
        int type = AlarmManager.RTC_WAKEUP;
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.CHINESE);
        start.set(Calendar.HOUR_OF_DAY, info.getStartHour());
        start.set(Calendar.MINUTE, info.getStartMinute());
        start.set(Calendar.SECOND, 0);
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.CHINESE);
        end.set(Calendar.HOUR_OF_DAY, info.getEndHour());
        end.set(Calendar.MINUTE, info.getEndMinute());
        end.set(Calendar.SECOND, 0);
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"), Locale.CHINESE);
        long time;
        if (now.before(start)) {
            time = start.getTimeInMillis();
        } else if (now.before(end)) {
            time = end.getTimeInMillis();
        } else {
            start.add(Calendar.DAY_OF_WEEK, 1);
            time = start.getTimeInMillis();
        }
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent ringtoneIntent = new Intent(this, SettingRingtoneService.class);
        PendingIntent pending =
                PendingIntent.getService(this,
                        PENDING_INTENT_REQUEST_CODE_I,
                        ringtoneIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManagerCompat.setExact(alarm, type, time, pending);
        LogHelper.d("下次闹铃生效时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE).format(time));
        return super.onStartCommand(intent, flags, startId);
    }
}
