package com.jzx.book.bookkeeping.ui.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jzx.book.bookkeeping.R;
import com.jzx.book.bookkeeping.base.BaseAdapter;
import com.jzx.book.bookkeeping.ui.decoration.VerticalDecoration;
import com.jzx.book.bookkeeping.ui.holder.RecycleHolder;

import java.util.List;

/**
 * Created by Jzx on 2019/1/22
 */
public class PopChooseT<T> extends PopupWindow {
    public interface OnChooseListener<T>{
        void onChoose(T t);
    }
    public PopChooseT(final Context context, List<T> mData,final OnChooseListener<T> l){
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int width = (int) (metrics.widthPixels / 2.0f);
        final int maxHeight = (int) (metrics.heightPixels * 0.35f);
        final int textViewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50,
                context.getResources().getDisplayMetrics());
        final int dividerHeight = 1;
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        VerticalDecoration decoration = new VerticalDecoration(0xFFEEEEEE,dividerHeight);
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        BaseAdapter<T> adapter = new BaseAdapter<T>(mData,0) {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
                if(getItemViewType(position) == TYPE_NORMAL){
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            textViewHeight));
                    tv.setTextColor(0xFFFFFFFF);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                    tv.setGravity(Gravity.CENTER);
                    final RecycleHolder holder = new RecycleHolder(tv);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(l != null){
                                dismiss();
                                int itemPosition = holder.getAdapterPosition();
                                T t = mData.get(itemPosition);
                                l.onChoose(t);
                            }
                        }
                    });
                    return holder;
                }
                return super.onCreateViewHolder(viewGroup, position);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder recycleHolder, int i) {
                ((TextView)recycleHolder.itemView).setText(mData.get(i).toString());
            }

            @Override
            public void bindViewHolder(RecycleHolder holder, int position, T t) {

            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        setContentView(recyclerView);
        setAnimationStyle(R.style.TopInOut);

        //计算recycleView高度
        int recycleViewHeight = mData.size() * textViewHeight + mData.size() * dividerHeight;
        setWidth(width);
        setHeight(recycleViewHeight > maxHeight ? maxHeight:recycleViewHeight);
        setBackgroundDrawable(new ColorDrawable(0xFF333333));
        setFocusable(true);
        setOutsideTouchable(false);
    }
}
