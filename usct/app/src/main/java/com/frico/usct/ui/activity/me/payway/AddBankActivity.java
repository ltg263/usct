package com.frico.usct.ui.activity.me.payway;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.BankVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddBankActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.et_sub_branch)
    EditText etSubBranch;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.et_bank_no)
    EditText etBankNo;
    @BindView(R.id.et_bank_phone)
    EditText etBankPhone;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    private BankVO.ListBean listBean;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_bank;
    }

    @Override
    public void initTitle() {
        listBean = (BankVO.ListBean) getIntent().getSerializableExtra("listBean");
        actionbar.setData(listBean == null ? "添加银行卡" : "编辑银行卡", R.drawable.ic_left_back2x, null, 0, null, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        tvSubmit.setEnabled(true);
        if (listBean != null) {
            etName.setText(listBean.getAccountname());
            etBankName.setText(listBean.getBankname());
            etSubBranch.setText(listBean.getSubbranch());
            etBankNo.setText(listBean.getCardnumber());
            etBankPhone.setText(listBean.getCardmobile());
            etNick.setText(listBean.getAlias());

            if(listBean.getVerifystatus() == 0){
                //如果改卡是待审核的，就暂不许更改
                tvSubmit.setText("审核中不可变更");
                tvSubmit.setEnabled(false);
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入您的姓名");
                    return;
                }

                if (TextUtils.isEmpty(etBankName.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入银行名称");
                    return;
                }

                if (TextUtils.isEmpty(etSubBranch.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入开户支行");
                    return;
                }

                if (TextUtils.isEmpty(etBankNo.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入银行卡号");
                    return;
                }

                if (TextUtils.isEmpty(etBankPhone.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入银行预留手机号");
                    return;
                }

              /*  if (TextUtils.isEmpty(etNick.getText().toString())) {
                    ToastUtil.showToast(AddBankActivity.this, "请输入您的别名");
                    return;
                }*/

                UiUtils.runOnUiThread(() -> {
                    if (listBean == null) {
                        addBank();
                    } else {
                        changeBank();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 修改银行卡
     */
    private void changeBank() {
        show(AddBankActivity.this, "修改中...");
        RetrofitUtil.getInstance().apiService()
                .bankupdate(listBean.getId(), etBankName.getText().toString(), etSubBranch.getText().toString(), etName.getText().toString(), etBankNo.getText().toString(), etBankPhone.getText().toString(), "000")
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
                            ToastUtil.showToast(AddBankActivity.this, "修改成功");
                            RxBus.get().post(BusAction.PAY_WAY_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AddBankActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AddBankActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AddBankActivity.this, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 添加银行卡
     */
    private void addBank() {
        show(AddBankActivity.this, "添加中...");
        RetrofitUtil.getInstance().apiService()
                .bankadd(etBankName.getText().toString(), etSubBranch.getText().toString(), etName.getText().toString(), etBankNo.getText().toString(), etBankPhone.getText().toString(), "000")
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
                            RxBus.get().post(BusAction.PAY_WAY_LIST, "");
                            ToastUtil.showToast(AddBankActivity.this, "添加成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AddBankActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AddBankActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AddBankActivity.this, e.getMessage());

                    }

                    @Override
                    public void onComplete() {

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
