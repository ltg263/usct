package com.frico.easy_pay.ui.activity.home;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.utils.ToastUtil;
import com.hwangjr.rxbus.RxBus;
import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.TabEntity;
import com.frico.easy_pay.ui.activity.adapter.MainFragmentPagerAdapter;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.widget.NoScrollViewPager;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 交易 tab页面
 */
public class MyAdvertisingFragment extends BaseFragment {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tablelayout)
    CommonTabLayout tableLayout;
    @BindView(R.id.vp)
    NoScrollViewPager viewPager;
    @BindView(R.id.fab_btn)
    FloatingActionButton fabBtn;
    Unbinder unbinder;
    private View view;

    private AdFragment adFragment;
//    private OrderFragment orderFragment;
    private HomeFragment orderFragment;

    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] mTitles = {"买入", "卖出"};
    private int[] mIconUnselectIds = {0, 0};
    private int[] mIconSelectIds = {0, 0};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    public static MyAdvertisingFragment newInstance(){
         MyAdvertisingFragment traderFragment = new MyAdvertisingFragment();
         return traderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_advertising, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar();
        initData();
    }

    private void initToolBar(){
        actionbar.setTitle("交易大厅");
        actionbar.setData("交易大厅", 0, null, 0, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tableLayout.setTabData(mTabEntities);
    }


    private void initData() {
        adFragment = AdFragment.newInstance();
//        orderFragment = OrderFragment.newInstance();
        orderFragment = HomeFragment.newInstance();

        fragmentList.add(orderFragment);
        fragmentList.add(adFragment);

        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getFragmentManager(), fragmentList));

        tableLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
                if (position == 1) {
                    fabBtn.show();
                } else {
                    fabBtn.hide();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tableLayout.setCurrentTab(position);
                if (position == 1) {
                    fabBtn.show();
                } else {
                    fabBtn.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //交易 买入页面
    public void showBuyFragment(){
        viewPager.setCurrentItem(0);
        fabBtn.show();
    }

    /**
     * 跳转到发布广告页面
     */
    @OnClick(R.id.fab_btn)
    public void gotoAd() {
        if (SctApp.mUserInfoData.getIsVip().equals("0")){
            ToastUtil.showToast(getActivity(),"非vip不能发布");
        }else {
            launch(PublishAdActivity.class);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
