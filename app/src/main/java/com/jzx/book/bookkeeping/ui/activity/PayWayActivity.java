package com.jzx.book.bookkeeping.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.dao.PayWay;
import com.jzx.book.bookkeeping.db.PayWayOperator;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;
import com.jzx.book.bookkeeping.ui.dialog.AddPayWayDialog;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class PayWayActivity extends BaseActivity {
    public static final String CHOOSE_PAY_WAY_B = "choosePayWay";
    public static final String PAY_WAY_ID_L = "payWayId";
    public static final String PAY_WAY_NAME_S = "payWayName";
    private RecyclerView recyclerView;

    private boolean choosePayWay;
    private List<PayWay> mData;
    private BaseAdapter<PayWay> adapter;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_pay_way;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        choosePayWay = getIntent().getBooleanExtra(CHOOSE_PAY_WAY_B,false);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new VerticalDecoration(0xFFDDDDDD,1));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPayWayDialog();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = PayWayOperator.getAllPayWay();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new BaseAdapter<PayWay>(mData,R.layout.item_text_view_left_right) {
                            @Override
                            public void bindViewHolder(RecycleHolder holder, int position, PayWay payWay) {
                                holder.setText(R.id.tvLeft,payWay.getName());
                            }
                        };
                        adapter.setOnAdapterClickListener(new BaseAdapter.AdapterClickListener<PayWay>() {
                            @Override
                            public void onClicked(int action, int listPosition, PayWay payWay) {
                                switch (action){
                                    case BaseAdapter.AdapterClickListener.ACTION_ITEM_CLICKED:
                                        if(choosePayWay){
                                            Intent intent = getIntent();
                                            intent.putExtra(PAY_WAY_ID_L,payWay.getId());
                                            intent.putExtra(PAY_WAY_NAME_S,payWay.getName());
                                            setResult(RESULT_OK,intent);
                                        }
                                        break;
                                    case BaseAdapter.AdapterClickListener.ACTION_ITEM_LONG_CLICKED:
                                        showDeleteDialog(payWay);
                                        break;
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }


    private void showDeleteDialog(final PayWay payWay){
        new AlertDialog.Builder(this)
                .setTitle("危险动作")
                .setMessage("警告：删除该交易方式后，所有有关["+payWay.getName()+"]交易方式的数据将被删除，且不可恢复，确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoadingDialog();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final boolean success = PayWayOperator.deletePayWay(payWay.getId());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissLoadingDialog();
                                        Snackbar.make(recyclerView,success?"删除成功!":"删除失败",Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    private AddPayWayDialog addDialog;
    private void showAddPayWayDialog(){
        if(addDialog == null){
            addDialog = new AddPayWayDialog(this, new AddPayWayDialog.OnAddPayWayEnsure() {
                @Override
                public void onEnsureClick(final String name) {
                    if(name.isEmpty()){
                        addDialog.showNameError(getResources().getString(R.string.app_text_input_pay_name));
                    }else{
                        addDialog.hideNameError();
                        addDialog.dismiss();
                        showLoadingDialog();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(PayWayOperator.queryPayWayByName(name) == null){
                                    final PayWay payWay = PayWayOperator.addPayWay(name);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissLoadingDialog();
                                            if(payWay == null){
                                                //添加失败
                                                Snackbar.make(recyclerView,"添加失败，请稍后重试",Snackbar.LENGTH_SHORT).show();
                                            }else{
                                                mData.add(payWay);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }else{
                                    //已经存在
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissLoadingDialog();
                                            Snackbar.make(recyclerView,"交易方式["+name+"]已存在",Snackbar.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
            });
        }
        addDialog.show();
    }
}
