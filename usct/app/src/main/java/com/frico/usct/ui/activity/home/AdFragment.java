package com.frico.usct.ui.activity.home;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.AdVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.decoration.HorizDecoration;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.impl.PtrHandler;
import com.frico.usct.refresh.PtrFrameLayout;
import com.frico.usct.refresh.RefreshGitHeaderView;
import com.frico.usct.ui.activity.MainActivity;
import com.frico.usct.ui.activity.adapter.AdListAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.base.BaseFragment;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
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

public class AdFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, PtrHandler {

    @BindView(R.id.home_recy)
    RecyclerView homeRecy;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;

    private int page = 1;
    private View view;
    private Unbinder unbinder;
    private AdListAdapter adListAdapter;
    private MainActivity activity;
    private List<AdVO.ListBean.DataBean> adList = new ArrayList<>();
    private View headerView;

    public static AdFragment newInstance() {
        AdFragment adFragment = new AdFragment();
        return adFragment;
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

    /**
     * 初始化
     */
    private void initView() {
        actionbar.setVisibility(View.GONE);
        rotateHeaderListViewFrame.setPtrHandler(this);
        homeRecy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        homeRecy.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(activity).inflate(R.layout.home_header_layout, homeRecy, false);
    }

    /**
     * 获取底部列表数据数据
     *
     * @param page
     */
    private void initNetData(int page) {
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
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
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
        adListAdapter.bindToRecyclerView(homeRecy);
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
    }

    /**
     * 下架
     *
     * @param advertno
     */
    private void soldOut(String advertno) {
        activity.show(activity, "下架中...");
        RetrofitUtil.getInstance().apiService()
                .cancleadvert(advertno)
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
                    @Tag(BusAction.AD_LIST)
            }
    )
    public void getAdList(String data) {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
