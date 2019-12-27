package com.frico.easy_pay.decoration;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */

public class DividerGridItemNewDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public DividerGridItemNewDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space / 3;

        // 这样好计算，不然会出现大小不一样的情况
        //由于每行都只有2个，所以第一个都是2的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % 2 != 0) {
            outRect.right = space;
        }
    }
}
