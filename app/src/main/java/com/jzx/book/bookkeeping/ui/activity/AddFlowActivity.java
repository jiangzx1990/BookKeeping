package com.jzx.book.bookkeeping.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.dao.Contact;
import com.jzx.book.bookkeeping.dao.PayType;
import com.jzx.book.bookkeeping.dao.PayWay;
import com.jzx.book.bookkeeping.ui.pop.ChooseDateDialog;

/**
 * Created by Jzx on 2019/1/18
 */
public class AddFlowActivity extends BaseActivity implements View.OnClickListener {
    private static final int CODE_CHOOSE_CONTACT = 100;
    private static final int CODE_CHOOSE_PAY_TYPE = 101;
    private static final int CODE_CHOOSE_PAY_WAY = 102;
    private EditText etAmount;
    private TextView tvPayPeople;
    private TextView tvPayType;
    private TextView tvPayWay;
    private TextView tvDate;
    private EditText etRemark;

    private Contact payPeople;
    private PayType payType;
    private PayWay payWay;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_add_flow;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        etAmount = findViewById(R.id.etAmount);
        tvPayPeople = findViewById(R.id.tvPayPeople);
        tvPayType = findViewById(R.id.tvPayType);
        tvPayWay = findViewById(R.id.tvPayWay);
        tvDate = findViewById(R.id.tvDate);
        etRemark = findViewById(R.id.etRemark);

        tvPayPeople.setOnClickListener(this);
        tvPayType.setOnClickListener(this);
        tvPayWay.setOnClickListener(this);
        tvDate.setOnClickListener(this);
    }

    private boolean check(){
        return true;
    }

    private void addFlow(){
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }


    private void showDatePicker(){
        new ChooseDateDialog(this, new ChooseDateDialog.OnDateEnsureListener() {
            @Override
            public void onDatePicked(String date) {
                Log.d("onDatePicked", date);
            }
        }).showAsDropDown(toolbar);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tvPayPeople:
                intent.setClass(this,ContactActivity.class);
                intent.putExtra(ContactActivity.CHOOSE_CONTACT_B,true);
                startActivityForResult(intent,CODE_CHOOSE_CONTACT);
                break;
            case R.id.tvPayType:
                intent.setClass(this,PayTypeActivity.class);
                intent.putExtra(PayTypeActivity.CHOOSE_PAY_TYPE_B,true);
                startActivityForResult(intent,CODE_CHOOSE_PAY_TYPE);
                break;
            case R.id.tvPayWay:
                intent.setClass(this,PayWayActivity.class);
                intent.putExtra(PayWayActivity.CHOOSE_PAY_WAY_B,true);
                startActivityForResult(intent,CODE_CHOOSE_PAY_WAY);
                break;
            case R.id.tvDate:
                showDatePicker();
                break;
            case R.id.tvAdd:
                if(check()){

                }
                break;
        }
    }


}
