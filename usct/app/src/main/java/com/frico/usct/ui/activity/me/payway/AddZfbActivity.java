package com.frico.usct.ui.activity.me.payway;

import android.app.Activity;
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
import com.hwangjr.rxbus.RxBus;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.config.Constant;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.PayWayListVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.LuBanUtils;
import com.frico.usct.utils.MatisseUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.UCropUtils;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.utils.Util;
import com.frico.usct.widget.TranslucentActionBar;
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

public class AddZfbActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {
    private static String KEY_PAY_WAY_TYPE = "payWayType";
    private static String KEY_LIST_BEAN = "listBean";

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
//    @BindView(R.id.et_account)
//    EditText etAccount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.img_ewm)
    ImageView imgEwm;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_alipay_pid_et)
    EditText etAlipayPidEt;
    @BindView(R.id.ll_alipay_pid_lay)
    LinearLayout llAlipayPidLay;

    private static final int selected_num = 1;
    private static final int PERMISSION_CAMERA = 110;
    private static final int IMAGE_PICKER_EWM = 105;

    private int mPayWayType;
    private String imgPath;
    private String title;
    private PayWayListVO.ListBean listBean;

    private boolean mIsReSelected;//是否重新选择了图片
    private boolean mIsAlipay;
    private String accountHintStr = "";


    public static void start(Activity activity,PayWayListVO.ListBean listBean,String title,int payWayType){
        Intent intent = new Intent(activity,AddZfbActivity.class);
        intent.putExtra(Constant.TITLE,title);
        intent.putExtra(KEY_PAY_WAY_TYPE,payWayType);
        intent.putExtra(KEY_LIST_BEAN,listBean);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_add_zfb;
    }

    @Override
    public void initTitle() {
        listBean = (PayWayListVO.ListBean) getIntent().getSerializableExtra(KEY_LIST_BEAN);
        if(listBean != null){
            mPayWayType = listBean.getCodetype();
        }else{
            mPayWayType = getIntent().getIntExtra(KEY_PAY_WAY_TYPE,PayWayListActivity.PAY_WAY_WX);
        }
        title = getIntent().getStringExtra(Constant.TITLE);
        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        if(mPayWayType == PayWayListActivity.PAY_WAY_WX){
            //微信
            accountHintStr = "请输入微信账号";
        }else if(mPayWayType == PayWayListActivity.PAY_WAY_ZFB){
            //支付宝
            accountHintStr = "请输入支付宝账号";
        }else if(mPayWayType == PayWayListActivity.PAY_WAY_YSF){
            //云闪付
            accountHintStr = "请输入云闪付账号";
        }else if(mPayWayType == PayWayListActivity.PAY_WAY_ZSM){
            //赞赏码
            accountHintStr = "请输入微信账号";
        }
//        etAccount.setHint(accountHintStr);

        if(title.contains("支付宝")){
            mIsAlipay = true;
            llAlipayPidLay.setVisibility(View.VISIBLE);
        }else{
            mIsAlipay = false;
            llAlipayPidLay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        tvSubmit.setEnabled(true);
        if (listBean != null) {
            mIsReSelected = false;
//            etAccount.setText(listBean.getAccount());
            etName.setText(listBean.getAccountname());
            etNick.setText(listBean.getAlias());
            etAlipayPidEt.setText(listBean.getAlipayPid());

            imgPath = listBean.getImg();
            Glide.with(AddZfbActivity.this).asBitmap().load(imgPath).into(imgEwm);
            if(listBean.getVerifystatus() == 0){
                tvSubmit.setText("审核中不可变更");
                tvSubmit.setEnabled(false);
            }
        }

        imgEwm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
//                if (TextUtils.isEmpty(etAccount.getText().toString())) {
//                    ToastUtil.showToast(AddZfbActivity.this, accountHintStr);
//                    return;
//                }

                //暂时不强制
//                if(title.contains("支付宝")){
//                    if(TextUtils.isEmpty(etAlipayPidEt.getText().toString())){
//                        ToastUtil.showToast(AddZfbActivity.this, "请输入支付宝PID");
//                        return;
//                    }
//                }

                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastUtil.showToast(AddZfbActivity.this, "请输入姓名");
                    return;
                }

               /* if (TextUtils.isEmpty(etNick.getText().toString())) {
                    ToastUtil.showToast(AddZfbActivity.this, "请输入别名");
                    return;
                }*/

                if (imgPath == null) {
                    ToastUtil.showToast(AddZfbActivity.this, "请上传收款码");
                    return;
                }


                    show(AddZfbActivity.this, "上传中...");
                    String zfbPid = etAlipayPidEt.getText().toString();
                    zfbPid = Util.removeSpaceStr(zfbPid);//去掉空格
                    if (listBean == null || mIsReSelected) {
                        UiUtils.runOnUiThread(() -> {
                            uploadImg();
                        });
                    } else {
//                        changeCode(mPayWayType, imgPath, etAccount.getText().toString(), etName.getText().toString(), etNick.getText().toString(),zfbPid);
                        changeCode(mPayWayType, imgPath,  etName.getText().toString(), etNick.getText().toString(),zfbPid);
                    }

                break;
            case R.id.img_ewm:
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(AddZfbActivity.this, IMAGE_PICKER_EWM, selected_num);
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

                        UCropUtils.cropImg(AddZfbActivity.this, uri, destinationUri);
                        break;
                    case UCrop.REQUEST_CROP:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(AddZfbActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
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
        LuBanUtils.compressImg(AddZfbActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        mIsReSelected = true;
                        imgPath = file.getPath();
                        LogUtils.e("压缩后的图片路径---" + imgPath);
                        setImg(imgEwm);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddZfbActivity.this, "图片压缩失败, 请稍后重试", Toast.LENGTH_SHORT).show();
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
        File file = new File(imgPath);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dirtype", toRequestBody("1"));
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
                            if (listBean == null) {
                                commitAddPayWay(result.getData());
//                                addPayWay(title.contains("微信") ? 3 : 2, result.getData(), etAccount.getText().toString(), etName.getText().toString(), etNick.getText().toString(),zfbPid);
                            } else {
                                commitChangeCode(result.getData());
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AddZfbActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(AddZfbActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AddZfbActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    private void commitChangeCode(String codeUrl){
//        String etAccountStr = etAccount.getText().toString().trim();
        String etNameStr = etName.getText().toString().trim();
        String etNickStr = etNick.getText().toString().trim();
        String zfbPidStr = etAlipayPidEt.getText().toString().trim();
        zfbPidStr = Util.removeSpaceStr(zfbPidStr);
//        changeCode(title.contains("微信") ? 3 : 2, codeUrl,etAccountStr, etNameStr, etNickStr,zfbPidStr);
        changeCode(title.contains("微信") ? 3 : 2, codeUrl, etNameStr, etNickStr,zfbPidStr);

    }

    /**
     * 编辑收款码
     *
     * @param payType
     * @param imgUrl
     * @param accountName
     * @param alies
     */
    private void changeCode(int payType, String imgUrl, String accountName, String alies,String alipay_pid) {
        RetrofitUtil.getInstance().apiService()
                .paycodeupdate(listBean.getId(), payType, imgUrl, accountName, null, alies,alipay_pid)
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
                            ToastUtil.showToast(AddZfbActivity.this, "编辑成功");
                            RxBus.get().post(BusAction.PAY_WAY_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AddZfbActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AddZfbActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AddZfbActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void commitAddPayWay(String codeUrl){
//        String etAccountStr = etAccount.getText().toString().trim();
        String etNameStr = etName.getText().toString().trim();
        String etNickStr = etNick.getText().toString().trim();
        String zfbPidStr = etAlipayPidEt.getText().toString().trim();
        zfbPidStr = Util.removeSpaceStr(zfbPidStr);
//        addPayWay(mPayWayType, codeUrl, etAccountStr, etNameStr, etNickStr,zfbPidStr);
        addPayWay(mPayWayType, codeUrl, etNameStr, etNickStr,zfbPidStr);
    }

    /**
     * 添加收款方式
     */
    private void addPayWay(int payType, String imgUrl,  String accountName, String alies,String alipay_pid) {
        RetrofitUtil.getInstance().apiService()
                .paycodeadd(payType, imgUrl, accountName, null, alies,alipay_pid)
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
                            //发送刷新收款方式列表
                            ToastUtil.showToast(AddZfbActivity.this, "添加成功");
                            RxBus.get().post(BusAction.PAY_WAY_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AddZfbActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AddZfbActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AddZfbActivity.this, e.getMessage());
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
