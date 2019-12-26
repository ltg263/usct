package com.frico.easy_pay.ui.activity.me.agency;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

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

public class MyAgencyActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tablelayout)
    CommonTabLayout tablelayout;
    @BindView(R.id.vp)
    NoScrollViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] mTitles = {"商户合伙人", "超级VIP"};
    private int[] mIconUnselectIds = {0, 0};
    private int[] mIconSelectIds = {0, 0};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    private HhrFragment adFragment;
    private HhrFragment orderFragment;
    @Override
    protected int setLayout() {
        return R.layout.activity_my_agency;
    }

    @Override
    public void initTitle() {
        actionbar.setData("代理申请", R.drawable.ic_left_back2x, null,
                0, null, new ActionBarClickListener() {
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

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tablelayout.setTabData(mTabEntities);
    }

    /**
     */
    @Override
    protected void initData() {
        adFragment = HhrFragment.newInstance();
        orderFragment = HhrFragment.newInstance();

        fragmentList.add(adFragment);
//        fragmentList.add(adFragment);
        fragmentList.add(orderFragment);

        viewPager.setScroll(true);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentList));
//        viewPager.setCurrentItem(1);
//        tablelayout.setCurrentTab(1);
        tablelayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
                if (position == 1) {

                } else {

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
                tablelayout.setCurrentTab(position);
                if (position == 1) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
