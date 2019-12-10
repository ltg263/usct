package com.frico.easy_pay.ui.activity.me.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.impl.CountDownTimerFinishedListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.ClearWriteEditText;
import com.frico.easy_pay.widget.MyCountDownTimer;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForgetLoginPwdActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener, CountDownTimerFinishedListener {
    private static String KEY_IS_SETTING = "isSetting";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_user_phone)
    ClearWriteEditText etUserPhone;
    @BindView(R.id.et_msg_code)
    ClearWriteEditText etMsgCode;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.et_pwd)
    ClearWriteEditText etPwd;
    @BindView(R.id.et_verify_pwd)
    ClearWriteEditText etVerifyPwd;
    @BindView(R.id.ll_go_login)
    LinearLayout llGoLogin;
    @BindView(R.id.tv_next)
    TextView tvSubmit;
    @BindView(R.id.forget_login_pwd_intro)
    TextView forgetLoginPwdIntro;

    private MyCountDownTimer countDownTimer;
    private static final int GETCODE = 60000;

    private boolean mIsSettingPW = false;


    public static void start(Activity activity, boolean isSettingPW) {
        Intent intent = new Intent(activity, ForgetLoginPwdActivity.class);
        intent.putExtra(KEY_IS_SETTING, isSettingPW);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_forget_login_pwd;
    }

    @Override
    public void initTitle() {
        mIsSettingPW = getIntent().getBooleanExtra(KEY_IS_SETTING, false);
        String title = mIsSettingPW ? "设置登录密码" : "找回密码";
        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        if (mIsSettingPW) {
            forgetLoginPwdIntro.setVisibility(View.GONE);
        }else{
            forgetLoginPwdIntro.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        getCode.setOnClickListener(this);
        llGoLogin.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvSubmit.setClickable(false);

        etVerifyPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    tvSubmit.setSelected(false);
                    tvSubmit.setClickable(false);
                } else {
                    tvSubmit.setSelected(true);
                    tvSubmit.setClickable(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String phone = etUserPhone.getText().toString().trim();
        switch (view.getId()) {
            case R.id.get_code:
                if (TextUtils.isEmpty(phone)) {
                    etUserPhone.setShakeAnimation();
                    ToastUtil.showToast(ForgetLoginPwdActivity.this, "请输入手机号");
                    return;
                }

                initCode();
                getMsgCode(phone, 2);
                break;
            case R.id.tv_next:
                String code = etMsgCode.getText().toString().trim();
                String pwd = etPwd.getText().toString();
                String verifyPwd = etVerifyPwd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    etUserPhone.setShakeAnimation();
                    ToastUtil.showToast(ForgetLoginPwdActivity.this, "请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    etMsgCode.setShakeAnimation();
                    ToastUtil.showToast(ForgetLoginPwdActivity.this, "请输入验证码");
                    return;
                }

                if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 12 || !UiUtils.isLetterDigit(pwd)) {
                    etPwd.setShakeAnimation();
                    ToastUtil.showToast(ForgetLoginPwdActivity.this, "请输入6-12位数字字母组合");
                    return;
                }

                if (!pwd.equals(verifyPwd)) {
                    etVerifyPwd.setShakeAnimation();
                    ToastUtil.showToast(ForgetLoginPwdActivity.this, "两次登录密码输入不一致");
                    return;
                }

                submitPwd(phone, code, pwd, verifyPwd);
                break;
            case R.id.ll_go_login:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 找回密码
     *
     * @param phone
     * @param code
     * @param pwd
     * @param verifyPwd
     */
    private void submitPwd(String phone, String code, String pwd, String verifyPwd) {
        show(ForgetLoginPwdActivity.this, "找回中...");
        RetrofitUtil.getInstance().apiService()
                .findpass(phone, code, pwd, verifyPwd)
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
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, "找回成功，请登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ForgetLoginPwdActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 初始化倒计时
     */
    private void initCode() {
        countDownTimer = new MyCountDownTimer(this, GETCODE, 1000, getCode, getString(R.string.getCode), this);
        countDownTimer.start();
        getCode.setClickable(false);
    }

    /**
     * 获取验证码
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
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, "验证发送成功，请注意查收");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ForgetLoginPwdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(ForgetLoginPwdActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void timeFinish() {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getCode.setText(getResources().getString(R.string.get_verification_code));
                getCode.setClickable(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
