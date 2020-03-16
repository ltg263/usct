package com.frico.usct.ui.activity.income;

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
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.OrderListVO;
import com.frico.usct.core.entity.OrderVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.decoration.HorizDecoration;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.adapter.HistoryListAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.frico.usct.ui.activity.income.IncomeFillterActivity.KEY_RESULT_END;
import static com.frico.usct.ui.activity.income.IncomeFillterActivity.KEY_RESULT_START;
import static com.frico.usct.ui.activity.income.IncomeFillterActivity.KEY_RESULT_STATUS;
import static com.frico.usct.ui.activity.income.IncomeFillterActivity.KEY_RESULT_TYPE;

public class HistoryOrderActivity extends BaseActivity implements ActionBarClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_fillter_btn)
    TextView tvFillterBtn;
    @BindView(R.id.tv_fillter_intro)
    TextView tvFillterIntro;
    @BindView(R.id.tv_fillter_all_count)
    TextView tvFillterAllCount;

    private HistoryListAdapter historyListAdapter;
    private int page = 1;
    private View headerView;
    private List<OrderVO> orderVOList = new ArrayList<>();


    private String mResultStartTime;
    private String mResultEndTime;
    private int mResultType;
    private int mResultStatus;


    @Override
    protected int setLayout() {
        return R.layout.activity_income_history;
    }

    @Override
    public void initTitle() {
        actionbar.setData("交易记录", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(HistoryOrderActivity.this).inflate(R.layout.home_header_layout, recycler, false);

        tvFillterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncomeFillterActivity.start(HistoryOrderActivity.this);
            }
        });
        initRecyData();
        getHisOrderList(page);
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        historyListAdapter = new HistoryListAdapter(this, R.layout.item_history_list_layout_new);
        historyListAdapter.addData(orderVOList);
        historyListAdapter.setOnLoadMoreListener(this);
        historyListAdapter.bindToRecyclerView(recycler);
        historyListAdapter.addHeaderView(headerView);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        historyListAdapter.setEmptyView(R.layout.loading_layout);

        //控件的点击事件
        historyListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderVO orderVO = historyListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.tv_put:
                    showDg(orderVO.getAmount(), orderVO.getOrdernum());
                    break;
                case R.id.tv_cancle:
                    showCancle(orderVO.getOrdernum(), position);
                    break;
                case R.id.tv_delete:
                    showDel(orderVO.getOrdernum());
                    break;
            }
            return true;
        });
    }

    /**
     * 删除确认
     *
     * @param ordernum
     */
    private void showDel(String ordernum) {

        SimpleDialog simpleDialog = new SimpleDialog(HistoryOrderActivity.this, "是否删除该订单", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                del(ordernum);
            }
        });
        simpleDialog.show();
    }

    /**
     * 查询全部
     * @param page
     */
    private void getHisOrderList(int page){
        getHisOrderList(page,5,mResultType,mResultStartTime,mResultEndTime);
    }

    /**
     * 获取历史订单列表
     *
     * @param page
     */
    private void getHisOrderList(int page,int status,int payType,String startTime,String endTime) {
        RetrofitUtil.getInstance().apiService()
                .orderlist(page, status, payType,startTime,endTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<OrderListVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Result<OrderListVO> result) {
                        LogUtils.e("--历史收单列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                historyListAdapter.getData().clear();
                            }

                            orderVOList.clear();
                            orderVOList = result.getData().getData();
                            historyListAdapter.addData(orderVOList);
                            historyListAdapter.loadMoreComplete();

                            if (orderVOList.size() < result.getData().getPer_page()) {
                                historyListAdapter.loadMoreEnd();

                                if (page == 1 && orderVOList.size() == 0) {
                                    historyListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(HistoryOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            historyListAdapter.loadMoreFail();
                            ToastUtil.showToast(HistoryOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        historyListAdapter.loadMoreFail();
                        ToastUtil.showToast(HistoryOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
        SimpleDialog simpleDialog = new SimpleDialog(HistoryOrderActivity.this, "取消有可能被申诉，USCT会被解冻", "提示", "知道了", "确定取消", new SimpleDialog.OnButtonClick() {
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
     * 取消
     *
     * @param ordernum
     * @param position
     */
    private void cancle(String ordernum, int position) {
        show(HistoryOrderActivity.this, "取消中...");
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
                        dismiss();
                        if (result.getCode() == 1) {
                            historyListAdapter.getData().remove(position);
                            historyListAdapter.notifyDataSetChanged();

                            if (historyListAdapter.getData().size() == 0) {
                                historyListAdapter.setEmptyView(R.layout.empty_layout);
                            }

                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            RxBus.get().post(BusAction.HIS_ORDER_LIST, "");
                            ToastUtil.showToast(HistoryOrderActivity.this, "取消成功");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(HistoryOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(HistoryOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(HistoryOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 购买弹窗
     */
    private void showDg(String amount, String ordernum) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HistoryOrderActivity.this);
        View commentView = View.inflate(HistoryOrderActivity.this, R.layout.dialog_put, null);
        EditText etPayPwd = commentView.findViewById(R.id.et_pay_pwd);
        TextView tvCount = commentView.findViewById(R.id.tv_count);
        TextView tvSure = commentView.findViewById(R.id.tv_sure);
        TextView tvClose = commentView.findViewById(R.id.tv_close);
        tvCount.setText(amount);

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
                    ToastUtil.showToast(HistoryOrderActivity.this, "请输入正确的支付密码");
                    return;
                }

                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                putOrder(ordernum, payPew);
            }
        });

        bottomSheetDialog.show();
    }

    /**
     * 放币
     *
     * @param payPwd
     * @param ordernum
     */
    private void putOrder(String ordernum, String payPwd) {
        show(HistoryOrderActivity.this, "确认中...");
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
                        dismiss();
                        if (result.getCode() == 1) {
                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            RxBus.get().post(BusAction.HIS_ORDER_LIST, "");
                            ToastUtil.showToast(HistoryOrderActivity.this, "确认成功");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(HistoryOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(HistoryOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(HistoryOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 删除
     *
     * @param ordernum
     */
    private void del(String ordernum) {
        show(HistoryOrderActivity.this, "删除中...");
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
                        dismiss();
                        if (result.getCode() == 1) {
                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            RxBus.get().post(BusAction.HIS_ORDER_LIST, "");
                            ToastUtil.showToast(HistoryOrderActivity.this, "删除成功");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(HistoryOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(HistoryOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(HistoryOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onLoadMoreRequested() {
        recycler.postDelayed(() -> {
            page++;

            LogUtils.d("--page---" + page);
            getHisOrderList(page);
        }, 1000);
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.HIS_ORDER_LIST)
            }
    )
    public void getOrderList(String data) {
        recycler.postDelayed(() -> {
            page = 1;
            getHisOrderList(page);
        }, 500);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }


    /**
     * 筛选弹窗
     */
    private void showFiltterDg(String amount, String ordernum) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HistoryOrderActivity.this);
        View commentView = View.inflate(HistoryOrderActivity.this, R.layout.dialog_put, null);
        EditText etPayPwd = commentView.findViewById(R.id.et_pay_pwd);
        TextView tvCount = commentView.findViewById(R.id.tv_count);
        TextView tvSure = commentView.findViewById(R.id.tv_sure);
        TextView tvClose = commentView.findViewById(R.id.tv_close);
        tvCount.setText(amount);

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
                    ToastUtil.showToast(HistoryOrderActivity.this, "请输入正确的支付密码");
                    return;
                }

                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();

                putOrder(ordernum, payPew);
            }
        });

        bottomSheetDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == IncomeFillterActivity.TYPE_REQUEST_CODE){
            mResultStartTime = data.getStringExtra(KEY_RESULT_START);
            mResultEndTime = data.getStringExtra(KEY_RESULT_END);
            mResultType = data.getIntExtra(KEY_RESULT_TYPE,0);
            mResultStatus = data.getIntExtra(KEY_RESULT_STATUS,0);
            // 筛选去
            getHisOrderList(1,mResultStatus,mResultType,mResultStartTime,mResultEndTime);
        }

    }



}
