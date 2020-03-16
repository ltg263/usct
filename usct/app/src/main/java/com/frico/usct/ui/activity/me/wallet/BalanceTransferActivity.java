package com.frico.usct.ui.activity.me.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.Result;
import com.frico.usct.dialog.BaseIncomeSureDialog;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.dialog.TransferSureDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.fragment.NewHomeFragment;
import com.frico.usct.ui.activity.qrcode.NewCaptureActivity;
import com.frico.usct.utils.DecimalInputTextWatcher;
import com.frico.usct.utils.FileImageUtils;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.TranslucentActionBar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 余额转账
 */
public class BalanceTransferActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_TO_USER_ID = "toUserId";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_account_balance_can_used)
    TextView tvAccountBalanceCanUsed;
    @BindView(R.id.et_transfer_id)
    EditText etTransferId;
    @BindView(R.id.et_transfer_count)
    EditText etTransferCount;
    //    @BindView(R.id.et_transfer_pay_password)
//    EditText etTransferPayPassword;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.ll_to_user_id)
    LinearLayout llToUserId;
    @BindView(R.id.tv_transfer_tip)
    TextView tvTransferTip;
    @BindView(R.id.tv_text_tools)
    TextView tvTextTools;
    @BindView(R.id.iv_rwm)
    ImageView ivRwm;

    private TransferSureDialog mSureDialog;

    private String mToUserId;
    private String title;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceTransferActivity.class));
    }

    public static void start(Activity activity, String toUserId) {
        Intent intent = new Intent(activity, BalanceTransferActivity.class);
        intent.putExtra(KEY_TO_USER_ID, toUserId);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.act_transfer;
    }

    @Override
    public void initTitle() {
        mToUserId = getIntent().getStringExtra(KEY_TO_USER_ID);
        LogUtils.w("mToUserId:"+mToUserId);
        title = "转账";
        if (!TextUtils.isEmpty(mToUserId)) {
            LogUtils.w(mToUserId);
            if(!mToUserId.contains(NewHomeFragment.KEY_ACQID)
                && NewCaptureActivity.bitmap != null){
                if(mToUserId.contains("wxp://")){
                    //非本站渠道  暂只支持支付宝和微信收款码
//                    ivRwm.setVisibility(View.VISIBLE);
//                    ivRwm.setImageBitmap(NewCaptureActivity.bitmap);
                    tvTextTools.setText("正在向微信进行转账");
                }else if(mToUserId.contains("qr.alipay.com")){
                    tvTextTools.setText("正在向支付宝进行转账");
                }else{
                    simpleDialog();
                }
            }else{
                mToUserId = NewHomeFragment.getToUserIdFromUrl(mToUserId);
                tvTextTools.setText("正在向"+mToUserId+"进行转账");
            }
            title = "付款";
            etTransferId.setEnabled(false);
            etTransferId.setFocusable(false);
            etTransferId.setKeyListener(null);//重点
            etTransferId.setText(mToUserId);
            llToUserId.setVisibility(View.GONE);
            tvTransferTip.setText("付款数量");
        } else {
            llToUserId.setVisibility(View.VISIBLE);
            etTransferId.setEnabled(true);
            etTransferId.setFocusable(true);
        }
        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                NewCaptureActivity.bitmap = null;
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        //输入总长度15位，小数2位
        etTransferCount.addTextChangedListener(new DecimalInputTextWatcher(etTransferCount, 15, 2));

    }

    /**

     */

    private void simpleDialog() {
        SimpleDialog simpleDialog = new SimpleDialog(BalanceTransferActivity.this,
                "请扫微信或支付宝收款码", "确定", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {
                BalanceTransferActivity.this.finish();
            }

            @Override
            public void onPosBtnClick() {
                BalanceTransferActivity.this.finish();
            }
        });
        simpleDialog.setCanceledOnTouchOutside(false);
        simpleDialog.show();
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        if (SctApp.mUserInfoData != null) {
            tvAccountBalanceCanUsed.setText(SctApp.mUserInfoData.getAvailable_money() + " USCT");
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
        if (vId == R.id.tv_submit) {
            showPwDialog();
        }
    }


    private void showPwDialog() {

        if (TextUtils.isEmpty(etTransferId.getText())) {
            ToastUtil.showToast(this, "请输入转出账号");
            return;
        }
        if (TextUtils.isEmpty(etTransferCount.getText())) {
            ToastUtil.showToast(this, "请输入金额");
            return;
        }

        tvSubmit.setClickable(false);
        String idOrPhone = etTransferId.getText().toString().trim();
        String count = etTransferCount.getText().toString().trim();
//        String payPassword = etTransferPayPassword.getText().toString().trim();
        double countDouble = Double.parseDouble(count);
        if (countDouble <= 0) {
            ToastUtil.showToast(this, "转账金额必须要大于0");
            tvSubmit.setClickable(true);
            return;
        }
        if (idOrPhone.length() == 6 && TextUtils.equals(idOrPhone, SctApp.mUserInfoData.getAcqid())) {
            ToastUtil.showToast(this, "自己不能给自己转账");
            tvSubmit.setClickable(true);
            return;
        }
        if (TextUtils.isEmpty(SctApp.mUserInfoData.getMobile()) && idOrPhone.length() == 11 && TextUtils.equals(idOrPhone, SctApp.mUserInfoData.getMobile())) {
            ToastUtil.showToast(this, "自己不能给自己转账");
            tvSubmit.setClickable(true);
            return;
        }

        if (mSureDialog == null) {
            mSureDialog = new TransferSureDialog(this);
        }
        if (!mSureDialog.isShowing()) {
            mSureDialog.show();
        }
        mSureDialog.initViewData(count, idOrPhone,mToUserId);
        mSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {
                tvSubmit.setClickable(true);
            }

            @Override
            public void onSure(String pw) {
//                submit(idOrPhone, count, pw);
                if(mToUserId.contains("wxp://")){
                    LogUtils.w("微信");
                    uploadImg(pw,"微信");
                    return;
                }
                if(mToUserId.contains("qr.alipay.com")){
                    uploadImg(pw,"支付宝");
                    LogUtils.w("支付宝");
                    return;
                }
                submit(idOrPhone, count, pw);
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
    /**
     * 上传单张图片
     * @param pw
     */
    private void uploadImg(String pw,String paycompany) {
        show(BalanceTransferActivity.this, "提交中...");
        File file =  FileImageUtils.saveBitmapFile(NewCaptureActivity.bitmap);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dirtype", toRequestBody("5"));//头像：3，申诉 ：2 ，收款码：1
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
                            withdrawRwm(pw,paycompany,result.getData());

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 通过二维码进行转账
     * @param paycompany
     * @param paypassword
     */
    private void withdrawRwm(String paypassword, String paycompany,String paycode) {
        String amount = etTransferCount.getText().toString().trim();
        RetrofitUtil.getInstance().apiService()
                .transferBalance(1, amount, paypassword,paycode,paycompany)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }

                    @Override
                    public void onNext(Result result) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        if (result.getCode() == 1) {
                            //转账成功
                            ToastUtil.showToast(BalanceTransferActivity.this, "转账成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }
                });
    }

    private void submit(String idOrPhone, String count, String payPassword) {
        //提交
        withdraw(idOrPhone, count, payPassword);
    }


    /**
     * 转账
     */
    public void withdraw(String acqid, String amount, String paypassword) {
        showNoTips(this);
        if (isNumeric(acqid)){
            RetrofitUtil.getInstance().apiService()
                    .transferBalance(acqid, amount, paypassword,1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                        }

                        @Override
                        public void onNext(Result result) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                            if (result.getCode() == 1) {
                                //转账成功
                                ToastUtil.showToast(BalanceTransferActivity.this, "转账成功");
                                finish();
                            } else if (result.getCode() == 2) {
                                ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                                SctApp.getInstance().gotoLoginActivity();
                            } else {
                                ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                            ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                        }
                    });
        }else {
        RetrofitUtil.getInstance().apiService()
                .transferBalance(acqid, amount, paypassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }

                    @Override
                    public void onNext(Result result) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        if (result.getCode() == 1) {
                            //转账成功
                            ToastUtil.showToast(BalanceTransferActivity.this, "转账成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }
                });

        }
    }

    /**
     * 判断字符串是不是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
