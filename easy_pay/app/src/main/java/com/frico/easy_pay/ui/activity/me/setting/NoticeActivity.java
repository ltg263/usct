package com.frico.easy_pay.ui.activity.me.setting;

import android.content.Intent;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.frico.easy_pay.R;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.NoticeVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.adapter.NoticeListAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.WebUrlActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.SwipeItemLayout;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoticeActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private NoticeListAdapter noticeListAdapter;
    private View headerView;
    private List<NoticeVO> noticeVOList = new ArrayList<>();

    @Override
    protected int setLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initTitle() {
        actionbar.setData("公告", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(NoticeActivity.this));
        headerView = LayoutInflater.from(NoticeActivity.this).inflate(R.layout.home_header_layout, recycler, false);

        initRecyData();
        getNoticeList();
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        noticeListAdapter = new NoticeListAdapter(R.layout.item_notice_list_layout);
        noticeListAdapter.addData(noticeVOList);
        noticeListAdapter.bindToRecyclerView(recycler);
        noticeListAdapter.addHeaderView(headerView);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        noticeListAdapter.setEmptyView(R.layout.loading_layout);

        //控件的点击事件
        noticeListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            NoticeVO noticeVO = noticeListAdapter.getData().get(position);
            if (view.getId() == R.id.tv_title) {
                startActivity(new Intent(NoticeActivity.this, WebUrlActivity.class).putExtra(Constant.URL, noticeVO.getUrl()).putExtra(Constant.TITLE, noticeVO.getTitle()));
            }
            return true;
        });
    }

    /**
     * 获取公告列表
     */
    private void getNoticeList() {
        RetrofitUtil.getInstance().apiService()
                .notice()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<List<NoticeVO>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<NoticeVO>> result) {
                        LogUtils.e("---公告--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            noticeVOList = result.getData();
                            if(noticeVOList == null && noticeVOList.size() > 0) {
                                noticeListAdapter.addData(noticeVOList);
                            }else{
                                noticeListAdapter.setEmptyView(R.layout.empty_layout);
                            }
                        } else {
                            ToastUtil.showToast(NoticeActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(NoticeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
