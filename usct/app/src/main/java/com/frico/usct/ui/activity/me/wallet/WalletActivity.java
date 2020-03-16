package com.frico.usct.ui.activity.me.wallet;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.BankVO;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.MainActivity;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.me.WithdrawActivity;
import com.frico.usct.ui.activity.me.payway.AddBankActivity;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {


    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_all_count)
    TextView tvAllCount;
    @BindView(R.id.tv_can_count)
    TextView tvCanCount;
    @BindView(R.id.tv_earnings_count)
    TextView tvEarningsCount;
    @BindView(R.id.tv_freeze_count)
    TextView tvFreezeCount;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_total_can)
    TextView tvTotalCan;
    @BindView(R.id.tv_total_earnings)
    TextView tvTotalEarnings;
    @BindView(R.id.tv_one_details)
    RelativeLayout tvOneDetails;
    @BindView(R.id.tv_two_details)
    RelativeLayout tvTwoDetails;
    @BindView(R.id.tv_three_details)
    RelativeLayout tvThreeDetails;
    @BindView(R.id.tv_four_details)
    RelativeLayout tvFourDetails;
    @BindView(R.id.ll_count_lay)

    LinearLayout llCountLay;
    @BindView(R.id.tv_buy)
    TextView tvBuy;//去购买
    @BindView(R.id.tv_out_glod)
    TextView tvOutGlod;//去提现
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;//转账
    @BindView(R.id.ll_work_money)
    LinearLayout llWorkMoney;//
    @BindView(R.id.tv_work_money_out_glod)
    TextView tvWorkMoneyOutGlod;//佣金提现
    @BindView(R.id.tv_work_money_transfer)
    TextView tvWorkMoneyTransfer;//佣金转账
    @BindView(R.id.tv_all_count_title)
    TextView tvAllCountTitle;
    @BindView(R.id.tv_today_income_title)
    TextView tvTodayIncomeTitle;
    @BindView(R.id.ll_work_money_btn_lay)
    LinearLayout llWorkMoneyBtnLay;
    @BindView(R.id.tv_earnings_count_title)
    TextView tvEarningsCountTitle;
    @BindView(R.id.ll_earnings_count_lay)
    LinearLayout llEarningsCountLay;
    @BindView(R.id.tv_freeze_count_title)
    TextView tvFreezeCountTitle;
    @BindView(R.id.tv_money_record)
    TextView tvMoneyRecord;
    @BindView(R.id.tv_work_money_commission_record)
    TextView tvWorkMoneyCommissionRecord;
    @BindView(R.id.tv_earnings_record)
    TextView tvEarningsRecord;
    @BindView(R.id.tv_freeze_record)
    TextView tvFreezeRecord;
    @BindView(R.id.tv_in_from_usdt)
    TextView tvInFromUsdt;
    @BindView(R.id.tv_out_usdt)
    TextView tvOutUsdt;

    private List<BankVO.ListBean> banklistBeanList;

    @Override
    protected int setLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initTitle() {
        actionbar.setData("USCT钱包", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        tvBuy.setOnClickListener(this);
        tvOutGlod.setOnClickListener(this);
        tvTransfer.setOnClickListener(this);
        tvWorkMoneyOutGlod.setOnClickListener(this);
        tvWorkMoneyTransfer.setOnClickListener(this);

        tvOneDetails.setOnClickListener(this);
        tvTwoDetails.setOnClickListener(this);
        tvThreeDetails.setOnClickListener(this);
        tvFourDetails.setOnClickListener(this);
        tvMoneyRecord.setOnClickListener(this);
        tvWorkMoneyCommissionRecord.setOnClickListener(this);
        tvEarningsRecord.setOnClickListener(this);
        tvFreezeRecord.setOnClickListener(this);
        tvInFromUsdt.setOnClickListener(this);
        tvOutUsdt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        show(WalletActivity.this, "加载中...");
        getUserInfo();
        getBankList();
    }

    private void getUserInfo() {
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
                            SctApp.mUserInfoData = result.getData();

                            tvAllCountTitle.setText("可用数量 " + sctToRmb(data.getAvailable_money()));
                            tvTodayIncomeTitle.setText("佣金收益 " + sctToRmb(data.getProfit_money()));

                            tvAllCount.setText(data.getAvailable_money());
                            tvEarningsCount.setText(data.getGive_money());
                            tvFreezeCount.setText(data.getFrozen_money());
                            tvCanCount.setText(data.getProfit_money());

                            tvTotalCan.setText("累计收益: " + data.getTotal_profit_money() + " USCT");
                            tvTotalEarnings.setText("累计赠送: " + data.getTotal_give_money() + " USCT");
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(WalletActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(WalletActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(WalletActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one_details:
            case R.id.tv_money_record:
                launch(MoneyDetailsActivity.class);
                break;
            case R.id.tv_two_details:
            case R.id.tv_work_money_commission_record:
                launch(MoneyTwoDetailsActivity.class);
                break;
            case R.id.tv_earnings_record:
            case R.id.tv_three_details:
                launch(MoneyThreeDetailsActivity.class);
                break;
            case R.id.tv_freeze_record:
            case R.id.tv_four_details:
                launch(MoneyFourDetailsActivity.class);
                break;
            case R.id.tv_buy:
                //去购买，交易大厅
                SctApp.mHomeSelectIndex = 2;
                MainActivity.start(this);
                break;
            case R.id.tv_out_glod:
                //提现
                if (!checkIsCanWithdraw()) {
                    showBindBankCard();
                } else {
                    WithdrawActivity.start(this);
                }
                break;
            case R.id.tv_transfer:
                //转账
                BalanceTransferActivity.start(this);
                break;
            case R.id.tv_work_money_out_glod:
                //佣金提现
                showCustomToast(this, "敬请期待");
                break;
            case R.id.tv_work_money_transfer:
                //佣金转账
                profitsettlement();
                break;
            case R.id.tv_in_from_usdt:
                //USDT充值
                launch(BalanceTransferFromUsdtActivity.class);
                break;
            case R.id.tv_out_usdt:
                //提币
                BalanceTransferWithdrawToUsdtActivity.start(this);
                break;
        }
    }

    //只有有审核通过的银行卡 的时候才可以提现,否则就去绑卡提示
    private boolean checkIsCanWithdraw() {
        if (banklistBeanList == null || banklistBeanList.size() == 0) {
            return false;
        } else {
            BankVO.ListBean temp = null;
            for (int i = 0; i < banklistBeanList.size(); i++) {
                temp = banklistBeanList.get(i);
                if (temp.getVerifystatus() == 1) {
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * 一键转余额
     */
    private void profitsettlement() {
        RetrofitUtil.getInstance().apiService()
                .profitsettlement()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(WalletActivity.this, "操作成功");
                            getUserInfo();
                        } else {
                            ToastUtil.showToast(WalletActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WalletActivity.this, e.getMessage());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private String sctToRmb(String sctCountStr) {


        return "";
        //usct兑换比例为1：1 所以暂时隐藏下面这段
//
//        if(! TextUtils.isEmpty(sctCountStr)){
//            try {
//                double sctCountDouble = Double.parseDouble(sctCountStr);
//                return " ≈ " + MathUtil.mul(sctCountDouble,RATE) + "元";
//            }catch (NumberFormatException e){
//                return "";
//            }
//        }else{
//            return "";
//        }
    }

    /**
     * 必须在 二维码支付方式到达后，再请求银行卡列表  否则，listitem 点击事件无法定位数据
     * 获取银行卡列表
     */
    private void getBankList() {
        RetrofitUtil.getInstance().apiService()
                .banklist()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<BankVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<BankVO> result) {
                        if (result.getCode() == 1) {
                            banklistBeanList = result.getData().getList();

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(WalletActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(WalletActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WalletActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 提示没有添加银行卡
     */
    private void showBindBankCard() {
        SimpleDialog simpleDialog = new SimpleDialog(this, "该账号暂无银行卡", "提示", "取消", "去添加", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                // 去添加银行卡
                launch(AddBankActivity.class);
            }
        });
        simpleDialog.setCanceledOnTouchOutside(false);
        simpleDialog.show();
    }
}
