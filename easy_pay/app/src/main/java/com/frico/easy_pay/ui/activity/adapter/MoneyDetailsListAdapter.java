package com.frico.easy_pay.ui.activity.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.MoneyDetailsVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;

import java.math.BigDecimal;

public class MoneyDetailsListAdapter extends BaseQuickAdapter<MoneyDetailsVO.ListBean.DataBean, BaseQuickViewHolder> {

    public MoneyDetailsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, MoneyDetailsVO.ListBean.DataBean item, int position) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        tvTitle.setText(item.getType_text());
        helper.setText(R.id.tv_time, item.getCreatetime());

        if(TextUtils.isEmpty(item.getMemo())){
            helper.setVisible(R.id.tv_memo,false);
        }else {
            helper.setVisible(R.id.tv_memo,true);
            helper.setText(R.id.tv_memo, "备注:" + item.getMemo());
        }

        TextView tvMoney = helper.getView(R.id.tv_money);

        BigDecimal bigDecimal = new BigDecimal(item.getMoney());

        int r = bigDecimal.compareTo(BigDecimal.ZERO); //和0，Zero比较
        if (r == -1) {  //小
            tvMoney.setTextColor(mContext.getResources().getColor(R.color.notic_bule));
            tvMoney.setText(item.getMoney());
        } else {
            tvMoney.setTextColor(mContext.getResources().getColor(R.color.notice));
            tvMoney.setText("+" + item.getMoney());
        }

        helper.setText(R.id.tv_yue, "结余: " + item.getEndmoney());
    }
}

