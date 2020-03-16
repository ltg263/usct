package com.frico.usct.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frico.usct.ui.activity.fragment.HomeLobbyFragment;
import com.frico.usct.ui.activity.me.NewMeFragment;
import com.google.gson.Gson;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.core.entity.StartVO;
import com.frico.usct.core.entity.UpdateVO;
import com.frico.usct.core.utils.BusAction;
import com.frico.usct.core.utils.ConfigUtil;
import com.frico.usct.dialog.NotificationDialog;
import com.frico.usct.dialog.SimpleDialog;
import com.frico.usct.service.HelperNotificationListenerService;
import com.frico.usct.service.NotificationUtils;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.fragment.NewHomeFragment;
import com.frico.usct.ui.activity.home.MyAdvertisingFragment;
import com.frico.usct.ui.activity.home.OrderFragment;
import com.frico.usct.ui.activity.income.IncomeFragment;
import com.frico.usct.ui.activity.me.MeFragment;
import com.frico.usct.ui.activity.response.ScoketVO;
import com.frico.usct.ui.activity.update.AutoUpgradeClient;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.Prefer;
import com.frico.usct.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.iv_deal)
    ImageView ivDeal;
    @BindView(R.id.rl_deal)
    RelativeLayout rlDeal;
    @BindView(R.id.iv_income)
    ImageView ivIncome;
    @BindView(R.id.rl_income)
    RelativeLayout rlIncome;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.tv_me)
    TextView tvMe;
    @BindView(R.id.rl_me)
    RelativeLayout rlMe;
    @BindView(R.id.tv_deal)
    TextView tvDeal;
    @BindView(R.id.tv_income)
    TextView tvIncome;
    @BindView(R.id.iv_trader)
    ImageView ivTrader;
    @BindView(R.id.tv_trader)
    TextView tvTrader;
    @BindView(R.id.rl_trader)
    RelativeLayout rlTrader;

    private long exitTime = 0;
    private Fragment nowFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
//    private HomeFragment homeFragment;
    private NewHomeFragment newHomeFragment;
    private MyAdvertisingFragment traderFragment;
    private OrderFragment orderFragment;
    private IncomeFragment incomeFragment;
    private MeFragment meFragment;
    private SimpleDialog simpleDialog;

    private int mSelectIndex = -1;
    private NewMeFragment newMeFragment;

    private HomeLobbyFragment lobbyFragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initTitle() {


        //进去首页后，启动通知栏监听服务
        if(! TextUtils.isEmpty(Prefer.getInstance().getSocketIp())) {
            ConfigUtil.resetSocketIp(Prefer.getInstance().getSocketIp());
        }
        startService(new Intent(this, HelperNotificationListenerService.class));


        mSelectIndex = getIntent().getIntExtra("index", -1);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //已经授权
            //do something
            ensureCollectorRunning();
        }
        LogUtils.e("---token-" + Prefer.getInstance().getToken());
        checkUpdate();
        setWifiDormancy(this);
    }

    @Override
    protected void onResume() {
        mSelectIndex = SctApp.mHomeSelectIndex;
        if (mSelectIndex >= 0) {
            showBuyAndPay(true);
        }
        SctApp.mHomeSelectIndex = -1;
        getAdImage();
        getNoticeEnable();

//        getUserInfo();


        showNotification();
       /* if(nowFragment == incomeFragment){
            incomeFragment.getData();
        }*/
       if (nowFragment== newHomeFragment){
           newHomeFragment.getData();
       }
        super.onResume();
    }

    private void showNotification() {
        if (SctApp.mNotificationData != null) {
            showNotificationDialog(SctApp.mNotificationData);
        }
    }

    /**
     * 获取线上广告图片
     */
    private void getAdImage() {
        RetrofitUtil.getInstance().apiService()
                .startimg()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<StartVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<StartVO> result) {
                        LogUtils.e("---获取广告图--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            Prefer.getInstance().setAdImageUrl(result.getData().getImgUrl());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 检测升级更新
     */
    private void checkUpdate() {
        RetrofitUtil.getInstance().apiService()
                .newversion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<UpdateVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<UpdateVO> result) {
                        LogUtils.e("---检测升级更新--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            UpdateVO data = result.getData();
                            if (data != null) {
                                //检测升级更新
                                AutoUpgradeClient.checkUpgrade(MainActivity.this, data);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取用户信息，这里是为了检验登录失效处理
     */
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
                        if (result.getCode() == 2) {
                            ToastUtil.showToast(MainActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        }else if(result.getCode() == 1){
                            SctApp.mUserInfoData = result.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void initData() {
        rlDeal.setOnClickListener(this);
        rlTrader.setOnClickListener(this);
        rlIncome.setOnClickListener(this);
        rlMe.setOnClickListener(this);
        ivIncome.setSelected(true);
        tvIncome.setSelected(true);
        newHomeFragment = NewHomeFragment.newInstance();
        incomeFragment = IncomeFragment.newInstance();
//        homeFragment = HomeFragment.newInstance();
        orderFragment = OrderFragment.newInstance();
        traderFragment = MyAdvertisingFragment.newInstance();

        lobbyFragment = HomeLobbyFragment.newInstance();


      //  meFragment = MeFragment.newInstance();
        newMeFragment = NewMeFragment.newInstance();
        //nowFragment = incomeFragment;
        nowFragment = newHomeFragment;
        fragmentManager = getSupportFragmentManager();
       // fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment, "C").commitAllowingStateLoss();
        fragmentManager.beginTransaction().replace(R.id.fl_content, nowFragment, "C").commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_deal:
                //交易订单
                showHomeFragment();
                break;
            case R.id.rl_income:
                //抢单
                showIncomeOrder();

                break;
            case R.id.rl_me:
                tvDeal.setSelected(false);
                ivDeal.setSelected(false);
                ivIncome.setSelected(false);
                tvIncome.setSelected(false);
                tvMe.setSelected(true);
                ivMe.setSelected(true);
                tvTrader.setSelected(false);
                ivTrader.setSelected(false);
               // switchFragment(nowFragment, meFragment, "T");
                switchFragment(nowFragment,newMeFragment,"T");
                break;
            case R.id.rl_trader:
                //买卖页面
                showBuyAndPay(false);
                break;
            default:
                break;
        }
    }

    private void showHomeFragment() {
        //交易订单
        tvDeal.setSelected(true);
        ivDeal.setSelected(true);
        tvTrader.setSelected(false);
        ivTrader.setSelected(false);
        ivIncome.setSelected(false);
        tvIncome.setSelected(false);
        tvMe.setSelected(false);
        ivMe.setSelected(false);
        switchFragment(nowFragment, orderFragment, "S");
    }

    private void showIncomeOrder(){
        //智能AI抢单
        tvDeal.setSelected(false);
        ivDeal.setSelected(false);
        tvTrader.setSelected(false);
        ivTrader.setSelected(false);
        ivIncome.setSelected(true);
        tvIncome.setSelected(true);
        tvMe.setSelected(false);
        ivMe.setSelected(false);
      //  switchFragment(nowFragment, incomeFragment, "C");
        switchFragment(nowFragment, newHomeFragment, "C");
    }


    /**
     * 打开买卖页面
     * @param isFromWallet  是否是从钱包进来的
     */
    private void showBuyAndPay(boolean isFromWallet){
        //买卖页面（大厅）
        tvDeal.setSelected(false);
        ivDeal.setSelected(false);
        ivIncome.setSelected(false);
        tvIncome.setSelected(false);
        tvMe.setSelected(false);
        ivMe.setSelected(false);
        tvTrader.setSelected(true);
        ivTrader.setSelected(true);
        //switchFragment(nowFragment, traderFragment, "Ta");
        switchFragment(nowFragment,lobbyFragment,"Ta");
      /*  if(isFromWallet){
            traderFragment.showBuyFragment();
        }*/
    }


    /**
     * 切换Fragment
     * <p>(hide、show、add)
     *
     * @param fromFragment
     * @param toFragment
     */
    private void switchFragment(Fragment fromFragment, Fragment toFragment, String tag) {
        if (nowFragment != toFragment) {
            nowFragment = toFragment;
            fragmentTransaction = fragmentManager.beginTransaction();
            if (!toFragment.isAdded() && null == fragmentManager.findFragmentByTag(tag)) {    // 先判断是否被add过
                fragmentTransaction.hide(fromFragment).add(R.id.fl_content, toFragment, tag).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到activity中, 并添加已显示存在的fangment唯一标示tag
            } else {
                fragmentTransaction.hide(fromFragment).show(toFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


    /**
     * 检测当前服务启动情况
     */
    private void ensureCollectorRunning() {
        ComponentName collectorComponent = new ComponentName(this, HelperNotificationListenerService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) {
            LogUtils.e("---检查结果 - 服务运行中-");
            return;
        }

        toggleNotificationListenerService();
    }

    /**
     * 可触发系统rebind操作 -- 重新启动服务
     */
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.SOCKET_CONTENT)
            }
    )
    public void getScoketMsg(ScoketVO data) {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification(Integer.valueOf(data.getMsgtype()), "USCT数字银行", data.getContent());

        try {
            int msgtype = TextUtils.isEmpty(data.getMsgtype()) ? 0 : Integer.parseInt(data.getMsgtype());
            if (msgtype > 100) {
                //type 大于100 就以全局对话框的形式提示用户
                if (SctApp.isInBackground()) {
                    //正在后台，就缓存起来
                    SctApp.mNotificationData = data;
                } else {
                    //在前台，直接显示对话框
                    SctApp.mNotificationData = null;
                    showNotificationDialog(data);
                }
            }else if(msgtype == 70){
                //客服消息
                if(! SctApp.mCurrentViewIsService) {
                    SctApp.mIsHaveMsgService = true;
                }
                refreshMsgStatus();
            }else if(msgtype == 71){
                if(! SctApp.mCurrentViewIsMember) {
                    SctApp.mIsHaveMsgMember = true;
                }
                refreshMsgStatus();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "发送推送弹框异常了");
        }
    }

    private void refreshMsgStatus(){
        /*if(nowFragment == meFragment){
            meFragment.initMsgStatus();
        }*/
        if(nowFragment == newMeFragment){
            newMeFragment.initMsgStatus();
        }
    }

    private void showNotificationDialog(ScoketVO data) {
        if (data == null) {
            return;
        }
        NotificationDialog dialog = new NotificationDialog(this, data.getContent(), new NotificationDialog.onButtonClickListener() {

            @Override
            public void positiveClick() {
                SctApp.mNotificationData = null;
            }

            @Override
            public void negativeClick() {
                SctApp.mNotificationData = null;
            }
        });
        dialog.show();
    }


    /**
     * 连续点击两下物理返回键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键将退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private static final String WIFI_SLEEP_POLICY = "WIFI_SLEEP_POLICY";

    public void setWifiDormancy(Context context) {
        int value = Settings.System.getInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        Log.d(TAG, "setWifiDormancy() returned: " + value);
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(WIFI_SLEEP_POLICY, value);
        editor.commit();

        if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_NEVER);
        }
    }


    public void restoreWifiDormancy(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        int defaultPolicy = prefs.getInt(WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        Settings.System.putInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, defaultPolicy);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


}


