package com.jzx.book.bookkeeping.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.dao.Flow;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzx on 2019/1/22
 */
public class FlowAdapter extends BaseAdapter<Flow> {
    private List<Long> selectedFlowIds = new ArrayList<>();
    private DecimalFormat format = new DecimalFormat("0.00");
    public FlowAdapter(List<Flow> data) {
        super(data, R.layout.item_flow);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            final RecycleHolder holder =
                    new RecycleHolder(LayoutInflater.from(viewGroup.getContext())
                            .inflate(layoutResId,viewGroup,false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = holder.getAdapterPosition();
                    long id = mData.get(itemPosition).getId();
                    boolean success;
                    if(inSelectedFlowIds(id)){
                        success = removeSelected(id);
                    }else{
                        success = addSelected(id);
                    }
                    if(success){
                        notifyItemChanged(itemPosition);
                    }
                }
            });
            return holder;
        }
        return super.onCreateViewHolder(viewGroup, position);
    }

    @Override
    public void bindViewHolder(RecycleHolder holder, int position, Flow flow) {
        if (inSelectedFlowIds(flow.getId())){
            holder.setImage(R.id.ivItemState,R.mipmap.ic_cbx_selecte);
        }else{
            holder.setImage(R.id.ivItemState,R.mipmap.ic_cbx_normal);
        }
        holder.setText(R.id.tvContact,flow.getContact());
        holder.setText(R.id.tvDate,flow.getDate());
        holder.setText(R.id.tvPayWay,flow.getPayWay());
        holder.setText(R.id.tvPayType,flow.getPayType());
        holder.setText(R.id.tvAmount,format.format(flow.getAmount()) + "元");
        holder.setText(R.id.tvRemark,flow.getRemark());
    }

    private boolean inSelectedFlowIds(long flowId){
        return selectedFlowIds.contains(flowId);
    }

    private boolean removeSelected(long flowId){
        if(inSelectedFlowIds(flowId)){
            return selectedFlowIds.remove(flowId);
        }
        return false;
    }

    private boolean addSelected(long flowId){
        if(!inSelectedFlowIds(flowId)){
            return selectedFlowIds.add(flowId);
        }
        return false;
    }
}
