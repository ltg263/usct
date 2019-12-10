package com.frico.easy_pay.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.HomeTopNotifyV0;
import com.frico.easy_pay.core.entity.QrcodeVo;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShowMyQrcodeActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.iv_my_qrcode)
    ImageView ivMyQrcode;

    public static void start(Activity activity){
        activity.startActivity(new Intent(activity,ShowMyQrcodeActivity.class));
    }

    @Override
    protected int setLayout() {
        return R.layout.act_show_qrcode;
    }

    @Override
    public void initTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        actionbar.setBackgroundColor(getResources().getColor(R.color.color_fde5b3));
        actionbar.setData("二维码收款", R.drawable.ic_left_back2x, null, 0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    @Override
    protected void initData() {
        getQrcode();
    }

    /**
     * 获取首页通知
     *
     */
    private void getQrcode() {
        RetrofitUtil.getInstance().apiService()
                .getReceiveQrCode()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<QrcodeVo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<QrcodeVo> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            //获取通知
//                            ToastUtil.showToast(activity, result.getData().getNotify());
                            new ImageLoaderImpl().loadImage(ShowMyQrcodeActivity.this, result.getData().getReceiveqrcode()).into(ivMyQrcode);

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ShowMyQrcodeActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ShowMyQrcodeActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ShowMyQrcodeActivity.this, e.getMessage());
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
