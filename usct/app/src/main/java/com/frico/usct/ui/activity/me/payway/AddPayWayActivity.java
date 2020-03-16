package com.frico.usct.ui.activity.me.payway;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPayWayActivity extends BaseActivity implements ActionBarClickListener, View.OnClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_pay_bank)
    TextView tvPayBank;
    @BindView(R.id.tv_pay_wx)
    TextView tvPayWx;
    @BindView(R.id.tv_pay_zfb)
    TextView tvPayZfb;
    @BindView(R.id.tv_pay_zsm)
    TextView tvPayZsm;
    @BindView(R.id.tv_pay_ysf)
    TextView tvPayYsf;

    @Override
    protected int setLayout() {
        return R.layout.activity_add_pay_way;
    }

    @Override
    public void initTitle() {
        actionbar.setData("添加收款方式", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvPayBank.setOnClickListener(this);
        tvPayWx.setOnClickListener(this);
        tvPayZfb.setOnClickListener(this);
        tvPayZsm.setOnClickListener(this);
        tvPayYsf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay_bank:
                launch(AddBankActivity.class);
                break;
            case R.id.tv_pay_wx:
//                startActivity(new Intent(AddPayWayActivity.this, AddZfbActivity.class).putExtra(Constant.TITLE, "添加微信码"));
                AddZfbActivity.start(AddPayWayActivity.this,null,"微信码",PayWayListActivity.PAY_WAY_WX);
                break;
            case R.id.tv_pay_zfb:
//                startActivity(new Intent(AddPayWayActivity.this, AddZfbActivity.class).putExtra(Constant.TITLE, "添加支付宝码"));
                AddZfbActivity.start(AddPayWayActivity.this,null,"支付宝码",PayWayListActivity.PAY_WAY_ZFB);
                break;
            case R.id.tv_pay_zsm:
//                startActivity(new Intent(AddPayWayActivity.this, AddZfbActivity.class).putExtra(Constant.TITLE, "添加微信赞赏码"));
                AddZfbActivity.start(AddPayWayActivity.this,null,"微信赞赏码",PayWayListActivity.PAY_WAY_ZSM);
                break;
            case R.id.tv_pay_ysf:
//                startActivity(new Intent(AddPayWayActivity.this, AddZfbActivity.class).putExtra(Constant.TITLE, "添加云闪付码"));
                AddZfbActivity.start(AddPayWayActivity.this,null,"聚合码",PayWayListActivity.PAY_WAY_YSF);
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
