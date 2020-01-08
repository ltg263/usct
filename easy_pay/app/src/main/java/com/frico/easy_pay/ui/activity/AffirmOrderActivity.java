package com.frico.easy_pay.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.DealOrderVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.dialog.BaseIncomeSureDialog;
import com.frico.easy_pay.dialog.PaySureDialog;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.payway.AddZfbActivity;
import com.frico.easy_pay.utils.CopyUtils;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.LuBanUtils;
import com.frico.easy_pay.utils.MatisseUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.UCropUtils;
import com.frico.easy_pay.utils.UiUtils;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class AffirmOrderActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_order_time)
    TextView tvOrderTime;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_deal_count)
    TextView tvDealCount;
    @BindView(R.id.tv_deal_money)
    TextView tvDealMoney;
    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.img_zfb)
    ImageView imgZfb;
    @BindView(R.id.img_wx)
    ImageView imgWx;
    @BindView(R.id.tv_receipt_name)
    TextView tvReceiptName;
    @BindView(R.id.tv_receipt_bank)
    TextView tvReceiptBank;
    @BindView(R.id.tv_receipt_sub_branch)
    TextView tvReceiptSubBranch;
    @BindView(R.id.tv_receipt_bank_no)
    TextView tvReceiptBankNo;
    @BindView(R.id.ll_order_bank)
    LinearLayout llOrderBank;
    @BindView(R.id.tv_receipt_account)
    TextView tvReceiptAccount;
    @BindView(R.id.img_receipt_ewm)
    ImageView imgReceiptEwm;
    @BindView(R.id.ll_order_zfb_wx)
    LinearLayout llOrderZfbWx;
    @BindView(R.id.et_pay_name)
    EditText etPayName;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_name_copy)
    TextView tvNameCopy;
    @BindView(R.id.tv_bank_name_copy)
    TextView tvBankNameCopy;
    @BindView(R.id.tv_sub_branch_copy)
    TextView tvSubBranchCopy;
    @BindView(R.id.tv_bank_no_copy)
    TextView tvBankNoCopy;
    @BindView(R.id.img_ysf)
    ImageView imgYsf;
    @BindView(R.id.img_zsm)
    ImageView imgZsm;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;
    @BindView(R.id.img_ewm)
    ImageView imgEwm;

    private CountDownTimer timer;
    private String OrderNo;
    private int payWay = 0;
    private DealOrderVO dealOrderVO;

    private String imgUrl;

    private String imgPath;
    private int payType;

    @Override
    protected int setLayout() {
        return R.layout.activity_affirm_order;
    }

    @Override
    public void initTitle() {
        OrderNo = getIntent().getStringExtra("orderno");
        payType = getIntent().getIntExtra("payType", 0);
        LogUtils.e("--需要支付的订单号--" + OrderNo);
        actionbar.setData("确认订单", R.drawable.ic_left_back2x, null, 0, "取消订单", this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        imgPath = null;
        imgUrl = null;

    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        tvCancle.setOnClickListener(this);

        tvNameCopy.setOnClickListener(this);
        tvBankNameCopy.setOnClickListener(this);
        tvSubBranchCopy.setOnClickListener(this);
        tvBankNoCopy.setOnClickListener(this);

        imgBank.setOnClickListener(this);
        imgZfb.setOnClickListener(this);
        imgWx.setOnClickListener(this);
        imgYsf.setOnClickListener(this);
        imgZsm.setOnClickListener(this);


        imgReceiptEwm.setOnClickListener(this);
        imgEwm.setOnClickListener(this);
        goPay(OrderNo);
    }

    /**
     * 抢单购买
     *
     * @param advertNo
     */
    public void goPay(String advertNo) {
        show(AffirmOrderActivity.this, "加载中...");
        RetrofitUtil.getInstance().apiService()
                .gopay(advertNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<DealOrderVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<DealOrderVO> result) {
                        LogUtils.e("--确认订单-" + new Gson().toJson(result));
                        dismiss();
                        if (result.getCode() == 1) {
                            dealOrderVO = result.getData();
                            if (dealOrderVO != null) {
                                initTime(dealOrderVO.getDifftime() * 1000);
                                tvOrderNo.setText(dealOrderVO.getOrderno() != null ? dealOrderVO.getOrderno() : "");
                                tvDealCount.setText(dealOrderVO.getAmount() + " USCT");
                                tvDealMoney.setText(dealOrderVO.getCnymoney() + " CNY");

                                if (dealOrderVO.getTradeway().contains(",")) {
                                    List<String> typeList = new ArrayList<>();
                                    String[] split = dealOrderVO.getTradeway().split(",");
                                    for (String s : split) {
                                        typeList.add(s);
                                    }

                                    if (typeList.contains("1")) {
                                        imgBank.setVisibility(View.VISIBLE);
                                    } else {
                                        imgBank.setVisibility(View.GONE);
                                    }

                                    if (typeList.contains("2")) {
                                        imgZfb.setVisibility(View.VISIBLE);
                                    } else {
                                        imgZfb.setVisibility(View.GONE);
                                    }

                                    if (typeList.contains("3")) {
                                        imgWx.setVisibility(View.VISIBLE);
                                    } else {
                                        imgWx.setVisibility(View.GONE);
                                    }
                                    if (typeList.contains("4")) {
                                        imgYsf.setVisibility(View.VISIBLE);
                                    } else {
                                        imgYsf.setVisibility(View.GONE);
                                    }
                                    if (typeList.contains("5")) {
                                        imgZsm.setVisibility(View.VISIBLE);
                                    } else {
                                        imgZsm.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (dealOrderVO.getTradeway().equals("1")) {
                                        imgBank.setVisibility(View.VISIBLE);
                                        imgZfb.setVisibility(View.GONE);
                                        imgWx.setVisibility(View.GONE);
                                        imgYsf.setVisibility(View.GONE);
                                        imgZsm.setVisibility(View.GONE);
                                    } else if (dealOrderVO.getTradeway().equals("2")) {
                                        imgBank.setVisibility(View.GONE);
                                        imgZfb.setVisibility(View.VISIBLE);
                                        imgWx.setVisibility(View.GONE);
                                        imgYsf.setVisibility(View.GONE);
                                        imgZsm.setVisibility(View.GONE);
                                    } else if (dealOrderVO.getTradeway().equals("3")) {
                                        imgBank.setVisibility(View.GONE);
                                        imgZfb.setVisibility(View.GONE);
                                        imgWx.setVisibility(View.VISIBLE);
                                        imgYsf.setVisibility(View.GONE);
                                        imgZsm.setVisibility(View.GONE);
                                    } else if (dealOrderVO.getTradeway().equals("4")) {
                                        imgBank.setVisibility(View.GONE);
                                        imgZfb.setVisibility(View.GONE);
                                        imgWx.setVisibility(View.GONE);
                                        imgYsf.setVisibility(View.VISIBLE);
                                        imgZsm.setVisibility(View.GONE);
                                    } else if (dealOrderVO.getTradeway().equals("5")) {
                                        imgBank.setVisibility(View.GONE);
                                        imgZfb.setVisibility(View.GONE);
                                        imgWx.setVisibility(View.GONE);
                                        imgYsf.setVisibility(View.GONE);
                                        imgZsm.setVisibility(View.VISIBLE);
                                    }
                                }

                                DealOrderVO.TradeinfoBean tradeinfo = dealOrderVO.getTradeinfo();
                                DealOrderVO.TradeinfoBean._$1Bean tradeinfo_$1 = tradeinfo.get_$1();
                                setSelected();
                                if (tradeinfo_$1 != null) {
                                    tvReceiptName.setText(tradeinfo_$1.getAccountname() != null ? tradeinfo_$1.getAccountname() : "");
                                    tvReceiptBank.setText(tradeinfo_$1.getBankname() != null ? tradeinfo_$1.getBankname() : "");
                                    tvReceiptBankNo.setText(tradeinfo_$1.getCardnumber() != null ? tradeinfo_$1.getCardnumber() : "");
                                    tvReceiptSubBranch.setText(tradeinfo_$1.getSubbranch() != null ? tradeinfo_$1.getSubbranch() : "");
                                }
                            }

                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AffirmOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AffirmOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AffirmOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void setSelected() {
        switch (payType) {
            case 0:
                break;
            case 1:
                imgBank.setSelected(true);
                llOrderBank.setVisibility(View.VISIBLE);
                llOrderZfbWx.setVisibility(View.GONE);
                payWay = 1;
                break;
            case 2:
                imgZfb.setSelected(true);
                llOrderBank.setVisibility(View.GONE);
                llOrderZfbWx.setVisibility(View.VISIBLE);
                payWay=2;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$2();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;
            case 3:
                imgWx.setSelected(true);
                llOrderBank.setVisibility(View.GONE);
                llOrderZfbWx.setVisibility(View.VISIBLE);
                payWay=3;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$3();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;
        }
    }

    /**
     * 倒数计时器
     */
    private void initTime(long time) {
        //固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick倒计时完成时被调用
        timer = new CountDownTimer(time, 1000) {

            //固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
            @Override
            public void onTick(long millisUntilFinished) {
                String htmlString = "<font color='#333'>订单剩余付款时间 </font>";
                tvOrderTime.setText(Html.fromHtml(htmlString + formatTime(millisUntilFinished)));
            }

            //倒计时完成时被调用
            @Override
            public void onFinish() {
                tvOrderTime.setText("订单已取消");
                ToastUtil.showToast(AffirmOrderActivity.this, "订单已取消");
                finish();
            }
        };

        timerStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_receipt_ewm:
                ArrayList<String> urlList = new ArrayList<>();
                urlList.add(imgUrl);

                Intent intent = new Intent(AffirmOrderActivity.this, ImagePreviewActivity.class);
                intent.putStringArrayListExtra("imageList", urlList);
                intent.putExtra(Constant.START_ITEM_POSITION, 0);
                intent.putExtra(Constant.START_IAMGE_POSITION, 0);
                startActivity(intent);
                break;
            case R.id.tv_submit:
                if (payWay == 0) {
                    ToastUtil.showToast(AffirmOrderActivity.this, "请选择收款方式");
                    return;
                }

                if (TextUtils.isEmpty(etPayName.getText().toString())) {
                    ToastUtil.showToast(AffirmOrderActivity.this, "请输入付款人姓名");
                    return;
                }

                if (TextUtils.isEmpty(imgPath)) {
                    ToastUtil.showToast(AffirmOrderActivity.this, "请上传凭证");
                    return;
                }
                showPaySureDialog(dealOrderVO.getAmount());
//                SimpleDialog simpleDialog = new SimpleDialog(AffirmOrderActivity.this, "是否已付款?", "提示", "取消", "我已付款", new SimpleDialog.OnButtonClick() {
//                    @Override
//                    public void onNegBtnClick() {
//
//                    }
//
//                    @Override
//                    public void onPosBtnClick() {
//                        UiUtils.runOnUiThread(() -> {
//                            paycomplete();
//                        });
//                    }
//                });
//                simpleDialog.show();
                break;
            case R.id.tv_cancle:
                UiUtils.runOnUiThread(() -> {
                    canclePay();
                });
                break;
            case R.id.img_bank:
                if (!imgBank.isSelected()) {
                    imgBank.setSelected(true);
                    llOrderBank.setVisibility(View.VISIBLE);
                    llOrderZfbWx.setVisibility(View.GONE);
                }

                if (imgZfb.getVisibility() == View.VISIBLE) {
                    imgZfb.setSelected(false);
                }

                if (imgWx.getVisibility() == ViewGroup.VISIBLE) {
                    imgWx.setSelected(false);
                }
                if (imgYsf.getVisibility() == ViewGroup.VISIBLE) {
                    imgYsf.setSelected(false);
                }
                if (imgZsm.getVisibility() == ViewGroup.VISIBLE) {
                    imgZsm.setSelected(false);
                }


                payWay = 1;
                break;
            case R.id.img_zfb:
                if (!imgZfb.isSelected()) {
                    imgZfb.setSelected(true);
                    llOrderBank.setVisibility(View.GONE);
                    llOrderZfbWx.setVisibility(View.VISIBLE);
                }

                if (imgBank.getVisibility() == View.VISIBLE) {
                    imgBank.setSelected(false);
                }

                if (imgWx.getVisibility() == ViewGroup.VISIBLE) {
                    imgWx.setSelected(false);
                }

                if (imgYsf.getVisibility() == ViewGroup.VISIBLE) {
                    imgYsf.setSelected(false);
                }
                if (imgZsm.getVisibility() == ViewGroup.VISIBLE) {
                    imgZsm.setSelected(false);
                }

                payWay = 2;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$2();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.img_wx:
                if (!imgWx.isSelected()) {
                    imgWx.setSelected(true);
                    llOrderBank.setVisibility(View.GONE);
                    llOrderZfbWx.setVisibility(View.VISIBLE);
                }

                if (imgBank.getVisibility() == View.VISIBLE) {
                    imgBank.setSelected(false);
                }

                if (imgZfb.getVisibility() == ViewGroup.VISIBLE) {
                    imgZfb.setSelected(false);
                }

                if (imgYsf.getVisibility() == ViewGroup.VISIBLE) {
                    imgYsf.setSelected(false);
                }
                if (imgZsm.getVisibility() == ViewGroup.VISIBLE) {
                    imgZsm.setSelected(false);
                }

                payWay = 3;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$3();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.img_ysf:
                if (!imgYsf.isSelected()) {
                    imgYsf.setSelected(true);
                    llOrderBank.setVisibility(View.GONE);
                    llOrderZfbWx.setVisibility(View.VISIBLE);
                }

                if (imgBank.getVisibility() == View.VISIBLE) {
                    imgBank.setSelected(false);
                }

                if (imgZfb.getVisibility() == ViewGroup.VISIBLE) {
                    imgZfb.setSelected(false);
                }

                if (imgWx.getVisibility() == ViewGroup.VISIBLE) {
                    imgWx.setSelected(false);
                }

                if (imgZsm.getVisibility() == ViewGroup.VISIBLE) {
                    imgZsm.setSelected(false);
                }


                payWay = 4;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$4();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;

            case R.id.img_zsm:
                if (!imgZsm.isSelected()) {
                    imgZsm.setSelected(true);
                    llOrderBank.setVisibility(View.GONE);
                    llOrderZfbWx.setVisibility(View.VISIBLE);
                }

                if (imgBank.getVisibility() == View.VISIBLE) {
                    imgBank.setSelected(false);
                }

                if (imgZfb.getVisibility() == ViewGroup.VISIBLE) {
                    imgZfb.setSelected(false);
                }
                if (imgYsf.getVisibility() == ViewGroup.VISIBLE) {
                    imgYsf.setSelected(false);
                }


                payWay = 5;
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dealOrderVO != null) {
                            DealOrderVO.TradeinfoBean._$2Bean bean = dealOrderVO.getTradeinfo().get_$5();
                            if (bean != null) {
                                tvReceiptAccount.setText(bean.getAccountname() != null ? bean.getAccountname() : "");
                                if (bean.getImg() != null) {
                                    imgUrl = bean.getImg();
                                    Glide.with(AffirmOrderActivity.this).asBitmap().load(bean.getImg()).into(imgReceiptEwm);
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.tv_name_copy:
                CopyUtils.copyText(AffirmOrderActivity.this, "text", tvReceiptName.getText().toString());
                ToastUtil.showToast(this, "已复制到剪切板");
                break;
            case R.id.tv_bank_name_copy:
                CopyUtils.copyText(AffirmOrderActivity.this, "text", tvReceiptBank.getText().toString());
                ToastUtil.showToast(this, "已复制到剪切板");
                break;
            case R.id.tv_sub_branch_copy:
                CopyUtils.copyText(AffirmOrderActivity.this, "text", tvReceiptSubBranch.getText().toString());
                ToastUtil.showToast(this, "已复制到剪切板");
                break;
            case R.id.tv_bank_no_copy:
                CopyUtils.copyText(AffirmOrderActivity.this, "text", tvReceiptBankNo.getText().toString());
                ToastUtil.showToast(this, "已复制到剪切板");
                break;
            case R.id.img_ewm:
                if (EasyPermissions.hasPermissions(this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(AffirmOrderActivity.this, IMAGE_PICKER_EWM, selected_num);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 取消付款
     */
    private void canclePay() {
        if (dealOrderVO == null)
            return;

        show(AffirmOrderActivity.this, "取消中...");
        RetrofitUtil.getInstance().apiService()
                .paycancle(dealOrderVO.getOrderno())
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
                            RxBus.get().post(BusAction.DEAL_LIST, "");
                            RxBus.get().post(BusAction.DEAL_ORDER_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AffirmOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AffirmOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AffirmOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 点击我已付款
     *
     * @param transferimage 凭证图片url
     */
    private void paycomplete(String paypassword, String transferimage) {
        if (dealOrderVO == null)
            return;

        show(AffirmOrderActivity.this, "确定中...");
        RetrofitUtil.getInstance().apiService()
                .paycomplete(dealOrderVO.getOrderno(), payWay, etPayName.getText().toString(), paypassword, transferimage)
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
                            ToastUtil.showToast(AffirmOrderActivity.this, "提交成功");
                            RxBus.get().post(BusAction.DEAL_LIST, "");
                            RxBus.get().post(BusAction.DEAL_ORDER_LIST, "");
                            RxBus.get().post(BusAction.REFRESH_MONEY, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AffirmOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(AffirmOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AffirmOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public String formatTime(long millisecond) {
        int minute;//分钟
        int second;//秒数
        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);
        if (minute < 10) {
            if (second < 10) {
                return "<font color='#ff9f49'> " + "0" + minute + " </font>" + "<font color='#333'> 分 </font>" + "<font color='#ff9f49'> " + "0" + second + " </font>" + "<font color='#333'> 秒 </font>";
            } else {
                return "<font color='#ff9f49'> " + "0" + minute + " </font>" + "<font color='#333'> 分 </font>" + "<font color='#ff9f49'> " + second + " </font>" + "<font color='#333'> 秒 </font>";
            }
        } else {
            if (second < 10) {
                return "<font color='#ff9f49'> " + minute + " </font>" + "<font color='#333'> 分 </font>" + "<font color='#ff9f49'> " + "0" + second + " </font>" + "<font color='#333'> 秒 </font>";
            } else {
                return "<font color='#ff9f49'> " + minute + " </font>" + "<font color='#333'> 分 </font>" + "<font color='#ff9f49'> " + second + " </font>" + "<font color='#333'> 秒 </font>";
            }
        }
    }

    /**
     * 取消倒计时
     */
    public void timerCancel() {
        if (timer != null)
            timer.cancel();
    }

    /**
     * 开始倒计时
     */
    public void timerStart() {
        if (timer != null)
            timer.start();
    }


    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        SimpleDialog simpleDialog = new SimpleDialog(AffirmOrderActivity.this, "是否取消该订单?", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
            @Override
            public void onNegBtnClick() {

            }

            @Override
            public void onPosBtnClick() {
                UiUtils.runOnUiThread(() -> {
                    canclePay();
                });
            }
        });
        simpleDialog.show();
    }

    private PaySureDialog mPaySureDialog;

    private void showPaySureDialog(String count) {
        if (mPaySureDialog == null) {
            mPaySureDialog = new PaySureDialog(this);
        }
        if (!mPaySureDialog.isShowing()) {
            mPaySureDialog.show();
        }
        mPaySureDialog.initViewData(count);
        mPaySureDialog.setOnNotifyListener(new BaseIncomeSureDialog.NotifyDialogListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onSure(String pw) {
                //启动上传图片
                uploadImg(pw);

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private static final int selected_num = 1;
    private static final int PERMISSION_CAMERA = 110;
    private static final int IMAGE_PICKER_EWM = 105;
    private boolean mIsReSelected;//是否重新选择了图片

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case IMAGE_PICKER_EWM:
                        Uri uri = Matisse.obtainResult(data).get(0);
                        Uri destinationUri = null;
                        try {
                            destinationUri = Uri.fromFile(File.createTempFile("crop", ".jpg"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        UCropUtils.cropImg(AffirmOrderActivity.this, uri, destinationUri);
                        break;
                    case UCrop.REQUEST_CROP:
                        Uri croppedFileUri = UCrop.getOutput(data);
                        UiUtils.runOnUiThread(() -> compressImg(croppedFileUri));
                        break;
                    case UCrop.RESULT_ERROR:
                        Toast.makeText(AffirmOrderActivity.this, "图片裁切失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }


    /**
     * 压缩图片
     *
     * @param uri
     */
    private void compressImg(Uri uri) {
        LuBanUtils.compressImg(AffirmOrderActivity.this, uri, new LuBanUtils.OnMyCompressListener() {
                    @Override
                    public void onSuccess(final File file) {
                        mIsReSelected = true;
                        imgPath = file.getPath();
                        LogUtils.e("压缩后的图片路径---" + imgPath);
                        setImg(imgEwm);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AffirmOrderActivity.this, "图片压缩失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /**
     * 显示选择的图片
     *
     * @param imgView
     */
    private void setImg(ImageView imgView) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String drawableUrl = ImageDownloader.Scheme.FILE.wrap(imgPath);
                    ImageLoader.getInstance().displayImage(drawableUrl, imgView);
                } catch (Exception mE) {
                    return;
                }
            }
        });
    }

    /**
     * 上传单张图片
     */
    private void uploadImg(String pw) {
        showNoTips(this);
        File file = new File(imgPath);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dirtype", toRequestBody("4"));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的name是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitUtil.getInstance().apiService()
                .uploadimg(body, map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Result<String> result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            //图片上传成功
                            if (!TextUtils.isEmpty(result.getData())) {
                                paycomplete(pw, result.getData());
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(AffirmOrderActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            dismiss();
                            ToastUtil.showToast(AffirmOrderActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(AffirmOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dismiss();
                    }
                });
    }

    /**
     * 创建请求体
     *
     * @param value
     * @return
     */
    private RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

}
