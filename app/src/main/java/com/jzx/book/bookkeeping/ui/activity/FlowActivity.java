package com.jzx.book.bookkeeping.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.dao.Contact;
import com.jzx.book.bookkeeping.dao.Flow;
import com.jzx.book.bookkeeping.dao.PayType;
import com.jzx.book.bookkeeping.db.ContactOperator;
import com.jzx.book.bookkeeping.db.FlowOperator;
import com.jzx.book.bookkeeping.db.PayTypeOperator;
import com.jzx.book.bookkeeping.ui.adapter.FlowAdapter;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;
import com.jzx.book.bookkeeping.ui.pop.PopChooseT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class FlowActivity extends BaseActivity implements View.OnClickListener {
    private List<Flow> mData = new ArrayList<>();
    private FlowAdapter adapter;

    private TextView tvContact;
    private TextView tvPayType;

    private long contactId;
    private long payTypeId;

    private RecyclerView recycleView;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_flow;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvContact = findViewById(R.id.tvContact);
        tvPayType = findViewById(R.id.tvPayType);

        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
        recycleView.addItemDecoration(
                new VerticalDecoration(0xFFDDDDDD,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        10,getResources().getDisplayMetrics())));

        tvContact.setOnClickListener(this);
        tvPayType.setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);

        refreshData();
    }

    private void refreshData(){
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Flow> data = FlowOperator.queryFlow(contactId,payTypeId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        mData.clear();
                        mData.addAll(data);
                        adapter = new FlowAdapter(mData);
                        recycleView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private PopChooseT<Contact> popContact;
    private void showChooseContactPop(){
        tvContact.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_up,0);
        if(popContact == null){
            showLoadingDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Contact> data = ContactOperator.getAllContacts();
                    Contact contact = new Contact();
                    contact.setName("选择交易人员");
                    data.add(0,contact);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            popContact = new PopChooseT<>(FlowActivity.this, data, new PopChooseT.OnChooseListener<Contact>() {
                                @Override
                                public void onChoose(Contact contact) {
                                    tvContact.setText(contact.toString());
                                    contactId = contact.getId();
                                    refreshData();
                                }
                            });
                            popContact.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    tvContact.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_down,0);
                                }
                            });
                            int[] position = new int[2];
                            tvContact.getLocationOnScreen(position);
                            popContact.showAtLocation(tvContact, Gravity.NO_GRAVITY,0,position[1]+tvContact.getHeight());
                        }
                    });
                }
            }).start();
        }else{
            int[] position = new int[2];
            tvContact.getLocationOnScreen(position);
            popContact.showAtLocation(tvContact, Gravity.NO_GRAVITY,0,position[1]+tvContact.getHeight());
        }
    }

    private PopChooseT<PayType> popChoosePayType;
    private void showChoosePayTypePop(){
        tvPayType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_up,0);
        if(popChoosePayType == null){
            showLoadingDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<PayType> data = PayTypeOperator.getAllPayType();
                    PayType payType = new PayType();
                    payType.setName("选择交易类型");
                    data.add(0,payType);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissLoadingDialog();
                            popChoosePayType = new PopChooseT<>(FlowActivity.this, data, new PopChooseT.OnChooseListener<PayType>() {
                                @Override
                                public void onChoose(PayType payType) {
                                    tvPayType.setText(payType.toString());
                                    payTypeId = payType.getId();
                                    refreshData();
                                }
                            });
                            popChoosePayType.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    tvPayType.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.mipmap.ic_down,0);
                                }
                            });

                            int[] position = new int[2];
                            tvPayType.getLocationOnScreen(position);
                            popChoosePayType.showAtLocation(tvPayType, Gravity.NO_GRAVITY,position[0],position[1]+tvContact.getHeight());
                        }
                    });
                }
            }).start();
        }else{
            int[] position = new int[2];
            tvPayType.getLocationOnScreen(position);
            popChoosePayType.showAtLocation(tvPayType, Gravity.NO_GRAVITY,position[0],position[1]+tvContact.getHeight());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvContact:
                //交易人员
                showChooseContactPop();
                break;
            case R.id.tvPayType:
                //交易类型
                showChoosePayTypePop();
                break;
            case R.id.fab:
                //删除
                if(adapter != null){
                    final Snackbar bar = Snackbar.make(recycleView,"",Snackbar.LENGTH_SHORT);
                    if(adapter.hasSelecteds()){
                        new AlertDialog.Builder(this)
                                .setTitle("警告")
                                .setMessage("您正在进行删除操作，删除后数据不可恢复，确定要删除吗？")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                final boolean deleted = FlowOperator.deleteFlows(adapter.getSelectedFlowIds());
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if(deleted){
                                                            refreshData();
                                                            bar.setText("删除成功");
                                                            bar.show();
                                                        }else{
                                                            bar.setText("删除失败，请稍后重试");
                                                            bar.show();
                                                        }
                                                    }
                                                });
                                            }
                                        }).start();
                                    }
                                }).create().show();
                    }else{
                        bar.setText("请选择需要删除的记录");
                        bar.show();
                    }
                }
                break;
        }
    }
}
