package com.jzx.book.bookkeeping.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.db.FlowOperator;
import com.jzx.book.bookkeeping.ui.pop.PopChooseDate;
import com.jzx.book.bookkeeping.utils.SafeString;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Jzx on 2019/1/18
 */
public class AddFlowActivity extends BaseActivity implements View.OnClickListener {
    private static final int CODE_CHOOSE_CONTACT = 100;
    private static final int CODE_CHOOSE_PAY_TYPE = 101;
    private static final int CODE_CHOOSE_PAY_WAY = 102;

    private TextView tvError;

    private EditText etAmount;
    private TextView tvPayPeople;
    private TextView tvPayType;
    private TextView tvPayWay;
    private TextView tvDate;
    private EditText etRemark;

    private long contactId = -1L;
    private long payTypeId = -1L;
    private long  payWayId = -1L;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_add_flow;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvError = findViewById(R.id.tvError);
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
        findViewById(R.id.tvAdd).setOnClickListener(this);

        if(savedInstanceState != null){
            contactId = savedInstanceState.getLong("contactId",-1L);
            payTypeId = savedInstanceState.getLong("payTypeId",-1L);
            payWayId = savedInstanceState.getLong("payWayId",-1L);
        }
        tvDate.setText(
                new SimpleDateFormat("yyyy年MM月dd日",  Locale.CHINESE)
                        .format(System.currentTimeMillis())
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            final int COLOR_SELECTED = 0xFF333333;
            switch (requestCode){
                case CODE_CHOOSE_CONTACT:
                    contactId = data.getLongExtra(ContactActivity.CONTACT_ID_L,-1L);
                    String contact = data.getStringExtra(ContactActivity.CONTACT_NAME_S);
                    tvPayPeople.setTextColor(COLOR_SELECTED);
                    tvPayPeople.setText(SafeString.handleStringIfNull(contact));
                    break;
                case CODE_CHOOSE_PAY_TYPE:
                    payTypeId = data.getLongExtra(PayTypeActivity.PAY_TYPE_ID_L,-1L);
                    String payType = data.getStringExtra(PayTypeActivity.PAY_TYPE_NAME_S);
                    tvPayType.setTextColor(COLOR_SELECTED);
                    tvPayType.setText(SafeString.handleStringIfNull(payType));
                    break;
                case CODE_CHOOSE_PAY_WAY:
                    payWayId = data.getLongExtra(PayWayActivity.PAY_WAY_ID_L,-1L);
                    String payWay = data.getStringExtra(PayWayActivity.PAY_WAY_NAME_S);
                    tvPayWay.setTextColor(COLOR_SELECTED);
                    tvPayWay.setText(SafeString.handleStringIfNull(payWay));
                    break;
            }
        }
    }

    private boolean check(){
        String amountStr = etAmount.getText().toString().trim();
        if(amountStr.length() == 0){
            setError("请输入交易金额");
            return false;
        }
        int index = amountStr.indexOf('.');
        String reg;
        if(index == -1){
            //无小数点，不能以0开头
            reg = "^[1-9]+[0-9]*$";
            if(!amountStr.matches(reg)){
                if(amountStr.equals("0")){
                    setError("交易金额需大于0.00元");
                    return false;
                }
                setError("非法数字");
                return false;
            }
        }else{
            //有小数点
            reg = "^([0]\\.[0-9]{1,2})|([1-9]+[0-9]*\\.[0-9]{1,2})$";
            if(!amountStr.matches(reg)){
                if(amountStr.substring(index+1).length() > 2){
                    setError("最多保留2位小数");
                    return false;
                }else{
                    setError("非法数字");
                    return false;
                }
            }
        }
        double amount = Double.parseDouble(amountStr);
        if(amount == 0){
            setError("交易金额需大于0.00元");
            return false;
        }
        if(contactId == -1){
            setError("请选择交易人员");
            return false;
        }
        if(payTypeId == -1){
            setError("请选择交易类型");
            return false;
        }
        if(payWayId == -1){
            setError("请选择交易方式");
            return false;
        }
        hideError();
        return true;
    }

    private void setError(CharSequence error){
        tvError.setText(error);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError(){
        tvError.setVisibility(View.GONE);
    }

    private void addFlow(){
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean success = FlowOperator.addFlow(Double.parseDouble(etAmount.getText().toString().trim()),
                        contactId,
                        payTypeId,
                        payWayId,
                        tvDate.getText().toString(),
                        etRemark.getText().toString().trim());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        Snackbar bar = Snackbar.make(toolbar,"",Snackbar.LENGTH_SHORT);
                        if(success){
                            bar.setText("添加成功");
                        }else{
                            bar.setText("添加失败，请稍后重试");
                        }
                        bar.show();
                    }
                });
            }
        }).start();
    }


    private void showDatePicker(){
        new PopChooseDate(this, new PopChooseDate.OnDateEnsureListener() {
            @Override
            public void onDatePicked(String date) {
                tvDate.setText(date);
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
                    addFlow();
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("contactId",contactId);
        outState.putLong("payTypeId", payTypeId);
        outState.putLong("payWayId",payWayId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactId = savedInstanceState.getLong("contactId",-1L);
        payTypeId = savedInstanceState.getLong("payTypeId",-1L);
        payWayId = savedInstanceState.getLong("payWayId",-1L);
    }
}
