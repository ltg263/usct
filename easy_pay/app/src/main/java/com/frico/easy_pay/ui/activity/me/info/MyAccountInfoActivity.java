package com.frico.easy_pay.ui.activity.me.info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.LuBanUtils;
import com.frico.easy_pay.utils.MatisseUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UCropUtils;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class MyAccountInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_MY_ACCOUNT_INFO = "accountInfo";
    private static final int PERMISSION_CAMERA = 110;
    private static final int IMAGE_PICKER_EWM = 105;
    private static final int selected_num = 1;//只能选择一张图片
    private static final int REQUEST_CODE = 301;//更改昵称的requestcode

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.ll_acount_header_lay)
    LinearLayout llAcountHeaderLay;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.ll_acount_nickname_lay)
    LinearLayout llAcountNicknameLay;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.ll_acount_phone_lay)
    LinearLayout llAcountPhoneLay;

    private MbpUserVO mUserInfo;
    private String imgPath;

    public static void start(Activity activity, MbpUserVO userInfo) {
        Intent intent = new Intent(activity, MyAccountInfoActivity.class);
        intent.putExtra(KEY_MY_ACCOUNT_INFO, userInfo);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_account_info;
    }

    @Override
    public void initTitle() {
        actionbar.setTitle("个人信息");
        actionbar.setData("个人信息", R.drawable.ic_left_back2x, null, 0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        initView();
    }

    private void initView() {
        llAcountHeaderLay.setOnClickListener(this);
        llAcountNicknameLay.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        mUserInfo = (MbpUserVO) getIntent().getSerializableExtra(KEY_MY_ACCOUNT_INFO);
        if(mUserInfo == null){
            getData();
        }else {
            setUserInfo(mUserInfo);
        }
    }

    private void setUserInfo(MbpUserVO userInfo) {
        if (userInfo != null) {
            tvNickName.setText(userInfo.getUsername());
            tvPhoneNumber.setText(userInfo.getMobile());
            if (TextUtils.isEmpty(userInfo.getmHeaderImgUrl())) {
                new ImageLoaderImpl().loadImageCircle(this, R.drawable.header_default).into(ivHeader);
            }else{
                new ImageLoaderImpl().loadImageCircle(this, userInfo.getmHeaderImgUrl()).into(ivHeader);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.ll_acount_header_lay) {
            //更改图片
            gotoSelectPhoto();
        } else if (vId == R.id.ll_acount_nickname_lay) {
            // 去更改昵称
            AccountInfoEditNickNameActivity.start(this,mUserInfo.getUsername(),REQUEST_CODE);
        }
    }

    private void gotoSelectPhoto() {
        if (EasyPermissions.hasPermissions(this, params)) {
            //具备权限 直接进行操作
            MatisseUtils.selectImg(MyAccountInfoActivity.this, IMAGE_PICKER_EWM, selected_num);
        } else {
            //权限拒绝 申请权限
            EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case IMAGE_PICKER_EWM:
                        Uri uri = Matisse.obtainResult(data).get(0);

                        Uri destinationUri = null;
                        try {
                            destinationUri = Uri.fromFile(File.createTempFile("crop", ".jpg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        UCropUtils.cropImg(MyAccountInfoActivity.this, uri, destinationUri);
                        break;
                    case UCrop.REQUEST_CROP:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(MyAccountInfoActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_CODE:
                        String newNameName = data.getStringExtra(AccountInfoEditNickNameActivity.KEY_NICK);
                        updateUserName(newNameName);
                        break;
                }
            }
        }

    }

    /**
     * 压缩图片
     *
     * @param uri
     */
    private void compressImg(Uri uri) {
        LuBanUtils.compressImg(MyAccountInfoActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        imgPath = file.getPath();
                        LogUtils.e("压缩后的图片路径---" + imgPath);
                        setImg(ivHeader);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyAccountInfoActivity.this, "图片压缩失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * 显示选择的图片
     *
     * @param imgView
     */
    private void setImg(ImageView imgView) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String drawableUrl = ImageDownloader.Scheme.FILE.wrap(imgPath);
//                    ImageLoader.getInstance().displayImage(drawableUrl, imgView);
                    new ImageLoaderImpl().loadImageCircle(MyAccountInfoActivity.this, drawableUrl).into(imgView);
                    //开始上传
                    uploadImg();
                } catch (Exception mE) {
                    return;
                }
            }
        });
    }


    /**
     * 上传单张图片
     */
    private void uploadImg() {
        show(MyAccountInfoActivity.this, "提交中...");
        File file = new File(imgPath);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dirtype", toRequestBody("3"));//头像：3，申诉 ：2 ，收款码：1
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的name是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitUtil.getInstance().apiService()
                .uploadimg(body, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Result<String> result) {
                        if (result.getCode() == 1) {
                            // 图片 上传成功 提交接口信息到接口
                            uploadHeaderUrl(result.getData());

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(MyAccountInfoActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(MyAccountInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
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

    public void uploadHeaderUrl(String headerImgUrl) {
        RetrofitUtil.getInstance().apiService()
                .uploadUserHeaderUrl(headerImgUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "更新成功");
                            //重新拉去一下个人信息
                            getData();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(MyAccountInfoActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(MyAccountInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 更改昵称
     * @param userName
     */
    public void updateUserName(String userName) {
        show(MyAccountInfoActivity.this, "提交中...");
        RetrofitUtil.getInstance().apiService()
                .uploadUserNickName(userName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "更新成功");
                            //重新拉去一下个人信息
                            getData();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(MyAccountInfoActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(MyAccountInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }


    /**
     * 获取个人信息
     */
    public void getData() {
        RetrofitUtil.getInstance().apiService()
                .getusernfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbpUserVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbpUserVO> result) {
                        if (result.getCode() == 1) {
                            MbpUserVO data = result.getData();
                            mUserInfo = data;
                            SctApp.mUserInfoData = result.getData();
                            setUserInfo(data);
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(MyAccountInfoActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(MyAccountInfoActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(MyAccountInfoActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
