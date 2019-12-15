package com.frico.easy_pay.ui.activity.me.wallet;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.BankVO;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.WithdrawActivity;
import com.frico.easy_pay.ui.activity.me.payway.AddBankActivity;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.Util;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewWalletActivity extends BaseActivity implements ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_wallet_sum_coin)
    TextView tvWalletSumCoin;
    @BindView(R.id.tv_wallet_today_earning)
    TextView tvWalletTodayEarning;
    @BindView(R.id.iv_usable_detail)
    ImageView ivUsableDetail;
    @BindView(R.id.tv_wallet_usable_num)
    TextView tvWalletUsableNum;
    @BindView(R.id.tv_wallet_withdraw)
    TextView tvWalletWithdraw;
    @BindView(R.id.tv_wallet_transfer)
    TextView tvWalletTransfer;
    @BindView(R.id.tv_wallet_recharge)
    TextView tvWalletRecharge;
    @BindView(R.id.tv_wallet_bring)
    TextView tvWalletBring;
    @BindView(R.id.ll_wallet_usable)
    LinearLayout llWalletUsable;
    @BindView(R.id.rl_card_usable_num)
    RelativeLayout rlCardUsableNum;
    @BindView(R.id.iv_earn_detail)
    ImageView ivEarnDetail;
    @BindView(R.id.tv_wallet_earn)
    TextView tvWalletEarn;
    @BindView(R.id.tv_wallet_total_earn)
    TextView tvWalletTotalEarn;
    @BindView(R.id.tv_wallet_transfer_balance)
    TextView tvWalletTransferBalance;
    @BindView(R.id.rl_wallet_earn_coin)
    RelativeLayout rlWalletEarnCoin;
    @BindView(R.id.iv_red_packet_detail)
    ImageView ivRedPacketDetail;
    @BindView(R.id.tv_wallet_red_packet)
    TextView tvWalletRedPacket;
    @BindView(R.id.tv_wallet_total_gift)
    TextView tvWalletTotalGift;
    @BindView(R.id.rl_wallet_red_packet)
    RelativeLayout rlWalletRedPacket;
    @BindView(R.id.iv_freeze_detail)
    ImageView ivFreezeDetail;
    @BindView(R.id.tv_wallet_freeze)
    TextView tvWalletFreeze;


    private List<BankVO.ListBean> bankListBeanList;


    @Override
    protected int setLayout() {
        return R.layout.activity_wallet_new;
    }

    @Override
    public void initTitle() {
        actionbar.setBackground(R.drawable.bg_shadow_blue);
        actionbar.setData("易钱包", R.drawable.ic_left_back_white, null, 0, null, this);
        //actionbar.setNeedTranslucent(true,false);
        actionbar.setTvTitleColor(Color.parseColor("#FEFEFE"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        show(NewWalletActivity.this, "加载中...");
        getUserInfo();
        getBankList();


    }


    /**
     * 必须在二维码支付方式到达后，再请求银行卡列表  否则，listItem 点击事件无法定位数据
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
                            bankListBeanList = result.getData().getList();

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(NewWalletActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(NewWalletActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(NewWalletActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
                    public void onNext(Result<MbpUserVO> mbpUserVOResult) {
                        dismiss();
                        if (mbpUserVOResult.getCode() == 1) {
                            MbpUserVO data = mbpUserVOResult.getData();
                            SctApp.mUserInfoData = mbpUserVOResult.getData();


                            tvWalletSumCoin.setText(subStr(data.getTotal_amount()));

                            tvWalletTodayEarning.setText(data.getProfit_money());
                            tvWalletTotalEarn.setText("累计收益: "+data.getTotal_profit_money()+" USCT");

                            tvWalletRedPacket.setText(data.getGive_money());
                            tvWalletTotalGift.setText("累计赠送: "+data.getTotal_give_money()+" USCT");

                            tvWalletFreeze.setText(data.getFrozen_money());

                            tvWalletUsableNum.setText(data.getAvailable_money());
                            tvWalletEarn.setText(data.getProfit_money());


                        } else if (mbpUserVOResult.getCode() == 2) {
                            ToastUtil.showToast(NewWalletActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(NewWalletActivity.this, mbpUserVOResult.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(NewWalletActivity.this, e.getMessage());
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

    //只有有审核通过的银行卡 的时候才可以提现,否则就去绑卡提示
    private boolean checkIsCanWithdraw() {
        if (bankListBeanList == null || bankListBeanList.size() == 0) {
            return false;
        } else {
            BankVO.ListBean temp = null;
            for (int i = 0; i < bankListBeanList.size(); i++) {
                temp = bankListBeanList.get(i);
                if (temp.getVerifystatus() == 1) {
                    return true;
                }
            }
            return false;
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
        getUserInfo();
    }

    @OnClick({R.id.iv_usable_detail, R.id.tv_wallet_withdraw, R.id.tv_wallet_transfer, R.id.tv_wallet_recharge, R.id.tv_wallet_bring, R.id.iv_earn_detail, R.id.tv_wallet_transfer_balance, R.id.iv_red_packet_detail, R.id.iv_freeze_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_usable_detail:
                //卡片1详情(可用数量)
                launch(MoneyDetailsActivity.class);
                break;
            case R.id.tv_wallet_withdraw:
                //提现
                if (!checkIsCanWithdraw()) {
                    showBindBankCard();
                } else {
                    WithdrawActivity.start(this);
                }
                break;
            case R.id.tv_wallet_transfer:
                //转账
                BalanceTransferActivity.start(this);
                break;
            case R.id.tv_wallet_recharge:
                //USCT充值
                launch(BalanceTransferFromUsdtActivity.class);
                break;
            case R.id.tv_wallet_bring:
                //USCT提币
                BalanceTransferWithdrawToUsdtActivity.start(this);
                break;
            case R.id.iv_earn_detail:
                launch(MoneyTwoDetailsActivity.class);
                break;
            case R.id.tv_wallet_transfer_balance:
                profitSettlement();
                break;
            case R.id.iv_red_packet_detail:
                launch(MoneyThreeDetailsActivity.class);
                break;
            case R.id.iv_freeze_detail:
                launch(MoneyFourDetailsActivity.class);
                break;
        }
    }
    /**
     * 一键转余额
     */
    private void profitSettlement() {
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
                            ToastUtil.showToast(NewWalletActivity.this, "操作成功");
                            getUserInfo();
                        } else {
                            ToastUtil.showToast(NewWalletActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(NewWalletActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //保留两位小数
    private String subStr(String before){
        String after = "";
        String str1 = Util.splitSctCount(before)[0];
        String str2 = Util.splitSctCount(before)[1];
        if (str2.length()>=2){
            str2 = str2.substring(0,2);
        }else {
            str2 = "00";
        }
        after = str1+"."+str2;
        return after;
    }
}
