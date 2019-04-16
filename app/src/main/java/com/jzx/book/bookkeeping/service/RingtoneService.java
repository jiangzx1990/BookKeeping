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
        if(info.isEnable()){
            Calendar now = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
            int week = now.get(Calendar.DAY_OF_WEEK);
            AudioManager am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

            int ringerModeCur = am.getRingerMode();
            int ringerModeTarget;

            if (week >= Calendar.MONDAY && week <= Calendar.FRIDAY) {
                //获取当前时间
                Calendar start = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
                start.set(Calendar.HOUR_OF_DAY, info.getStartHour());
                start.set(Calendar.MINUTE, info.getStartMinute());
                start.set(Calendar.SECOND,0);

                Calendar end = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
                end.set(Calendar.HOUR_OF_DAY, info.getEndHour());
                end.set(Calendar.MINUTE, info.getEndMinute());
                end.set(Calendar.SECOND,0);

                boolean inRange = now.after(start)  && now.before(end);

                if (inRange) {
                    ringerModeTarget = info.getRingtoneWay();
                } else {
                    ringerModeTarget = AudioManager.RINGER_MODE_NORMAL;
                }
            } else {
                //周六、日
                ringerModeTarget = AudioManager.RINGER_MODE_NORMAL;
            }
            if(ringerModeCur != ringerModeTarget){
                am.setRingerMode(ringerModeTarget);
                LogHelper.d("设置响铃模式成功：" + ringerModeTarget);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
