package com.frico.usct.ui.activity.me.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.widget.TranslucentActionBar;
import com.google.gson.Gson;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MyGroupInfoVO;
import com.frico.usct.core.entity.MyGroupItemVO;
import com.frico.usct.core.entity.MyGroupMemberListVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.decoration.HorizDecoration;
import com.frico.usct.impl.PtrHandler;
import com.frico.usct.refresh.PtrFrameLayout;
import com.frico.usct.refresh.RefreshGitHeaderView;
import com.frico.usct.ui.activity.adapter.MyGroupListAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseQuickAdapter;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.Util;
import com.frico.usct.utils.image.ImageLoaderImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的团队
 */
public class MyGroupActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    private static String KEY_IS_MY_GROUP = "isMyGroup";
    private static String KEY_ACQID = "acqid";
    private static String KEY_GROUP_ITEM = "groupItem";
    @BindView(R.id.iv_group_tag)
    ImageView ivGroupTag;
    @BindView(R.id.tv_group_tag_name)
    TextView tvGroupTagName;
    @BindView(R.id.tv_my_group_first_count)
    TextView tvMyGroupFirstCount;
    @BindView(R.id.tv_my_group_count_all)
    TextView tvMyGroupCountAll;
    @BindView(R.id.tv_my_group_sct_amount)
    TextView tvMyGroupSctAmount;
    @BindView(R.id.ll_director_lay)
    LinearLayout llDirectorLay;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.im_my_tag)
    ImageView imMyTag;
    @BindView(R.id.iv_member_my_group_header)
    ImageView imMemberHeader;
    @BindView(R.id.tv_member_my_group_first_count)
    TextView tvMemberMyGroupFirstCount;
    @BindView(R.id.tv_member_my_group_count_all)
    TextView tvMemberMyGroupCountAll;
    @BindView(R.id.tv_member_my_group_sct_amount)
    TextView tvMemberMyGroupSctAmount;
    @BindView(R.id.ll_member_lay)
    LinearLayout llMemberLay;
    @BindView(R.id.home_recy)
    RecyclerView homeRecy;
    @BindView(R.id.rotate_header_list_view_frame)
    RefreshGitHeaderView rotateHeaderListViewFrame;
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    private MyGroupListAdapter myGroupListAdapter;
    private boolean mIsMyGroup;

    private List<MyGroupItemVO> orderVOList = new ArrayList<>();
    private int page;
    private String mAcqid;

    private MyGroupItemVO mCurrentGroupItemV0;
    private View headerView;

    /**
     * 我的团队
     *
     * @param activity
     * @param isMyGroup 是否是我的团队  （区分 我的团队  和 我的儿子辈的团队）
     */
    public static void start(Activity activity, boolean isMyGroup, String acqid, MyGroupItemVO myGroupItemVO) {
        Intent intent = new Intent(activity, MyGroupActivity.class);
        intent.putExtra(KEY_IS_MY_GROUP, isMyGroup);
        intent.putExtra(KEY_ACQID, acqid);
        intent.putExtra(KEY_GROUP_ITEM, myGroupItemVO);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_my_group;
    }

    @Override
    public void initTitle() {
        actionbar.setData("我的团队", R.drawable.ic_left_back2x, null, 0, null,
                new ActionBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        mIsMyGroup = getIntent().getBooleanExtra(KEY_IS_MY_GROUP, false);
        mAcqid = getIntent().getStringExtra(KEY_ACQID);
        if (!mIsMyGroup) {
            mCurrentGroupItemV0 = (MyGroupItemVO) getIntent().getSerializableExtra(KEY_GROUP_ITEM);
        }
        if (mIsMyGroup) {
            showMyGroup();
        } else {
            showMySoneGroup();
        }
        initView();
        setRecyData();
        getMyGroupInfo();
        getMyGroupList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 展示我的团队
     */
    private void showMyGroup() {
        llDirectorLay.setVisibility(View.VISIBLE);
        llMemberLay.setVisibility(View.GONE);
    }

    /**
     * 展示我的下级 的团队
     */
    private void showMySoneGroup() {
        llDirectorLay.setVisibility(View.GONE);
        llMemberLay.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化
     */
    private void initView() {

        rotateHeaderListViewFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMyGroupList();
            }
        });
        homeRecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        homeRecy.addItemDecoration(new HorizDecoration(10));

        headerView = LayoutInflater.from(this).inflate(R.layout.home_header_layout, homeRecy, false);
    }


    /**
     * 渲染底部列表数据
     */
    private void setRecyData() {
        myGroupListAdapter = new MyGroupListAdapter(this, R.layout.item_my_group);
        myGroupListAdapter.addData(orderVOList);
        myGroupListAdapter.setOnLoadMoreListener(this);
        //recyclerView的加载动画
        myGroupListAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //适配器与recyclerView绑定  可以不用使用recyclerView.setAdapter();
//        myGroupListAdapter.setHeaderView(headerView);
        myGroupListAdapter.bindToRecyclerView(homeRecy);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        myGroupListAdapter.setEmptyView(R.layout.loading_layout);

        myGroupListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //进入子数据页面
                MyGroupActivity.start(MyGroupActivity.this, false, myGroupListAdapter.getItem(position).getAcqid(), myGroupListAdapter.getItem(position));
            }
        });
    }


    private void getMyGroupList() {
        getData(mAcqid, 1);
    }

    /**
     * 获取团队 列表数据
     *
     * @param page
     */
    private void getData(String acqid, int page) {
        RetrofitUtil.getInstance().apiService()
                .getMyTeamLower(acqid, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MyGroupMemberListVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MyGroupMemberListVO> result) {
                        LogUtils.e("--团队列表--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            if (page == 1) {
                                myGroupListAdapter.getData().clear();
                            }
                            if (orderVOList != null) {
                                orderVOList.clear();
                            }
                            orderVOList = result.getData().getData();
                            if (orderVOList != null && orderVOList.size() > 0) {
                                myGroupListAdapter.addData(orderVOList);

                                myGroupListAdapter.loadMoreComplete();
                                if (rotateHeaderListViewFrame != null) {
                                    rotateHeaderListViewFrame.refreshComplete();
                                }

                                if (orderVOList.size() < result.getData().getPer_page()) {
                                    myGroupListAdapter.loadMoreEnd();

                                    if (page == 1 && orderVOList.size() == 0) {
                                        myGroupListAdapter.setEmptyView(R.layout.empty_layout);
                                    }
                                }
                            } else {
                                if (rotateHeaderListViewFrame != null) {
                                    rotateHeaderListViewFrame.refreshComplete();
                                }
                                myGroupListAdapter.loadMoreEnd();
                                if (page == 1) {
                                    myGroupListAdapter.setEmptyView(R.layout.empty_layout);
                                }
                            }
                        } else {
                            myGroupListAdapter.loadMoreFail();
                            if (rotateHeaderListViewFrame != null) {
                                rotateHeaderListViewFrame.refreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        myGroupListAdapter.loadMoreFail();
                        if (rotateHeaderListViewFrame != null) {
                            rotateHeaderListViewFrame.refreshComplete();
                        }
                        ToastUtil.showToast(MyGroupActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getMyGroupInfo() {
        show(MyGroupActivity.this, "加载中...");
        RetrofitUtil.getInstance().apiService()
                .getMyTeam(mAcqid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MyGroupInfoVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MyGroupInfoVO> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            MyGroupInfoVO data = result.getData();
                            if (mIsMyGroup) {
                                //我的团队130
                                tvGroupTagName.setText(data.getTeamleveltext());
                                tvMyGroupFirstCount.setText(Html.fromHtml(getResources().getString(R.string.group_num, data.getDirect_nums() + "")));
                                tvMyGroupCountAll.setText(Html.fromHtml(getResources().getString(R.string.group_num, data.getTeam_nums() + "")));
                                tvMyGroupSctAmount.setText(Html.fromHtml(getResources().getString(R.string.group_num_no, data.getTotal_profit())));

                                if (data.getTeam_level() == 0) {
                                    ivGroupTag.setImageResource(R.drawable.tag_group_normal);
                                } else if (data.getTeam_level() == 1) {
                                    ivGroupTag.setImageResource(R.drawable.tag_group_manager);
                                } else if (data.getTeam_level() == 2) {
                                    ivGroupTag.setImageResource(R.drawable.tag_group_chief);
                                } else if (data.getTeam_level() == 3) {
                                    ivGroupTag.setImageResource(R.drawable.tag_group_director);
                                }

                            } else {
                                //子团队
                                if (TextUtils.isEmpty(mCurrentGroupItemV0.getAgentname())) {
                                    tvUserId.setText("会员ID:" + mCurrentGroupItemV0.getAcqid());
                                } else {
                                    tvUserId.setText("会员ID:" + mCurrentGroupItemV0.getAcqid() + " (" + mCurrentGroupItemV0.getAgentname() + ")");
                                }
                                tvPhone.setText(Util.hintPhoneNumber(mCurrentGroupItemV0.getMobile()));

                                tvMemberMyGroupFirstCount.setText(data.getDirect_nums() + "人");
                                tvMemberMyGroupCountAll.setText(data.getTeam_nums() + "人");
                                tvMemberMyGroupSctAmount.setText(data.getTotal_profit());
                                if (data.getTeam_level() == 0) {
                                    imMyTag.setImageResource(R.drawable.icon_normal);
                                } else if (data.getTeam_level() == 1) {
                                    imMyTag.setImageResource(R.drawable.icon_manager);
                                } else if (data.getTeam_level() == 2) {
                                    imMyTag.setImageResource(R.drawable.icon_chief);
                                } else if (data.getTeam_level() == 3) {
                                    imMyTag.setImageResource(R.drawable.icon_director);
                                }
                                if (TextUtils.isEmpty(mCurrentGroupItemV0.getAvater())) {
                                    int resId = R.drawable.header_default_1;
                                    new ImageLoaderImpl().loadImageCircle(MyGroupActivity.this, resId).into(imMemberHeader);
                                } else {
                                    new ImageLoaderImpl().loadImageCircle(MyGroupActivity.this, mCurrentGroupItemV0.getAvater()).into(imMemberHeader);
                                }

                            }


                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MyGroupActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(MyGroupActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(MyGroupActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onLoadMoreRequested() {
        homeRecy.postDelayed(() -> {
            page++;
            getData(mAcqid, page);
        }, 1000);
    }
}
