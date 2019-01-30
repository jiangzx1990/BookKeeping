package com.jzx.book.bookkeeping.worker;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import com.jzx.book.bookkeeping.sp.SPUtils;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SetRingtoneWorker extends Worker {
    public static final String SET_RINGTONE_WORKER_S = "worker_set_ringtone";
    public static final long INTERVAL_TIME_MINUTE_L = 1L;

    public SetRingtoneWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
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
        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(SetRingtoneWorker.class)
                        .setInitialDelay(INTERVAL_TIME_MINUTE_L, TimeUnit.MINUTES)
                        .addTag(SET_RINGTONE_WORKER_S)
                        .build();
        WorkManager.getInstance()
                .beginUniqueWork(SET_RINGTONE_WORKER_S,
                        ExistingWorkPolicy.REPLACE,
                        request)
                .enqueue();
        return Result.success();
    }
}
