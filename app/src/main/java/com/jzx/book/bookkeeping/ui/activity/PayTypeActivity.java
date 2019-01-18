package com.jzx.book.bookkeeping.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.dao.PayType;
import com.jzx.book.bookkeeping.db.PayTypeOperator;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class PayTypeActivity extends BaseActivity {
    public static final String CHOOSE_PAY_TYPE_B = "choosePayType";
    public static final String PAY_TYPE_ID_L = "typeId";
    public static final String PAY_TYPE_NAME_S = "typeName";
    private RecyclerView recyclerView;

    private List<PayType> mData;
    private BaseAdapter<PayType> adapter;

    private boolean choosePayType;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_pay_type;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new VerticalDecoration(0xFFDDDDDD,1));

        choosePayType = getIntent().getBooleanExtra(CHOOSE_PAY_TYPE_B,false);

        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = PayTypeOperator.getAllPayFlow();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        adapter = new BaseAdapter<PayType>(mData,R.layout.item_text_view_left_right) {
                            @Override
                            public void bindViewHolder(RecycleHolder holder, int position, PayType payType) {
                                holder.setText(R.id.tvLeft,payType.getName());
                                holder.setText(R.id.tvRight,payType.getDes());
                            }
                        };
                        adapter.setOnAdapterClickListener(new BaseAdapter.AdapterClickListener<PayType>() {
                            @Override
                            public void onClicked(int itemPosition, int listPosition, PayType payType) {
                                if(choosePayType){
                                    Intent intent = getIntent();
                                    intent.putExtra(PAY_TYPE_ID_L,payType.getId());
                                    intent.putExtra(PAY_TYPE_NAME_S,payType.getName());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}
