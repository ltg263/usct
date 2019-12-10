package com.frico.easy_pay.ui.activity.adapter;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.NoticeVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;

public class NoticeListAdapter extends BaseQuickAdapter<NoticeVO, BaseQuickViewHolder> {

    public NoticeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, NoticeVO item, int position) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.addOnClickListener(R.id.tv_title);
    }
}

