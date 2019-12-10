package com.frico.easy_pay.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.DealItemVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeListAdapter extends BaseQuickAdapter<DealItemVO, BaseQuickViewHolder> {

    public HomeListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, DealItemVO item, int position) {
        helper.setText(R.id.tv_count, item.getAmount());
        String timeString = item.getCreatetime();
        Date date = TimeUtil.timeToDate(timeString);
        String timeStr = TimeUtil.dateToTime4(date);
        helper.setText(R.id.tv_time, timeStr);
        helper.setText(R.id.tv_limit, item.getAmountmin() + " - " + item.getAmountmax());

        ImageView imgBank = helper.getView(R.id.img_bank);
        ImageView imgZfb = helper.getView(R.id.img_zfb);
        ImageView imgWx = helper.getView(R.id.img_wx);
        ImageView imgYsf = helper.getView(R.id.img_ysf);
        ImageView imgZsm = helper.getView(R.id.img_zsm);

        View view = helper.getView(R.id.tv_robbing);
        if(item.getTradetype() == 1){
            view.setBackgroundResource(R.drawable.shape_blue_14dp);
            helper.setVisible(R.id.iv_item_trader_order_customer_tag,false);
            helper.setVisible(R.id.ltv_item_order_customer,false);
        }else{
            if(item.getRate() > 0) {
//                helper.setVisible(R.id.tv_customer_order_give, true);
//                helper.setText(R.id.tv_customer_order_give, "送 " + item.getRate() + "‰");
//                helper.setVisible(R.id.iv_item_trader_order_customer_tag,true);
                helper.setText(R.id.ltv_item_order_customer, "送 " + item.getRate() + "‰");
                helper.setVisible(R.id.ltv_item_order_customer,true);
            }else{
//                helper.setText(R.id.tv_customer_order_give, "");
                helper.setText(R.id.ltv_item_order_customer, "");
            }
            view.setBackgroundResource(R.drawable.shape_orange_from_to_14dp);
        }

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

        helper.addOnClickListener(R.id.tv_robbing);
    }
}

