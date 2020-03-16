package com.frico.usct.ui.activity.me.setting;

import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.Result;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.impl.CountDownTimerFinishedListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.MyCountDownTimer;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener, ActionBarClickListener, CountDownTimerFinishedListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.get_code)
    TextView getCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_phone_code)
    EditText etPhoneCode;

    private MyCountDownTimer countDownTimer;
    private static final int GETCODE = 60000;

    @Override
    protected int setLayout() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initTitle() {
        actionbar.setData("绑定新手机号", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvNext.setOnClickListener(this);
        getCode.setOnClickListener(this);
        tvNext.setClickable(false);

        etPhoneCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etPhoneNum.getText().toString())) {
                    if (!TextUtils.isEmpty(s.toString())) {
                        tvNext.setClickable(true);
                        tvNext.setSelected(true);
                    } else {
                        tvNext.setClickable(false);
                        tvNext.setSelected(false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String phone = etPhoneNum.getText().toString().trim();
        switch (view.getId()) {
            case R.id.tv_next:
                String code = etPhoneCode.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    ToastUtil.showToast(BindPhoneActivity.this, getString(R.string.editMobile));
                    return;
                }

                if (TextUtils.isEmpty(code) || code.length() != 6) {
                    ToastUtil.showToast(BindPhoneActivity.this, getString(R.string.editCode));
                    return;
                }

                chagePhone(phone, code);
                break;
            case R.id.get_code:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(BindPhoneActivity.this, getString(R.string.editMobile));
                    return;
                }

                initCode();
                getMsgCode(phone);
                break;
            default:
                break;
        }
    }

    /**
     * 绑定新手机号
     * @param phone
     * @param code
     */
    private void chagePhone(String phone, String code) {
        show(BindPhoneActivity.this, "修改中...");
        RetrofitUtil.getInstance().apiService()
                .changenewmobile(phone, code)
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
                            ToastUtil.showToast(BindPhoneActivity.this, "修改成功");
                            launch(BindPhoneActivity.class);
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BindPhoneActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(BindPhoneActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(BindPhoneActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 获取验证码
     *
     * @param mobile
     */
    private void getMsgCode(String mobile) {
        RetrofitUtil.getInstance().apiService()
                .sendmsg(mobile, 4)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(BindPhoneActivity.this, "验证发送成功，请注意查收");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BindPhoneActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(BindPhoneActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BindPhoneActivity.this, e.getMessage());
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
        countDownTimer = new MyCountDownTimer(BindPhoneActivity.this, GETCODE, 1000, getCode, getString(R.string.getCode), this);
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
