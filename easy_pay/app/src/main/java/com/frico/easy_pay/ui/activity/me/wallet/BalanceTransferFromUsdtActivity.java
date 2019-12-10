package com.frico.easy_pay.ui.activity.me.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.ChongBiInfoVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.TransferUsdtSureDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.DecimalInputTextWatcher;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * sct充值
 */
public class BalanceTransferFromUsdtActivity extends BaseActivity implements View.OnClickListener {
    private static final String KEY_TO_USER_ID = "toUserId";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_account_balance_can_used)
    TextView tvAccountBalanceCanUsed;
    @BindView(R.id.et_transfer_count)
    EditText etTransferCount;
    //    @BindView(R.id.et_transfer_pay_password)
//    EditText etTransferPayPassword;
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.iv_usdt_qrcord)
    ImageView ivUsdtQrcord;

    private TransferUsdtSureDialog mSureDialog;

    private String mToUserId;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceTransferFromUsdtActivity.class));
    }

//    public static void start(Activity activity, String toUserId) {
//        Intent intent = new Intent(activity, BalanceTransferFromUsdtActivity.class);
//        intent.putExtra(KEY_TO_USER_ID, toUserId);
//        activity.startActivity(intent);
//    }

    @Override
    protected int setLayout() {
        return R.layout.act_transfer_usdt;
    }

    @Override
    public void initTitle() {
        mToUserId = getIntent().getStringExtra(KEY_TO_USER_ID);
        String title = "USCT充值";

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
        getChongBiInfoData();
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
        String address = tvAccountBalanceCanUsed.getText().toString();
        String count = etTransferCount.getText().toString().trim();
//        String payPassword = etTransferPayPassword.getText().toString().trim();
        double countDouble = Double.parseDouble(count);
        if (countDouble <= 0) {
            ToastUtil.showToast(this, "充值金额必须要大于0");
            tvSubmit.setClickable(true);
            return;
        }

        if (mSureDialog == null) {
            mSureDialog = new TransferUsdtSureDialog(this);
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
     * 获取充币信息
     */
    public void getChongBiInfoData() {
        RetrofitUtil.getInstance().apiService()
                .getChongBiInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<ChongBiInfoVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<ChongBiInfoVO> result) {
                        if (result.getCode() == 1) {
                            //初始化充币信息
                            if (result.getData() != null) {
                                tvAccountBalanceCanUsed.setText(result.getData().getAddress());
                                new ImageLoaderImpl().loadImage(BalanceTransferFromUsdtActivity.this,result.getData().getImg()).into(ivUsdtQrcord);
                            }

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 确认充币信息
     */
    public void submit(String count, String address, String pw) {
        RetrofitUtil.getInstance().apiService()
                .submitChongBi("1" ,count, address, pw)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, "等待确认中");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(BalanceTransferFromUsdtActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
