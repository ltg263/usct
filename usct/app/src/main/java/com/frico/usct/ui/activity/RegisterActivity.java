package com.frico.usct.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.config.Constant;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.netty.NettyConnector;
import com.frico.usct.core.netty.NettyHandler;
import com.frico.usct.core.utils.ConfigUtil;
import com.frico.usct.core.utils.Constants;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.impl.CountDownTimerFinishedListener;
import com.frico.usct.service.NotificationUtils;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.me.WebUrlActivity;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.Prefer;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.ClearWriteEditText;
import com.frico.usct.widget.MyCountDownTimer;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_invite_code)
    ClearWriteEditText etInviteCode;
    @BindView(R.id.et_user_nick)
    ClearWriteEditText etUserNick;
    @BindView(R.id.et_user_mail)
    ClearWriteEditText etUserMail;
    @BindView(R.id.et_user_phone)
    ClearWriteEditText etUserPhone;
    @BindView(R.id.et_msg_code)
    ClearWriteEditText etMsgCode;
    @BindView(R.id.et_pwd)
    ClearWriteEditText etPwd;
    @BindView(R.id.et_verify_pwd)
    ClearWriteEditText etVerifyPwd;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.checkbox)
    CheckBox checkBox;
    @BindView(R.id.tv_register_state)
    TextView tvRegisterState;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.ll_go_login)
    LinearLayout llGoLogin;

    private MyCountDownTimer countDownTimer;
    private static final int GETCODE = 60000;
    private SimpleDialog simpleDialog;

    @Override
    protected int setLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initTitle() {
        actionbar.setData("注册", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvNext.setOnClickListener(this);
        getCode.setOnClickListener(this);
        tvRegisterState.setOnClickListener(this);
        llRegister.setOnClickListener(this);
        llGoLogin.setOnClickListener(this);
        tvNext.setClickable(false);

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            checkBox.setChecked(b);
            if (b) {
                tvNext.setBackgroundResource(R.drawable.mb_login_selected);
                tvNext.setClickable(true);
            } else {
                tvNext.setBackgroundResource(R.drawable.mb_btn_grayback);
                tvNext.setClickable(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        String phone = etUserPhone.getText().toString();
        switch (view.getId()) {
            case R.id.tv_next:
                String inviteCode = etInviteCode.getText().toString();
                String userNick = etUserNick.getText().toString();
                String userMail = etUserMail.getText().toString();
                String code = etMsgCode.getText().toString();
                String pwd = etPwd.getText().toString();
                String verifyPwd = etVerifyPwd.getText().toString();

                if (TextUtils.isEmpty(inviteCode)) {
                    etInviteCode.setShakeAnimation();
                    ToastUtil.showToast(RegisterActivity.this, "请输入邀请码");
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    etUserPhone.setShakeAnimation();
                    ToastUtil.showToast(RegisterActivity.this, "请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    etMsgCode.setShakeAnimation();
                    ToastUtil.showToast(RegisterActivity.this, "请输入验证码");
                    return;
                }


                registerUserBySms(inviteCode,phone,code);
                break;
            case R.id.ll_register:
                checkBox.setChecked(!checkBox.isChecked());
                break;
            case R.id.tv_register_state:
                getAgreement();
                break;
            case R.id.get_code:
                if (TextUtils.isEmpty(phone)) {
                    etUserPhone.setShakeAnimation();
                    ToastUtil.showToast(RegisterActivity.this, "请输入手机号");
                    return;
                }

                startRunTime();
                getMsgCode(phone, 1);
                break;
            case R.id.ll_go_login:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户协议
     */
    private void getAgreement() {
        show(RegisterActivity.this, "获取中...");
        RetrofitUtil.getInstance().apiService()
                .agreement()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<String> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            startActivity(new Intent(RegisterActivity.this, WebUrlActivity.class).putExtra(Constant.URL, result.getData()).putExtra(Constant.TITLE, "注册协议"));
                        }else {
                            ToastUtil.showToast(RegisterActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(RegisterActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 用户注册App
     *
     * @param inviteCode
     * @param userNick
     * @param userMail
     * @param phone
     * @param code
     * @param pwd
     * @param verifyPwd
     */
    private void registerUser(String inviteCode, String userNick, String userMail, String phone, String code, String pwd, String verifyPwd) {
        show(RegisterActivity.this, "注册中...");
        RetrofitUtil.getInstance().apiService()
                .register(inviteCode, userNick, pwd, verifyPwd, userMail, phone, code, 3)
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
                            Prefer.getInstance().setMobile(phone);
                            ToastUtil.showToast(RegisterActivity.this, "注册成功");
                            finish();
                        } else {
                            ToastUtil.showToast(RegisterActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(RegisterActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 用户注册App
     *
     * @param inviteCode
     * @param phone
     * @param code
     */
    private void registerUserBySms(String inviteCode, String phone, String code) {
        show(RegisterActivity.this, "注册中...");
        RetrofitUtil.getInstance().apiService()
                .smsRegister(inviteCode,  phone, code, 3)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<String> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            String token = result.getData();
                            Prefer.getInstance().setToken(token);
                            getUserInfo();
                        } else {
                            ToastUtil.showToast(RegisterActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(RegisterActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 登录操作
     */
    private void getUserInfo() {
        show(RegisterActivity.this, "获取信息中...");
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
                                Prefer.getInstance().setSocketIp(data.getIp());
                                cancelLoginOutNotify();

                                ConfigUtil.resetSocketIp(data.getIp());
                                reConnector();

                                NettyHandler.getInstance().login();
                                SctApp.mUserInfoData = result.getData();
                                launch(MainActivity.class);
                                finish();
                            }
                        } else {
                            ToastUtil.showToast(RegisterActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(RegisterActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
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
                            ToastUtil.showToast(RegisterActivity.this, "验证发送成功，请注意查收");
                        } else {
                            ToastUtil.showToast(RegisterActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(RegisterActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 开始验证码获取倒计时
     */
    private void startRunTime() {
        countDownTimer = new MyCountDownTimer(RegisterActivity.this, GETCODE, 1000, getCode, getString(R.string.getCode), this);
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

    private void editInfoCancle() {
        if (simpleDialog == null) {
            simpleDialog = new SimpleDialog(RegisterActivity.this, "是否放弃本次注册？", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
                @Override
                public void onNegBtnClick() {

                }

                @Override
                public void onPosBtnClick() {
                    finish();
                }
            });
        }
        simpleDialog.show();
    }


    @Override
    public void onLeftClick() {
        editInfoCancle();
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
        cancleDialog();
    }

    /**
     * 物理返回键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            editInfoCancle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
}
