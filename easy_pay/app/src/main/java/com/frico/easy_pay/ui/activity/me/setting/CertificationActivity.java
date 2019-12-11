package com.frico.easy_pay.ui.activity.me.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.payway.AddZfbActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.LuBanUtils;
import com.frico.easy_pay.utils.MatisseUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UCropUtils;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class CertificationActivity extends BaseActivity implements ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_certification_name)
    EditText etCertificationName;
    @BindView(R.id.et_certification_num)
    TextView etCertificationNum;
    @BindView(R.id.iv_certification_front)
    ImageView ivCertificationFront;
    @BindView(R.id.iv_certification_reverse)
    ImageView ivCertificationReverse;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private static final int selected_num = 1;
    private static final int PERMISSION_CAMERA = 110;
    private static final int IMAGE_PICKER_REVERSE = 105;
    private static final int IMAGE_PICKER_FRONT = 107;
    private static final int FRONT = 10;
    private static final int REVERSE = 20;

    @Override
    protected int setLayout() {
        return R.layout.activity_certification;
    }

    @Override
    public void initTitle() {
        actionbar.setData("实名认证", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_certification_front, R.id.iv_certification_reverse, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_certification_front:
                //正面 打开相机or相册
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(CertificationActivity.this, IMAGE_PICKER_FRONT, selected_num,true);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            case R.id.iv_certification_reverse:
                //反面 打开相机or相册
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(CertificationActivity.this, IMAGE_PICKER_REVERSE, selected_num,true);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (TextUtils.isEmpty(etCertificationName.getText().toString().trim())){
            ToastUtil.showToast(this,"请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(etCertificationNum.getText().toString().trim())){
            ToastUtil.showToast(this,"请输入身份证号码");
            return;
        }

    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
    private boolean mIsReSelected;//是否重新选择了图片
    private String imgPath;
    /**
     * 压缩图片
     *
     * @param uri
     */
    private void compressImg(Uri uri,ImageView imageView) {
        LuBanUtils.compressImg(CertificationActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        mIsReSelected = true;
                        imgPath = file.getPath();
                        LogUtils.e("压缩后的图片路径---" + imgPath);
                        setImg(imageView);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CertificationActivity.this, "图片压缩失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setImg(ImageView imgView) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String drawableUrl = ImageDownloader.Scheme.FILE.wrap(imgPath);
                    ImageLoader.getInstance().displayImage(drawableUrl, imgView);
                } catch (Exception mE) {
                    return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case IMAGE_PICKER_FRONT:
                        Uri uri = Matisse.obtainResult(data).get(0);
                        Uri destinationUri = null;
                        try {
                            destinationUri = Uri.fromFile(File.createTempFile("crop", ".jpg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        UCropUtils.cropImg(CertificationActivity.this, uri, destinationUri,FRONT);

                        break;
                    case IMAGE_PICKER_REVERSE:
                        Uri uri2 = Matisse.obtainResult(data).get(0);
                        Uri destinationUri2 = null;
                        try {
                            destinationUri2 = Uri.fromFile(File.createTempFile("crop", ".jpg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        LogUtils.e(destinationUri2.toString());
                        UCropUtils.cropImg(CertificationActivity.this, uri2, destinationUri2,REVERSE);
                        break;
                    case FRONT:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri,ivCertificationFront));
                        break;
                    case REVERSE:
                        Uri croppedFileUri2 = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri2,ivCertificationReverse));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(CertificationActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    }
}
