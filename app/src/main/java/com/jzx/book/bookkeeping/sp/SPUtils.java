package com.jzx.book.bookkeeping.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

import com.jzx.book.bookkeeping.helper.ContextProvider;

public class SPUtils {
    public static class RingtoneSP{
        public static class RingtoneInfo{
            int ringtoneWay;
            int startHour;
            int startMinute;
            int endHour;
            int endMinute;
            boolean enable;

            public RingtoneInfo(int ringtoneWay,
                                int startHour,
                                int startMinute,
                                int endHour,
                                int endMinute,
                                boolean enable) {
                this.ringtoneWay = ringtoneWay;
                this.startHour = startHour;
                this.startMinute = startMinute;
                this.endHour = endHour;
                this.endMinute = endMinute;
                this.enable = enable;
            }

            public void setRingtoneWay(int ringtoneWay) {
                this.ringtoneWay = ringtoneWay;
            }

            public void setStartHour(int startHour) {
                this.startHour = startHour;
            }

            public void setStartMinute(int startMinute) {
                this.startMinute = startMinute;
            }

            public void setEndHour(int endHour) {
                this.endHour = endHour;
            }

            public void setEndMinute(int endMinute) {
                this.endMinute = endMinute;
            }

            public void setEnable(boolean enable) {
                this.enable = enable;
            }

            public int getRingtoneWay() {
                return ringtoneWay;
            }

            public int getStartHour() {
                return startHour;
            }

            public int getEndHour() {
                return endHour;
            }

            public int getStartMinute() {
                return startMinute;
            }

            public int getEndMinute() {
                return endMinute;
            }

            public boolean isEnable() {
                return enable;
            }
        }
        private static final String SP_FILE_NAME_S = "sp_ringtone";
        private static final String KEY_RINGTONE_WAY_I = "ringtone_way";
        private static final String KEY_RINGTONE_HOUR_START_I = "ringtone_hour_start";
        private static final String KEY_RINGTONE_MINUTE_START_I = "ringtone_minute_start";
        private static final String KEY_RINGTONE_HOUR_END_I = "ringtone_hour_end";
        private static final String KEY_RINGTONE_MINUTE_END_I = "ringtone_minute_end";
        private static final String KEY_ENABLE_RINGTONE_B = "enable_ringtone_scene";

        public static final int RINGTONE_HOUR_START_DEFAULT = 9;
        public static final int RINGTONE_HOUR_END_DEFAULT = 18;
        public static final int RINGTONE_MINUTE_DEFAULT = 0;

        public static boolean setRingtoneInfo(int ringtoneWay,
                                              int startTimeHour,
                                              int startTimeMinute,
                                              int endTimeHour,
                                              int endTimeMinute,
                                              boolean enable){
            Context context = ContextProvider.getContext();
            SharedPreferences.Editor editor =
                    context.getSharedPreferences(SP_FILE_NAME_S,Context.MODE_PRIVATE).edit();
            editor.putInt(KEY_RINGTONE_WAY_I,ringtoneWay);
            editor.putInt(KEY_RINGTONE_HOUR_START_I,startTimeHour);
            editor.putInt(KEY_RINGTONE_MINUTE_START_I,startTimeMinute);
            editor.putInt(KEY_RINGTONE_HOUR_END_I,endTimeHour);
            editor.putInt(KEY_RINGTONE_MINUTE_END_I,endTimeMinute);
            editor.putBoolean(KEY_ENABLE_RINGTONE_B,enable);
            return editor.commit();
        }

        public static RingtoneInfo getRingtoneInfo(){
            Context context = ContextProvider.getContext();
            SharedPreferences pre = context.getSharedPreferences(SP_FILE_NAME_S,Context.MODE_PRIVATE);
            return new RingtoneInfo(pre.getInt(KEY_RINGTONE_WAY_I, AudioManager.RINGER_MODE_VIBRATE),
                    pre.getInt(KEY_RINGTONE_HOUR_START_I, RINGTONE_HOUR_START_DEFAULT),
                    pre.getInt(KEY_RINGTONE_MINUTE_START_I,RINGTONE_MINUTE_DEFAULT),
                    pre.getInt(KEY_RINGTONE_HOUR_END_I, RINGTONE_HOUR_END_DEFAULT),
                    pre.getInt(KEY_RINGTONE_MINUTE_END_I,RINGTONE_MINUTE_DEFAULT),
                    pre.getBoolean(KEY_ENABLE_RINGTONE_B,true));
        }
    }
}
