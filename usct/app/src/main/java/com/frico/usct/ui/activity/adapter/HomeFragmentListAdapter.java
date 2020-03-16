package com.frico.usct.ui.activity.adapter;



import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.entity.OrderVO;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickViewHolder;
import com.frico.usct.utils.TimeUtil;

public class HomeFragmentListAdapter extends BaseQuickAdapter<OrderVO, BaseQuickViewHolder> {
    private Context mContext;

    public HomeFragmentListAdapter(Context context,int resId){
        super(resId);
        mContext = context;
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, OrderVO item, int position) {
        String str1 = mContext.getResources().getString(R.string.collection,item.getRealpaymoney()+"");
        helper.setText(R.id.tv_home_loot_money, Html.fromHtml(str1));
        helper.setText(R.id.tv_home_loot_name,"姓名： "+item.getInfo().getAccountname());

        //'1' => '银行卡',
        //'2' => '支付宝',
        //'3' => '微信',
        //'4' => '云闪付',
        //'5' => '微信赞赏码',
        ImageView imageView = helper.getView(R.id.img_income_loot_icon);
        if (item.getPaytype().equals("3")) {
            imageView.setImageResource(R.drawable.icon_loot_weixin);
        } else if (item.getPaytype().equals("2")) {
            imageView.setImageResource(R.drawable.icon_loot_alipay);
        } else if (item.getPaytype().equals("1")) {
            imageView.setImageResource(R.drawable.icon_loot_bank);
        }else if(item.getPaytype().equals("4")){
            //云闪付
            imageView.setImageResource(R.drawable.icon_loot_agro);
        }else if(item.getPaytype().equals("5")){
            //微信赞赏码
            imageView.setImageResource(R.drawable.icon_loot_zsm);
        }
        //设置倒计时
        setResidueTimeStr(helper,item.getAutocancletime(),position,item);

        String timeStr = TimeUtil.formatTimeHms(item.getCreatetime());

        //String dueTime = mContext.getResources().getString(R.string.time_duo_down,timeStr);
        helper.setText(R.id.tv_home_loot_time, timeStr);

        TextView tvPut = helper.getView(R.id.tv_home_loot_commit);
        //status 0=已超时,1=待支付,2=待确认,3=已完成,4=系统处理中 5
        switch (item.getStatus()) {
          /*  case "0":
                tvPut.setVisibility(View.GONE);
                break;
            case "1":
                tvPut.setVisibility(View.GONE);
                break;*/
            case "2":
                tvPut.setVisibility(View.VISIBLE);
                break;
/*            case "3":
                tvPut.setVisibility(View.GONE);
                break;
            case "4":
                tvPut.setVisibility(View.GONE);
                break;
            case "5":

                break;*/
            default:
                tvPut.setVisibility(View.GONE);
                break;
        }

        helper.addOnClickListener(R.id.tv_home_loot_commit);

    }
    private void setResidueTimeStr(BaseQuickViewHolder helper,long autoCancelTime,int position,OrderVO item){
        long currentMillTime = System.currentTimeMillis()/1000;//当前时间戳（s）
        long residueTimeLong = autoCancelTime - currentMillTime;
        String timeStr = TimeUtil.timeCountDownMinuteSecond(autoCancelTime * 1000);
        if(residueTimeLong <= 0){
            if(item.getCanclereson() == 2){
                helper.setText(R.id.tv_home_loot_count_down , "补单中");
            }else {
                helper.setText(R.id.tv_home_loot_count_down, "关闭中");
                notifyFinishListener(position);
            }
        }else{
            String dueTime = mContext.getResources().getString(R.string.time_duo_down,timeStr+"");
            helper.setText(R.id.tv_home_loot_count_down, Html.fromHtml(dueTime));

        }
    }

    private IncomeListAdapter.OnCountDownFinishedListener listener;

    public void setOnCountDownFinishListener(IncomeListAdapter.OnCountDownFinishedListener listener){
        this.listener = listener;
    }


    private void notifyFinishListener(int position){
        if(listener != null){
            listener.onItemFinish(position);
        }
    }

    public interface OnCountDownFinishedListener{
        void onItemFinish(int position);
    }

}
