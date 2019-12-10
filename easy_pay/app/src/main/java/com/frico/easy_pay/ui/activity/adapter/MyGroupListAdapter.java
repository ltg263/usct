package com.frico.easy_pay.ui.activity.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.MyGroupItemVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.utils.Util;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;

public class MyGroupListAdapter extends BaseQuickAdapter<MyGroupItemVO, BaseQuickViewHolder> {
    private Activity mContext;
    public MyGroupListAdapter(Activity activity,int layoutResId) {
        super(layoutResId);
        mContext = activity;
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, MyGroupItemVO item, int position) {
        helper.setText(R.id.tv_item_group_id,item.getAcqid());

        helper.setText(R.id.tv_item_group_phone, Util.hintPhoneNumber(item.getMobile()));
        helper.setText(R.id.tv_item_group_account, item.getAvailable_money() + " USCT");

        helper.setText(R.id.tv_item_group_count, item.getTeam_nums()+"人");
        if(!TextUtils.isEmpty(item.getAgentname())) {
            helper.setText(R.id.tv_item_group_class, item.getAgentname());
        }else{
            helper.setText(R.id.tv_item_group_class, "");
        }

        ImageView classIcon = helper.getView(R.id.tv_item_group_class_icon);
        ImageView ivHeader = helper.getView(R.id.img_item_gourp_header);


        //'0'=>'无','1'=>'经理', '2'=>'总监','3'=>'董事',
        if (item.getTeam_level() == 0) {
            classIcon.setImageResource(R.drawable.icon_normal);
        } else if (item.getTeam_level() == 1) {
            classIcon.setImageResource(R.drawable.icon_manager);
        } else if (item.getTeam_level() == 2 || item.getTeam_level() == 3) {
            classIcon.setImageResource(R.drawable.icon_chief);
        }
        if (TextUtils.isEmpty(item.getAvater())) {
            int resId = R.drawable.header_default_1;
            new ImageLoaderImpl().loadImageCircle(mContext, resId).into(ivHeader);
        }else{
            new ImageLoaderImpl().loadImageCircle(mContext, item.getAvater()).into(ivHeader);
        }

    }
}

