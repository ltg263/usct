package com.frico.easy_pay.ui.activity.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.QrcodeVo;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

public class ShowQrCodeActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_qr_code_download)
    TextView tvQrCodeDownload;

    private  String imgUrl;


    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ShowQrCodeActivity.class));
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_qr_code_new;
    }

    @Override
    public void initTitle() {
        actionBar.setData("二维码收款", R.drawable.ic_left_back2x, null, 0, null, this);
        //actionbar.setNeedTranslucent(true,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionBar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
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
                            imgUrl =result.getData().getReceiveqrcode();
                           // new ImageLoaderImpl().loadImage(ShowQrCodeActivity.this,imgUrl ).into(ivQrCode);
                            Glide.with(ShowQrCodeActivity.this)
                                    .load(imgUrl)
                                    .into(ivQrCode);

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ShowQrCodeActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ShowQrCodeActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ShowQrCodeActivity.this, e.getMessage());
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

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @OnClick(R.id.tv_qr_code_download)
    public void onViewClicked() {
        //TODO download img
        //show(ShowQrCodeActivity.this,"下载中");
        OkHttpUtils//
                .get()//
                .url(imgUrl)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),getSaveFileName(imgUrl) )//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                      //  dismiss();
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri uri = Uri.fromFile(response);
                        intent.setData(uri);
                        ShowQrCodeActivity.this.sendBroadcast(intent);
                        ToastUtil.showToast(ShowQrCodeActivity.this,"下载成功");

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                    }
                });
    }
    /**
     * 文件保存名称
     *
     * @param downloadUrl
     * @return
     */
    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "qr_code.jpg";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }
}
