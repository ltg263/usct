package com.frico.easy_pay.ui.activity.me.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.entity.UserCertificationVO;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.core.utils.Constants;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.LuBanUtils;
import com.frico.easy_pay.utils.MatisseUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UCropUtils;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class CertificationActivity extends BaseActivity implements ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_certification_name)
    EditText etCertificationName;
    @BindView(R.id.et_certification_num)
    EditText etCertificationNum;
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
    @BindView(R.id.ll_certification)
    LinearLayout llCertification;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.ll_no_pass)
    LinearLayout llNoPass;
    @BindView(R.id.tv_re_submit)
    TextView tvReSubmit;

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
        switch (SctApp.mUserInfoData.getIs_certification()) {
            //0-未认证 1-已认证 2-待审核 3-审核失败
            case 0:
                //doNothing
                break;
            case 1:

                RetrofitUtil.getInstance()
                        .apiService()
                        .getUserCertification()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Result<UserCertificationVO>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Result<UserCertificationVO> userCertificationVOResult) {
                                LogUtils.e(userCertificationVOResult.getData().toString());
                                if (userCertificationVOResult.getData() != null) {
                                    UserCertificationVO.UserCertificationBean data = userCertificationVOResult.getData().getUserCertification();
                                    etCertificationName.setText(data.getRealname());
                                    etCertificationNum.setText(data.getIdnum());
                                    etCertificationName.setFocusable(false);
                                    etCertificationNum.setFocusable(false);
                                    Glide.with(CertificationActivity.this).load(Constants.BASE_URL + data.getId_card_obverse()).error(R.drawable.id_card_front).into(ivCertificationFront);
                                    Glide.with(CertificationActivity.this).load(Constants.BASE_URL + data.getId_card_reverse()).error(R.drawable.id_card_reverse).into(ivCertificationReverse);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case 2:
                //审核中
                llCertification.setVisibility(View.GONE);
                llNoPass.setVisibility(View.VISIBLE);
                tvSubmit.setVisibility(View.GONE);
                iv.setImageResource(R.drawable.ic_agency_shz);
                text.setText(R.string.certification_shz);
                break;
            case 3:
                //未通过
                llCertification.setVisibility(View.GONE);
                tvSubmit.setVisibility(View.GONE);
                llNoPass.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_agency_no);
                tvReSubmit.setVisibility(View.VISIBLE);
                text.setText(R.string.certification_wtg);
                break;
        }


    }

    private void reSubmit() {
        llCertification.setVisibility(View.VISIBLE);
        tvSubmit.setVisibility(View.VISIBLE);
        llNoPass.setVisibility(View.GONE);
        tvReSubmit.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_certification_front, R.id.iv_certification_reverse, R.id.tv_submit,R.id.tv_re_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_certification_front:
                //正面 打开相机or相册
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(CertificationActivity.this, IMAGE_PICKER_FRONT, selected_num, true);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            case R.id.iv_certification_reverse:
                //反面 打开相机or相册
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(CertificationActivity.this, IMAGE_PICKER_REVERSE, selected_num, true);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            case R.id.tv_submit:
                submit();
                break;
            case R.id.tv_re_submit:
                reSubmit();
                break;
        }
    }



    static String card_obverse_side = "";
    static String card_reverse_side = "";
    private static int uploadIndex = 0;

    private void submit() {
        if (TextUtils.isEmpty(etCertificationName.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(etCertificationNum.getText().toString().trim())) {
            ToastUtil.showToast(this, "请输入身份证号码");
            return;
        }
        if (imgPath1 == null || "".equals(imgPath1)) {
            ToastUtil.showToast(this, "请上传身份证正面图");
            return;
        }
        if (imgPath2 == null || "".equals(imgPath2)) {
            ToastUtil.showToast(this, "请上传身份证反面图");
            return;
        }
        uploadIndex = 0;
        show(this, "上传中");
        File file = new File(imgPath1);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("type", toRequestBody("1"));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的name是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        RetrofitUtil.getInstance()
                .apiService()
                .uploadImg(body, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<JsonObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.e(d.toString());
                    }

                    @Override
                    public void onNext(Result<JsonObject> stringResult) {
                        LogUtils.e(stringResult.getData().toString());

                        JsonElement path = stringResult.getData().get("path");
                        card_obverse_side = path.getAsString();
                        LogUtils.e(card_obverse_side);
                        uploadIndex++;
                        // myHandler.sendEmptyMessage(1);
                        RxBus.get().post(BusAction.UPLOAD_IMG, "");

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("ERROR:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("complete");
                    }
                });

        File file2 = new File(imgPath2);
        Map<String, RequestBody> map2 = new HashMap<>();
        map2.put("type", toRequestBody("1"));
        // MultipartBody.Part  和后端约定好Key，这里的name是用image
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("image", file.getName(), requestFile2);
        RetrofitUtil.getInstance()
                .apiService()
                .uploadImg(body2, map2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<JsonObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.e(d.toString());
                    }

                    @Override
                    public void onNext(Result<JsonObject> stringResult) {
                        JsonElement path = stringResult.getData().get("path");
                        card_reverse_side = path.getAsString();
                        uploadIndex++;
                        RxBus.get().post(BusAction.UPLOAD_IMG, "");
                        //myHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("ERROR:" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("complete");
                    }
                });
    }


    @Subscribe(tags = {@Tag(BusAction.UPLOAD_IMG)})
    public void toCommit(String data) {
        if (uploadIndex == 2) {
            RetrofitUtil.getInstance().apiService()
                    .certification(etCertificationName.getText().toString(), etCertificationNum.getText().toString(), card_obverse_side, card_reverse_side)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Result<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            LogUtils.e(d.toString());
                            dismiss();
                        }

                        @Override
                        public void onNext(Result<String> stringResult) {

                            if (stringResult.getCode() == 1) {

                            } else {
                                ToastUtil.showToast(CertificationActivity.this, stringResult.getMsg());
                            }
                            dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("ERROR:" + e.toString());
                            dismiss();
                        }

                        @Override
                        public void onComplete() {
                            LogUtils.e("onComplete:");
                            dismiss();
                        }
                    });
        }
    }


    /**
     * 创建请求体
     *
     * @param value
     * @return
     */
    private RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
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
    private String imgPath1;
    private String imgPath2;

    /**
     * 压缩图片
     *
     * @param uri
     */
    private void compressImg(Uri uri, ImageView imageView, int type) {
        LuBanUtils.compressImg(CertificationActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {

                        mIsReSelected = true;
                        imgPath = file.getPath();
                        if (type == FRONT) {
                            imgPath1 = imgPath;
                        } else if (type == REVERSE) {
                            imgPath2 = imgPath;
                        }
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

                        UCropUtils.cropImg(CertificationActivity.this, uri, destinationUri, FRONT);

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
                        UCropUtils.cropImg(CertificationActivity.this, uri2, destinationUri2, REVERSE);
                        break;
                    case FRONT:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri, ivCertificationFront, FRONT));
                        break;
                    case REVERSE:
                        Uri croppedFileUri2 = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri2, ivCertificationReverse, REVERSE));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(CertificationActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    }
}
