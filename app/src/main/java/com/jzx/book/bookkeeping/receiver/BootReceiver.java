package com.jzx.book.bookkeeping.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jzx.book.bookkeeping.service.SettingRingtoneService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, SettingRingtoneService.class));
        }
    }
}
