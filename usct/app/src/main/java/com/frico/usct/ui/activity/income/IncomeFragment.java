package com.frico.usct.ui.activity.income;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.HomeTopNotifyV0;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.OrderListVO;
import com.frico.usct.core.entity.OrderVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.entity.TodayIncomeOrderInfoVO;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.decoration.HorizDecoration;
import com.frico.usct.dialog.BaseIncomeSureDialog;
import com.frico.usct.dialog.IncomeSureDialog;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.impl.CountDownTimerIncomeLootListener;
import com.frico.usct.impl.PtrHandler;
import com.frico.usct.refresh.PtrFrameLayout;
import com.frico.usct.refresh.RefreshGitHeaderView;
import com.frico.usct.ui.activity.MainActivity;
import com.frico.usct.ui.activity.adapter.IncomeListAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.base.BaseFragment;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.Util;
import com.frico.usct.widget.AutoScrollTextView;
import com.frico.usct.widget.IncomeOrderCountDownTimer;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 收单页面
 */
public class IncomeFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, PtrHandler, ActionBarClickListener {
    private static final int TOP_NOTIFY_SIZE = 5;//滚动显示的队列大小
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.home_recy)
    RecyclerView homeRecy;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    @BindView(R.id.tv_income_loot_can_used_balance)
    TextView tvIncomeLootCanUsedBalance;
    @BindView(R.id.btn_income_loot_switch)
    Button btnIncomeLootSwitch;
    @BindView(R.id.iv_start_loot)
    ImageView ivStartLoot;
    @BindView(R.id.tv_income_tool_today_info)
    TextView tvIncomeToolTodayInfo;
    @BindView(R.id.tv_income_loot_status_intro)
    TextView tvIncomeLootStatusIntro;
    @BindView(R.id.rtv_top_scroll_intro)
    AutoScrollTextView rtvTopScrollIntro;
    @BindView(R.id.ll_top_scroll_order_lay)
    LinearLayout llTopScrollOrderLay;
    @BindView(R.id.iv_income_top_notify_close_btn)
    ImageView ivIncomeTopNotifyCloseBtn;
    @BindView(R.id.ll_income_top_notify_lay)
    LinearLayout llIncomeTopNotifyLay;
    @BindView(R.id.tv_income_notify_top_content)
    TextView tvIncomeNotifyTopContent;
    @BindView(R.id.tv_income_activate)
    TextView tvIncomeActivate;


    private int page = 1;
    private View view;
    private Unbinder unbinder;
    private IncomeListAdapter incomeListAdapter;
    private MainActivity activity;
    private List<OrderVO> orderVOList = new ArrayList<>();
    private View headerView;

    private int mLastAdapterDataCount;  //记录adapter的数据count

    private AnimationDrawable mAnimationDrawable;

    private int mCurrentOrderCount;//当前订单数
    private double mCurrentAllMoney;//当前总sct额度
    private boolean mIsRefreshListData;//是否正在刷新数据

    private int mActivateMoney;//激活金额

    public static IncomeFragment newInstance() {
        IncomeFragment homeFragment = new IncomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_income, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();

        actionbar.setData("智能AI抢单", 0, null, 0, "订单记录", this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        initView();
        setRecyData();
        initNetData(page, false);
        getTodayNotify();
        return view;
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


    /**
     * 初始化
     */
    private void initView() {

        ivIncomeTopNotifyCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llIncomeTopNotifyLay.setVisibility(View.GONE);
            }
        });

        rotateHeaderListViewFrame.setPtrHandler(this);
        homeRecy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeRecy.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, homeRecy, false);
        if (SctApp.mUserInfoData != null) {
            tvIncomeLootCanUsedBalance.setText("可抢USCT: " + SctApp.mUserInfoData.getAvailable_money());
        }
        ivStartLoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //收款开关
                if (SctApp.mUserInfoData.isPaymentStatusOpen()) {
                    changePaymentStatusClose();
                } else {
                    changePaymentStatusOpen();
                }
            }
        });
        tvIncomeActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去激活
                showActivate();
            }
        });
    }

    private void startAnim() {
        ivStartLoot.setBackgroundResource(R.drawable.anim_list_start_loot);
        //获取背景，并将其强转成AnimationDrawable
        mAnimationDrawable = (AnimationDrawable) ivStartLoot.getBackground();
        //判断是否在运行
        if (!mAnimationDrawable.isRunning()) {
            //开启帧动画
            mAnimationDrawable.start();
        }
    }

    private void stopAnim() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        ivStartLoot.setBackgroundResource(R.drawable.bg_loot_stop);
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
                            initSwitch(result.getData().isPayWayOpen(),result.getData().isActivated(),result.getData().getIs_activate_money());
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
            tvIncomeLootCanUsedBalance.setText("可抢USCT: " + SctApp.mUserInfoData.getAvailable_money());
        }
    }

    /**
     *
     * @param isOpen
     * @param isActivate  是否已激活
     */
    private void initSwitch(boolean isOpen,boolean isActivate,int activateMoney) {
        mActivateMoney = activateMoney;
        if( (!isActivate) && activateMoney > 0 ) {
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
        }
    }


    private void switchOpen() {
        //开着的
        btnIncomeLootSwitch.setBackgroundResource(R.drawable.selector_btn_income_loot_close);
        btnIncomeLootSwitch.setText("停止抢单");
    }

    private void switchClose() {
        btnIncomeLootSwitch.setBackgroundResource(R.drawable.selector_btn_income_loot_open);
        btnIncomeLootSwitch.setText("开始抢单");
    }

    private void initTodayInfo(int orderCount, double allMoney) {
        mCurrentOrderCount = orderCount;
        mCurrentAllMoney = allMoney;
        String exchange = getResources().getString(R.string.income_loot_top_intro, orderCount + "", allMoney + "");
        tvIncomeToolTodayInfo.setText(Html.fromHtml(exchange));
    }

    private void addNetOrder(OrderVO orderVO) {
        int currentCount = mCurrentOrderCount + 1;
        double payaccount = TextUtils.isEmpty(orderVO.getAmount()) ? 0.0 : Double.parseDouble(orderVO.getAmount());
        double moneyAll = Util.add(mCurrentAllMoney, payaccount);
        initTodayInfo(currentCount, moneyAll);
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
                                incomeListAdapter.getData().clear();
                            }

                            orderVOList.clear();
                            orderVOList = result.getData().getData();
                            incomeListAdapter.addData(orderVOList);

                            mLastAdapterDataCount = incomeListAdapter.getItemCount();

                            incomeListAdapter.loadMoreComplete();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }

                            if (orderVOList.size() < result.getData().getPer_page()) {
                                incomeListAdapter.loadMoreEnd();

                                if (page == 1 && orderVOList.size() == 0) {
                                    incomeListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }

                            //列表有数据就倒计时刷新
                            if (incomeListAdapter.getItemCount() > 0) {
                                startRunTime();
                            } else {
                                cancelCountDownTimer();
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(activity, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            activity.finish();
                        } else {
                            incomeListAdapter.loadMoreFail();
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
                        incomeListAdapter.loadMoreFail();
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
        incomeListAdapter = new IncomeListAdapter(getActivity(), R.layout.item_income_loot_list_layout);
        incomeListAdapter.addData(orderVOList);
        incomeListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        incomeListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
        incomeListAdapter.setHeaderView(headerView);
        incomeListAdapter.bindToRecyclerView(homeRecy);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        incomeListAdapter.setEmptyView(R.layout.loading_layout);

        incomeListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderVO orderVO = incomeListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_income_money_submit_btn:
//                    showDg(orderVO.getRealpaymoney(), orderVO.getAmount(), orderVO.getOrdernum());
                    showIncomeDialog(orderVO, orderVO.getOrdernum(), orderVO.getRealpaymoney());
                    break;
                case R.id.tv_cancle:
                    showCancle(orderVO.getOrdernum(), position);
                    break;
                case R.id.tv_delete:
                    del(orderVO.getOrdernum());
                    break;
            }
            return true;
        });

        incomeListAdapter.setOnCountDownFinishListener(new IncomeListAdapter.OnCountDownFinishedListener() {
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

    /**
     * 是否取消
     *
     * @param ordernum
     * @param position
     */
    private void showCancle(String ordernum, int position) {
        SimpleDialog simpleDialog = new SimpleDialog(activity, "取消有可能被申诉，USCT会被解冻", "提示", "知道了", "确定取消", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                cancle(ordernum, position);
            }
        });
        simpleDialog.show();
    }

    /**
     * 去激活
     *
     */
    private void showActivate() {
        SimpleDialog simpleDialog = new SimpleDialog(activity, "激活将扣除"+ mActivateMoney + "USCT", "提示", "知道了", "确定激活", new SimpleDialog.OnButtonClick() {
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
     * @param ordernum
     * @param position
     */
    private void cancle(String ordernum, int position) {
        activity.show(activity, "取消中...");
        RetrofitUtil.getInstance().apiService()
                .ordercancle(ordernum)
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
                            incomeListAdapter.getData().remove(position);
                            incomeListAdapter.notifyDataSetChanged();

                            if (incomeListAdapter.getData().size() == 0) {
                                incomeListAdapter.setEmptyView(R.layout.empty_layout);
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
     *
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

    private IncomeSureDialog mSureDialog;

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

//    /**
//     * 购买弹窗
//     */
//    private void showDg(String realpaymoney, String amount, String ordernum) {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
//        View commentView = View.inflate(activity, R.layout.dialog_put, null);
//        EditText etPayPwd = commentView.findViewById(R.id.et_pay_pwd);
//        TextView tvCount = commentView.findViewById(R.id.tv_count);
//        TextView tvMoney = commentView.findViewById(R.id.tv_money);
//        TextView tvSure = commentView.findViewById(R.id.tv_sure);
//        TextView tvClose = commentView.findViewById(R.id.tv_close);
//
//        tvMoney.setText(realpaymoney);
//        tvCount.setText(amount);
//
//        bottomSheetDialog.setContentView(commentView);
//
//        ViewGroup parent = (ViewGroup) commentView.getParent();
//        parent.setBackgroundResource(R.color.transparent);
//
//        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
//        if (bottomSheet != null) {
//            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
//            int peekHeight = getResources().getDisplayMetrics().heightPixels;
//            int height = peekHeight / 2;
//            //设置弹窗最大高度
//            layoutParams.height = height;
//            bottomSheet.setLayoutParams(layoutParams);
//            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
//            //peekHeight即弹窗的最大高度
//            behavior.setPeekHeight(height);
//            // 初始为展开状态
//            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//
//        tvClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bottomSheetDialog != null)
//                    bottomSheetDialog.dismiss();
//            }
//        });
//
//        tvSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String payPew = etPayPwd.getText().toString();
//                if (TextUtils.isEmpty(payPew) || payPew.length() != 6) {
//                    ToastUtil.showToast(activity, "请输入正确的支付密码");
//                    return;
//                }
//
//                if (bottomSheetDialog != null)
//                    bottomSheetDialog.dismiss();
//
//                putOrder(ordernum, payPew);
//            }
//        });
//
//        bottomSheetDialog.show();
//    }

    /**
     * 放币
     *
     * @param payPwd
     * @param ordernum
     */
    private void putOrder(OrderVO orderVO, String ordernum, String payPwd) {
        activity.show(activity, "确认中...");
        RetrofitUtil.getInstance().apiService()
                .orderok(ordernum, payPwd)
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
     * 上拉获取更多数据
     */
    @Override
    public void onLoadMoreRequested() {
        homeRecy.postDelayed(() -> {
            page++;
            initNetData(page, false);
        }, 1000);
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
        homeRecy.postDelayed(() -> {
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

    /**
     * 延迟5s 刷新订单数据
     */
    public void delayedRefreshTodayOrderInfo() {
        homeRecy.postDelayed(() -> {
            getTodayOrderInfo();

        }, 5000);
    }

    /**
     * 延迟2s 刷新收单列表
     */
    public void delayedRefreshIncomeList() {
        homeRecy.postDelayed(() -> {
            initNetData(1, true);

        }, 3000);
    }


    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {
        launch(HistoryOrderActivity.class);
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
                    if (incomeListAdapter != null) {
                        incomeListAdapter.notifyDataSetChanged();
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
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (rtvTopScrollIntro != null) {
            rtvTopScrollIntro.stopScroll();
        }
        cancelCountDownTimer();
    }

    private void initScrollTextView() {
        List<String> nameList = new ArrayList<>();
        nameList.add("有人成功收款1000元");
        nameList.add("有人成功收款2000元");
        nameList.add("有人成功收款5000元");
        rtvTopScrollIntro.setText(nameList.get(0));
        rtvTopScrollIntro.setList(nameList);
        rtvTopScrollIntro.startScroll();
        rtvTopScrollIntro.setClickLisener(new AutoScrollTextView.ItemClickLisener() {
            @Override
            public void onClick(int position) {
//                ToastUtil.showToast(getActivity(), "点击");
            }
        });
    }

    public void addTextToScrollTextView(String notifyText) {
//        rtvTopScrollIntro.stopScroll();//禁止滚动
        List<String> nameList = rtvTopScrollIntro.getList();
        int size = nameList.size();
        if (size >= TOP_NOTIFY_SIZE) {
            nameList.remove(size - 1);
        }
        nameList.add(0, notifyText);
        llTopScrollOrderLay.setVisibility(View.VISIBLE);
        rtvTopScrollIntro.setText(nameList.get(0));
        rtvTopScrollIntro.setList(nameList);

//        rtvTopScrollIntro.startScroll();//禁止滚动
//        if(! rtvTopScrollIntro.isRunning()) {
//            rtvTopScrollIntro.startScroll();
//        }
    }

    /**
     * 平台通告信息（例如：今天放单时间：10:22-22:00）
     *
     * @param content
     */
    private void initTopNotifyStr(String content) {
        if (!TextUtils.isEmpty(content)) {
            llIncomeTopNotifyLay.setVisibility(View.VISIBLE);
            tvIncomeNotifyTopContent.setText(content);
        }
    }

}
