package com.frico.easy_pay.ui.activity.me.wallet;

import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MoneyDetailsVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.adapter.MoneyDetailsListAdapter;
import com.frico.easy_pay.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.SwipeItemLayout;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MoneyThreeDetailsActivity extends BaseActivity implements ActionBarClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_top_intro)
    TextView tvTopIntro;

    private MoneyDetailsListAdapter moneyDetailsListAdapter;
    private List<MoneyDetailsVO.ListBean.DataBean> moneyList = new ArrayList<>();

    private int page = 1;

    @Override
    protected int setLayout() {
        return R.layout.activity_money_details;
    }

    @Override
    public void initTitle() {
        actionbar.setData("赠送资产明细", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        tvTopIntro.setText("*收单释放后的USCT会直接转入到余额");
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(MoneyThreeDetailsActivity.this));
        initRecyData();
        getMessageList(page);
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        moneyDetailsListAdapter = new MoneyDetailsListAdapter(R.layout.money_details_list_layout);
        moneyDetailsListAdapter.addData(moneyList);
        moneyDetailsListAdapter.setOnLoadMoreListener(this);
        moneyDetailsListAdapter.bindToRecyclerView(recycler);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        moneyDetailsListAdapter.setEmptyView(R.layout.loading_layout);
    }

    /**
     * 获取消息列表
     */
    private void getMessageList(int page) {
        RetrofitUtil.getInstance().apiService()
                .givemoneylog(0, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MoneyDetailsVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MoneyDetailsVO> result) {
                        LogUtils.e("---可用资产明细--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                moneyDetailsListAdapter.getData().clear();
                            }

                            moneyList.clear();
                            moneyList = result.getData().getList().getData();
                            moneyDetailsListAdapter.addData(moneyList);
                            moneyDetailsListAdapter.loadMoreComplete();

                            if (moneyList.size() < result.getData().getList().getPer_page()) {
                                moneyDetailsListAdapter.loadMoreEnd();

                                if (page == 1 && moneyList.size() == 0) {
                                    moneyDetailsListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MoneyThreeDetailsActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            moneyDetailsListAdapter.loadMoreFail();
                            ToastUtil.showToast(MoneyThreeDetailsActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(MoneyThreeDetailsActivity.this, e.getMessage());
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
            getMessageList(page);
        }, 1000);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
