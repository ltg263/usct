package com.frico.easy_pay.ui.activity.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.DealOrderItemVO;
import com.frico.easy_pay.core.entity.MbDealOrderVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.decoration.HorizDecoration;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.LetSctSureDialog;
import com.frico.easy_pay.impl.PtrHandler;
import com.frico.easy_pay.refresh.PtrFrameLayout;
import com.frico.easy_pay.refresh.RefreshGitHeaderView;
import com.frico.easy_pay.ui.activity.AffirmOrderActivity;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.adapter.OrderListAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.income.AppealActivity;
import com.frico.easy_pay.ui.activity.me.CallCenterActivity;
import com.frico.easy_pay.ui.activity.me.WebUrlActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
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

public class OrderFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, PtrHandler {

    @BindView(R.id.home_recy)
    RecyclerView homeRecy;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    private int page = 1;
    private View view;
    private Unbinder unbinder;
    private OrderListAdapter orderListAdapter;
    private MainActivity activity;
    private List<DealOrderItemVO> dealOrderItemVOList = new ArrayList<>();
    private View headerView;

    public static OrderFragment newInstance() {
        OrderFragment orderFragment = new OrderFragment();
        return orderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ad, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();

        initView();
        setRecyData();
        initNetData(page);
        return view;
    }

    private void initView() {
        actionbar.setData("交易订单", 0, null, 0, null, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        rotateHeaderListViewFrame.setPtrHandler(this);
        homeRecy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeRecy.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, homeRecy, false);
    }

    /**
     * 刷新数据
     */
    private void refreshList() {
        initNetData(1);
    }

    /**
     * 获取列表数据
     *
     * @param page
     */
    private void initNetData(int page) {
        RetrofitUtil.getInstance().apiService()
                .tradeorder(page, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbDealOrderVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbDealOrderVO> result) {
                        LogUtils.e("--交易订单列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                orderListAdapter.getData().clear();
                            }

                            dealOrderItemVOList.clear();
                            dealOrderItemVOList = result.getData().getList().getData();
                            orderListAdapter.addData(dealOrderItemVOList);

                            orderListAdapter.loadMoreComplete();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }

                            if (dealOrderItemVOList.size() < result.getData().getList().getPer_page()) {
                                orderListAdapter.loadMoreEnd();

                                if (page == 1 && dealOrderItemVOList.size() == 0) {
                                    orderListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(getActivity(), result.getMsg());
                            orderListAdapter.loadMoreFail();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }
                            ToastUtil.showToast(activity, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        orderListAdapter.loadMoreFail();
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
        orderListAdapter = new OrderListAdapter(R.layout.item_order_list_layout);
        orderListAdapter.addData(dealOrderItemVOList);
        orderListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        orderListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
        orderListAdapter.setHeaderView(headerView);
        orderListAdapter.bindToRecyclerView(homeRecy);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        orderListAdapter.setEmptyView(R.layout.loading_layout);

        orderListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DealOrderItemVO dealOrderItemVO = orderListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_order_put:
//                    showDg(dealOrderItemVO.getOrderno());
                    showLetSctDialog(dealOrderItemVO.getOrderno(), dealOrderItemVO.getCnymoney());
                    break;
                case R.id.tv_order_appeal:
                    if (dealOrderItemVO.getButtontype()== 1) { //买入
                        if (TextUtils.equals(dealOrderItemVO.getAppealstatus(), "2")) {
                            //已申诉的不可再申诉了
                            ToastUtil.showToast(getActivity(), "已申诉，不可继续申诉！");
                        } else {
                            AppealActivity.start(getActivity(), dealOrderItemVO);
                        }
                    } else if (dealOrderItemVO.getButtontype() == 2) {//卖出
                        if (TextUtils.equals(dealOrderItemVO.getByappealstatus(), "2")) {
                            //已申诉的不可再申诉了
                            ToastUtil.showToast(getActivity(), "已申诉，不可继续申诉！");
                        } else {
                            AppealActivity.start(getActivity(), dealOrderItemVO);
                        }
                    }

                    break;
                case R.id.tv_order_cancle:
                    canclePay(dealOrderItemVO.getOrderno());
                    break;
                case R.id.tv_order_pay:
                    startActivity(new Intent(activity, AffirmOrderActivity.class).putExtra("orderno", dealOrderItemVO.getOrderno()));
                    break;
                case R.id.tv_order_chat:
                    //联系对方
                    gotoOpposite(dealOrderItemVO);
                    break;
            }
            return true;
        });
    }

    private LetSctSureDialog mLetSctSureDialog;

    private void showLetSctDialog(String orderno, String payaccount) {
        if (mLetSctSureDialog == null) {
            mLetSctSureDialog = new LetSctSureDialog(getActivity());
        }
        if (!mLetSctSureDialog.isShowing()) {
            mLetSctSureDialog.show();
        }
        mLetSctSureDialog.initViewData(payaccount);
        mLetSctSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String pw) {
                putOrder(orderno, pw);
            }
        });
    }

    /**
     * 购买弹窗
     */
    private void showDg(String orderno) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View commentView = View.inflate(activity, R.layout.dialog_confirm_order, null);
        EditText etPayPwd = commentView.findViewById(R.id.et_pay_pwd);
//        TextView tvCount = commentView.findViewById(R.id.tv_count);
//        TextView tvMoney = commentView.findViewById(R.id.tv_money);
        TextView tvSure = commentView.findViewById(R.id.tv_sure);
        TextView tvClose = commentView.findViewById(R.id.tv_close);

//        tvMoney.setText(realpaymoney);
//        tvCount.setText(amount);

        bottomSheetDialog.setContentView(commentView);

        ViewGroup parent = (ViewGroup) commentView.getParent();
        parent.setBackgroundResource(R.color.transparent);

        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            int peekHeight = getResources().getDisplayMetrics().heightPixels;
            int height = peekHeight / 2;
            //设置弹窗最大高度
            layoutParams.height = height;
            bottomSheet.setLayoutParams(layoutParams);
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(height);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

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
                String payPew = etPayPwd.getText().toString();
                if (TextUtils.isEmpty(payPew) || payPew.length() != 6) {
                    ToastUtil.showToast(activity, "请输入正确的支付密码");
                    return;
                }

                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                putOrder(orderno, payPew);
            }
        });

        bottomSheetDialog.show();
    }


    /**
     * 放币
     *
     * @param orderno
     */
    private void putOrder(String orderno, String payPassword) {
        if (orderno == null)
            return;

        activity.show(activity, "确认中...");
        RetrofitUtil.getInstance().apiService()
                .tradeorderok(orderno, payPassword)
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
                            RxBus.get().post(BusAction.DEAL_ORDER_LIST, "");
                            RxBus.get().post(BusAction.DEAL_LIST, "");
                            ToastUtil.showToast(activity, "确认成功");
                            //刷新数据
                            refreshList();
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
     * 取消付款
     *
     * @param orderno
     */
    private void canclePay(String orderno) {
        activity.show(activity, "取消中...");
        RetrofitUtil.getInstance().apiService()
                .paycancle(orderno)
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
                            RxBus.get().post(BusAction.DEAL_ORDER_LIST, "");
                            RxBus.get().post(BusAction.DEAL_LIST, "");
                            ToastUtil.showToast(activity, "取消成功");
                            refreshList();
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
                    @Tag(BusAction.DEAL_ORDER_LIST)
            }
    )
    public void getDealList(String data) {
        homeRecy.postDelayed(() -> {
            page = 1;
            initNetData(page);
        }, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 和对方聊天
     */
    private void gotoOpposite(DealOrderItemVO dealOrderItemVO){
        //消息列表
        String messageListUrl = "http://kefu.yzf.life/?action=page.customerService.index";//正式的客服地址
        String toAcqid = "600002";
        if (dealOrderItemVO.getButtontype() == 1) { //买入]
            toAcqid = dealOrderItemVO.getByuserid();
        }else if(dealOrderItemVO.getButtontype() == 2){//卖出
            toAcqid = dealOrderItemVO.getUserid() + "";
        }

        if (!TextUtils.isEmpty(Prefer.getInstance().getUserId())) {
            messageListUrl = messageListUrl + "&acqid=" + Prefer.getInstance().getUserId()+"&toacqid="+toAcqid;
        }
        CallCenterActivity.start(getActivity(), false, "联系对方", messageListUrl, WebUrlActivity.TYPE_MSG_1V1);
    }


}
