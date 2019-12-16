package com.frico.easy_pay.ui.activity.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.CoinVO;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.SwitchMultiButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeLobbyFragment extends BaseFragment implements OnTabSelectListener, SwitchMultiButton.OnSwitchListener {

    View view;
    @BindView(R.id.v_statusbar)
    View vStatusbar;
    @BindView(R.id.tab_lobby)
    CommonTabLayout tabLobby;
    @BindView(R.id.lobby_more)
    TextView lobbyMore;
    @BindView(R.id.ll_lobby_more)
    LinearLayout llLobbyMore;


    Unbinder unbinder;

    private final String[] mTitles = {
            "USCT", "BCT", "USDT"
            , "ETH", "EOS"
    };
    @BindView(R.id.switch_btn_lobby)
    SwitchMultiButton switchBtnLobby;
    @BindView(R.id.et_buy_num)
    EditText etBuyNum;
    @BindView(R.id.tv_buy_sort)
    TextView tvBuySort;
    @BindView(R.id.iv_buy_alipay)
    ImageView ivBuyAlipay;
    @BindView(R.id.iv_buy_we_chat)
    ImageView ivBuyWeChat;
    @BindView(R.id.iv_buy_bank)
    ImageView ivBuyBank;
    @BindView(R.id.tv_change_buy_way)
    TextView tvChangeBuyWay;
    @BindView(R.id.ll_buy_one_step)
    LinearLayout llBuyOneStep;
    @BindView(R.id.ll_root_buy)
    LinearLayout llRootBuy;
    @BindView(R.id.et_sale_num)
    EditText etSaleNum;
    @BindView(R.id.tv_sale_all)
    TextView tvSaleAll;
    @BindView(R.id.et_sale_min_num)
    EditText etSaleMinNum;
    @BindView(R.id.et_sale_max_num)
    EditText etSaleMaxNum;
    @BindView(R.id.iv_sale_alipay)
    ImageView ivSaleAlipay;
    @BindView(R.id.iv_sale_we_chat)
    ImageView ivSaleWeChat;
    @BindView(R.id.iv_sale_bank)
    ImageView ivSaleBank;
    @BindView(R.id.tv_sale_change_sale_way)
    TextView tvSaleChangeSaleWay;
    @BindView(R.id.ll_sale_one_step)
    LinearLayout llSaleOneStep;
    @BindView(R.id.ll_root_sale)
    LinearLayout llRootSale;
    @BindView(R.id.recycler_lobby)
    RecyclerView recyclerLobby;

    private final int BUY_WAY_NUM = 1001;
    private final int BUY_WAY_MONEY = 1002;

    private int buyWay = BUY_WAY_NUM;

    private final int BUY = 2001;
    private final int SALE = 2002;
    private int lobbyStatus = BUY;


    public static HomeLobbyFragment newInstance() {
        Bundle args = new Bundle();
        HomeLobbyFragment fragment = new HomeLobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_lobby, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        ArrayList<CustomTabEntity> coinVOList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params = vStatusbar.getLayoutParams();
            params.height = getStatusBarHeight();
            vStatusbar.setLayoutParams(params);
        }
        for (int i = 0; i < mTitles.length; i++) {
            CoinVO coinVO = new CoinVO(mTitles[i]);
            coinVOList.add(coinVO);
        }
        if (coinVOList.size() > 5) {
            //tabLobby.setTabWidth(70);
            llLobbyMore.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 160, 0);
            tabLobby.setLayoutParams(params);
        } else {
            llLobbyMore.setVisibility(View.GONE);
            //tabLobby.setTabSpaceEqual(true);
        }
        tabLobby.setTabData(coinVOList);
        //设置默认选中tab为购买
        switchBtnLobby.setSelectedTab(0);
        changeStatus(0);
        switchBtnLobby.setOnSwitchListener(this);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }


    @Override
    public void onSwitch(int position, String tabText) {
        changeStatus(position);
    }

    private void changeStatus(int position) {
        switch (position) {
            case 0:
                //购买状态
                ToastUtil.showToast(getActivity(), "购买");
                lobbyStatus=BUY;
                llRootBuy.setVisibility(View.VISIBLE);
                llRootSale.setVisibility(View.GONE);
                break;
            case 1:
                //出售状态
                ToastUtil.showToast(getActivity(), "出售");
                lobbyStatus=SALE;
                llRootBuy.setVisibility(View.GONE);
                llRootSale.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.lobby_more, R.id.iv_buy_alipay, R.id.iv_buy_we_chat, R.id.iv_buy_bank, R.id.tv_change_buy_way, R.id.ll_buy_one_step, R.id.tv_sale_all, R.id.iv_sale_alipay, R.id.iv_sale_we_chat, R.id.iv_sale_bank, R.id.tv_sale_change_sale_way, R.id.ll_sale_one_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_alipay:
                changeViewStatus(ivBuyAlipay);
                break;
            case R.id.iv_buy_we_chat:
                changeViewStatus(ivBuyWeChat);
                break;
            case R.id.iv_buy_bank:
                changeViewStatus(ivBuyBank);
                break;
            case R.id.tv_change_buy_way:
                changeTextStatus(tvChangeBuyWay);
                break;
            case R.id.ll_buy_one_step:
                commitBuy();
                break;
            case R.id.tv_sale_all:
                saleAllNum();
                break;
            case R.id.iv_sale_alipay:
                changeViewStatus(ivSaleAlipay);
                break;
            case R.id.iv_sale_we_chat:
                changeViewStatus(ivSaleWeChat);
                break;
            case R.id.iv_sale_bank:
                changeViewStatus(ivSaleBank);
                break;
            case R.id.tv_sale_change_sale_way:
                changeTextStatus(tvSaleChangeSaleWay);
                break;
            case R.id.ll_sale_one_step:
                commitSale();
                break;
            case R.id.lobby_more:
                showMorePop();
                break;
        }
    }

    private void commitBuy() {

    }

    private void saleAllNum() {
    }

    private void showMorePop() {

    }

    private void commitSale() {

    }

    private void changeTextStatus(TextView textView) {
        switch (buyWay) {
            case BUY_WAY_NUM:
                if (lobbyStatus == BUY) {
                    textView.setText("使用数量购买");
                } else if (lobbyStatus == SALE) {
                    textView.setText("使用数量出售");
                }
                buyWay=BUY_WAY_MONEY;
                break;
            case BUY_WAY_MONEY:
                if (lobbyStatus == BUY) {
                    textView.setText("使用金额购买");
                } else if (lobbyStatus == SALE) {
                    textView.setText("使用金额出售");
                }
                buyWay=BUY_WAY_NUM;
                break;

        }
    }

    private void changeViewStatus(ImageView imageView) {
        imageView.setSelected(!imageView.isSelected());
    }

    private void initData(){

    }
}
