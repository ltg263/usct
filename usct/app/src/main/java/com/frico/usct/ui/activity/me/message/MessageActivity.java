package com.frico.usct.ui.activity.me.message;

import android.content.Intent;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MessageItemVO;
import com.frico.usct.core.entity.MessageVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.adapter.MessageListAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MessageActivity extends BaseActivity implements ActionBarClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private MessageListAdapter messageListAdapter;
    private List<MessageItemVO> messageItemVOList = new ArrayList<>();

    private int page = 1;

    @Override
    protected int setLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initTitle() {
        actionbar.setData("消息", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recycler.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(MessageActivity.this));
        initRecyData();
        getMessageList(page);
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        messageListAdapter = new MessageListAdapter(R.layout.message_list_layout);
        messageListAdapter.addData(messageItemVOList);
        messageListAdapter.setOnLoadMoreListener(this);
        messageListAdapter.bindToRecyclerView(recycler);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        messageListAdapter.setEmptyView(R.layout.loading_layout);

        //控件的点击事件
        messageListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MessageItemVO messageItemVO = messageListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.ll_message:
                    if (messageItemVO.getStatus().equals("0")) {
                        messageItemVO.setStatus("1");
                        messageListAdapter.notifyDataSetChanged();
                        readMsg(messageItemVO, messageItemVO.getId());
                    } else {
                        startActivity(new Intent(MessageActivity.this, MessageDetailActivity.class).putExtra("messBean", messageItemVO));
                    }
                    break;
                case R.id.delete:
                    break;
            }
            return true;
        });
    }

    /**
     * 点击未读消息
     *
     * @param messageItemVO
     * @param id
     */
    private void readMsg(MessageItemVO messageItemVO, int id) {
        RetrofitUtil.getInstance().apiService()
                .readmessage("" + id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            startActivity(new Intent(MessageActivity.this, MessageDetailActivity.class).putExtra("messBean", messageItemVO));
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MessageActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 获取消息列表
     */
    private void getMessageList(int page) {
        RetrofitUtil.getInstance().apiService()
                .message(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MessageVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MessageVO> result) {
                        LogUtils.e("---消息--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                messageListAdapter.getData().clear();
                            }

                            messageItemVOList.clear();
                            messageItemVOList = result.getData().getData();

                            messageListAdapter.addData(messageItemVOList);
                            messageListAdapter.loadMoreComplete();

                            if (messageItemVOList.size() < result.getData().getPer_page()) {
                                messageListAdapter.loadMoreEnd();

                                if (page == 1 && messageItemVOList.size() == 0) {
                                    messageListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MessageActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            messageListAdapter.loadMoreFail();
                            ToastUtil.showToast(MessageActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(MessageActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {
        recycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                getMessageList(page);
            }
        }, 1000);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }


}
