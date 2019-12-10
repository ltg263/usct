package com.frico.easy_pay.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.frico.easy_pay.R;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.impl.FileDownLaodListener;
import com.frico.easy_pay.ui.activity.adapter.ImagePreviewAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.FileSaveUtils;
import com.frico.easy_pay.widget.nineimg.CustomViewPager;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 图片预览 Activity
 */
public class ImagePreviewActivity extends BaseActivity {

    private int itemPosition;
    private List<String> imageList;
    private CustomViewPager viewPager;
    private ImageView imgSave;
    private LinearLayout main_linear;
    private int mStartPosition;
    private int mCurrentPosition;
    private ImagePreviewAdapter adapter;

    private static final int SAVE_SUCCESS = 0;//保存图片成功
    private static final int SAVE_FAILURE = 1;//保存图片失败
    private static final int PERMISSION_SD = 110;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_SUCCESS:
                    showCustomToast(ImagePreviewActivity.this, "已保存到相册");
                    break;
                case SAVE_FAILURE:
                    showCustomToast(ImagePreviewActivity.this, "保存失败,请稍后再试");
                    break;
            }
        }
    };

    @Override
    protected int setLayout() {
        return R.layout.activity_nineimage_preview;
    }

    @Override
    public void initTitle() {

    }

    @Override
    protected void initData() {
        initView();
        getIntentData();
        setListener();
    }

    private void initView() {
        viewPager = findViewById(R.id.imageBrowseViewPager);
        main_linear = findViewById(R.id.main_linear);
        imgSave = findViewById(R.id.img_save);
    }

    /**
     * 初始化数据
     */
    private void getIntentData() {
        if (getIntent() != null) {
            mStartPosition = getIntent().getIntExtra(Constant.START_IAMGE_POSITION, 0);
            mCurrentPosition = mStartPosition;
            itemPosition = getIntent().getIntExtra(Constant.START_ITEM_POSITION, 0);
            imageList = getIntent().getStringArrayListExtra("imageList");
        }

        renderView();
        getData();
    }

    private void renderView() {
        if (imageList == null) return;
        if (imageList.size() == 1) {
            main_linear.setVisibility(View.GONE);
        } else {
            main_linear.setVisibility(View.VISIBLE);
        }
        adapter = new ImagePreviewAdapter(this, imageList, itemPosition);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrentPosition);
    }

    /**
     * 获取数据
     */
    private void getData() {
        View view;
        for (String pic : imageList) {

            //创建底部指示器(小圆点)
            view = new View(ImagePreviewActivity.this);
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            //设置间隔
            if (!pic.equals(imageList.get(0))) {
                layoutParams.leftMargin = 20;
            }
            //添加到LinearLayout
            main_linear.addView(view, layoutParams);
        }
    }

    /**
     * 设置指示器
     *
     * @param position
     */
    private void hideAllIndicator(int position) {
        for (int i = 0; i < imageList.size(); i++) {
            if (i != position) {
                main_linear.getChildAt(i).setEnabled(false);
            }
        }
    }

    /**
     * 监听事件
     */
    private void setListener() {
        main_linear.getChildAt(mCurrentPosition).setEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideAllIndicator(position);
                main_linear.getChildAt(position).setEnabled(true);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setPageTransformer(false, (page, position) -> {
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            page.setScaleX(normalizedposition / 2 + 0.5f);
            page.setScaleY(normalizedposition / 2 + 0.5f);
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImg();
            }
        });
    }

    /**
     * 点击保存单张图片
     */
    private void saveImg() {
        if (mCurrentPosition < imageList.size()) {
            if (EasyPermissions.hasPermissions(ImagePreviewActivity.this, params)) {
                //具备权限 直接进行操作
                downLoadRourse(imageList.get(mCurrentPosition));
            } else {
                //权限拒绝 申请权限
                EasyPermissions.requestPermissions(ImagePreviewActivity.this, "为了您更好使用该应用，请允许获取以下权限", PERMISSION_SD, params);
            }
        }
    }

    /**
     * 保存图片
     */
    private void downLoadRourse(String imgUrl) {
        FileSaveUtils.startDownSourceImg(ImagePreviewActivity.this, 1);
        FileSaveUtils.saveSingleImageToPhotos(ImagePreviewActivity.this, imgUrl, new FileDownLaodListener() {
            @Override
            public void onSuccess() {
                mHandler.obtainMessage(SAVE_SUCCESS).sendToTarget();
            }

            @Override
            public void onFail() {
                mHandler.obtainMessage(SAVE_FAILURE).sendToTarget();
            }
        });
    }


    /**
     * 结束转场动画
     */
    @Override
    public void finishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(Constant.START_IAMGE_POSITION, mStartPosition);
        data.putExtra(Constant.CURRENT_IAMGE_POSITION, mCurrentPosition);
        data.putExtra(Constant.CURRENT_ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }
}
