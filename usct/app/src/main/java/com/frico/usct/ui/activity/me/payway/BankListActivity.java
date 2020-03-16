package com.frico.usct.ui.activity.me.payway;

import android.content.Intent;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.BankVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.adapter.BankListAdapter;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.SwipeItemLayout;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BankListActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.ry_record)
    RecyclerView ryRecord;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    private BankListAdapter bankListAdapter;
    private List<BankVO.ListBean> listBeanList = new ArrayList<BankVO.ListBean>();

    @Override
    protected int setLayout() {
        return R.layout.activity_pay_way_list;
    }

    @Override
    public void initTitle() {
        actionbar.setData("银行卡", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        ryRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ryRecord.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(BankListActivity.this));
        initRecyData();
        getList();

        //添加收款方式
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(AddPayWayActivity.class);
            }
        });
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        bankListAdapter = new BankListAdapter(R.layout.item_pay_list_layout);
        bankListAdapter.addData(listBeanList);
        bankListAdapter.bindToRecyclerView(ryRecord);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        bankListAdapter.setEmptyView(R.layout.loading_layout);

        //控件的点击事件
        bankListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BankVO.ListBean listBean = bankListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.view:
                    switchBtn(listBean);
                    break;
                case R.id.ll_pay_bg:
                    startActivity(new Intent(BankListActivity.this, AddBankActivity.class).putExtra("listBean", listBean));
                    break;
            }
            return true;
        });

    }

    /**
     * 点击开关
     *
     * @param listBean
     */
    private void switchBtn(BankVO.ListBean listBean) {
        show(BankListActivity.this, listBean.getStatus().equals("normal") ? "关闭中..." : "打开中...");
        RetrofitUtil.getInstance().apiService()
                .bankswitch(listBean.getId())
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
                            listBean.setStatus(listBean.getStatus().equals("normal") ? "hidden" : "normal");
                            bankListAdapter.notifyDataSetChanged();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BankListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(BankListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(BankListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 获取银行卡列表
     */
    private void getList() {
        RetrofitUtil.getInstance().apiService()
                .banklist()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<BankVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<BankVO> result) {
                        if (result.getCode() == 1) {
                            bankListAdapter.getData().clear();

                            listBeanList = result.getData().getList();
                            bankListAdapter.addData(listBeanList);

                            if (listBeanList.size() == 0) {
                                bankListAdapter.setEmptyView(R.layout.empty_layout);
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BankListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(BankListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BankListActivity.this, e.getMessage());
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
                    @Tag(BusAction.PAY_WAY_LIST)
            }
    )
    public void getpayList(String data) {
        ryRecord.postDelayed(new Runnable() {
            @Override
            public void run() {
                getList();
            }
        }, 500);

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

}
