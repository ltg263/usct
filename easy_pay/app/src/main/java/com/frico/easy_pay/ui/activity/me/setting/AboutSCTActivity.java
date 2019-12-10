package com.frico.easy_pay.ui.activity.me.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;

public class AboutSCTActivity extends BaseActivity implements ActionBarClickListener {

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    private String versionName;

    @Override
    protected int setLayout() {
        return R.layout.activity_about_sct;
    }

    @Override
    public void initTitle() {
        actionbar.setData("关于我们", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        tvVersion.setText("当前版本:V" + getVersionName());
        tvCopy.setText("Copyright ©2019 USCT Co.Ltd \n All Right Reserved");
    }

    /**
     * 优化，通过类级变量缓存版本号，不必每次都从包管理器获取
     *
     * @return
     */
    private String getVersionName() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            versionName = "1.0.1";
        }
        return versionName;
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {

    }
}
