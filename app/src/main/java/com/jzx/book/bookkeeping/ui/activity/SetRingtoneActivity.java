package com.jzx.book.bookkeeping.ui.activity;

import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.service.SettingRingtoneService;
import com.jzx.book.bookkeeping.sp.SPUtils;
import com.jzx.book.bookkeeping.utils.TimeUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class SetRingtoneActivity extends BaseActivity implements View.OnClickListener {
    private static final int START = 0;
    private static final int END = 1;

    private RadioGroup rgpRingWay;

    private TextView tvStart;
    private TextView tvEnd;

    private CheckBox cbxRingtoneMode;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_timing_setting_ring;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        rgpRingWay = findViewById(R.id.rgpRingWay);
        //震动
        RadioButton rbtShock = rgpRingWay.findViewById(R.id.rbtShock);
        //静音
        RadioButton rbtMute = rgpRingWay.findViewById(R.id.rbtMute);

        findViewById(R.id.tvEnsure).setOnClickListener(this);
        tvStart = findViewById(R.id.tvStart);
        tvStart.setText(TimeUtils.convertTimeStr(info.getStartHour(),info.getStartMinute()));
        tvStart.setOnClickListener(this);
        tvEnd = findViewById(R.id.tvEnd);
        tvEnd.setText(TimeUtils.convertTimeStr(info.getEndHour(),info.getEndMinute()));
        tvEnd.setOnClickListener(this);

        cbxRingtoneMode = findViewById(R.id.cbxRingtoneMode);

        if(info.getRingtoneWay() == AudioManager.RINGER_MODE_VIBRATE){
            rbtShock.setChecked(true);
        }else{
            rbtMute.setChecked(true);
        }

        cbxRingtoneMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonView.setText(R.string.app_text_enable_ringtone_scene);
                }else{
                    buttonView.setText(R.string.app_text_disable_ringtone_scene);
                }
            }
        });

        cbxRingtoneMode.setChecked(info.isEnable());
        cbxRingtoneMode.setText(info.isEnable()?
                R.string.app_text_enable_ringtone_scene:
                R.string.app_text_disable_ringtone_scene);
    }

    private TimePickerDialog timeDialog;
    private SPUtils.RingtoneSP.RingtoneInfo info = SPUtils.RingtoneSP.getRingtoneInfo();
    private int witch = START;
    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(witch == START){
                info.setStartHour(hourOfDay);
                info.setEndMinute(minute);
                tvStart.setText(TimeUtils.convertTimeStr(hourOfDay,minute));
            }else{
                info.setEndHour(hourOfDay);
                info.setEndMinute(minute);
                tvEnd.setText(TimeUtils.convertTimeStr(hourOfDay,minute));
            }
        }
    };

    private void showTimePickerDialog(final int witch){
        this.witch = witch;
        if(timeDialog == null){
            timeDialog = new TimePickerDialog(this, listener,info.getStartHour(),info.getStartMinute(),true);
        }
        if(witch == START){
            timeDialog.updateTime(info.getStartHour(),info.getStartMinute());
        }else{
            timeDialog.updateTime(info.getEndHour(),info.getEndMinute());
        }
        if(!timeDialog.isShowing()){
            timeDialog.show();
        }
    }

    private boolean check(){
        if(rgpRingWay.getCheckedRadioButtonId() == R.id.rbtMute){
            info.setRingtoneWay(AudioManager.RINGER_MODE_SILENT);
        }else{
            info.setRingtoneWay(AudioManager.RINGER_MODE_VIBRATE);
        }
        //检查开始时间与结束时间
        Calendar start = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
        start.set(Calendar.HOUR_OF_DAY,info.getStartHour());
        start.set(Calendar.MINUTE,info.getStartMinute());

        Calendar end = Calendar.getInstance(TimeZone.getDefault(),Locale.CHINESE);
        end.set(Calendar.HOUR_OF_DAY,info.getEndHour());
        end.set(Calendar.MINUTE,info.getEndMinute());

        Snackbar bar = Snackbar.make(toolbar,"",Snackbar.LENGTH_LONG);
        if(start.after(end)){
            bar.setText("结束时间不能小于开始时间");
            bar.show();
            return false;
        }else{
            start.add(Calendar.MINUTE,30);
            if(start .after(end)){
                bar.setText("结束时间与开始时间间隔需大于30分钟");
                bar.show();
                return false;
            }
        }

        info.setEnable(cbxRingtoneMode.isChecked());
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvStart:
                showTimePickerDialog(START);
                break;
            case R.id.tvEnd:
                showTimePickerDialog(END);
                break;
            case R.id.tvEnsure:
                if(check()){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if(!manager.isNotificationPolicyAccessGranted()){
                            Toast toast = Toast.makeText(this,"",Toast.LENGTH_LONG);
                            toast.setText("需要授权应用启用|禁用免打扰");
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                            Intent requestPermission = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                            startActivity(requestPermission);
                            return;
                        }
                    }
                    SPUtils.RingtoneSP.setRingtoneInfo(info.getRingtoneWay(),
                            info.getStartHour(),
                            info.getStartMinute(),
                            info.getEndHour(),
                            info.getEndMinute(),
                            info.isEnable());
                    startService(new Intent(this, SettingRingtoneService.class));
                    finish();
                }
                break;
        }
    }
}
