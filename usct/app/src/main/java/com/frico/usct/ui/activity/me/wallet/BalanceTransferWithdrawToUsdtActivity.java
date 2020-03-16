package com.frico.usct.ui.activity.me.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.Result;
import com.frico.usct.dialog.BaseIncomeSureDialog;
import com.frico.usct.dialog.TransferUsdtOutSureDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.DecimalInputTextWatcher;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * sct提币到usdt
 */
public class BalanceTransferWithdrawToUsdtActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_TO_USER_ID = "toUserId";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_account_balance_can_used)
    EditText etAccountBalanceCanUsed;
    @BindView(R.id.et_transfer_count)
    EditText etTransferCount;
    //    @BindView(R.id.et_transfer_pay_password)
//    EditText etTransferPayPassword;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;

    private TransferUsdtOutSureDialog mSureDialog;

    private String mToUserId;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceTransferWithdrawToUsdtActivity.class));
    }

//    public static void start(Activity activity, String toUserId) {
//        Intent intent = new Intent(activity, BalanceTransferFromUsdtActivity.class);
//        intent.putExtra(KEY_TO_USER_ID, toUserId);
//        activity.startActivity(intent);
//    }

    @Override
    protected int setLayout() {
        return R.layout.act_transfer_to_usdt;
    }

    @Override
    public void initTitle() {
        mToUserId = getIntent().getStringExtra(KEY_TO_USER_ID);
        String title = "USCT提币";

        actionbar.setData(title, R.drawable.ic_left_back2x, null, 0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        //输入总长度15位，小数2位
        etTransferCount.addTextChangedListener(new DecimalInputTextWatcher(etTransferCount, 15, 2));


    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
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

        if (TextUtils.isEmpty(etTransferCount.getText())) {
            ToastUtil.showToast(this, "请输入金额");
            return;
        }

        tvSubmit.setClickable(false);
        String address = etAccountBalanceCanUsed.getText().toString();
        String count = etTransferCount.getText().toString().trim();
//        String payPassword = etTransferPayPassword.getText().toString().trim();
        double countDouble = Double.parseDouble(count);
        if (countDouble <= 0) {
            ToastUtil.showToast(this, "充值金额必须要大于0");
            tvSubmit.setClickable(true);
            return;
        }

        if (mSureDialog == null) {
            mSureDialog = new TransferUsdtOutSureDialog(this);
        }
        if (!mSureDialog.isShowing()) {
            mSureDialog.show();
        }
        mSureDialog.initViewData(count, null);
        mSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {
                tvSubmit.setClickable(true);
            }

            @Override
            public void onSure(String pw) {

                submit(count, address, pw);

            }
        });

    }



    /**
     * 确认提币信息
     */
    public void submit(String count, String address, String pw) {
        RetrofitUtil.getInstance().apiService()
                .submitChongBi("2",count, address, pw)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(BalanceTransferWithdrawToUsdtActivity.this, "等待确认中");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferWithdrawToUsdtActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferWithdrawToUsdtActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BalanceTransferWithdrawToUsdtActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
