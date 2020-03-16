package com.frico.usct.ui.activity.home;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.DecimalInputTextWatcher;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.frico.usct.utils.UiUtils;
import com.frico.usct.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PublishAdActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_deal_count)
    EditText etDealCount;
    @BindView(R.id.et_min_deal)
    EditText etMinDeal;
    @BindView(R.id.et_max_deal)
    EditText etMaxDeal;
    @BindView(R.id.img_bank)
    ImageView imgBank;
    @BindView(R.id.img_zfb)
    ImageView imgZfb;
    @BindView(R.id.img_wx)
    ImageView imgWx;
    @BindView(R.id.tv_put_all)
    TextView tvPutAll;
    @BindView(R.id.img_ysf)
    ImageView imgYsf;
    @BindView(R.id.img_zsm)
    ImageView imgZsm;
    @BindView(R.id.ll_bottom)
    RelativeLayout llBottom;

    private List<String> strList = new ArrayList<>();
    private String curMax = "0.0000";

    @Override
    protected int setLayout() {
        return R.layout.activity_publish_ad;
    }

    @Override
    public void initTitle() {
        actionbar.setData("卖出", R.drawable.ic_left_back2x, null, 0, null, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        etDealCount.addTextChangedListener(new DecimalInputTextWatcher(etDealCount, 15, 2));
        etMinDeal.addTextChangedListener(new DecimalInputTextWatcher(etMinDeal, 15, 2));
        etMaxDeal.addTextChangedListener(new DecimalInputTextWatcher(etMaxDeal, 15, 2));
    }

    @Override
    protected void initData() {
        tvSubmit.setOnClickListener(this);
        imgBank.setOnClickListener(this);
        imgZfb.setOnClickListener(this);
        imgWx.setOnClickListener(this);
        imgYsf.setOnClickListener(this);
        imgZsm.setOnClickListener(this);
        tvPutAll.setOnClickListener(this);

        getAdMax();
    }

    private void getAdMax() {
        show(PublishAdActivity.this, "加载中...");
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
                        LogUtils.e("--获取用户信息--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            MbpUserVO data = result.getData();
                            SctApp.mUserInfoData = result.getData();
                            curMax = data.getMax();
                            etDealCount.setHint("最大可交易数量 " + data.getMax());
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PublishAdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PublishAdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PublishAdActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_put_all:
                etDealCount.setText(curMax);
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(etDealCount.getText().toString())) {
                    ToastUtil.showToast(PublishAdActivity.this, "请输入交易数量");
                    return;
                }

                if (TextUtils.isEmpty(etMinDeal.getText().toString())) {
                    ToastUtil.showToast(PublishAdActivity.this, "请输入最小交易数量");
                    return;
                }

                if (TextUtils.isEmpty(etMaxDeal.getText().toString())) {
                    ToastUtil.showToast(PublishAdActivity.this, "请输入最大交易数量");
                    return;
                }

                if (strList.size() == 0) {
                    ToastUtil.showToast(PublishAdActivity.this, "请选择支持的收款方式");
                    return;
                }
                //提交更改
                sendAd();
                break;
            case R.id.img_bank:
                if (imgBank.isSelected()) {
                    imgBank.setSelected(false);
                } else {
                    imgBank.setSelected(true);
                }

                if (!strList.contains("1")) {
                    strList.add("1");
                } else {
                    strList.remove("1");
                }
                break;
            case R.id.img_zfb:
                if (imgZfb.isSelected()) {
                    imgZfb.setSelected(false);
                } else {
                    imgZfb.setSelected(true);
                }

                if (!strList.contains("2")) {
                    strList.add("2");
                } else {
                    strList.remove("2");
                }
                break;
            case R.id.img_wx:
                if (imgWx.isSelected()) {
                    imgWx.setSelected(false);
                } else {
                    imgWx.setSelected(true);
                }

                if (!strList.contains("3")) {
                    strList.add("3");
                } else {
                    strList.remove("3");
                }
                break;

            case R.id.img_ysf:
                if (imgYsf.isSelected()) {
                    imgYsf.setSelected(false);
                } else {
                    imgYsf.setSelected(true);
                }

                if (!strList.contains("4")) {
                    strList.add("4");
                } else {
                    strList.remove("4");
                }
                break;
            case R.id.img_zsm:
                if (imgZsm.isSelected()) {
                    imgZsm.setSelected(false);
                } else {
                    imgZsm.setSelected(true);
                }

                if (!strList.contains("5")) {
                    strList.add("5");
                } else {
                    strList.remove("5");
                }
                break;
            default:
                break;
        }
    }

    private void sendAd() {
        //输入的 交易数量 一定是小于等于 可交易数量的
        // 最大交易数量 小于等于 输入的卖币数量
        //最小交易数量 小于等于 最大交易数量
        double curMaxCount = TextUtils.isEmpty(curMax) ? 0 : Double.parseDouble(curMax);
        double dealCount = 0;//交易金额
        dealCount = TextUtils.isEmpty(etDealCount.getText().toString()) ? 0 : Double.parseDouble(etDealCount.getText().toString().trim());
        double minCount = 0;//最小金额
        minCount = TextUtils.isEmpty(etMinDeal.getText().toString()) ? 0 : Double.parseDouble(etMinDeal.getText().toString().trim());
        double maxCount = 0;//最大金额
        maxCount = TextUtils.isEmpty(etMaxDeal.getText().toString()) ? 0 : Double.parseDouble(etMaxDeal.getText().toString().trim());

        if (dealCount <= curMaxCount) {
            //输入的交易数量小于等于最大可交易数量
            if (maxCount <= dealCount) {
                if (minCount <= maxCount) {
                    // 提交数据到服务器
                    if (minCount >= 1) {
                        doCreateAd(dealCount + "", minCount + "", maxCount + "");
                    } else {
                        //最少交易1sct
                        ToastUtil.showToast(this, "最小交易数量为 1 usct！");
                        return;
                    }
                } else {
                    //最小数量 需要小于等于 最大数量
                    ToastUtil.showToast(this, "最小数量不能大于最大数量！");
                    return;
                }
            } else {
                //最大交易数量不可大于本次卖币数量
                ToastUtil.showToast(this, "最大数量不能大于交易数量！");
                return;
            }
        } else {
            ToastUtil.showToast(this, "交易数量不能大于可交易数量！");
            return;
        }

    }

    private void doCreateAd(String dealCount, String minCount, String maxCount) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : strList) {
            stringBuffer.append(s);
            if (i < strList.size() - 1) {
                stringBuffer.append(",");
            }
            i++;
        }

        LogUtils.e("--收款方式---" + stringBuffer.toString());

        UiUtils.runOnUiThread(() -> {
            publishAd(stringBuffer.toString(), dealCount, minCount, maxCount);
        });
    }

    /**
     * 发布广告
     *
     * @param payStr
     */
    private void publishAd(String payStr, String dealCount, String minCount, String maxCount) {
        show(PublishAdActivity.this, "发布中...");
        RetrofitUtil.getInstance().apiService()
                .publishadvert(dealCount, minCount, maxCount, payStr)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        LogUtils.e("-发布广告--" + new Gson().toJson(result));
                        dismiss();
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(PublishAdActivity.this, "发布成功");
                            RxBus.get().post(BusAction.AD_LIST, "");
                            finish();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PublishAdActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PublishAdActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PublishAdActivity.this, e.getMessage());
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
}
