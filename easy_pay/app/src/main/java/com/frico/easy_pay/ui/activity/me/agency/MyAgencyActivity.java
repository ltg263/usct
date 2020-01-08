package com.frico.easy_pay.ui.activity.me.agency;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

    public static String KEY_TYPE = "key_type";
    private int type;//1-代理 2-会员

    public static void start(Activity activity, int viewType){
        Intent intent = new Intent(activity, MyAgencyActivity.class);
        intent.putExtra(KEY_TYPE,viewType);
        activity.startActivity(intent);
    }

    private HhrFragment adFragment;
    private HhrFragment orderFragment;
    @Override
    protected int setLayout() {
        return R.layout.activity_my_agency;
    }

    public int getType() {
        return type;
    }

    @Override
    public void initTitle() {
        type = getIntent().getIntExtra(KEY_TYPE,0);
        String str = "超级VIP";
        if(type == 1){
            str = "商户合伙人";
        }
        actionbar.setData(str, R.drawable.ic_left_back2x, null,
                0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionbar.setStatusBarHeight(getStatusBarHeight());

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
//        fragmentList.add(orderFragment);

        viewPager.setScroll(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentList));

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
