package com.frico.easy_pay.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.BankVO;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.WithdrawSureDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.DecimalInputTextWatcher;
import com.frico.easy_pay.utils.MathUtil;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.frico.easy_pay.config.Constant.RATE;

public class WithdrawActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_user_name)
    TextView tvBankUserName;
    @BindView(R.id.tv_bank_code)
    TextView tvBankCode;
    @BindView(R.id.tv_withdraw_switch_bank_card)
    TextView tvWithdrawSwitchBankCard;
    @BindView(R.id.tv_withdraw_can_withdraw_sct_count)
    TextView tvWithdrawCanWithdrawSctCount;
    @BindView(R.id.tv_withdraw_can_withdraw_money)
    TextView tvWithdrawCanWithdrawMoney;
    @BindView(R.id.et_withdraw_input_money)
    EditText etWithdrawInputMoney;
    @BindView(R.id.tv_withdraw_select_all)
    TextView tvWithdrawSelectAll;
    @BindView(R.id.tv_withdraw_count_and_rate_intro)
    TextView tvWithdrawCountAndRateIntro;
    @BindView(R.id.btn_withdraw_bottom_commit)
    Button btnWithdrawBottomCommit;
    @BindView(R.id.tv_withdraw_bottom_intro)
    TextView tvWithdrawBottomIntro;


    private MbpUserVO mUserInfoData;
    private List<BankVO.ListBean> mBankList;
    private BankVO.ListBean mCurrentBank;
    private String mPassword;
    private WithdrawSureDialog mWithdrawSureDialog;
    private BnakListAdapter mAdapter;
//
    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, WithdrawActivity.class));
    }

    @Override
    protected int setLayout() {
        return R.layout.act_withdraw;
    }

    @Override
    public void initTitle() {
        actionbar.setData("提现", R.drawable.ic_left_back2x, null, 0, "提现记录", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                //提现记录
                WithdrawOrderActivity.start(WithdrawActivity.this);
            }
        });
        etWithdrawInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshBottomBtn();
            }
        });
        btnWithdrawBottomCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交订单
                showPassword(etWithdrawInputMoney.getText().toString().trim());
            }
        });
        tvWithdrawSwitchBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBankList == null){
                    getList();
                }else{
                    showSelectBankDg(mBankList);
                }
            }
        });

        //输入总长度15位，小数2位
        etWithdrawInputMoney.addTextChangedListener(new DecimalInputTextWatcher(etWithdrawInputMoney, 15, 2));
    }

    private void showPassword(String withdrawCount){
        double withmoneyMoneySctDouble = Double.parseDouble(withdrawCount);
        if(withmoneyMoneySctDouble < mUserInfoData.getAcq_withdraw_amount()){
            ToastUtil.showToast(this,"单次提现数量必须大于"+ mUserInfoData.getAcq_withdraw_amount()+" usct");
            return;
        }
        if(mWithdrawSureDialog == null){
            mWithdrawSureDialog = new WithdrawSureDialog(this);
        }
        if(! mWithdrawSureDialog.isShowing()) {
            mWithdrawSureDialog.show();
        }
        mWithdrawSureDialog.initViewData(withdrawCount);
        mWithdrawSureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener(){
            @Override
            public void onCancel() {
                dismiss();
            }

            @Override
            public void onSure(String pw) {
                // 确定
                commit(pw);
            }
        });
    }
    
    private void commit(String paypassword){
        String withmoneySct = etWithdrawInputMoney.getText().toString();
        String bankcardId = mCurrentBank.getId();

        withdraw(withmoneySct,bankcardId,paypassword);
    }

    private void refreshBottomBtn() {
        String inputStr = etWithdrawInputMoney.getText().toString();
        double inputDouble = 0;
        if (TextUtils.isEmpty(inputStr)) {
            btnWithdrawBottomCommit.setEnabled(false);
        } else {
            btnWithdrawBottomCommit.setEnabled(true);
            inputDouble = Double.parseDouble(inputStr);
        }
        setWithdrawIntro(inputDouble, mUserInfoData.getRate());
    }

    @Override
    protected void initData() {
        getData();
        getList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 获取个人信息
     */
    public void getData() {
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
                        if (result.getCode() == 1) {
                            mUserInfoData = result.getData();
                            SctApp.mUserInfoData = result.getData();
                            initViewUserData(mUserInfoData);
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(WithdrawActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(WithdrawActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WithdrawActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 提现
     */
    public void withdraw(String withmoney,String bankcard,String paypassword) {
        RetrofitUtil.getInstance().apiService()
                .withdraw(withmoney,bankcard,paypassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            //提现成功
                            ToastUtil.showToast(WithdrawActivity.this,"提现成功");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(WithdrawActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(WithdrawActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WithdrawActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取银行卡列表
     */
    private void getList() {
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
                            createCanUseBankCard(result.getData().getList());
                            if (mBankList.size() > 0) {
                                initBankDefault(mBankList.get(0));
                                mCurrentBank = mBankList.get(0);
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(WithdrawActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(WithdrawActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WithdrawActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        tvWithdrawSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etWithdrawInputMoney.setText(mUserInfoData.getAvailable_money());
            }
        });
    }

    /**
     * 初始化banklist数据
     * 只要审核通过的银行卡数据
     * @param bankList
     */
    private void createCanUseBankCard(List<BankVO.ListBean> bankList){
        List<BankVO.ListBean> resultBankList = new ArrayList<>();
        if(bankList == null){
            mBankList = resultBankList;
            return;
        }
        BankVO.ListBean temp = null;
        for(int i = 0;i< bankList.size();i++){
            temp = bankList.get(i);
            if(temp.getVerifystatus() == 1){
                resultBankList.add(temp);
            }
        }
        mBankList = resultBankList;
    }


    private void initViewUserData(MbpUserVO userInfoData) {
        String withdrawStr = userInfoData.getAvailable_money() + sctToRmbStr(userInfoData.getAvailable_money());
        tvWithdrawCanWithdrawMoney.setText(withdrawStr);
        // 设置剩余提现次数
        setWithdrawIntro(0, userInfoData.getRate());
        tvWithdrawBottomIntro.setText("*说明：单次提现最小"+ userInfoData.getAcq_withdraw_amount()+" USCT。");
    }

    private void setWithdrawIntro(double inputMoney, double rate) {
        double withdrawRateMoney = MathUtil.mul(inputMoney,rate);
        double withdrawRateMoneyRmb = MathUtil.mul(withdrawRateMoney, Constant.RATE);
        double withdrawMoneyRmb = MathUtil.mul(inputMoney, Constant.RATE);
        String exchange = getResources().getString(R.string.withdraw_rate_count_intro,withdrawMoneyRmb +"", doubleFormate(withdrawRateMoneyRmb+"") + "");
        tvWithdrawCountAndRateIntro.setText(Html.fromHtml(exchange));
    }

    private void initBankDefault(BankVO.ListBean defaultBank) {
        if (defaultBank != null) {
            tvBankName.setText(defaultBank.getBankname());
            tvBankUserName.setText(defaultBank.getAccountname());
            tvBankCode.setText(hintFrountNumber(defaultBank.getCardnumber()));
        }
    }




    private String sctToRmbStr(String sctCountStr) {
        return "";

//
//        if (!TextUtils.isEmpty(sctCountStr)) {
//            try {
//                double sctCountDouble = Double.parseDouble(sctCountStr);
//                return " ≈ " + MathUtil.mul(sctCountDouble, RATE) + "元";
//            } catch (NumberFormatException e) {
//                return "";
//            }
//        } else {
//            return "";
//        }
    }

    private String sctToRmb(String sctCountStr){
        if (!TextUtils.isEmpty(sctCountStr)) {
            try {
                double sctCountDouble = Double.parseDouble(sctCountStr);
                return  MathUtil.mul(sctCountDouble, RATE) +"";
            } catch (NumberFormatException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    private String hintFrountNumber(String bankNumber) {
        int showLastCount = 4;//只显示后四位
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(bankNumber)) {
            char[] bankNumberArray = bankNumber.toCharArray();
            int length = bankNumber.length() - 1;
            for (int i = length; i <= length && i >= 0; i--) {
                if (i < 4) {
                    sb.append(bankNumberArray[length - i]);
                } else {
                    sb.append("*");
                }
                if (i % 4 == 0) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }


    public double doubleFormate(String doubleStr) {
        BigDecimal bg = new BigDecimal(doubleStr);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }





    /**
     * 筛选银行卡弹框
     */
    private void showSelectBankDg(List<BankVO.ListBean> listBank) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(WithdrawActivity.this);
        View commentView = View.inflate(WithdrawActivity.this, R.layout.dialog_bank_list_select, null);
        ImageView closeBtn = commentView.findViewById(R.id.iv_select_bank_close_btn);
        ListView listView = commentView.findViewById(R.id.listview);


        bottomSheetDialog.setContentView(commentView);

        ViewGroup parent = (ViewGroup) commentView.getParent();
        parent.setBackgroundResource(R.color.transparent);

        FrameLayout bottomSheet = bottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            int peekHeight = getResources().getDisplayMetrics().heightPixels;
            int height = peekHeight / 2;
            //设置弹窗最大高度
            layoutParams.height = height;
            bottomSheet.setLayoutParams(layoutParams);
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(height);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetDialog != null)
                    bottomSheetDialog.dismiss();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //选择一张卡了
                mCurrentBank = mAdapter.getItem(i);
                mAdapter.setSelectedIndex(i);
                initBankDefault(mCurrentBank);
                if (bottomSheetDialog != null) {
                    bottomSheetDialog.dismiss();
                }
            }
        });
        if(mAdapter == null) {
            mAdapter = new BnakListAdapter(this, listBank);
        }
        listView.setAdapter(mAdapter);
        bottomSheetDialog.show();
    }


    private void showBankCardListDialog(){

    }


    class BnakListAdapter extends BaseAdapter {
        private Activity activity;
        private List<BankVO.ListBean> bankList;
        private int selectedIndex;

        public void setSelectedIndex(int selectedIndex){
            this.selectedIndex = selectedIndex;
        }

        public BnakListAdapter(Activity activity,List<BankVO.ListBean> bankList){
            this.activity = activity;
            this.bankList = bankList;
        }
        @Override
        public int getCount() {
            return bankList.size();
        }

        @Override
        public BankVO.ListBean getItem(int i) {
            return bankList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewItem = LayoutInflater.from(activity).inflate(R.layout.item_dialog_bank_list_select,null);
            BankVO.ListBean item = getItem(i);
            ImageView iv = viewItem.findViewById(R.id.iv_bank_icon);
            TextView bankName = viewItem.findViewById(R.id.tv_bank_name);
            TextView bankUser = viewItem.findViewById(R.id.tv_bank_user_name);
            ImageView ivSelect = viewItem.findViewById(R.id.iv_item_dialog_bank_select);
            if(i == selectedIndex){
                ivSelect.setVisibility(View.VISIBLE);
            }else{
                ivSelect.setVisibility(View.GONE);
            }
            bankName.setText(item.getBankname()+"("+getLastNumber(item.getCardnumber())+")");
            bankUser.setText(item.getAccountname());
            return viewItem;
        }

        private String getLastNumber(String bankCard){
            if(!TextUtils.isEmpty(bankCard)){
                int length = bankCard.length() - 1;
                String returnCode = bankCard.substring(length - 3,length + 1);
                return returnCode;
            }else{
                return "";
            }
        }
    }

}
