package com.frico.usct.ui.activity.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.BasePayWayListItemBean;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.usct.ui.activity.me.payway.PayWayListActivity;
import com.frico.usct.widget.SwitchButton;

public class PayWayListAdapter extends BaseQuickAdapter<BasePayWayListItemBean, BaseQuickViewHolder> {

    public PayWayListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, BasePayWayListItemBean item, int position) {
        RelativeLayout rlPayBg = helper.getView(R.id.ll_pay_bg);
        RelativeLayout rlRed = helper.getView(R.id.rl_red);
        CheckBox cbSelectDel = helper.getView(R.id.cb_select_del);
        TextView tvPayTitle = helper.getView(R.id.tv_pay_title);
        TextView tvUserName = helper.getView(R.id.tv_user_name);
        TextView tvAccountNo = helper.getView(R.id.tv_account_no);
        TextView tvCurCount = helper.getView(R.id.tv_cur_count);
        TextView tvSetNormal = helper.getView(R.id.tv_set_normal);
        SwitchButton switchBtn = helper.getView(R.id.switch_btn);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        ImageView ivIconD = helper.getView(R.id.iv_icon_d);
        ImageView imgAudit = helper.getView(R.id.img_audit);
        View view = helper.getView(R.id.view);
        tvPayTitle.setText(item.getNickName());
//        tvAccountNo.setText(item.getAliasName());
        tvCurCount.setText("当前交易USCT: " + item.getCurrentSctCount());
        rlPayBg.getLayoutParams().width = ((PayWayListActivity) mContext).getWindowManager().getDefaultDisplay().getWidth() - 80;
        cbSelectDel.setChecked(false);
        if (((PayWayListActivity) mContext).getActionBarText().equals("取消")) {
            cbSelectDel.setVisibility(View.VISIBLE);
        } else {
            cbSelectDel.setVisibility(View.GONE);
        }

        if (item.getPayWayStatus().equals("normal")) {
            switchBtn.setChecked(true);
        } else {
            switchBtn.setChecked(false);
        }

        if (item.getVerifyStatus() == 0 || item.getVerifyStatus() == 1 || item.getVerifyStatus() == 2) {
            switchBtn.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            imgAudit.setVisibility(View.GONE);
            tvSetNormal.setVisibility(View.GONE);
            if (item.getType() == 2) {
                tvPayTitle.setText("支付宝");
                tvUserName.setText(item.getNickName());
                rlPayBg.setBackgroundResource(R.drawable.bg_btn_pay_way_zfb);
                ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_zfb_x));
                ivIconD.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_zfb));
                tvSetNormal.setVisibility(View.VISIBLE);
                if (item.isDefault()) {
                    tvSetNormal.setText("当前默认");
                } else {
                    tvSetNormal.setText("点我设置聚合");
                }
            } else if (item.getType() == 3) {
                tvPayTitle.setText("微信");
                tvUserName.setText(item.getNickName());
                rlPayBg.setBackgroundResource(R.drawable.bg_btn_pay_way_wx);
                ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_wx_x));
                ivIconD.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_wx));
                tvSetNormal.setVisibility(View.VISIBLE);
                if (item.isDefault()) {
                    tvSetNormal.setText("当前默认");
                } else {
                    tvSetNormal.setText("点我设置聚合");
                }
            } else if (item.getType() == 4) {
                tvPayTitle.setText("聚合支付");
                tvUserName.setText(item.getNickName());
                rlPayBg.setBackgroundResource(R.drawable.bg_btn_pay_way_jhzf);
                ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_jhzf_x));
                ivIconD.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_jhzf));
            } else if (item.getType() == 5) {
                tvPayTitle.setText("微信赞赏码");
                tvUserName.setText(item.getNickName());
                rlPayBg.setBackgroundResource(R.drawable.bg_btn_pay_way_wx);
                ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_wx_x));
                ivIconD.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_wx));
            } else {
                //只有银行卡可删除
                tvPayTitle.setText("银行卡");
                tvUserName.setText(item.getNickName());
                rlPayBg.setBackgroundResource(R.drawable.bg_btn_pay_way_yhk);
                ivIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_yhk_x));
                ivIconD.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pay_yhk));
            }
            if (item.getVerifyStatus() == 0 || item.getVerifyStatus() == 2) {
                if (item.getVerifyStatus() == 2) {
                    imgAudit.setBackgroundResource(R.drawable.ic_pay_no_tg);
                } else {
                    imgAudit.setBackgroundResource(R.drawable.ic_pay_shz);
                }
                switchBtn.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                imgAudit.setVisibility(View.VISIBLE);//
//                rlPayBg.setBackground(mContext.getResources().getDrawable(R.drawable.ic_pay_audit2x));
                rlRed.setVisibility(View.VISIBLE);
                rlRed.getBackground().setAlpha(150);
            } else {
                rlRed.setVisibility(View.GONE);
            }
        }

        helper.addOnClickListener(R.id.view);
        helper.addOnClickListener(R.id.ll_pay_bg);
        helper.addOnClickListener(R.id.cb_select_del);
        helper.addOnClickListener(R.id.tv_set_normal);
    }
}

