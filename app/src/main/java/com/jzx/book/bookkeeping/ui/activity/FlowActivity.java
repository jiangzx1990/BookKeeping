package com.jzx.book.bookkeeping.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseActivity;
import com.jzx.book.bookkeeping.dao.Flow;
import com.jzx.book.bookkeeping.db.FlowOperator;
import com.jzx.book.bookkeeping.ui.adapter.FlowAdapter;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;

import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public class FlowActivity extends BaseActivity {
    private List<Flow> mData;
    private FlowAdapter adapter;

    private TextView tvContact;
    private TextView tvPayType;

    private RecyclerView recycleView;

    @Override
    public int providerLayoutRes() {
        return R.layout.activity_flow;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
        recycleView.addItemDecoration(
                new VerticalDecoration(0xFFDDDDDD,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        10,getResources().getDisplayMetrics())));
        new Thread(new Runnable() {
            @Override
            public void run() {
                mData = FlowOperator.queryFlow(0,0);
                adapter = new FlowAdapter(mData);
                recycleView.setAdapter(adapter);
            }
        }).start();
    }
}
