package com.jzx.book.bookkeeping.ui.activity;
import android.content.Intent;
import android.os.Bundle;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.dao.AssetsSummary;
import com.jzx.book.bookkeeping.db.FlowOperator;
import com.jzx.book.bookkeeping.ui.pop.PopMenu;

import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tvBorrowOut;//借出
    private TextView tvBorrowIn;//借入
    @Override
    public int providerLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tvBorrowOut = findViewById(R.id.tvBorrowOut);
        tvBorrowIn = findViewById(R.id.tvBorrowIn);

        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final AssetsSummary summary = FlowOperator.getAssetsSummary();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        DecimalFormat format = new DecimalFormat("0.00");
                        int colorPositive = getResources().getColor(R.color.colorPositive);
                        int colorNegative = getResources().getColor(R.color.colorNegative);
                        //借出
                        StringBuilder borrowOut = new StringBuilder();
                        borrowOut.append("借出金额:");
                        borrowOut.append(format.format(summary.getBorrowOutTotal()));
                        borrowOut.append("元");
                        borrowOut.append("\n");

                        borrowOut.append("收到还款:");
                        borrowOut.append(format.format(summary.getBorrowOutBackTotal()));
                        borrowOut.append("元");
                        borrowOut.append("\n");

                        borrowOut.append("尚未还款:");
                        borrowOut.append(format.format(summary.getBorrowOutBalance()));
                        borrowOut.append("元");

                        SpannableStringBuilder borrowOutSb =
                                new SpannableStringBuilder(borrowOut);
                        int borrowOutStart = borrowOut.lastIndexOf(":");
                        int borrowOutEnd = borrowOut.length() - 1;
                        ForegroundColorSpan borrowOutSpan;
                        if(summary.getBorrowOutBalance() > 0){
                            borrowOutSpan = new ForegroundColorSpan(colorNegative);
                        }else{
                            borrowOutSpan = new ForegroundColorSpan(colorPositive);
                        }
                        borrowOutSb.setSpan(borrowOutSpan,
                                borrowOutStart,
                                borrowOutEnd,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        tvBorrowOut.setText(borrowOutSb);

                        //借入
                        StringBuilder borrowIn = new StringBuilder();
                        borrowIn.append("借入金额:");
                        borrowIn.append(format.format(summary.getBorrowInTotal()));
                        borrowIn.append("元");
                        borrowIn.append("\n");

                        borrowIn.append("已还欠款:");
                        borrowIn.append(format.format(summary.getBorrowInBackTotal()));
                        borrowIn.append("元");
                        borrowIn.append("\n");

                        borrowIn.append("尚有欠款:");
                        borrowIn.append(format.format(summary.getBorrowInBalance()));
                        borrowIn.append("元");

                        SpannableStringBuilder borrowInSb = new SpannableStringBuilder(borrowIn);
                        int borrowInStart = borrowIn.lastIndexOf(":");
                        int borrowInEnd = borrowIn.length() - 1;
                        ForegroundColorSpan borrowInSpan;
                        if(summary.getBorrowInBalance() > 0){
                            borrowInSpan = new ForegroundColorSpan(colorNegative);
                        }else{
                            borrowInSpan = new ForegroundColorSpan(colorPositive);
                        }
                        borrowInSb.setSpan(borrowInSpan,
                                borrowInStart,
                                borrowInEnd,
                                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        tvBorrowIn.setText(borrowInSb);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.more){
            showPopMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private PopMenu menu;
    private void showPopMenu(){
        if(menu == null){
            menu = new PopMenu(this, new PopMenu.PopMenuListener() {
                @Override
                public void onMenuClicked(int menu) {
                    switch (menu) {
                        case PopMenu.MENU_PEOPLE_MANAGE:
                            //交易人员管理
                            startActivity(new Intent(MainActivity.this, ContactActivity.class));
                            break;
                        case PopMenu.MENU_PAY_TYPE_MANAGE:
                            //交易方式管理
                            startActivity(new Intent(MainActivity.this, PayWayActivity.class));
                            break;
                        case PopMenu.MENU_PAY_FLOW:
                            //交易流向
                            startActivity(new Intent(MainActivity.this, PayTypeActivity.class));
                            break;
                        case PopMenu.MENU_OUT_IN_FLOW:
                            //往来流水
                            startActivity(new Intent(MainActivity.this, FlowActivity.class));
                            break;
                    }
                }
            });
        }
        int[] position = new int[2];
        toolbar.getLocationOnScreen(position);
        menu.showAtLocation(findViewById(R.id.toolBar),
                Gravity.NO_GRAVITY,
                menu.getWidth(),
                position[1]+toolbar.getHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(this,AddFlowActivity.class));
                break;
        }
    }
}
