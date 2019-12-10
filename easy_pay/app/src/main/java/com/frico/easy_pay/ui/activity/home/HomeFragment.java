package com.frico.easy_pay.ui.activity.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.DealItemVO;
import com.frico.easy_pay.core.entity.DealVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.decoration.HorizDecoration;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.impl.PtrHandler;
import com.frico.easy_pay.refresh.PtrFrameLayout;
import com.frico.easy_pay.refresh.RefreshGitHeaderView;
import com.frico.easy_pay.ui.activity.AffirmOrderActivity;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.adapter.HomeListAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.me.setting.SetPayPwdActivity;
import com.frico.easy_pay.utils.DecimalInputTextWatcher;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.MoneyEditText;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, PtrHandler, ActionBarClickListener {

//    @BindView(R.id.actionbar)
//    TranslucentActionBar actionbar;
    @BindView(R.id.home_recy)
    RecyclerView homeRecy;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;

    private int page = 1;
    private View view;
    private Unbinder unbinder;
    private HomeListAdapter homeListAdapter;
    private MainActivity activity;
    private List<DealItemVO> dealItemVOList = new ArrayList<>();
    private View headerView;
    private BottomSheetDialog bottomSheetDialog;

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();

//        actionbar.setData("交易大厅", 0, null, 0, "", this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            actionbar.setStatusBarHeight(getStatusBarHeight());
//        }

        initView();
        setRecyData();
        initNetData(page);
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        rotateHeaderListViewFrame.setPtrHandler(this);
        homeRecy.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        homeRecy.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, homeRecy, false);
    }

    /**
     * 获取列表数据
     *
     * @param page
     */
    private void initNetData(int page) {
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
     * 渲染底部列表数据
     */
    private void setRecyData() {
        homeListAdapter = new HomeListAdapter(R.layout.item_deal_list_layout);
        homeListAdapter.addData(dealItemVOList);
        homeListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        homeListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
        homeListAdapter.setHeaderView(headerView);
        homeListAdapter.bindToRecyclerView(homeRecy);
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

        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
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

    /**
     * 下拉刷新
     *
     * @param frame
     */
    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        frame.postDelayed(() -> {
            page = 1;
            initNetData(page);
        }, 1000);
    }

    /**
     * 上拉获取更多数据
     */
    @Override
    public void onLoadMoreRequested() {
        homeRecy.postDelayed(() -> {
            page++;
            initNetData(page);
        }, 1000);
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.DEAL_LIST)
            }
    )
    public void getDealList(String data) {
        homeRecy.postDelayed(() -> {
            page = 1;
            initNetData(page);
        }, 500);
    }

    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {
        //交易大厅
//        launch(MyAdvertisingActivity.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
