package com.frico.usct.ui.activity.me.setting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.impl.ActionBarClickListener;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.utils.Prefer;
import com.frico.usct.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements ActionBarClickListener {
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.iv_setting_switch)
    ImageView ivSettingSwitch;
    @BindView(R.id.tv_setting_safety)
    TextView tvSettingSafety;
    @BindView(R.id.tv_setting_about_us)
    TextView tvSettingAboutUs;

    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initTitle() {
        actionbar.setData("设置", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

    }

    @Override
    protected void initData() {
        if (Prefer.getInstance().getRingSwitchIsOpen()){
            ivSettingSwitch.setImageResource(R.drawable.open1);
        }else {
            ivSettingSwitch.setImageResource(R.drawable.close1);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_setting_switch, R.id.tv_setting_safety, R.id.tv_setting_about_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting_switch:
                openOrCloseSwitch();
                break;
            case R.id.tv_setting_safety:
                launch(AccountSaveActivity.class);
                break;
            case R.id.tv_setting_about_us:
                launch(AboutSCTActivity.class);
                break;
        }
    }

    private void openOrCloseSwitch() {
        if (Prefer.getInstance().getRingSwitchIsOpen()) {
            closeRingSwitch();
        } else {
            openRingSwitch();
        }
    }
    private void openRingSwitch() {
        //打开通知铃声开关
        Prefer.getInstance().setRingSwitch(true);
        ivSettingSwitch.setImageResource(R.drawable.open1);
    }

    private void closeRingSwitch() {
        //关闭通知铃声开关
        Prefer.getInstance().setRingSwitch(false);
        ivSettingSwitch.setImageResource(R.drawable.close1);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
