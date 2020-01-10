package com.frico.easy_pay.ui.activity.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.AdVO;
import com.frico.easy_pay.core.entity.CoinVO;
import com.frico.easy_pay.core.entity.DealItemVO;
import com.frico.easy_pay.core.entity.DealVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.decoration.HorizDecoration;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.PtrHandler;
import com.frico.easy_pay.refresh.PtrFrameLayout;
import com.frico.easy_pay.refresh.RefreshGitHeaderView;
import com.frico.easy_pay.ui.activity.AffirmOrderActivity;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.adapter.AdListAdapter;
import com.frico.easy_pay.ui.activity.adapter.HomeListAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.me.setting.SetPayPwdActivity;
import com.frico.easy_pay.utils.DecimalInputTextWatcher;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.MoneyEditText;
import com.frico.easy_pay.widget.SwitchMultiButton;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeLobbyFragment extends BaseFragment implements OnTabSelectListener, SwitchMultiButton.OnSwitchListener, BaseQuickAdapter.RequestLoadMoreListener, PtrHandler {

    View view;
    @BindView(R.id.v_statusbar)
    View vStatusbar;
    @BindView(R.id.tab_lobby)
    CommonTabLayout tabLobby;
    @BindView(R.id.lobby_more)
    TextView lobbyMore;
    @BindView(R.id.ll_lobby_more)
    LinearLayout llLobbyMore;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    private int page = 1;


    Unbinder unbinder;

    private final String[] mTitles = {
            "USCT",
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


    private int buyType = BUY_WAY_NUM;

    private final int BUY = 2001;
    private final int SALE = 2002;
    private int lobbyStatus = BUY;

    private final int NONE = 3000;
    private final int ALI_PAY = 3001;
    private final int WE_CHAT_PAY = 3002;
    private final int BANK_PAY = 3003;
    private int buyWay = NONE;
    private int payWay = NONE;
    int payType = 0;

    private String curMax = "0.0000";
    private List<String> strList = new ArrayList<>();
    private AdListAdapter adListAdapter;
    private List<AdVO.ListBean.DataBean> adList = new ArrayList<>();
    private HomeListAdapter homeListAdapter;
    private List<DealItemVO> dealItemVOList = new ArrayList<>();
    private View headerView;
    private MainActivity activity;
    private View headerView2;


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
        activity = (MainActivity) getActivity();
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
        tabLobby.setOnTabSelectListener(this);
        //设置默认选中tab为购买
        switchBtnLobby.setSelectedTab(0);

        switchBtnLobby.setOnSwitchListener(this);
        curMax = SctApp.mUserInfoData.getMax();
        String hint = "最大可售数量" + SctApp.mUserInfoData.getMax();
        etSaleNum.setHint(hint);

        rotateHeaderListViewFrame.setPtrHandler(this);
        recyclerLobby.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerLobby.addItemDecoration(new HorizDecoration(10));
        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, recyclerLobby, false);
        headerView2 = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, recyclerLobby, false);
        setRecyData();
        changeStatus(0);
        initData(page);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onTabSelect(int position) {
        LogUtils.e(String.valueOf(position));
        if (position >= 1) {
            ToastUtil.showToast(activity, "敬请期待");
            tabLobby.setCurrentTab(0);
        }

    }

    @Override
    public void onTabReselect(int position) {
        LogUtils.e(String.valueOf(position));
        if (position >= 1) {
            ToastUtil.showToast(activity, "敬请期待");
            tabLobby.setCurrentTab(0);
        }
    }


    @Override
    public void onSwitch(int position, String tabText) {
        changeStatus(position);
    }

    private void changeStatus(int position) {
        switch (position) {
            case 0:
                //购买状态
                ToastUtil.showToast(activity, "购买");
                lobbyStatus = BUY;
                llRootBuy.setVisibility(View.VISIBLE);
                llRootSale.setVisibility(View.GONE);
                //先清空数据
                adListAdapter.getData().clear();
                adList.clear();
                adListAdapter.setNewData(adList);
                recyclerLobby.setAdapter(homeListAdapter);
                initData(page);

                break;
            case 1:
                //出售状态
                ToastUtil.showToast(activity, "出售");
                lobbyStatus = SALE;
                llRootBuy.setVisibility(View.GONE);
                llRootSale.setVisibility(View.VISIBLE);
                //先清空数据
                homeListAdapter.getData().clear();
                dealItemVOList.clear();
                homeListAdapter.setNewData(dealItemVOList);
                recyclerLobby.setAdapter(adListAdapter);
                initData(page);
                break;
        }
    }

    @OnClick({R.id.lobby_more, R.id.iv_buy_alipay, R.id.iv_buy_we_chat, R.id.iv_buy_bank, R.id.tv_change_buy_way, R.id.ll_buy_one_step, R.id.tv_sale_all, R.id.iv_sale_alipay, R.id.iv_sale_we_chat, R.id.iv_sale_bank, R.id.tv_sale_change_sale_way, R.id.ll_sale_one_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_buy_alipay:
                buyWay = (buyWay != ALI_PAY) ? ALI_PAY : NONE;
                changeViewStatus(ivBuyAlipay);
                break;
            case R.id.iv_buy_we_chat:
                buyWay = (buyWay != WE_CHAT_PAY) ? WE_CHAT_PAY : NONE;
                changeViewStatus(ivBuyWeChat);
                break;
            case R.id.iv_buy_bank:
                buyWay = (buyWay != BANK_PAY) ? BANK_PAY : NONE;
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

    //一键购买
    private void commitBuy() {
        String numStr = etBuyNum.getText().toString().trim();
        if (TextUtils.isEmpty(numStr)) {
            ToastUtil.showToast(activity, "数量不能为空");
            return;
        }
        float buyNum = Float.parseFloat(numStr);
        if (1 > buyNum || buyNum > 100000) {
            ToastUtil.showToast(activity, "数量不能在区间外");
            return;
        }


        if (buyWay==NONE){
            ToastUtil.showToast(activity, "请选择支付方式");
            return;
        }

        switch (buyWay) {
            case ALI_PAY://payType=2
                //支付宝
                payType = 2;
                break;
            case BANK_PAY://payType=1
                //跳转银行卡
                payType = 1;
                break;
            case WE_CHAT_PAY://payType=3
                //跳转微信支付
                payType = 3;
                break;
        }
        RetrofitUtil.getInstance()
                .apiService()
                .randomBuy(etBuyNum.getText().toString(), payType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.e(d.toString());
                    }

                    @Override
                    public void onNext(Result<String> stringResult) {
                        LogUtils.e(stringResult.toString());
                        ToastUtil.showToast(activity, stringResult.getMsg());
                        if (stringResult.getCode()==1){
                            Intent intent = new Intent(activity,AffirmOrderActivity.class);
                            intent.putExtra("orderno",stringResult.getData());
                            intent.putExtra("payType",payType);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("complete");
                    }
                });
    }

    //全部出售，把余额都填入edit
    private void saleAllNum() {
        etSaleNum.setText(curMax);
    }

    //更多
    private void showMorePop() {

    }

    //一键出售
    private void commitSale() {
        if (TextUtils.isEmpty(etSaleNum.getText().toString())) {
            ToastUtil.showToast(activity, "请输入交易数量");
            return;
        }

        if (TextUtils.isEmpty(etSaleMinNum.getText().toString())) {
            ToastUtil.showToast(activity, "请输入最小交易数量");
            return;
        }

        if (TextUtils.isEmpty(etSaleMaxNum.getText().toString())) {
            ToastUtil.showToast(activity, "请输入最大交易数量");
            return;
        }

        if (!ivSaleAlipay.isSelected() && !ivSaleBank.isSelected() && !ivSaleWeChat.isSelected()) {
            ToastUtil.showToast(activity, "请选择支持的收款方式");
            return;
        }
        if (ivSaleBank.isSelected()) {
            strList.add("1");
        }else {
            strList.remove("1");
        }
        if (ivSaleAlipay.isSelected()) {
            strList.add("2");
        }else {
            strList.remove("2");
        }
        if (ivSaleWeChat.isSelected()) {
            strList.add("3");
        }else {
            strList.remove("3");
        }

        //输入的 交易数量 一定是小于等于 可交易数量的
        // 最大交易数量 小于等于 输入的卖币数量
        //最小交易数量 小于等于 最大交易数量
        double curMaxCount = TextUtils.isEmpty(curMax) ? 0 : Double.parseDouble(curMax);
        double dealCount = 0;//交易金额
        dealCount = TextUtils.isEmpty(etSaleNum.getText().toString()) ? 0 : Double.parseDouble(etSaleNum.getText().toString().trim());
        double minCount = 0;//最小金额
        minCount = TextUtils.isEmpty(etSaleMinNum.getText().toString()) ? 0 : Double.parseDouble(etSaleMinNum.getText().toString().trim());
        double maxCount = 0;//最大金额
        maxCount = TextUtils.isEmpty(etSaleMaxNum.getText().toString()) ? 0 : Double.parseDouble(etSaleMaxNum.getText().toString().trim());
        if (dealCount <= curMaxCount) {
            //输入的交易数量小于等于最大可交易数量
            if (maxCount <= dealCount) {
                if (minCount <= maxCount) {
                    // 提交数据到服务器
                    if (minCount >= 1) {
                        createSale(dealCount + "", minCount + "", maxCount + "");
                    } else {
                        //最少交易1sct
                        ToastUtil.showToast(activity, "最小交易数量为 1 usct！");
                        return;
                    }
                } else {
                    //最小数量 需要小于等于 最大数量
                    ToastUtil.showToast(activity, "最小数量不能大于最大数量！");
                    return;
                }
            } else {
                //最大交易数量不可大于本次卖币数量
                ToastUtil.showToast(activity, "最大数量不能大于交易数量！");
                return;
            }
        } else {
            ToastUtil.showToast(activity, "交易数量不能大于可交易数量！");
            return;
        }
    }

    private void createSale(String count, String min, String max) {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strList) {
            stringBuilder.append(s);
            if (i < strList.size() - 1) {
                stringBuilder.append(",");
            }
            i++;
        }
        LogUtils.e("--收款方式---" + stringBuilder.toString());
        activity.show(activity, "提交中");
        RetrofitUtil.getInstance().apiService()
                .publishadvert(count, min, max, stringBuilder.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        LogUtils.e("-发布广告--" + new Gson().toJson(result));
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(activity, "发布成功");
                            RxBus.get().post(BusAction.AD_LIST, "");

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();

                        } else {
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void changeTextStatus(TextView textView) {
        switch (buyType) {
            case BUY_WAY_NUM:
                if (lobbyStatus == BUY) {
                    textView.setText("使用数量购买");
                    tvBuySort.setText("USCT");
                } else if (lobbyStatus == SALE) {
                    textView.setText("使用数量出售");
                }
                buyType = BUY_WAY_MONEY;
                break;
            case BUY_WAY_MONEY:
                if (lobbyStatus == BUY) {
                    textView.setText("使用金额购买");
                    tvBuySort.setText("CNY");
                } else if (lobbyStatus == SALE) {
                    textView.setText("使用金额出售");
                }
                buyType = BUY_WAY_NUM;
                break;

        }
    }

    private void changeViewStatus(ImageView imageView) {
        imageView.setSelected(!imageView.isSelected());
        changePayStatus();
    }

    private void changePayStatus() {
        switch (buyWay) {
            case ALI_PAY:
                ivBuyBank.setSelected(false);
                ivBuyWeChat.setSelected(false);
                break;
            case WE_CHAT_PAY:
                ivBuyBank.setSelected(false);
                ivBuyAlipay.setSelected(false);
                break;
            case BANK_PAY:
                ivBuyAlipay.setSelected(false);
                ivBuyWeChat.setSelected(false);
                break;
        }

    }

    private void initData(int page) {
        //这里获取列表数据
        switch (lobbyStatus) {
            case BUY:
                getBuyData(page);
                break;
            case SALE:
                getSaleData(page);
                break;
        }

    }

    private void getBuyData(int page) {
        RetrofitUtil.getInstance().apiService()
                .tradeadvert(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<DealVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<DealVO> result) {
                        LogUtils.e("--交易列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                homeListAdapter.getData().clear();
                            }

                            dealItemVOList.clear();
                            dealItemVOList = result.getData().getData();
                            homeListAdapter.addData(dealItemVOList);

                            homeListAdapter.loadMoreComplete();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }

                            if (dealItemVOList.size() < result.getData().getPer_page()) {
                                homeListAdapter.loadMoreEnd();

                                if (page == 1 && dealItemVOList.size() == 0) {
                                    homeListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else {
                            homeListAdapter.loadMoreFail();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        homeListAdapter.loadMoreFail();
                        if (rotateHeaderListViewFrame != null) {
                            rotateHeaderListViewFrame.refreshComplete();
                        }
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.AD_LIST)
            }
    )
    public void getAdList(String data) {
        recyclerLobby.postDelayed(() -> {
            page = 1;
            initData(page);
        }, 500);
    }

    private void getSaleData(int page) {
        RetrofitUtil.getInstance().apiService()
                .advert(1, 0, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<AdVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<AdVO> result) {
                        LogUtils.e("--广告列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                adListAdapter.getData().clear();
                            }
                            adList.clear();
                            adList = result.getData().getList().getData();

                            adListAdapter.addData(adList);
                            adListAdapter.loadMoreComplete();

                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }

                            if (adList.size() < result.getData().getList().getPer_page()) {
                                adListAdapter.loadMoreEnd();

                                if (page == 1 && adList.size() == 0) {
                                    adListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            activity.finish();
                        } else {
                            adListAdapter.loadMoreFail();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        adListAdapter.loadMoreFail();
                        if (rotateHeaderListViewFrame != null) {
                            rotateHeaderListViewFrame.refreshComplete();
                        }
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 渲染底部列表数据
     */
    private void setRecyData() {
        adListAdapter = new AdListAdapter(R.layout.item_ad_list_layout);
        adListAdapter.addData(adList);
        adListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        adListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
        adListAdapter.setHeaderView(headerView);
        adListAdapter.bindToRecyclerView(recyclerLobby);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        adListAdapter.setEmptyView(R.layout.loading_layout);

        adListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            AdVO.ListBean.DataBean dataBean = adListAdapter.getData().get(position);
            if (view.getId() == R.id.tv_sold_out) {
                SimpleDialog simpleDialog = new SimpleDialog(activity, "确定要下架此广告?", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
                    @Override
                    public void onNegBtnClick() {

                    }

                    @Override
                    public void onPosBtnClick() {
                        soldOut(dataBean.getAdvertno());
                    }
                });
                simpleDialog.show();
            }
            return true;
        });

        homeListAdapter = new HomeListAdapter(R.layout.item_deal_list_layout);
        homeListAdapter.addData(dealItemVOList);
        homeListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        homeListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
        homeListAdapter.setHeaderView(headerView2);
        homeListAdapter.bindToRecyclerView(recyclerLobby);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        homeListAdapter.setEmptyView(R.layout.loading_layout);

        homeListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            DealItemVO dealItemVO = homeListAdapter.getData().get(position);
            String limitText = "限额" + dealItemVO.getAmountmin() + " - " + dealItemVO.getAmountmax();
            showDg(limitText, dealItemVO.getAmountmax(), dealItemVO.getAdvertno());
            return true;
        });
    }

    /**
     * 下架
     *
     * @param advertNo
     */
    private void soldOut(String advertNo) {
        activity.show(activity, "下架中...");
        RetrofitUtil.getInstance().apiService()
                .cancleadvert(advertNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            RxBus.get().post(BusAction.AD_LIST, "");
                            ToastUtil.showToast(activity, "下架成功");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            activity.finish();
                        } else {
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 下拉刷新
     *
     * @param frame
     */
    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        frame.postDelayed(() -> {
            page = 1;
            initData(page);
        }, 1000);
    }

    /**
     * 上拉获取更多数据
     */
    @Override
    public void onLoadMoreRequested() {
        recyclerLobby.postDelayed(() -> {
            page++;
            initData(page);
        }, 1000);
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.DEAL_ORDER_LIST)
            }
    )
    public void getDealList(String data) {
        recyclerLobby.postDelayed(() -> {
            page = 1;
            initData(page);
        }, 500);
    }

    private BottomSheetDialog bottomSheetDialog;
    /**
     * 购买弹窗
     */
    private void showDg(String limitText, String maxAmount, String advertno) {
        bottomSheetDialog = new BottomSheetDialog(activity);
        View commentView = View.inflate(activity, R.layout.dialog_deal, null);
        MoneyEditText etMoney = commentView.findViewById(R.id.et_money);
        TextView tvLimit = commentView.findViewById(R.id.tv_limit);
        TextView tvSure = commentView.findViewById(R.id.tv_sure);
        TextView tvClose = commentView.findViewById(R.id.tv_close);
        etMoney.setHint(limitText);

        etMoney.addTextChangedListener(new DecimalInputTextWatcher(etMoney, 15, 2));

        bottomSheetDialog.setContentView(commentView);

        ViewGroup parent = (ViewGroup) commentView.getParent();
        parent.setBackgroundResource(R.color.transparent);

        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            int peekHeight = getResources().getDisplayMetrics().heightPixels;
            int height =  peekHeight / 2;
            //设置弹窗最大高度
            layoutParams.height = height;
            bottomSheet.setLayoutParams(layoutParams);
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(height);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        tvLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMoney.setText(maxAmount);
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = etMoney.getMoneyText().toString();
                LogUtils.e("--输入的金额---" + money);
                if (TextUtils.isEmpty(money)) {
                    ToastUtil.showToast(activity, "请输入购买金额");
                    return;
                }
                if(SctApp.mUserInfoData != null && ! SctApp.mUserInfoData.getIs_setpaypassword()){
                    //未设置交易密码
                    ToastUtil.showToast(activity, "请先设置交易密码");
                    //设置支付密码
                    launch(SetPayPwdActivity.class);
                    return;
                }

                showSure(money, advertno);

            }
        });

        bottomSheetDialog.show();
    }
    /**
     * 二次确认
     *
     * @param money
     * @param advertno
     */
    private void showSure(String money, String advertno) {
        SimpleDialog simpleDialog = new SimpleDialog(activity, "是否确定抢单?", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                gotoPay(money, advertno);
            }
        });
        simpleDialog.setCanceledOnTouchOutside(false);
        simpleDialog.show();
    }
    /**
     * 抢单购买
     *
     * @param amount
     * @param advertno
     */
    private void gotoPay(String amount, String advertno) {
        activity.show(activity, "抢单中...");
        RetrofitUtil.getInstance().apiService()
                .buytradeadvert(advertno, amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<String> result) {
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            startActivity(new Intent(activity, AffirmOrderActivity.class).putExtra("orderno", result.getData()));
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            activity.finish();
                        } else {
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
