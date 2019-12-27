package com.frico.easy_pay.decoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by wanghongchuang
 * on 2016/9/10.
 * email:844285775@qq.com
 */
public class HorizDecoration extends RecyclerView.ItemDecoration {
    private int pixels;

    public HorizDecoration(int pixels) {
        this.pixels = pixels;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //判断下方向   ->   竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
            // 最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) != layoutManager.getItemCount() - 1) {
                outRect.bottom = pixels;
            }
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) != layoutManager.getItemCount() - 1) {
                outRect.right = pixels;
            }
        }
    }
}