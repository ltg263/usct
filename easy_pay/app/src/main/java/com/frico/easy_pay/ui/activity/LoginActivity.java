package com.frico.easy_pay.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.entity.UpdateVO;
import com.frico.easy_pay.core.netty.NettyConnector;
import com.frico.easy_pay.core.netty.NettyHandler;
import com.frico.easy_pay.core.utils.ConfigUtil;
import com.frico.easy_pay.core.utils.Constants;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.impl.CountDownTimerFinishedListener;
import com.frico.easy_pay.service.NotificationUtils;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.setting.ForgetLoginPwdActivity;
import com.frico.easy_pay.ui.activity.update.AutoUpgradeClient;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.ClearWriteEditText;
import com.frico.easy_pay.widget.MyCountDownTimer;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.user_et_phone)
    ClearWriteEditText userEtPhone;
    @BindView(R.id.user_et_pwd)
    ClearWriteEditText userEtPwd;
    @BindView(R.id.user_login)
    TextView userLogin;
    @BindView(R.id.user_tv_regist)
    TextView userTvRegist;
    @BindView(R.id.user_tv_forget)
    TextView userTvForget;
    @BindView(R.id.top_intro_login_normal_tv)
    TextView topIntroLoginNormalTv;
    @BindView(R.id.top_intro_login_normal_b_line_v)
    View topIntroLoginNormalBLineV;
    @BindView(R.id.top_intro_login_normal_lay)
    LinearLayout topIntroLoginNormalLay;
    @BindView(R.id.top_intro_login_code_tv)
    TextView topIntroLoginCodeTv;
    @BindView(R.id.top_intro_login_code_b_line_v)
    View topIntroLoginCodeBLineV;
    @BindView(R.id.top_intro_login_code_lay)
    LinearLayout topIntroLoginCodeLay;
    @BindView(R.id.login_input_normal_lay)
    LinearLayout loginInputNormalLay;
    @BindView(R.id.code_user_et_phone_cwe)
    ClearWriteEditText codeUserEtPhoneCwe;
    @BindView(R.id.et_msg_code)
    ClearWriteEditText etMsgCode;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.login_input_code_lay)
    LinearLayout loginInputCodeLay;


    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }


    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initTitle() {
        actionbar.setData("登录", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        checkUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //进入了登录页面了，就清空登录数据
        Prefer.getInstance().clearData();
        getNoticeEnable();
    }

    @Override
    protected void initData() {
        userLogin.setOnClickListener(this);
        userTvRegist.setOnClickListener(this);
        userTvForget.setOnClickListener(this);
        userLogin.setClickable(false);
        topIntroLoginNormalLay.setOnClickListener(this);
        topIntroLoginCodeLay.setOnClickListener(this);
        getCode.setOnClickListener(this);

        if (!TextUtils.isEmpty(Prefer.getInstance().getMobile())) {  //赋值，设置光标位置
            userEtPhone.setText(Prefer.getInstance().getMobile());
            userEtPhone.setSelection(Prefer.getInstance().getMobile().length());
            userEtPhone.setClearIconVisible(false);

            codeUserEtPhoneCwe.setText(Prefer.getInstance().getMobile());
            codeUserEtPhoneCwe.setSelection(Prefer.getInstance().getMobile().length());
            codeUserEtPhoneCwe.setClearIconVisible(false);
        }

        userEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    userLogin.setSelected(false);
                    userLogin.setClickable(false);
                } else {
                    userLogin.setSelected(true);
                    userLogin.setClickable(true);
                }
            }
        });
        etMsgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    userLogin.setSelected(false);
                    userLogin.setClickable(false);
                } else {
                    userLogin.setSelected(true);
                    userLogin.setClickable(true);
                }
            }
        });
    }

    /**
     * 检测升级更新
     */
    private void checkUpdate() {
        RetrofitUtil.getInstance().apiService()
                .newversion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<UpdateVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<UpdateVO> result) {
                        LogUtils.e("---检测升级更新--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            UpdateVO data = result.getData();
                            if(data != null) {
                                //检测升级更新
                                AutoUpgradeClient.checkUpgrade(LoginActivity.this, data);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_login:
                login();
                break;
            case R.id.user_tv_regist:
                launch(RegisterActivity.class);
//                 以下为 临时测试代码
//                copy("123.64");
//                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
//                String qrcode = "alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&money=0.01&bankAccount=%e5%90%b4%e5%a5%87&cardNo=6217000030001234567";
//                String qrcode = "HTTPS://QR.ALIPAY.COM/FKX00335U9OKBJ58TUPQCC";
//                String qrcode = "https://qr.alipay.com/fkx04737pkprujruezeuuf5";
//                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode="+ qrcode);
////                Uri uri = Uri.parse("alipay://platformapi/startapp?appId=20000056");//跳转支付宝转账向商家付款界面）
//                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=20000123&qrcode="+qrcode );//跳转支付宝转账向商家付款界面）
////                Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=alipays://platformapi/startapp?appId=09999988&actionType=toCard&sourceId=bill&money=0.01&bankAccount=%e5%90%b4%e5%a5%87&cardNo=6217000030001234567");
//                startUri(uri);
//                testPay();
                break;
            case R.id.user_tv_forget:
                launch(ForgetLoginPwdActivity.class);
                break;
            case R.id.get_code:
                sendCode();
                break;
            case R.id.top_intro_login_code_lay:
                showCodeLogin();
                break;
            case R.id.top_intro_login_normal_lay:
                showNoramlLogin();
                break;
            default:
                break;
        }
    }


    private void startUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void testPay() {
        String intentFullUrl = "intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Ffkx01396j2soqxvhycceg74%3F_s%3Dweb-other&_t=1472443966571#Intent;scheme=alipayqr;package=com.eg.android.AlipayGphone;end";
        Intent intent = null;
        try {
            intent = Intent.parseUri(intentFullUrl, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }


    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    private boolean copy(String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText(null, copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private void login(){
        String mobile = null;
        String pwd = null;
        if(isNormalLogin){
            //普通登录
            mobile = userEtPhone.getText().toString();
            pwd = userEtPwd.getText().toString();
        }else{
            //验证码登录
            mobile = codeUserEtPhoneCwe.getText().toString();
            pwd = etMsgCode.getText().toString();
        }
        if (TextUtils.isEmpty(mobile)) {
            if(isNormalLogin){
                userEtPhone.setShakeAnimation();
            }else{
                codeUserEtPhoneCwe.setShakeAnimation();
            }
            ToastUtil.showToast(LoginActivity.this, "请输入手机号/用户名/会员码");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            if(isNormalLogin){
                userEtPwd.setShakeAnimation();
            }else{
                etMsgCode.setShakeAnimation();
            }

            ToastUtil.showToast(LoginActivity.this, "请输入密码");
            return;
        }

        if(isNormalLogin){
            login(mobile,pwd);
        }else{
            loginByVerificationCode(mobile,pwd);
        }
    }

    /**
     * 登录操作
     */
    private void login(String mobile, String pwd) {
        show(LoginActivity.this, "登录中...");
        RetrofitUtil.getInstance().apiService()
                .login(mobile, pwd, 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbpUserVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbpUserVO> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            MbpUserVO data = result.getData();
                            if (data != null) {
                                Prefer.getInstance().setToken(data.getToken());
                                Prefer.getInstance().setMobile(data.getMobile());
                                Prefer.getInstance().setUserId(data.getAcqid());
                                Prefer.getInstance().setInviteCode(data.getRegcode());
                                Prefer.getInstance().setUserName(data.getUsername());
                                Prefer.getInstance().setLoginTime(data.getCreatetime());
                                Prefer.getInstance().setExpireTime(data.getExpiretime());
                                cancelLoginOutNotify();
                                Prefer.getInstance().setSocketIp(data.getIp());
                                ConfigUtil.resetSocketIp(data.getIp());
                                reConnector();
                                NettyHandler.getInstance().login();
                                launch(MainActivity.class);
                                finish();
                            }
                        } else {
                            userLogin.setClickable(true);
                            ToastUtil.showToast(LoginActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        userLogin.setClickable(true);
                        ToastUtil.showToast(LoginActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 登录操作
     */
    private void loginByVerificationCode(String mobile, String verificationCode) {
        show(LoginActivity.this, "登录中...");
        RetrofitUtil.getInstance().apiService()
                .loginByInventorCode(mobile, verificationCode, 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<MbpUserVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MbpUserVO> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            MbpUserVO data = result.getData();
                            if (data != null) {
                                Prefer.getInstance().setToken(data.getToken());
                                Prefer.getInstance().setMobile(data.getMobile());
                                Prefer.getInstance().setUserId(data.getAcqid());
                                Prefer.getInstance().setInviteCode(data.getRegcode());
                                Prefer.getInstance().setUserName(data.getUsername());
                                Prefer.getInstance().setLoginTime(data.getCreatetime());
                                Prefer.getInstance().setExpireTime(data.getExpiretime());
                                cancelLoginOutNotify();
                                Prefer.getInstance().setSocketIp(data.getIp());
                                ConfigUtil.resetSocketIp(data.getIp());
                                reConnector();
                                NettyHandler.getInstance().login();
                                launch(MainActivity.class);
                                finish();
                            }
                        } else {
                            userLogin.setClickable(true);
                            ToastUtil.showToast(LoginActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        userLogin.setClickable(true);
                        ToastUtil.showToast(LoginActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void onBackPressed() {
        SctApp.getInstance().exit(LoginActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onLeftClick() {
        SctApp.getInstance().exit(LoginActivity.this);
    }

    @Override
    public void onRightClick() {

    }

    /**
     * 重新 socket连接
     */
    private void reConnector() {
        /**
         * 长连接地址
         */
        LogUtils.e("---常量-----" + Constants.APP_HOST + "----" + Constants.APP_PORT);
        NettyConnector.addHostPort();
//        NettyConnector.addHostPort(Constants.BASE_URL, Constants.APP_PORT);
        NettyConnector.connect(NettyHandler.getInstance()).connect(true);
    }


    /**
     * 取消被挤掉的消息
     */
    private void cancelLoginOutNotify() {
        NotificationUtils.getInstance().cancelNotificationById(NotificationUtils.LOGIN_OUT_NOTIFICATION_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }



    private void sendCode(){
        String phone = codeUserEtPhoneCwe.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            codeUserEtPhoneCwe.setShakeAnimation();
            ToastUtil.showToast(LoginActivity.this, "请输入手机号");
            return;
        }

        startRunTime();

        //
        getMsgCode(phone, 6);
    }

    /**
     * 获取注册验证码
     *
     * @param mobile
     * @param event
     */
    private void getMsgCode(String mobile, int event) {
        RetrofitUtil.getInstance().apiService()
                .sendmsg(mobile, event)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(LoginActivity.this, "验证发送成功，请注意查收");
                        } else {
                            ToastUtil.showToast(LoginActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(LoginActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private MyCountDownTimer countDownTimer;
    private static final int GETCODE = 60000;
    /**
     * 开始验证码获取倒计时
     */
    private void startRunTime() {
        countDownTimer = new MyCountDownTimer(LoginActivity.this, GETCODE, 1000, getCode, getString(R.string.getCode), this);
        countDownTimer.start();
        getCode.setClickable(false);
    }

    /**
     * 获取验证码结束
     */
    @Override
    public void timeFinish() {
        UiUtils.runOnUiThread(() -> {
            getCode.setText(getResources().getString(R.string.get_verification_code));
            getCode.setClickable(true);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    boolean isNormalLogin = false;

    private void showNoramlLogin() {
        isNormalLogin = true;
        loginInputNormalLay.setVisibility(View.VISIBLE);
        loginInputCodeLay.setVisibility(View.GONE);
        topIntroLoginNormalBLineV.setBackgroundColor(getResources().getColor(R.color.main_bule));
        topIntroLoginNormalTv.setTextColor(getResources().getColor(R.color.main_bule));
        topIntroLoginCodeBLineV.setBackgroundColor(getResources().getColor(R.color.color_c));
        topIntroLoginCodeTv.setTextColor(getResources().getColor(R.color.color_c));
    }

    private void showCodeLogin() {
        isNormalLogin = false;
        loginInputNormalLay.setVisibility(View.GONE);
        loginInputCodeLay.setVisibility(View.VISIBLE);
        topIntroLoginCodeBLineV.setBackgroundColor(getResources().getColor(R.color.main_bule));
        topIntroLoginCodeTv.setTextColor(getResources().getColor(R.color.main_bule));
        topIntroLoginNormalBLineV.setBackgroundColor(getResources().getColor(R.color.color_c));
        topIntroLoginNormalTv.setTextColor(getResources().getColor(R.color.color_c));
    }


}
