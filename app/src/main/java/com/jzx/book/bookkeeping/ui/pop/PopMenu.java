package com.jzx.book.bookkeeping.ui.pop;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;

/**
 * Created by Jzx on 2019/1/16
 */
public class PopMenu extends PopupWindow {
    public static final int MENU_PEOPLE_MANAGE = 0;
    public static final int MENU_PAY_TYPE_MANAGE = 1;
    public static final int MENU_PAY_FLOW = 2;
    public static final int MENU_OUT_IN_FLOW = 3;
    public PopMenu(@NonNull Context context,final PopMenuListener listener){
        final DisplayMetrics metrics =context.getResources().getDisplayMetrics();
        final int width = (int) (metrics.widthPixels / 2.0f);
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        Drawable divider = new ColorDrawable(0xFFFFFFFF){
            @Override
            public int getIntrinsicHeight() {
                return 1;
            }

            @Override
            public int getIntrinsicWidth() {
                return width;
            }
        };
        root.setDividerDrawable(divider);
        root.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        LinearLayout.LayoutParams rootParam =
                new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT);
        root.setLayoutParams(rootParam);

        final float fontSize = 15.0f;
        final int textColor = 0xFFFFFFFF;
        final int textHeight =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50.0f,metrics);
        TextView tvPeople = new TextView(context);
        tvPeople.setLayoutParams(new LinearLayout.LayoutParams(width,textHeight));
        tvPeople.setGravity(Gravity.CENTER);
        tvPeople.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
        tvPeople.setTextColor(textColor);
        tvPeople.setText("交易人员");
        tvPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    listener.onMenuClicked(MENU_PEOPLE_MANAGE);
                }
            }
        });

        TextView tvPayType = new TextView(context);
        tvPayType.setLayoutParams(new LinearLayout.LayoutParams(width,textHeight));
        tvPayType.setGravity(Gravity.CENTER);
        tvPayType.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
        tvPayType.setTextColor(textColor);
        tvPayType.setText("交易方式");
        tvPayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    listener.onMenuClicked(MENU_PAY_TYPE_MANAGE);
                }
            }
        });

        TextView tvPayFlow = new TextView(context);
        tvPayFlow.setLayoutParams(new LinearLayout.LayoutParams(width,textHeight));
        tvPayFlow.setGravity(Gravity.CENTER);
        tvPayFlow.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
        tvPayFlow.setTextColor(textColor);
        tvPayFlow.setText("交易类型");
        tvPayFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    listener.onMenuClicked(MENU_PAY_FLOW);
                }
            }
        });

        TextView tvFlow = new TextView(context);
        tvFlow.setLayoutParams(new LinearLayout.LayoutParams(width,textHeight));
        tvFlow.setGravity(Gravity.CENTER);
        tvFlow.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fontSize);
        tvFlow.setTextColor(textColor);
        tvFlow.setText("往来流水");
        tvFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null){
                    listener.onMenuClicked(MENU_OUT_IN_FLOW);
                }
            }
        });

        root.addView(tvPeople);
        root.addView(tvPayType);
        root.addView(tvPayFlow);
        root.addView(tvFlow);

        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        setContentView(root);
        setWidth(width);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0xFF333333));
        setAnimationStyle(R.style.TopInOut);
    }


    public interface PopMenuListener{
        void onMenuClicked(int menu);
    }
}
