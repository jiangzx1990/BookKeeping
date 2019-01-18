package com.jzx.book.bookkeeping.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.util.List;

/**
 * Created by Jzx on 2019/1/17
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPTY = -1;
    protected static final int TYPE_NORMAL = 0;

    protected int layoutResId;
    protected List<T> mData;

    public BaseAdapter(List<T> data, @LayoutRes int layoutResId){
        this.mData = data;
        this.layoutResId = layoutResId;
    }

    protected AdapterClickListener<T> listener;
    public void setOnAdapterClickListener(AdapterClickListener<T> listener){
        this.listener = listener;
    }
    public interface AdapterClickListener<T>{
        int ACTION_ITEM_CLICKED = 0;
        int ACTION_ITEM_LONG_CLICKED =1;
        void onClicked(int action,int listPosition,T t);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        int viewType = getItemViewType(position);
        final RecycleHolder holder;
        if(viewType == TYPE_NORMAL){
            holder = new RecycleHolder(
                    LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId,viewGroup,false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int itemPosition = holder.getAdapterPosition();
                        listener.onClicked(AdapterClickListener.ACTION_ITEM_CLICKED,itemPosition,mData.get(itemPosition));
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        int itemPosition = holder.getAdapterPosition();
                        listener.onClicked(AdapterClickListener.ACTION_ITEM_LONG_CLICKED,itemPosition,mData.get(itemPosition));
                    }
                    return true;
                }
            });
        }else{
            holder = new RecycleHolder(
                    LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_empty,viewGroup,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder recycleHolder, int i) {
        if(getItemViewType(i) == TYPE_NORMAL){
            bindViewHolder((RecycleHolder) recycleHolder,i,mData.get(i));
        }
    }

    public abstract void bindViewHolder(RecycleHolder holder, int position, T t);

    @Override
    public int getItemCount() {
        return (mData == null || mData.size() ==0)? 1 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (mData == null || mData.size() ==0)? TYPE_EMPTY : TYPE_NORMAL;
    }
}
