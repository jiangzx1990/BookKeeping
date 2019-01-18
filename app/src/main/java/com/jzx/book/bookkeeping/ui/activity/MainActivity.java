package com.jzx.book.bookkeeping.ui.activity;
import android.content.Intent;
import android.os.Bundle;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.ui.pop.PopMenu;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
        final View contentLayout = findViewById(R.id.cdlContent);
        tvBorrowOut = findViewById(R.id.tvBorrowOut);
        tvBorrowIn = findViewById(R.id.tvBorrowIn);
        contentLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //获取屏幕剩余高度[contentLayout的高度]
                        int height = contentLayout.getMeasuredHeight();
                        int textViewHeight = (int) (height / 2.0f);
                        tvBorrowOut.getLayoutParams().height = textViewHeight;
                        tvBorrowIn.getLayoutParams().height = textViewHeight;
                        ((CoordinatorLayout.MarginLayoutParams)tvBorrowIn.getLayoutParams()).topMargin = textViewHeight;
                        contentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        tvBorrowOut.setOnClickListener(this);
        tvBorrowIn.setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);
        tvBorrowOut.setText("借出");
        tvBorrowIn.setText("借入");
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
            case R.id.tvBorrowOut:
                break;
            case R.id.tvBorrowIn:
                break;
            case R.id.fab:
                startActivity(new Intent(this,AddFlowActivity.class));
                break;
        }
    }
}
