package com.jzx.book.bookkeeping.ui.holder;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jzx on 2019/1/17
 */
public class RecycleHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> widgets;
    public RecycleHolder(@NonNull View itemView) {
        super(itemView);
        widgets = new SparseArray<>();
    }

    public <T extends View> T getView(@IdRes int viewId){
        View t = widgets.get(viewId);
        if(t == null){
            t = itemView.findViewById(viewId);
            widgets.put(viewId,t);
        }
        return (T) t;
    }

    public void setText(@IdRes int viewId,CharSequence text){
        TextView textView = getView(viewId);
        if (text != null){
            textView.setText(text);
        }
    }

    public void setImage(@IdRes int viewId,@DrawableRes int imgRes){
        ImageView iv = getView(viewId);
        iv.setImageResource(imgRes);
    }
}
