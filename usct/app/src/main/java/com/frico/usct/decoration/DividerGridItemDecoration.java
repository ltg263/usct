package com.frico.usct.decoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public DividerGridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space / 2;
        outRect.bottom = space / 2;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % 3 == 0) {
            outRect.left = 0;
        }
    }
}
