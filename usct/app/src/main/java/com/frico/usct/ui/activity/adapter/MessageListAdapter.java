package com.frico.usct.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.entity.MessageItemVO;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickViewHolder;

public class MessageListAdapter extends BaseQuickAdapter<MessageItemVO, BaseQuickViewHolder> {

    public MessageListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, MessageItemVO item, int position) {
        ImageView iv_read = helper.getView(R.id.iv_read);
        TextView tv_message_content = helper.getView(R.id.tv_message_content);

        if (!item.getStatus().equals("0")) {
            iv_read.setVisibility(View.GONE);
            tv_message_content.setTextColor(mContext.getResources().getColor(R.color.color_9));
        } else {
            iv_read.setVisibility(View.VISIBLE);
            tv_message_content.setTextColor(mContext.getResources().getColor(R.color.color_3));
        }

        tv_message_content.setText(item.getTitle());
        helper.setText(R.id.tv_time, item.getCreatetime());

        helper.addOnClickListener(R.id.ll_message);
        helper.addOnClickListener(R.id.delete);
    }
}

