package com.frico.easy_pay.ui.activity.home;

import android.os.Build;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.frico.easy_pay.R;
import com.frico.easy_pay.core.entity.TabEntity;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.adapter.MainFragmentPagerAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.widget.NoScrollViewPager;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAdvertisingActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tablelayout)
    CommonTabLayout tableLayout;
    @BindView(R.id.vp)
    NoScrollViewPager viewPager;
    @BindView(R.id.fab_btn)
    FloatingActionButton fabBtn;

    private AdFragment adFragment;
//    private OrderFragment orderFragment;
    private HomeFragment orderFragment;

    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] mTitles = {"交易订单", "我的广告"};
    private int[] mIconUnselectIds = {0, 0};
    private int[] mIconSelectIds = {0, 0};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected int setLayout() {
        return R.layout.activity_my_advertising;
    }

    @Override
    public void initTitle() {
        actionbar.setData("交易订单", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tableLayout.setTabData(mTabEntities);
    }

    @Override
    protected void initData() {
        adFragment = AdFragment.newInstance();
//        orderFragment = OrderFragment.newInstance();
        orderFragment = HomeFragment.newInstance();

        fragmentList.add(orderFragment);
        fragmentList.add(adFragment);

        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));

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

    /**
     * 跳转到发布广告页面
     */
    @OnClick(R.id.fab_btn)
    public void gotoAd() {
        launch(PublishAdActivity.class);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
