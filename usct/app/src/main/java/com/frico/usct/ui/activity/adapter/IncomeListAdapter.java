package com.frico.usct.ui.activity.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.entity.OrderVO;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.usct.utils.TimeUtil;

public class IncomeListAdapter extends BaseQuickAdapter<OrderVO, BaseQuickViewHolder> {
    private Context mConext;
    public IncomeListAdapter(Context context,int layoutResId) {
        super(layoutResId);
        mConext = context;
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, OrderVO item, int position) {
        helper.setText(R.id.tv_income_loot_money,item.getRealpaymoney()+" CNY");

        helper.setText(R.id.tv_income_loot_account, item.getInfo().getAlias() );

        //'1' => '银行卡',
        //'2' => '支付宝',
        //'3' => '微信',
        //'4' => '云闪付',
        //'5' => '微信赞赏码',
        ImageView imgPayWay = helper.getView(R.id.img_income_loot_icon);
        if (item.getPaytype().equals("3")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_weixin);
        } else if (item.getPaytype().equals("2")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_alipay);
        } else if (item.getPaytype().equals("1")) {
            imgPayWay.setImageResource(R.drawable.icon_loot_bank);
        }else if(item.getPaytype().equals("4")){
            //云闪付
            imgPayWay.setImageResource(R.drawable.icon_loot_agro);
        }else if(item.getPaytype().equals("5")){
            //微信赞赏码
            imgPayWay.setImageResource(R.drawable.icon_loot_zsm);
        }
        //设置倒计时
        setResidueTimeStr(helper,item.getAutocancletime(),position,item);

        String timeStr = TimeUtil.formatTimeHms(item.getCreatetime());
        helper.setText(R.id.tv_income_create_time, timeStr);

        TextView tvPut = helper.getView(R.id.tv_income_money_submit_btn);
        //status 0=已超时,1=待支付,2=待确认,3=已完成,4=系统处理中 5
        switch (item.getStatus()) {
            case "0":
                tvPut.setVisibility(View.GONE);
                break;
            case "1":
                tvPut.setVisibility(View.GONE);
                break;
            case "2":
                tvPut.setVisibility(View.VISIBLE);
                break;
            case "3":
                tvPut.setVisibility(View.GONE);
                break;
            case "4":
                tvPut.setVisibility(View.GONE);
                break;
            case "5":

                break;
            default:
                tvPut.setVisibility(View.GONE);
                break;
        }

        helper.addOnClickListener(R.id.tv_income_money_submit_btn);
    }


    private void setResidueTimeStr(BaseQuickViewHolder helper,long autocancletime,int position,OrderVO item){
        long currentMilltime = System.currentTimeMillis()/1000;//当前时间戳（s）
        long residueTimeLong = autocancletime - currentMilltime;
        String timeStr = TimeUtil.timeCountDownMinuteSecond(autocancletime * 1000);
        if(residueTimeLong <= 0){
            if(item.getCanclereson() == 2){
                helper.setText(R.id.tv_income_residue, "补单中");
            }else {
                helper.setText(R.id.tv_income_residue, "关闭中");
                notifiyFinishListener(position);
            }
        }else{
            helper.setText(R.id.tv_income_residue, timeStr);
            int colorId = mConext.getResources().getColor(R.color.color_13825);
            if(residueTimeLong < 180){
                colorId = mConext.getResources().getColor(R.color.color_13825);
            }else{
                colorId = mConext.getResources().getColor(R.color.color_3);
            }
            helper.setTextColor(R.id.tv_income_residue,colorId);
        }
    }

    private OnCountDownFinishedListener listener;

    public void setOnCountDownFinishListener(OnCountDownFinishedListener listener){
        this.listener = listener;
    }

    private void notifiyFinishListener(int position){
        if(listener != null){
            listener.onItemFinish(position);
        }
    }

    public interface OnCountDownFinishedListener{
        void onItemFinish(int position);
    }
}

