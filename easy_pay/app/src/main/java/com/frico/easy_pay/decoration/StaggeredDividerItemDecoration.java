package com.frico.easy_pay.decoration;

import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;

/**
 * 创建日期：2019/6/12 on 11:14
 * 作者:王红闯 hongchuangwang
 * 设置RecyclerView 的瀑布流样式之横纵方向间距
 */
public class StaggeredDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int interval;

    public StaggeredDividerItemDecoration(Context context, int interval) {
        this.context = context;
        this.interval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int spanIndex = params.getSpanIndex();
        int interval = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                this.interval, context.getResources().getDisplayMetrics());
        // 中间间隔
        //放弃之前的方法。这里设置让左右一样列表左右切换导致中间距离出现问题
        outRect.left = interval / 2;
        outRect.right = interval / 2;
        // 下方间隔
        outRect.bottom = interval / 2;
    }


}


