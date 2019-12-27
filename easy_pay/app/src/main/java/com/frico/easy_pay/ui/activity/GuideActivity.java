package com.frico.easy_pay.ui.activity;

import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 新手引导页
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    @BindView(R.id.vp_guide)
    ViewPager mViewPager;
    @BindView(R.id.tv_go)
    TextView tvGo;

    private int[] mImageIds = new int[]{
            R.drawable.mb_guide_back4,
            R.drawable.mb_guide_back4,
            R.drawable.mb_guide_back4
    };

    private ArrayList<ImageView> mImageViews = new ArrayList<>();

    @Override
    protected int setLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void initTitle() {
    }

    @Override
    protected void initData() {
        //初始化三张图片的ImageView
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setImageResource(mImageIds[i]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViews.add(view);
        }
        mViewPager.setAdapter(new GuideAdapter(this));
        //监听ViewPager滑动事件,更新小红点位置
        mViewPager.addOnPageChangeListener(this);

        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在sp中记录访问过引导页的状态
                Prefer.getInstance().setNeedGuide(false);
                //跳到主页面
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;//引导页不允许任何按键操作
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LogUtils.e("-----onPageScrolled------");
//        if (position == mImageIds.length - 1) {
//            mViewPager.setOnTouchListener((view, motionEvent) -> {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_UP:
//                        //在sp中记录访问过引导页的状态
//                        Prefer.getInstance().setNeedGuide(false);
//                        //跳到主页面
//                        startActivity(new Intent(GuideActivity.this, LoginActivity.class));
//                        finish();
//                        break;
//                }
//                return true;
//            });
//        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 2) {
            tvGo.setVisibility(View.VISIBLE);
        } else {
            tvGo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 内部适配器
     */
    private static class GuideAdapter extends PagerAdapter {
        private GuideActivity activity;

        private GuideAdapter(GuideActivity activity) {
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return activity.mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = activity.mImageViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
