package com.frico.usct.ui.activity.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.core.entity.AdVO;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LobbyAdapter extends BaseQuickAdapter<AdVO.ListBean.DataBean, BaseQuickViewHolder> {


    public LobbyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseQuickViewHolder helper, AdVO.ListBean.DataBean item, int position) {
        ImageView ivLobbyRvUserHead = helper.getView(R.id.iv_lobby_rv_user_head);
        TextView tvLobbyRvUsername = helper.getView(R.id.tv_lobby_rv_username);
        TextView tvNum = helper.getView(R.id.tv_num);
        TextView tvSection = helper.getView(R.id.tv_section);
        TextView tvTradingNum = helper.getView(R.id.tv_trading_num);
        ImageView ivAlipay = helper.getView(R.id.iv_alipay);
        ImageView ivWeChat = helper.getView(R.id.iv_we_chat);
        ImageView ivBank = helper.getView(R.id.iv_bank);
        TextView tvBuy = helper.getView(R.id.tv_buy);


        String limitText = "限额： "+item.getAmountmin() + " - " + item.getAmountmax();
        helper.setText(R.id.tv_section,limitText);

        helper.setText(R.id.tv_trading_num, item.getTradeamount());



    }

    static
    class ViewHolder {
        @BindView(R.id.iv_lobby_rv_user_head)
        ImageView ivLobbyRvUserHead;
        @BindView(R.id.tv_lobby_rv_username)
        TextView tvLobbyRvUsername;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_section)
        TextView tvSection;
        @BindView(R.id.tv_trading_num)
        TextView tvTradingNum;
        @BindView(R.id.iv_alipay)
        ImageView ivAlipay;
        @BindView(R.id.iv_we_chat)
        ImageView ivWeChat;
        @BindView(R.id.iv_bank)
        ImageView ivBank;
        @BindView(R.id.tv_buy)
        TextView tvBuy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
