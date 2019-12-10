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
import com.frico.easy_pay.ui.activity.LoginActivity;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SetPayPwdActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_next)
    EditText etPwdNext;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private String pwd;
    private String pwdNext;

    @Override
    protected int setLayout() {
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    public void initTitle() {
        actionbar.setData("设置支付密码", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        tvSubmit.setClickable(false);
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshTvSumit();
            }
        });
        etPwdNext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshTvSumit();
            }
        });
    }




    private void refreshTvSumit(){
        String pwd = etPwd.getText().toString().trim();
        String pwdNext = etPwdNext.getText().toString().trim();
        if( (!TextUtils.isEmpty(pwd)) && !TextUtils.isEmpty(pwdNext)){
            tvSubmit.setClickable(true);
            tvSubmit.setEnabled(true);
        }else{
            tvSubmit.setClickable(false);
            tvSubmit.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        pwd = etPwd.getText().toString();
        pwdNext = etPwdNext.getText().toString();
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
                    ToastUtil.showToast(SetPayPwdActivity.this, "请输入6位数字密码");
                    return;
                }

                if (!pwd.equals(pwdNext)) {
                    ToastUtil.showToast(SetPayPwdActivity.this, "两次密码输入不一致");
                    return;
                }

                submitPwd(pwd, pwdNext);
                break;
            default:
                break;
        }
    }

    /**
     * 设置支付密码
     *
     * @param pwd
     * @param payPwd
     */
    private void submitPwd(String pwd, String payPwd) {
        RetrofitUtil.getInstance().apiService()
                .changepaypass(pwd, payPwd)
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
                            ToastUtil.showToast(SetPayPwdActivity.this, "设置成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(SetPayPwdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(SetPayPwdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(SetPayPwdActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
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

}
