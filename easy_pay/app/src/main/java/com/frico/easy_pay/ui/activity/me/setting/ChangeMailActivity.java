package com.frico.easy_pay.ui.activity.me.setting;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.frico.easy_pay.widget.MyCountDownTimer;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangeMailActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.et_mail_code)
    EditText etMailCode;

    private MyCountDownTimer countDownTimer;
    private static final int GETCODE = 60000;

    @Override
    protected int setLayout() {
        return R.layout.activity_change_mail;
    }

    @Override
    public void initTitle() {
        actionbar.setData("绑定邮箱", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvNext.setOnClickListener(this);
        getCode.setOnClickListener(this);
        tvNext.setClickable(false);

        etMailCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etMail.getText().toString())) {
                    if (!TextUtils.isEmpty(s.toString())) {
                        tvNext.setClickable(true);
                        tvNext.setEnabled(true);
                    } else {
                        tvNext.setClickable(false);
                        tvNext.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String mail = etMail.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_next:
                String code = etMailCode.getText().toString().trim();
                if (TextUtils.isEmpty(mail)) {
                    ToastUtil.showToast(ChangeMailActivity.this, "请输入邮箱账号");
                    return;
                }

                if (!UiUtils.isEmail(mail)) {
                    ToastUtil.showToast(ChangeMailActivity.this, "输入邮箱格式错误");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(ChangeMailActivity.this, "请输入邮箱验证码");
                    return;
                }

                changeMail(mail, code);
                break;
            case R.id.get_code:
                if (TextUtils.isEmpty(mail)) {
                    ToastUtil.showToast(ChangeMailActivity.this, "请输入邮箱账号");
                    return;
                }

                if (!UiUtils.isEmail(mail)) {
                    ToastUtil.showToast(ChangeMailActivity.this, "输入邮箱格式错误");
                    return;
                }

                initCode();
                getMsgCode(mail);
                break;
            default:
                break;
        }
    }

    private void changeMail(String mail, String code) {
        show(ChangeMailActivity.this, "修改中...");
        RetrofitUtil.getInstance().apiService()
                .changeemail(mail, code)
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
                            ToastUtil.showToast(ChangeMailActivity.this, "修改成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ChangeMailActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ChangeMailActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ChangeMailActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 获取邮箱验证码
     *
     * @param mail
     */
    private void getMsgCode(String mail) {
        RetrofitUtil.getInstance().apiService()
                .sendemail(mail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(ChangeMailActivity.this, "验证码已发送，注意查收");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ChangeMailActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ChangeMailActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(ChangeMailActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 倒计时操作
     */
    private void initCode() {
        countDownTimer = new MyCountDownTimer(ChangeMailActivity.this, GETCODE, 1000, getCode, getString(R.string.getCode), this);
        countDownTimer.start();
        getCode.setClickable(false);
    }


    @Override
    public void timeFinish() {
        UiUtils.runOnUiThread(() -> {
            getCode.setText(getResources().getString(R.string.get_verification_code));
            getCode.setClickable(true);
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
        cancleDialog();
    }
}
