package com.frico.easy_pay.ui.activity.income;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.DealOrderItemVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
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

public class AppealActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.et_season)
    EditText etSeason;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.img_ewm)
    ImageView imgEwm;

    private static final int selected_num = 1;
    private static final int PERMISSION_CAMERA = 110;
    private static final int IMAGE_PICKER_EWM = 105;
    @BindView(R.id.upload_img_lay)
    LinearLayout uploadImgLay;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_appeal_text_count)
    TextView tvAppealTextCount;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;

    private String imgPath;
    private String orderno;
    public static String KEY_BOTTON_TYPE = "buttontype";

    private String buttontype; // if (item.getButtontype().equals("2")) {//卖出
    private boolean isNeedImg;


    public static void start(Activity activity, DealOrderItemVO dealOrderItemVO) {
        Intent intent = new Intent(activity, AppealActivity.class);
        intent.putExtra("appeal", dealOrderItemVO.getOrderno());
        intent.putExtra(AppealActivity.KEY_BOTTON_TYPE, dealOrderItemVO.getButtontype());
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_appeal;
    }

    @Override
    public void initTitle() {
        orderno = getIntent().getStringExtra("appeal");
        buttontype = getIntent().getIntExtra(KEY_BOTTON_TYPE,0) + "";
        isNeedImg = !TextUtils.equals(buttontype, "2");//只有 卖出不需要图片上传

        actionbar.setData("申诉", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        etSeason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String countStr = etSeason.getText().toString().trim();
                int count = TextUtils.isEmpty(countStr) ? 0: countStr.length();
                tvAppealTextCount.setText("注:申诉理由最多150个字！已输入"+ count +"字");
            }
        });
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        imgEwm.setOnClickListener(this);
        tvOrderNo.setText(orderno);
        if (buttontype.equals("2")) {
            //卖出 不需要上传图片
            uploadImgLay.setVisibility(View.INVISIBLE);
        } else {
            uploadImgLay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etSeason.getText().toString())) {
                    ToastUtil.showToast(AppealActivity.this, "请输入申诉理由");
                    return;
                }

                if (isNeedImg && imgPath == null) {
                    ToastUtil.showToast(AppealActivity.this, "请上传申诉图片");
                    return;
                }
                if (isNeedImg) {
                    UiUtils.runOnUiThread(() -> {
                        uploadImg();
                    });
                } else {
                    //不需要传图的，直接提交
                    submitAppeal(orderno, etSeason.getText().toString(), null);
                }
                break;
            case R.id.img_ewm:
                if (EasyPermissions.hasPermissions(this, params)) {
                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(AppealActivity.this, IMAGE_PICKER_EWM, selected_num);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            default:
                break;
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

                        UCropUtils.cropImg(AppealActivity.this, uri, destinationUri);
                        break;
                    case UCrop.REQUEST_CROP:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(AppealActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
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
        LuBanUtils.compressImg(AppealActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        imgPath = file.getPath();
                        LogUtils.e("压缩后的图片路径---" + imgPath);
                        setImg(imgEwm);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AppealActivity.this, "图片压缩失败, 请稍后重试", Toast.LENGTH_SHORT).show();
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
                    ImageLoader.getInstance().displayImage(drawableUrl, imgView);
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
        show(AppealActivity.this, "提交中...");
        File file = new File(imgPath);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dirtype", toRequestBody("2"));
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
                            //2支付宝  3微信
                            submitAppeal(orderno, etSeason.getText().toString(), result.getData());
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AppealActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(AppealActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AppealActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 提交申诉
     */
    private void submitAppeal(String orderNo, String reason, String imgUrl) {
        RetrofitUtil.getInstance().apiService()
                .appeal(orderNo, reason, imgUrl)
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
                            ToastUtil.showToast(AppealActivity.this, "提交成功");
                            //发送刷新收款方式列表
                            RxBus.get().post(BusAction.ORDER_LIST, "");
                            RxBus.get().post(BusAction.DEAL_ORDER_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AppealActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AppealActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AppealActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
