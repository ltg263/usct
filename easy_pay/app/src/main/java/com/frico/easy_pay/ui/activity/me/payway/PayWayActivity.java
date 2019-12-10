package com.frico.easy_pay.ui.activity.me.payway;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayWayActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {


    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.tv_pay_wx)
    TextView tvPayWx;
    @BindView(R.id.tv_pay_zfb)
    TextView tvPayZfb;


    @Override
    protected int setLayout() {
        return R.layout.activity_pay_way;
    }

    @Override
    public void initTitle() {
        actionbar.setData("收款方式", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvBank.setOnClickListener(this);
        tvPayZfb.setOnClickListener(this);
        tvPayWx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bank:
                launch(BankListActivity.class);
                break;
            case R.id.tv_pay_wx:
                //微信二维码
                launch(PayWayListActivity.class);
                break;
            case R.id.tv_pay_zfb:
                //支付宝二维码

                break;
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
}
