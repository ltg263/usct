package com.frico.easy_pay.ui.activity.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.WithdrawItemVO;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.easy_pay.utils.TimeUtil;

import java.text.ParseException;

public class WithdrawListAdapter extends BaseQuickAdapter<WithdrawItemVO, BaseQuickViewHolder> {
    private Activity activity;
    public WithdrawListAdapter(Activity activity, int layoutResId) {
        super(layoutResId);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, WithdrawItemVO item, int position) {
//        helper.setText(R.id.tv_order_no,item.getOrdernum());
        //提现方式
        helper.setText(R.id.tv_withdraw_type, "提现方式:银行卡");
//        helper.setText(R.id.tv_pay_count, item.getPayamount() + " SCT");

        helper.setText(R.id.tv_time, getTimeStr(item.getCreatetime()));
        helper.setText(R.id.tv_count, " -"+item.getMoney()+" USCT");
//        helper.setText(R.id.tv_account_alias, item.getInfo().getAlias());
//        helper.setText(R.id.tv_pay_name, item.getPayaccount());
        //1=已取消,2=待审核,3=已完成,4=被驳回
        TextView tvStatus = helper.getView(R.id.tv_status);
        int colorId = mContext.getResources().getColor(R.color.color_9);
        if(item.getStatus() == 2){
            tvStatus.setText("待审核");
            colorId = mContext.getResources().getColor(R.color.color_fe840c);
        }else if(item.getStatus() == 1){
            tvStatus.setText("已取消");
        }else if(item.getStatus() == 3){
            tvStatus.setText("已完成");
        }else if(item.getStatus() == 4){
            tvStatus.setText("被驳回");
        }

        tvStatus.setTextColor(colorId);
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

