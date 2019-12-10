package com.frico.easy_pay.ui.activity.me.setting;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbShareVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.impl.FileDownLaodListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.CopyUtils;
import com.frico.easy_pay.utils.FileSaveUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class ShareActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_share_copy)
    TextView tvShareCopy;
    @BindView(R.id.tv_share_link)
    TextView tvShareLink;
    @BindView(R.id.tv_share_ewm)
    ImageView tvShareEwm;

    private String shareUrl;
    private String shareImgUrl;

    @Override
    protected int setLayout() {
        return R.layout.activity_share;
    }

    @Override
    public void initTitle() {
        actionbar.setData("我的分享", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        getShareInfo();

        tvShareCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareUrl != null)
                    CopyUtils.copyUrl(ShareActivity.this, "复制链接", shareUrl);
            }
        });

        tvShareEwm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (shareImgUrl != null)
                    saveImg();
                return true;
            }
        });
    }

    /**
     * 获取分享的内容
     */
    private void getShareInfo() {
        show(ShareActivity.this, "加载中...");
        RetrofitUtil.getInstance().apiService()
                .share()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbShareVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbShareVO> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            MbShareVO data = result.getData();
                            if (data != null) {
                                shareUrl = data.getUrl();
                                shareImgUrl = data.getShareimg();
                                tvShareLink.setText("链接: " + data.getUrl());
                                Glide.with(ShareActivity.this).asBitmap().load(data.getShareimg()).into(tvShareEwm);
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ShareActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        }else {
                            ToastUtil.showToast(ShareActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ShareActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    private static final int PERMISSION_SD = 110;

    /**
     * 点击保存单张图片
     */
    private void saveImg() {
        if (EasyPermissions.hasPermissions(ShareActivity.this, params)) {
            //具备权限 直接进行操作
            downLoadRourse(shareImgUrl);
        } else {
            //权限拒绝 申请权限
            EasyPermissions.requestPermissions(ShareActivity.this, "为了您更好使用该应用，请允许获取以下权限", PERMISSION_SD, params);
        }
    }

    /**
     * 保存图片
     */
    private void downLoadRourse(String imgUrl) {
        FileSaveUtils.startDownSourceImg(ShareActivity.this, 1);
        FileSaveUtils.saveSingleImageToPhotos(ShareActivity.this, imgUrl, new FileDownLaodListener() {
            @Override
            public void onSuccess() {
                ToastUtil.showToast(ShareActivity.this, "二维码已保存,打开相册查看");
            }

            @Override
            public void onFail() {
                ToastUtil.showToast(ShareActivity.this, "保存失败,请稍后重试");
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
