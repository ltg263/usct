package com.frico.easy_pay.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.AdVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AdListAdapter extends BaseQuickAdapter<AdVO.ListBean.DataBean, BaseQuickViewHolder> {

    public AdListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, AdVO.ListBean.DataBean item, int position) {
        ImageView imgBank = helper.getView(R.id.img_bank);
        ImageView imgZfb = helper.getView(R.id.img_zfb);
        ImageView imgWx = helper.getView(R.id.img_wx);
        ImageView imgYsf = helper.getView(R.id.img_ysf);
        ImageView imgZsm = helper.getView(R.id.img_zsm);

        helper.setText(R.id.tv_order_no, item.getAdvertno());
        helper.setText(R.id.tv_time, item.getCreatetime());
        helper.setText(R.id.tv_count, item.getAmount());
        helper.setText(R.id.tv_freeze_count, item.getFrozenamount());
        helper.setText(R.id.tv_deal_count, item.getTradeamount());

        String limitText = item.getAmountmin() + " - " + item.getAmountmax();
        helper.setText(R.id.tv_limit_count, limitText);

        if (item.getTradeway().contains(",")) {
            List<String> typeList = new ArrayList<>();
            String[] split = item.getTradeway().split(",");
            for (String s : split) {
                typeList.add(s);
            }

            if (typeList.contains("1")) {
                imgBank.setVisibility(View.VISIBLE);
            } else {
                imgBank.setVisibility(View.GONE);
            }

            if (typeList.contains("2")) {
                imgZfb.setVisibility(View.VISIBLE);
            } else {
                imgZfb.setVisibility(View.GONE);
            }

            if (typeList.contains("3")) {
                imgWx.setVisibility(View.VISIBLE);
            } else {
                imgWx.setVisibility(View.GONE);
            }
            if (typeList.contains("4")) {
                imgYsf.setVisibility(View.VISIBLE);
            } else {
                imgYsf.setVisibility(View.GONE);
            }
            if (typeList.contains("5")) {
                imgZsm.setVisibility(View.VISIBLE);
            } else {
                imgZsm.setVisibility(View.GONE);
            }

        } else {
            if (item.getTradeway().equals("1")) {
                imgBank.setVisibility(View.VISIBLE);
                imgZfb.setVisibility(View.GONE);
                imgWx.setVisibility(View.GONE);
                imgYsf.setVisibility(View.GONE);
                imgZsm.setVisibility(View.GONE);
            } else if (item.getTradeway().equals("2")) {
                imgBank.setVisibility(View.GONE);
                imgZfb.setVisibility(View.VISIBLE);
                imgWx.setVisibility(View.GONE);
                imgYsf.setVisibility(View.GONE);
                imgZsm.setVisibility(View.GONE);
            } else if (item.getTradeway().equals("3")) {
                imgBank.setVisibility(View.GONE);
                imgZfb.setVisibility(View.GONE);
                imgWx.setVisibility(View.VISIBLE);
                imgYsf.setVisibility(View.GONE);
                imgZsm.setVisibility(View.GONE);
            } else if (item.getTradeway().equals("4")) {
                imgBank.setVisibility(View.GONE);
                imgZfb.setVisibility(View.GONE);
                imgWx.setVisibility(View.GONE);
                imgYsf.setVisibility(View.VISIBLE);
                imgZsm.setVisibility(View.GONE);
            } else if (item.getTradeway().equals("5")) {
                imgBank.setVisibility(View.GONE);
                imgZfb.setVisibility(View.GONE);
                imgWx.setVisibility(View.GONE);
                imgYsf.setVisibility(View.GONE);
                imgZsm.setVisibility(View.VISIBLE);
            }
        }

        helper.addOnClickListener(R.id.tv_sold_out);
    }
}

