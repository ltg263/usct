package com.frico.usct.ui.activity.me.setting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.me.payway.AddPayWayActivity;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSaveActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_set_pwd)
    TextView tvSetPwd;
    @BindView(R.id.tv_set_pay_pw)
    TextView tvSetPayPw;
    @BindView(R.id.tv_bind_mail)
    TextView tvBindMail;
    @BindView(R.id.tv_add_new_pay_way)
    TextView tvAddNewPayWay;
    @BindView(R.id.tv_set_approve)
    TextView tvSetApprove;

    @Override
    protected int setLayout() {
        return R.layout.activity_account_save;
    }

    @Override
    public void initTitle() {
        actionbar.setData("安全设置", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvSetPwd.setOnClickListener(this);
        tvSetPayPw.setOnClickListener(this);
        tvBindMail.setOnClickListener(this);
        tvAddNewPayWay.setOnClickListener(this);
        tvSetApprove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set_pwd:
                //设置登录密码
//                ForgetLoginPwdActivity.start(AccountSaveActivity.this, true);
                ChangeLoginPwdActivity.startSetLoginPW(AccountSaveActivity.this);
                break;
            case R.id.tv_set_pay_pw:
                //设置支付密码
                launch(SetPayPwdActivity.class);
                break;
            case R.id.tv_bind_mail:
                //绑定邮箱
                launch(ChangeMailActivity.class);
                break;
            case R.id.tv_set_approve:
                launch(CertificationActivity.class);
                break;
            case R.id.tv_add_new_pay_way:
                //添加收款方式
                launch(AddPayWayActivity.class);
                break;

            //更改登录密码
//                launch(ChangeLoginPwdActivity.class);
//                break;
//            case R.id.tv_change_phone:
//                //更改手机号  废弃
//                launch(ChangePhoneActivity.class);
//                break;
//            case R.id.tv_change_mail:
//                //更改邮箱
//                launch(ChangeMailActivity.class);
//                break;
//            case R.id.tv_change_pay_pwd:
//                //更改支付密码
//                launch(SetPayPwdActivity.class);
//                break;
            default:
                break;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
    }


}
