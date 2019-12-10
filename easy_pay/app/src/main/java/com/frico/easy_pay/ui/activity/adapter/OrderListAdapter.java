package com.frico.easy_pay.ui.activity.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.DealOrderItemVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;

public class OrderListAdapter extends BaseQuickAdapter<DealOrderItemVO, BaseQuickViewHolder> {

    public OrderListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, DealOrderItemVO item, int position) {
        helper.setText(R.id.tv_order_no, item.getOrderno());
        helper.setText(R.id.tv_time, item.getCreatetime());
        helper.setText(R.id.tv_count, item.getAmount());
        helper.setText(R.id.tv_type, item.getType());

        ImageView imgPayWay = helper.getView(R.id.img_pay_way);
        LinearLayout llPay = helper.getView(R.id.ll_pay);
        LinearLayout llPayWay = helper.getView(R.id.ll_pay_way);

        helper.setText(R.id.tv_pay_count, item.getCnymoney());
        helper.setText(R.id.tv_pay_name, item.getPayaccount());

        TextView tvOrderStatus = helper.getView(R.id.tv_order_status);

        TextView tvOrderPut = helper.getView(R.id.tv_order_put);
        TextView tvOrderAppeal = helper.getView(R.id.tv_order_appeal);
        TextView tvOrderCancle = helper.getView(R.id.tv_order_cancle);
        TextView tvOrderPay = helper.getView(R.id.tv_order_pay);

        if (item.getPayway().equals("1")) {
            imgPayWay.setBackgroundResource(R.drawable.ic_pay_bank2x);
            llPayWay.setVisibility(View.VISIBLE);
        } else if (item.getPayway().equals("2")) {
            imgPayWay.setBackgroundResource(R.drawable.ic_pay_zfb2x);
            llPayWay.setVisibility(View.VISIBLE);
        } else if (item.getPayway().equals("3")) {
            imgPayWay.setBackgroundResource(R.drawable.ic_pay_wx2x);
            llPayWay.setVisibility(View.VISIBLE);
        } else if (item.getPayway().equals("4")) {
            imgPayWay.setBackgroundResource(R.drawable.ic_pay_ysf2x);
            llPayWay.setVisibility(View.VISIBLE);
        } else if (item.getPayway().equals("5")) {
            imgPayWay.setBackgroundResource(R.drawable.ic_pay_zsm2x);
            llPayWay.setVisibility(View.VISIBLE);
        } else {
            llPayWay.setVisibility(View.GONE);
        }

        if (item.getButtontype() ==1 ) { //买入
            llPay.setVisibility(View.GONE);
        } else if (item.getButtontype() == 2) {//卖出
            llPay.setVisibility(View.VISIBLE);
        }
        tvOrderStatus.setVisibility(View.VISIBLE);
        //1=已取消,2=待支付,3=待确认,4=已完成
        switch (item.getStatus()) {
            case "1":
                tvOrderStatus.setText("已取消");
                tvOrderStatus.setSelected(true);

                tvOrderCancle.setVisibility(View.GONE);
                tvOrderAppeal.setVisibility(View.GONE);
                tvOrderPut.setVisibility(View.GONE);
                tvOrderPay.setVisibility(View.GONE);
                break;
            case "2":
                tvOrderStatus.setText("待支付");
                tvOrderStatus.setSelected(true);

                if (item.getButtontype() == 1) { //买入
                    tvOrderCancle.setVisibility(View.VISIBLE);
                    tvOrderPay.setVisibility(View.VISIBLE);
                } else if (item.getButtontype() == 2) { //卖出
                    tvOrderCancle.setVisibility(View.GONE);
                    tvOrderPay.setVisibility(View.GONE);
                } else {
                    tvOrderCancle.setVisibility(View.GONE);
                    tvOrderPay.setVisibility(View.GONE);
                }

                tvOrderAppeal.setVisibility(View.GONE);
                tvOrderPut.setVisibility(View.GONE);
                break;
            case "3":
                tvOrderStatus.setText("待确认");
                tvOrderStatus.setSelected(true);

                if (item.getButtontype() == 1) { //买入
                    tvOrderPut.setVisibility(View.GONE);
                    tvOrderStatus.setVisibility(View.VISIBLE);
                } else if (item.getButtontype() == 2) { //卖出
                    tvOrderPut.setVisibility(View.VISIBLE);
                    tvOrderStatus.setVisibility(View.GONE);
                } else {
                    tvOrderPut.setVisibility(View.GONE);
                    tvOrderStatus.setVisibility(View.VISIBLE);
                }

                tvOrderAppeal.setVisibility(View.VISIBLE);
                tvOrderCancle.setVisibility(View.GONE);
                tvOrderPay.setVisibility(View.GONE);

                if (item.getButtontype() == 1) { //买入的单子
                    if(TextUtils.equals(item.getAppealstatus(),"2")){
                        //已申诉
                        tvOrderAppeal.setText("已申诉");
                    }else{
                        tvOrderAppeal.setText("申诉");
                    }
                } else if (item.getButtontype() == 2) {//卖出的单子
                    if(TextUtils.equals(item.getByappealstatus(),"2")){
                        //已申诉
                        tvOrderAppeal.setText("已申诉");
                    }else{
                        tvOrderAppeal.setText("申诉");
                    }
                }

                break;
            case "4":
                tvOrderStatus.setVisibility(View.VISIBLE);
                tvOrderStatus.setText("已完成");
                tvOrderStatus.setSelected(false);

                tvOrderCancle.setVisibility(View.GONE);
                tvOrderAppeal.setVisibility(View.GONE);
                tvOrderPut.setVisibility(View.GONE);
                tvOrderPay.setVisibility(View.GONE);
                break;
            case "5":
                tvOrderStatus.setVisibility(View.VISIBLE);
                tvOrderStatus.setText("系统处理中");
                tvOrderStatus.setSelected(false);
                tvOrderCancle.setVisibility(View.GONE);
                tvOrderAppeal.setVisibility(View.GONE);
                tvOrderPut.setVisibility(View.GONE);
                tvOrderPay.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        helper.addOnClickListener(R.id.tv_order_put);  //放币
        helper.addOnClickListener(R.id.tv_order_appeal);  //申诉
        helper.addOnClickListener(R.id.tv_order_cancle);  //取消
        helper.addOnClickListener(R.id.tv_order_pay);  //去支付
        helper.addOnClickListener(R.id.tv_type);  //去支付
        helper.addOnClickListener(R.id.tv_order_chat);  //去支付
    }
}

