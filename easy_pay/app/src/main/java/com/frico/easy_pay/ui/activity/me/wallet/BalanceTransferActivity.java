package com.frico.easy_pay.ui.activity.me.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.TransferSureDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.DecimalInputTextWatcher;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 余额转账
 */
public class BalanceTransferActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_TO_USER_ID = "toUserId";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_account_balance_can_used)
    TextView tvAccountBalanceCanUsed;
    @BindView(R.id.et_transfer_id)
    EditText etTransferId;
    @BindView(R.id.et_transfer_count)
    EditText etTransferCount;
    //    @BindView(R.id.et_transfer_pay_password)
//    EditText etTransferPayPassword;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.ll_to_user_id)
    LinearLayout llToUserId;
    @BindView(R.id.tv_transfer_tip)
    TextView tvTransferTip;

    private TransferSureDialog mSureDialog;

    private String mToUserId;
    private String title;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceTransferActivity.class));
    }

    public static void start(Activity activity, String toUserId) {
        Intent intent = new Intent(activity, BalanceTransferActivity.class);
        intent.putExtra(KEY_TO_USER_ID, toUserId);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.act_transfer;
    }

    @Override
    public void initTitle() {
        mToUserId = getIntent().getStringExtra(KEY_TO_USER_ID);
        title = "转账";
        if (!TextUtils.isEmpty(mToUserId)) {
            title = "付款";
            etTransferId.setEnabled(false);
            etTransferId.setFocusable(false);
            etTransferId.setKeyListener(null);//重点
            etTransferId.setText(mToUserId);
            llToUserId.setVisibility(View.GONE);
            tvTransferTip.setText("付款数量");
        } else {
            llToUserId.setVisibility(View.VISIBLE);
            etTransferId.setEnabled(true);
            etTransferId.setFocusable(true);
        }
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
        if (SctApp.mUserInfoData != null) {
            tvAccountBalanceCanUsed.setText(SctApp.mUserInfoData.getAvailable_money() + " USCT");
        }
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

        if (TextUtils.isEmpty(etTransferId.getText())) {
            ToastUtil.showToast(this, "请输入转出账号");
            return;
        }
        if (TextUtils.isEmpty(etTransferCount.getText())) {
            ToastUtil.showToast(this, "请输入金额");
            return;
        }

        tvSubmit.setClickable(false);
        String idOrPhone = etTransferId.getText().toString().trim();
        String count = etTransferCount.getText().toString().trim();
//        String payPassword = etTransferPayPassword.getText().toString().trim();
        double countDouble = Double.parseDouble(count);
        if (countDouble <= 0) {
            ToastUtil.showToast(this, "转账金额必须要大于0");
            tvSubmit.setClickable(true);
            return;
        }
        if (idOrPhone.length() == 6 && TextUtils.equals(idOrPhone, SctApp.mUserInfoData.getAcqid())) {
            ToastUtil.showToast(this, "自己不能给自己转账");
            tvSubmit.setClickable(true);
            return;
        }
        if (TextUtils.isEmpty(SctApp.mUserInfoData.getMobile()) && idOrPhone.length() == 11 && TextUtils.equals(idOrPhone, SctApp.mUserInfoData.getMobile())) {
            ToastUtil.showToast(this, "自己不能给自己转账");
            tvSubmit.setClickable(true);
            return;
        }

        if (mSureDialog == null) {
            mSureDialog = new TransferSureDialog(this);
        }
        if (!mSureDialog.isShowing()) {
            mSureDialog.show();
        }
        mSureDialog.initViewData(count, idOrPhone);
        mSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {
                tvSubmit.setClickable(true);
            }

            @Override
            public void onSure(String pw) {

                submit(idOrPhone, count, pw);
            }
        });

    }

    private void submit(String idOrPhone, String count, String payPassword) {
        //提交
        withdraw(idOrPhone, count, payPassword);
    }


    /**
     * 转账
     */
    public void withdraw(String acqid, String amount, String paypassword) {
        showNoTips(this);
        if (isNumeric(acqid)){
            RetrofitUtil.getInstance().apiService()
                    .transferBalance(acqid, amount, paypassword,1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Result>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                        }

                        @Override
                        public void onNext(Result result) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                            if (result.getCode() == 1) {
                                //转账成功
                                ToastUtil.showToast(BalanceTransferActivity.this, "转账成功");
                                finish();
                            } else if (result.getCode() == 2) {
                                ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                                SctApp.getInstance().gotoLoginActivity();
                            } else {
                                ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                            ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            tvSubmit.setClickable(true);
                            cancleDialog();
                        }
                    });
        }else {
        RetrofitUtil.getInstance().apiService()
                .transferBalance(acqid, amount, paypassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }

                    @Override
                    public void onNext(Result result) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        if (result.getCode() == 1) {
                            //转账成功
                            ToastUtil.showToast(BalanceTransferActivity.this, "转账成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                        ToastUtil.showToast(BalanceTransferActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        tvSubmit.setClickable(true);
                        cancleDialog();
                    }
                });

        }
    }

    /**
     * 判断字符串是不是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
