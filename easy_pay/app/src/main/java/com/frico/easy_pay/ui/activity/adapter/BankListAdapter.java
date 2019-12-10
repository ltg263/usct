package com.frico.easy_pay.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.BankVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.widget.SwitchButton;


public class BankListAdapter extends BaseQuickAdapter<BankVO.ListBean, BaseQuickViewHolder> {

    public BankListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, BankVO.ListBean item, int position) {
        LinearLayout llPayBg = helper.getView(R.id.ll_pay_bg);
        TextView tvPayTitle = helper.getView(R.id.tv_pay_title);
        TextView tvAccountNo = helper.getView(R.id.tv_account_no);
        TextView tvCurCount = helper.getView(R.id.tv_cur_count);
        SwitchButton switchBtn = helper.getView(R.id.switch_btn);
        ImageView imgAudit = helper.getView(R.id.img_audit);

        ImageView delete = helper.getView(R.id.img_pay_delete);
        delete.setVisibility(View.VISIBLE);

        tvPayTitle.setText(item.getAccountname());
        tvAccountNo.setText(item.getAlias());
        tvCurCount.setText("当前交易USCT: " + item.getReceivemoney());

        if (item.getStatus().equals("normal")) {
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
        }

        if (item.getVerifystatus() == 0) {
            switchBtn.setVisibility(View.GONE);
            imgAudit.setBackgroundResource(R.drawable.mb_yuyue_audit2x);
            imgAudit.setVisibility(View.VISIBLE);

            llPayBg.setBackgroundResource(R.drawable.ic_pay_audit2x);
        } else if (item.getVerifystatus() == 1) {
            switchBtn.setVisibility(View.VISIBLE);
            imgAudit.setVisibility(View.GONE);

            llPayBg.setBackgroundResource(R.drawable.ic_pay_way_bank2x);
        } else if (item.getVerifystatus() == 2) {
            switchBtn.setVisibility(View.GONE);
            imgAudit.setBackgroundResource(R.drawable.mb_yuyue_faile2x);
            imgAudit.setVisibility(View.VISIBLE);

            llPayBg.setBackgroundResource(R.drawable.ic_pay_audit2x);
        }

        helper.addOnClickListener(R.id.view);
        helper.addOnClickListener(R.id.img_pay_delete);
        helper.addOnClickListener(R.id.ll_pay_bg);
    }

}

