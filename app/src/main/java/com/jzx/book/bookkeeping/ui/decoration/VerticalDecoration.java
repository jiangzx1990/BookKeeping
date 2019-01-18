package com.jzx.book.bookkeeping.ui.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jzx on 2019/1/17
 */
public class VerticalDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mDividerHeight;
    private int mPaddingLeft;
    private int mPaddingRight;

    public void setPaddingLeftRight(int paddingLeft, int paddingRight){
        this.mPaddingLeft=paddingLeft;
        this.mPaddingRight=paddingRight;
    }

    public VerticalDecoration(@ColorInt int color, int dividerHeight){
        this.mDivider=new ColorDrawable(color);
        this.mDividerHeight=dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft()+mPaddingLeft;
        final int right = parent.getWidth() - parent.getPaddingRight()-mPaddingRight;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0,0,mDividerHeight);
    }
}
