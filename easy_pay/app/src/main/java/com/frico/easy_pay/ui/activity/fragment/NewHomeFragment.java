package com.frico.easy_pay.ui.activity.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.HomeTopNotifyV0;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.OrderListVO;
import com.frico.easy_pay.core.entity.OrderVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.entity.TodayIncomeOrderInfoVO;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.decoration.HorizDecoration;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.IncomeSureDialog;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.impl.CountDownTimerIncomeLootListener;
import com.frico.easy_pay.impl.PtrHandler;
import com.frico.easy_pay.refresh.PtrFrameLayout;
import com.frico.easy_pay.refresh.RefreshGitHeaderView;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.adapter.HomeFragmentListAdapter;
import com.frico.easy_pay.ui.activity.adapter.IncomeListAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.income.HistoryOrderActivity;
import com.frico.easy_pay.ui.activity.me.ShowMyQrcodeActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.IncomeOrderCountDownTimer;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chargepail.qz.www.qzzxing.activity.CaptureActivity;
import chargepail.qz.www.qzzxing.activity.CodeUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class NewHomeFragment extends BaseFragment implements ActionBarClickListener, BaseQuickAdapter.RequestLoadMoreListener, PtrHandler {

    @BindView(R.id.gif_collect)
    GifImageView gifCollect;
    Unbinder unbinder;
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_home_click_to_buy)
    TextView tvHomeClickToBuy;
    @BindView(R.id.tv_home_usable_coin)
    TextView tvHomeUsableCoin;
    @BindView(R.id.tv_home_info)
    TextView tvHomeInfo;
    @BindView(R.id.tv_home_trading)
    TextView tvHomeTrading;
    @BindView(R.id.home_recycle)
    RecyclerView homeRecycle;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    @BindView(R.id.tv_home_scan)
    TextView tvHomeScan;
    @BindView(R.id.tv_home_qr_code)
    TextView tvHomeQrCode;


    private View view;
    private GifDrawable gifDrawable;
    private MainActivity activity;
    private int page = 1;
    private HomeFragmentListAdapter adapter;
    private List<OrderVO> orderVOList = new ArrayList<>();
    private View headerView;

    private AnimationDrawable mAnimationDrawable;

    private int mCurrentOrderCount;//当前订单数
    private double mCurrentAllMoney;//当前总sct额度
    private boolean mIsRefreshListData;//是否正在刷新数据

    private int mActivateMoney;//激活金额


    public static NewHomeFragment newInstance() {
        NewHomeFragment homeFragment = new NewHomeFragment();
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        RxBus.get().register(this);
        activity = (MainActivity) getActivity();
        actionbar.setData("智能AI抢单", 0, null, 0, "", this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        initView();
        getTodayOrderInfo();
        setRecycleData();
        initNetData(page, false);
        getTodayNotify();
        return view;
    }

    private void setRecycleData() {
        adapter = new HomeFragmentListAdapter(getActivity(), R.layout.item_home_loot_list_layout_new);
        adapter.addData(orderVOList);
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setHeaderView(headerView);
        adapter.bindToRecyclerView(homeRecycle);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        adapter.setEmptyView(R.layout.loading_layout);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderVO orderVO = (OrderVO) adapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_home_loot_commit:
//                    showDg(orderVO.getRealpaymoney(), orderVO.getAmount(), orderVO.getOrdernum());
                    showHomeDialog(orderVO, orderVO.getOrdernum(), orderVO.getRealpaymoney());
                    break;
                case R.id.tv_cancle:
                    showCancel(orderVO.getOrdernum(), position);
                    break;
                case R.id.tv_delete:
                    del(orderVO.getOrdernum());
                    break;
            }
            return true;
        });
        adapter.setOnCountDownFinishListener(new IncomeListAdapter.OnCountDownFinishedListener() {
            @Override
            public void onItemFinish(int position) {
                //有倒计时结束了，需要重新刷新一下数据
                //正在刷新就不要发送延迟任务了
                if (!mIsRefreshListData) {
                    mIsRefreshListData = true;
                    delayedRefreshIncomeList();
                }
//                initNetData(1);
            }
        });
    }

    private IncomeSureDialog mSureDialog;

    private void showHomeDialog(OrderVO orderVO, String orderNum, String count) {
        if (mSureDialog == null) {
            mSureDialog = new IncomeSureDialog(getActivity());
        }
        if (!mSureDialog.isShowing()) {
            mSureDialog.show();
        }
        mSureDialog.initViewData(count);
        mSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String pw) {
                putOrder(orderVO, orderNum, pw);
            }
        });
    }

    /**
     * 放币
     *
     * @param payPwd
     * @param orderNum
     */
    private void putOrder(OrderVO orderVO, String orderNum, String payPwd) {
        activity.show(activity, "确认中...");
        RetrofitUtil.getInstance().apiService()
                .orderok(orderNum, payPwd)
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
                            //立即更新ui
//                            addNetOrder(orderVO);

                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            ToastUtil.showToast(activity, "确认成功");

                            refreshIncomeFragment();
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

    private void refreshIncomeFragment() {
        delayedRefreshTodayOrderInfo();
        initNetData(1, false);
    }

    /**
     * 延迟5s 刷新订单数据
     */
    public void delayedRefreshTodayOrderInfo() {
        homeRecycle.postDelayed(() -> {
            getTodayOrderInfo();

        }, 5000);
    }

    /**
     * 延迟2s 刷新收单列表
     */
    public void delayedRefreshIncomeList() {
        homeRecycle.postDelayed(() -> {
            initNetData(1, true);

        }, 3000);
    }


    private void initView() {
        gifDrawable = (GifDrawable) gifCollect.getDrawable();
        gifDrawable.start();
        homeRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeRecycle.addItemDecoration(new HorizDecoration(10));
        rotateHeaderListViewFrame.setPtrHandler(this);

        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, homeRecycle, false);
        if (SctApp.mUserInfoData != null) {
            tvHomeUsableCoin.setText("可用USCT: " + SctApp.mUserInfoData.getAvailable_money());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gifDrawable.stop();
        unbinder.unbind();
    }

    @OnClick({R.id.gif_collect, R.id.tv_home_click_to_buy, R.id.tv_home_trading,R.id.tv_home_scan, R.id.tv_home_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gif_collect:
                if (SctApp.mUserInfoData.isPaymentStatusOpen()) {
                    changePaymentStatusClose();
                } else {
                    changePaymentStatusOpen();
                }
                break;
            case R.id.tv_home_click_to_buy:
                break;
            case R.id.tv_home_trading:
                launch(HistoryOrderActivity.class);
                break;
            case R.id.tv_home_scan:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, CodeUtils.RESULT_SUCCESS);
                }
                break;
            case R.id.tv_home_qr_code:
                ShowMyQrcodeActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stubclose1
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getTodayOrderInfo();
            getData();
        }
    }


    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onLoadMoreRequested() {
        homeRecycle.postDelayed(() -> {
            page++;
            initNetData(page, false);
        }, 1000);
    }

    /**
     * 获取个人信息
     */
    public void getData() {
        RetrofitUtil.getInstance().apiService()
                .getusernfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbpUserVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbpUserVO> result) {
                        if (result.getCode() == 1) {
                            initSwitch(result.getData().isPayWayOpen(), result.getData().isActivated(), result.getData().getIs_activate_money());
                            SctApp.mUserInfoData = result.getData();
                            initCanUseSct();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(getActivity(), result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取个人信息
     */
    public void getTodayOrderInfo() {
        RetrofitUtil.getInstance().apiService()
                .getUserTodayOrderInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<TodayIncomeOrderInfoVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<TodayIncomeOrderInfoVO> result) {
                        if (result.getCode() == 1) {
                            TodayIncomeOrderInfoVO todayIncomeOrderInfoVO = result.getData();
                            if (todayIncomeOrderInfoVO != null) {
                                initTodayInfo(todayIncomeOrderInfoVO.getReceive_success_nums(), todayIncomeOrderInfoVO.getReceive_success_amount());
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(getActivity(), result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initCanUseSct() {
        if (SctApp.mUserInfoData != null) {
            tvHomeUsableCoin.setText("可用USCT: " + SctApp.mUserInfoData.getAvailable_money());
        }
    }

    /**
     * @param isOpen
     * @param isActivate 是否已激活
     */
    private void initSwitch(boolean isOpen, boolean isActivate, int activateMoney) {
        mActivateMoney = activateMoney;
        if (isOpen) {
            switchOpen();
            tvHomeClickToBuy.setText("正在抢单中");
            //startAnim();
        } else {
            //stopAnim();
            switchClose();
            tvHomeClickToBuy.setText("点击开始抢单");
        }
       /* if( (!isActivate) && activateMoney > 0 ) {
            //只有没激活，且金额大于0才启动激活引导
            tvIncomeActivate.setVisibility(View.VISIBLE);
            ivStartLoot.setVisibility(View.GONE);
            tvIncomeLootStatusIntro.setVisibility(View.GONE);
        }else{
            tvIncomeActivate.setVisibility(View.GONE);
            ivStartLoot.setVisibility(View.VISIBLE);
            tvIncomeLootStatusIntro.setVisibility(View.VISIBLE);
            if (isOpen) {
                switchOpen();
                tvIncomeLootStatusIntro.setText("正在抢单中");
                startAnim();
            } else {
                stopAnim();
                tvIncomeLootStatusIntro.setText("点击开始抢单");
                switchClose();
            }
        }*/
    }

    private void stopAnim() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        // ivStartLoot.setBackgroundResource(R.drawable.bg_loot_stop);
    }

    private void changePaymentStatusOpen() {
        changePaymentStatusOpen("normal");
    }

    private void changePaymentStatusClose() {
        changePaymentStatusOpen("hidden");
    }

    /**
     * 开关收款方式
     */
    private void changePaymentStatusOpen(String status) {
        RetrofitUtil.getInstance().apiService()
                .changepaymentstatus(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            getData();
                        } else if (result.getCode() == 0) {
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void switchOpen() {
        //开着的
        gifCollect.setImageResource(R.drawable.collecting);
        //btnIncomeLootSwitch.setText("停止抢单");
    }

    private void switchClose() {
        gifCollect.setImageResource(R.drawable.collect);
        //btnIncomeLootSwitch.setText("开始抢单");
    }

    private void initTodayInfo(int orderCount, double allMoney) {
        mCurrentOrderCount = orderCount;
        mCurrentAllMoney = allMoney;
        String exchange = getResources().getString(R.string.income_loot_top_intro, orderCount + "", allMoney + "");
        tvHomeInfo.setText(Html.fromHtml(exchange));
    }

    /**
     * 获取列表数据
     *
     * @param page
     */
    private void initNetData(int page, boolean isResetRefresh) {
        RetrofitUtil.getInstance().apiService()
                .orderlist(page, 2, 0, null, null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<OrderListVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<OrderListVO> result) {
                        if (isResetRefresh) {
                            mIsRefreshListData = false;
                        }
                        LogUtils.e("--收单列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                adapter.getData().clear();
                            }

                            orderVOList.clear();
                            orderVOList = result.getData().getData();
                            adapter.addData(orderVOList);

                            // mLastAdapterDataCount = incomeListAdapter.getItemCount();

                            adapter.loadMoreComplete();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }

                            if (orderVOList.size() < result.getData().getPer_page()) {
                                adapter.loadMoreEnd();

                                if (page == 1 && orderVOList.size() == 0) {
                                    adapter.setEmptyView(R.layout.empty_layout);
                                }
                            }

                            //列表有数据就倒计时刷新
                            if (adapter.getItemCount() > 0) {
                                startRunTime();
                            } else {
                                cancelCountDownTimer();
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            activity.finish();
                        } else {
                            adapter.loadMoreFail();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isResetRefresh) {
                            mIsRefreshListData = false;
                        }
                        adapter.loadMoreFail();
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
     * 是否取消
     *
     * @param orderNum
     * @param position
     */
    private void showCancel(String orderNum, int position) {
        SimpleDialog simpleDialog = new SimpleDialog(activity, "取消有可能被申诉，USCT会被解冻", "提示", "知道了", "确定取消", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                cancel(orderNum, position);
            }
        });
        simpleDialog.show();
    }

    /**
     * 去激活
     */
    private void showActivate() {
        SimpleDialog simpleDialog = new SimpleDialog(activity, "激活将扣除" + mActivateMoney + "USCT", "提示", "知道了", "确定激活", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                // 激活接口
                activate();
            }
        });
        simpleDialog.show();
    }

    /**
     * 取消
     *
     * @param orderNum
     * @param position
     */
    private void cancel(String orderNum, int position) {
        activity.show(activity, "取消中...");
        RetrofitUtil.getInstance().apiService()
                .ordercancle(orderNum)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        LogUtils.e("--取消抢单--" + new Gson().toJson(result));
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            adapter.getData().remove(position);
                            adapter.notifyDataSetChanged();

                            if (adapter.getData().size() == 0) {
                                adapter.setEmptyView(R.layout.empty_layout);
                            }

                            ToastUtil.showToast(activity, "取消成功");
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
                        LogUtils.e("--取消抢单错误--" + e.getMessage());
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 激活
     */
    private void activate() {
        activity.show(activity, "激活中...");
        RetrofitUtil.getInstance().apiService()
                .vipActivate()
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
                            ToastUtil.showToast(activity, "激活成功");
                            getTodayOrderInfo();
                            getData();

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


    private void showIncomeDialog(OrderVO orderVO, String ordernum, String count) {
        if (mSureDialog == null) {
            mSureDialog = new IncomeSureDialog(getActivity());
        }
        if (!mSureDialog.isShowing()) {
            mSureDialog.show();
        }
        mSureDialog.initViewData(count);
        mSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String pw) {
                putOrder(orderVO, ordernum, pw);

            }
        });
    }


    /**
     * 下架
     *
     * @param ordernum
     */
    private void del(String ordernum) {
        activity.show(activity, "删除中...");
        RetrofitUtil.getInstance().apiService()
                .orderdel(ordernum)
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
                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            ToastUtil.showToast(activity, "删除成功");
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
     * 获取首页通知
     */
    private void getTodayNotify() {
        RetrofitUtil.getInstance().apiService()
                .getNoticeToday()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<HomeTopNotifyV0>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<HomeTopNotifyV0> result) {
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            //获取通知
//                            ToastUtil.showToast(activity, result.getData().getNotify());
                            initTopNotifyStr(result.getData().getNotify());
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
            initNetData(page, false);
            getData();
            getTodayOrderInfo();
        }, 1000);
    }

    /**
     * 平台通告信息（例如：今天放单时间：10:22-22:00）
     *
     * @param content
     */
    private void initTopNotifyStr(String content) {
        if (!TextUtils.isEmpty(content)) {
//            llIncomeTopNotifyLay.setVisibility(View.VISIBLE);
//            tvIncomeNotifyTopContent.setText(content);
        }
    }


    private IncomeOrderCountDownTimer countDownTimer;
    private static final int GETCODE = 60 * 60 * 1000;//倒计时一个小时
    private boolean mIsCountDownTimerAlive = false;

    /**
     * 拉去到列表数据不为空的时候，启动计时器（已启动就忽略）
     */
    private void startRunTime() {
        if (countDownTimer == null) {
            countDownTimer = new IncomeOrderCountDownTimer(getActivity(), GETCODE, 1000, new CountDownTimerIncomeLootListener() {
                @Override
                public void timeFinish() {
                    mIsCountDownTimerAlive = false;
                }

                @Override
                public void timeCountDown() {
                    //每秒更新一次列表数据
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (!mIsCountDownTimerAlive) {
            mIsCountDownTimerAlive = true;
            countDownTimer.start();
        }
    }

    //取消倒计时
    private void cancelCountDownTimer() {
        if (countDownTimer != null) {
            mIsCountDownTimerAlive = false;
            countDownTimer.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       RxBus.get().unregister(this);
       /* if (unbinder != null) {
            unbinder.unbind();
        }*/
        /*if (rtvTopScrollIntro != null) {
            rtvTopScrollIntro.stopScroll();
        }*/
        cancelCountDownTimer();
    }
    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.ORDER_LIST)
            }
    )
    public void getOrderList(String data) {
        homeRecycle.postDelayed(() -> {
            page = 1;
            initNetData(page, false);
            getData();

        }, 500);
    }

    /**
     * 根据成交通知消息
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.SOCKET_ORDER_PUBLIC_NOTIFY)
            }
    )
    public void addNewOrderNotify(String data) {
//        addTextToScrollTextView(data);
    }


}
