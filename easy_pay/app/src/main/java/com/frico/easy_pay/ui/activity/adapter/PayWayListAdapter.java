package com.frico.easy_pay.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.BasePayWayListItemBean;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.widget.SwitchButton;

public class PayWayListAdapter extends BaseQuickAdapter<BasePayWayListItemBean, BaseQuickViewHolder> {

    public PayWayListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, BasePayWayListItemBean item, int position) {
        LinearLayout llPayBg = helper.getView(R.id.ll_pay_bg);
        TextView tvPayTitle = helper.getView(R.id.tv_pay_title);
        TextView tvAccountNo = helper.getView(R.id.tv_account_no);
        TextView tvCurCount = helper.getView(R.id.tv_cur_count);
        TextView tvSetNormal = helper.getView(R.id.tv_set_normal);
        SwitchButton switchBtn = helper.getView(R.id.switch_btn);
        ImageView imgAudit = helper.getView(R.id.img_audit);
        ImageView imgDel = helper.getView(R.id.img_pay_delete);
        View view = helper.getView(R.id.view);

        tvPayTitle.setText(item.getNickName());
        tvAccountNo.setText(item.getAliasName());
        tvCurCount.setText("当前交易USCT: " + item.getCurrentSctCount());

        if (item.getPayWayStatus().equals("normal")) {
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
        }

        if (item.getVerifyStatus() == 0) {
            switchBtn.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

            imgAudit.setBackgroundResource(R.drawable.mb_yuyue_audit2x);
            imgAudit.setVisibility(View.VISIBLE);

            llPayBg.setBackgroundResource(R.drawable.ic_pay_audit2x);
        } else if (item.getVerifyStatus() == 1) {
            switchBtn.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);

            imgAudit.setVisibility(View.GONE);

            imgDel.setVisibility(View.GONE);

            tvSetNormal.setVisibility(View.GONE);
            if (item.getType() == 2) {
                tvPayTitle.setText(item.getNickName()+"(支付宝)");
                llPayBg.setBackgroundResource(R.drawable.ic_pay_way_zfb2x);
                tvSetNormal.setVisibility(View.VISIBLE);
                if(item.isDefault()){
                    tvSetNormal.setText("当前默认");
                }else{
                    tvSetNormal.setText("点我设置聚合");
                }
            } else if (item.getType() == 3) {
                tvPayTitle.setText(item.getNickName() + "(微信)");
                llPayBg.setBackgroundResource(R.drawable.ic_pay_way_wx2x);
                tvSetNormal.setVisibility(View.VISIBLE);
                if(item.isDefault()){
                    tvSetNormal.setText("当前默认");
                }else{
                    tvSetNormal.setText("点我设置聚合");
                }
            }else if(item.getType() == 4){
                tvPayTitle.setText(item.getNickName() + "(聚合码)");
                llPayBg.setBackgroundResource(R.drawable.ic_pay_way_ysf_2x);
            }else if(item.getType() == 5){
                tvPayTitle.setText(item.getNickName() + "(微信赞赏码)");
                llPayBg.setBackgroundResource(R.drawable.ic_pay_way_zsm_2x);
            }else{
                //只有银行卡可删除
                imgDel.setVisibility(View.VISIBLE);
                tvPayTitle.setText(item.getNickName()+"(银行卡)");
                llPayBg.setBackgroundResource(R.drawable.ic_pay_way_bank2x);

            }
        } else if (item.getVerifyStatus() == 2) {
            switchBtn.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

            imgAudit.setBackgroundResource(R.drawable.mb_yuyue_faile2x);
            imgAudit.setVisibility(View.VISIBLE);
            llPayBg.setBackgroundResource(R.drawable.ic_pay_audit2x);
        }

        helper.addOnClickListener(R.id.view);
        helper.addOnClickListener(R.id.ll_pay_bg);
        helper.addOnClickListener(R.id.img_pay_delete);
        helper.addOnClickListener(R.id.img_pay_delete);
        helper.addOnClickListener(R.id.tv_set_normal);
    }
}

