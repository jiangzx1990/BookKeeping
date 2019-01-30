package com.jzx.book.bookkeeping.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jzx.book.bookkeeping.sp.SPUtils;
import com.jzx.book.bookkeeping.worker.SetRingtoneWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class SettingRingtoneService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WorkManager.getInstance()
                .cancelAllWorkByTag(SetRingtoneWorker.SET_RINGTONE_WORKER_S);
        if(SPUtils.RingtoneSP.getRingtoneInfo().isEnable()){
            OneTimeWorkRequest request =
                    new OneTimeWorkRequest.Builder(SetRingtoneWorker.class)
                            .addTag(SetRingtoneWorker.SET_RINGTONE_WORKER_S)
                            .setInitialDelay(SetRingtoneWorker.INTERVAL_TIME_MINUTE_L, TimeUnit.MINUTES)
                            .build();
            WorkManager.getInstance()
                    .beginUniqueWork(SetRingtoneWorker.SET_RINGTONE_WORKER_S,
                            ExistingWorkPolicy.REPLACE,
                            request)
                    .enqueue();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
