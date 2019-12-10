package com.frico.easy_pay.ui.activity.adapter;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.OrderVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.utils.TimeUtil;

import java.text.ParseException;

public class HistoryListAdapter extends BaseQuickAdapter<OrderVO, BaseQuickViewHolder> {
    private Activity activity;
    public HistoryListAdapter(Activity activity,int layoutResId) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, OrderVO item, int position) {
//        helper.setText(R.id.tv_order_no,item.getOrdernum());
        helper.setText(R.id.tv_history_order_money, item.getRealpaymoney() + " CNY");
//        helper.setText(R.id.tv_pay_count, item.getPayamount() + " SCT");

        helper.setText(R.id.tv_history_order_time, getTimeStr(item.getCreatetime()));
//        helper.setText(R.id.tv_account_alias, item.getInfo().getAlias());
//        helper.setText(R.id.tv_pay_name, item.getPayaccount());

        TextView tvStatus = helper.getView(R.id.tv_history_order_status);
//        TextView tvPut = helper.getView(R.id.tv_put);
//        TextView tvCancle = helper.getView(R.id.tv_cancle);
//        TextView tvDelete = helper.getView(R.id.tv_delete);
        ImageView imgPayWay = helper.getView(R.id.img_history_order_icon);
        helper.setText(R.id.tv_history_order_type, item.getInfo().getAccountname());

        if (item.getPaytype().equals("3")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_weixin);

        } else if (item.getPaytype().equals("2")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_alipay);
        } else if (item.getPaytype().equals("1")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_bank);
        } else if (item.getPaytype().equals("4")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_agro);
        } else if (item.getPaytype().equals("5")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_zsm);
        }
        int statusColorId = activity.getResources().getColor(R.color.color_ff8e1f);
        //status 0=已超时,1=待支付,2=待确认,3=已完成,4=系统处理中 5
        switch (item.getStatus()) {
            case "0":
                statusColorId = activity.getResources().getColor(R.color.color_fb341f);
                tvStatus.setText("未支付");
                tvStatus.setSelected(true);
//                tvPut.setVisibility(View.GONE);
//                tvCancle.setVisibility(View.GONE);
//                tvDelete.setVisibility(View.VISIBLE);
                break;
            case "1":
                tvStatus.setText("待支付");
                tvStatus.setSelected(true);
//                tvPut.setVisibility(View.GONE);
//                tvCancle.setVisibility(View.GONE);
//                tvDelete.setVisibility(View.GONE);
                break;
            case "2":
                tvStatus.setText("");
                tvStatus.setSelected(true);
//                tvPut.setVisibility(View.VISIBLE);
//                if (item.getIscancancle() == 1) {
//                    tvCancle.setVisibility(View.VISIBLE);
//                } else {
//                    tvCancle.setVisibility(View.GONE);
//                }
//                tvDelete.setVisibility(View.GONE);
                break;
            case "3":
                tvStatus.setText("收款成功");
                tvStatus.setSelected(false);
//                tvPut.setVisibility(View.GONE);
//                tvCancle.setVisibility(View.GONE);
//                tvDelete.setVisibility(View.VISIBLE);
                break;
            case "4":
                tvStatus.setText("系统处理中");
                tvStatus.setSelected(true);
//                tvPut.setVisibility(View.GONE);
//                tvCancle.setVisibility(View.GONE);
//                tvDelete.setVisibility(View.GONE);
                break;
            case "5":

                break;
            default:
                tvStatus.setText("");
//                tvPut.setVisibility(View.GONE);
//                tvCancle.setVisibility(View.GONE);
//                tvDelete.setVisibility(View.GONE);
                break;
        }
        tvStatus.setTextColor(statusColorId);
//        helper.addOnClickListener(R.id.tv_delete);
//        helper.addOnClickListener(R.id.tv_put);
//        helper.addOnClickListener(R.id.tv_cancle);
        helper.addOnClickListener(R.id.img_history_order_icon);
    }

    /**
     *
     * @param createTime  2019-09-11 16:15:24
     * @return
     */
    private String getTimeStr(String createTime){
        try {
            if(TimeUtil.IsToday(createTime)){
                //今天
                return "今日:" + TimeUtil.formatTimeMD3(createTime);
            }else{
                return TimeUtil.formatTimeMD2(createTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return createTime;
        }
    }

}

