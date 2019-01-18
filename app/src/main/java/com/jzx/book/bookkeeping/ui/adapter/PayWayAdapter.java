package com.jzx.book.bookkeeping.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.dao.PayWay;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.util.List;

/**
 * Created by Jzx on 2019/1/18
 */
public class PayWayAdapter extends BaseAdapter<PayWay> {
    public PayWayAdapter(List<PayWay> data) {
        super(data, R.layout.item_text_view_left_right);
    }

    @Override
    public void bindViewHolder(RecycleHolder holder, int position, PayWay payWay) {
        holder.setText(R.id.tvLeft,payWay.getName());
    }
}
