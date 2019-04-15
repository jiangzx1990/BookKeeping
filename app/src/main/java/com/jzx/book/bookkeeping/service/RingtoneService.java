package com.jzx.book.bookkeeping.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jzx.book.bookkeeping.helper.LogHelper;
import com.jzx.book.bookkeeping.sp.SPUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class RingtoneService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SPUtils.RingtoneSP.RingtoneInfo info = SPUtils.RingtoneSP.getRingtoneInfo();
        Calendar now = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
        int week = now.get(Calendar.DAY_OF_WEEK);
        AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (week >= Calendar.MONDAY && week <= Calendar.FRIDAY) {
            //获取当前时间
            Calendar start = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
            start.set(Calendar.HOUR_OF_DAY, info.getStartHour());
            start.set(Calendar.MINUTE, info.getStartMinute());

            Calendar end = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
            end.set(Calendar.HOUR_OF_DAY, info.getEndHour());
            end.set(Calendar.MINUTE, info.getEndMinute());

            boolean inRange = (now.after(start) || now.equals(start)) &&
                    (now.before(end) || now.equals(end));
            if (inRange) {
                if (info.getRingtoneWay() != am.getRingerMode()) {
                    am.setRingerMode(info.getRingtoneWay());
                }
            } else {
                if (info.getRingtoneWay() != AudioManager.RINGER_MODE_NORMAL) {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        } else {
            //周六、日
            if (am.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        }
        LogHelper.d( "情景模式设置成功");
        startService(new Intent(this,SettingRingtoneService.class));
        return super.onStartCommand(intent, flags, startId);
    }
}
