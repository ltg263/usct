package com.frico.easy_pay.ui.activity.me.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangeLoginPwdActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    private static final String KEY_IS_MODIFY = "key_is_modify";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd_next)
    EditText etPwdNext;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_pwd_next_2)
    EditText etPwdNext2;

    private String loginNewPwd;
    private String loginNewPwd2;

    private boolean mIsModify;//修改密码 、设置密码

    /**
     * 设置登录密码
     *
     * @param activity
     */
    public static void startSetLoginPW(Activity activity) {
        Intent intent = new Intent(activity, ChangeLoginPwdActivity.class);
        intent.putExtra(KEY_IS_MODIFY, false);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_change_login_pwd;
    }

    @Override
    public void initTitle() {
        mIsModify = getIntent().getBooleanExtra(KEY_IS_MODIFY, true);
        String title = mIsModify ? "修改密码" : "设置密码";
        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        tvSubmit.setClickable(false);

        etPwdNext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshCommitBtn();
            }
        });

        etPwdNext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshCommitBtn();
            }
        });
    }

    private void refreshCommitBtn(){
        String pw = etPwdNext.getText().toString();
        String pw2 = etPwdNext2.getText().toString();
        if(!TextUtils.isEmpty(pw) && !TextUtils.isEmpty(pw2)){
            tvSubmit.setClickable(true);
            tvSubmit.setEnabled(true);
        }else{
            tvSubmit.setClickable(false);
            tvSubmit.setEnabled(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                commitChange();
                break;
            default:
                break;
        }
    }

    private void commitChange(){
        loginNewPwd = etPwdNext.getText().toString();
        loginNewPwd2 = etPwdNext2.getText().toString();
        if(!TextUtils.equals(loginNewPwd,loginNewPwd2)){
            ToastUtil.showToast(ChangeLoginPwdActivity.this,"两次密码输入不一致");
            return ;
        }
        if (loginNewPwd.length() < 6 || loginNewPwd.length() > 12 || !UiUtils.isLetterDigit(loginNewPwd)) {
            ToastUtil.showToast(ChangeLoginPwdActivity.this, "请输入6-12位数字字母组合密码");
            return;
        }

        submitPwd( loginNewPwd);
    }

    /**
     * 确认修改密码
     */
    private void submitPwd(String loginNewPwd) {
        show(ChangeLoginPwdActivity.this, "设置中...");
        RetrofitUtil.getInstance().apiService()
                .changepass( loginNewPwd, loginNewPwd)
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
                            ToastUtil.showToast(ChangeLoginPwdActivity.this, "设置成功, 请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(ChangeLoginPwdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(ChangeLoginPwdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(ChangeLoginPwdActivity.this, e.getMessage());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
