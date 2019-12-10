package com.frico.easy_pay.ui.activity.me;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.netty.NettyHandler;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.core.utils.Constants;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.ui.activity.LoginActivity;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.me.group.MyGroupActivity;
import com.frico.easy_pay.ui.activity.me.info.MyAccountInfoActivity;
import com.frico.easy_pay.ui.activity.me.message.MessageActivity;
import com.frico.easy_pay.ui.activity.me.payway.PayWayListActivity;
import com.frico.easy_pay.ui.activity.me.setting.AboutSCTActivity;
import com.frico.easy_pay.ui.activity.me.setting.AccountSaveActivity;
import com.frico.easy_pay.ui.activity.me.setting.DoTaskActivity;
import com.frico.easy_pay.ui.activity.me.setting.NoticeActivity;
import com.frico.easy_pay.ui.activity.me.wallet.BalanceTransferActivity;
import com.frico.easy_pay.ui.activity.me.wallet.BalanceTransferFromUsdtActivity;
import com.frico.easy_pay.ui.activity.me.wallet.BalanceTransferWithdrawToUsdtActivity;
import com.frico.easy_pay.ui.activity.me.wallet.NewWalletActivity;
import com.frico.easy_pay.ui.activity.me.wallet.WalletActivity;
import com.frico.easy_pay.utils.AnalyticsUtils;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.Util;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chargepail.qz.www.qzzxing.activity.CaptureActivity;
import chargepail.qz.www.qzzxing.activity.CodeUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_ACQID = "acqid";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_all_count)
    TextView tvAllCount;
    @BindView(R.id.tv_can_money)
    TextView tvCanMoney;
    @BindView(R.id.tv_earnings)
    TextView tvEarnings;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_connect_us)
    TextView tvConnectUs;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @BindView(R.id.tv_switch)
    TextView tvSwitch;
    @BindView(R.id.switch_btn)
    ImageView switchBtn;
    @BindView(R.id.tv_task)
    TextView tvTask;
    @BindView(R.id.tv_me_fragment_nick_name)
    TextView tvMeFragmentNickName;
    @BindView(R.id.tv_me_fragment_user_id)
    TextView tvMeFragmentUserId;
    @BindView(R.id.tv_all_sct_count_first)
    TextView tvAllSctCountFirst;
    @BindView(R.id.tv_all_sct_count_second)
    TextView tvAllSctCountSecond;
    @BindView(R.id.tv_can_use_sct_count_first)
    TextView tvCanUseSctCountFirst;
    @BindView(R.id.tv_can_use_sct_count_second)
    TextView tvCanUseSctCountSecond;
    @BindView(R.id.tv_income_count_first)
    TextView tvIncomeCountFirst;
    @BindView(R.id.tv_income_count_second)
    TextView tvIncomeCountSecond;
    @BindView(R.id.iv_me_fragment_header)
    ImageView ivMeFragmentHeader;
    @BindView(R.id.ll_account_info_lay)
    LinearLayout llAccountInfoLay;
    @BindView(R.id.tv_my_group)
    TextView tvMyGroup;
    @BindView(R.id.iv_me_fragment_message)
    ImageView ivMeFragmentMessage;
    @BindView(R.id.tv_user_level_text)
    TextView tvUserLevelText;
    @BindView(R.id.tv_switch_notify_ring)
    TextView tvSwitchNotifyRing;
    @BindView(R.id.switch_notify_ring_btn)
    ImageView switchNotifyRingBtn;
    @BindView(R.id.ll_all_sct_count_first_lay)
    LinearLayout llAllSctCountFirstLay;
    @BindView(R.id.ll_can_use_sct_count_first_lay)
    LinearLayout llCanUseSctCountFirstLay;
    @BindView(R.id.tv_my_message_list)
    TextView tvMyMessageList;
    @BindView(R.id.ll_scan)
    LinearLayout llScan;
    @BindView(R.id.ll_show_qrcode)
    LinearLayout llShowQrcode;
    @BindView(R.id.tv_switch_qure_auto)
    TextView tvSwitchQureAuto;
    @BindView(R.id.switch_qure_auto_btn)
    ImageView switchQureAutoBtn;
    @BindView(R.id.ll_income_count_first_lay)
    LinearLayout llIncomeCountFirstLay;
    @BindView(R.id.tv_wallet_recharge)
    TextView tvWalletRecharge;
    @BindView(R.id.tv_wallet_withdraw)
    TextView tvWalletWithdraw;


    private View view;
    private Unbinder unbinder;
    private MainActivity activity;

    private MbpUserVO mUserInfoData;

    public static MeFragment newInstance() {
        MeFragment meFragment = new MeFragment();
        return meFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();

        actionbar.setData("我的", 0, null, 0, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
        initViewListener();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stubclose1
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    private void initViewListener() {
        switchBtn.setOnClickListener(this);
        tvSwitch.setOnClickListener(this);
        tvWallet.setOnClickListener(this);
        tvPayWay.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvConnectUs.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        tvCustomer.setOnClickListener(this);
        tvTask.setOnClickListener(this);
        llAccountInfoLay.setOnClickListener(this);
        tvMyGroup.setOnClickListener(this);
        ivMeFragmentMessage.setOnClickListener(this);
        tvSwitchNotifyRing.setOnClickListener(this);
        tvMyMessageList.setOnClickListener(this);
        llScan.setOnClickListener(this);
        llShowQrcode.setOnClickListener(this);
        tvSwitchQureAuto.setOnClickListener(this);
        switchQureAutoBtn.setOnClickListener(this);
        tvWalletRecharge.setOnClickListener(this);
        tvWalletWithdraw.setOnClickListener(this);
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
                            initViewData(mUserInfoData);
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(getActivity(), result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initViewData(MbpUserVO userinfo) {
        tvUserLevelText.setText(userinfo.getLeveltext());
        tvAllCount.setText(userinfo.getTotal_amount());
        tvCanMoney.setText(userinfo.getAvailable_money());
        tvEarnings.setText(userinfo.getTotal_profit_money());
        tvLevel.setText(userinfo.getUsername() + ": " + userinfo.getLeveltext());
        initSwitch(userinfo.isPayWayOpen());
        initSwitchOrderAuto(userinfo.isOrderAutoOpen());
        String nickName = "--";
        if (!userinfo.getIs_setUserName()) {
            nickName = "未设置昵称";
        } else {
            nickName = TextUtils.isEmpty(userinfo.getUsername()) ? "--" : userinfo.getUsername();
        }
        tvMeFragmentNickName.setText(nickName);

        tvMeFragmentUserId.setText("ID:" + userinfo.getAcqid());
        tvAllSctCountFirst.setText(Util.splitSctCount(userinfo.getTotal_amount())[0] + ".");
        tvAllSctCountSecond.setText(Util.splitSctCount(userinfo.getTotal_amount())[1]);
        tvCanUseSctCountFirst.setText(Util.splitSctCount(userinfo.getAvailable_money())[0] + ".");
        tvCanUseSctCountSecond.setText(Util.splitSctCount(userinfo.getAvailable_money())[1]);
        tvIncomeCountFirst.setText(Util.splitSctCount(userinfo.getProfit_money())[0] + ".");
        tvIncomeCountSecond.setText(Util.splitSctCount(userinfo.getProfit_money())[1]);
        if (TextUtils.isEmpty(userinfo.getmHeaderImgUrl())) {
            new ImageLoaderImpl().loadImageCircle(getActivity(), R.drawable.header_default).into(ivMeFragmentHeader);
        } else {
            new ImageLoaderImpl().loadImageCircle(getActivity(), userinfo.getmHeaderImgUrl()).into(ivMeFragmentHeader);
        }

        initMsgStatus();
    }


    public void initMsgStatus() {
        if (SctApp.mIsHaveMsgMember) {
            tvMyMessageList.setText("消息列表(有新消息)");
        } else {
            tvMyMessageList.setText("消息列表");
        }

        if (SctApp.mIsHaveMsgService) {
            tvCustomer.setText("联系客服(有新消息)");
        } else {
            tvCustomer.setText("联系客服");
        }
    }

    private void changePaymentStatusOpen() {
        changePaymentStatus("normal");
    }

    private void changePaymentStatusClose() {
        changePaymentStatus("hidden");
    }


    /**
     * 开关收款方式
     */
    private void changePaymentStatus(String status) {
        RetrofitUtil.getInstance().apiService()
                .changepaymentstatus(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void changeOrderAutoStatusOpen() {
        changeOrderAutoStatus("normal");
    }

    private void changeOrderAutoStatusClose() {
        changeOrderAutoStatus("hidden");
    }


    /**
     * 自动回调开关
     */
    private void changeOrderAutoStatus(String status) {
        RetrofitUtil.getInstance().apiService()
                .changeOrderAutoStatus(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_account_info_lay:
                // 个人信息页面
                MyAccountInfoActivity.start(getActivity(), mUserInfoData);
                break;
            case R.id.tv_switch:
            case R.id.switch_btn:
                //收款开关
                if (SctApp.mUserInfoData.isPaymentStatusOpen()) {
                    changePaymentStatusClose();
                } else {
                    changePaymentStatusOpen();
                }
                break;

            case R.id.switch_qure_auto_btn:
                //收款开关
                if (SctApp.mUserInfoData.isOrderAutoStatusOpen()) {
                    changeOrderAutoStatusClose();
                } else {
                    changeOrderAutoStatusOpen();
                }
                break;
            case R.id.tv_switch_notify_ring:
                //通知铃声开关
                openOrCloseSwitch();
                break;
            case R.id.tv_my_group:
                //我的团队
                MyGroupActivity.start(getActivity(), true, Prefer.getInstance().getUserId(), null);
                break;
            case R.id.tv_wallet:
                //我的钱包
                launch(NewWalletActivity.class);
                break;

            case R.id.tv_pay_way:
                //收款方式
//                launch(PayWayActivity.class);
                launch(PayWayListActivity.class);
                break;
            case R.id.tv_account:
                //账户安全
                launch(AccountSaveActivity.class);
                break;
            case R.id.tv_task:
                // 做任务
                DoTaskActivity.start(getActivity());
                break;
            case R.id.tv_notice:
                //公告
                launch(NoticeActivity.class);
                break;
            case R.id.tv_message:
                //消息
                launch(MessageActivity.class);
                break;
            case R.id.tv_connect_us:
                //关于我们
                launch(AboutSCTActivity.class);

                break;
            case R.id.iv_me_fragment_message:
                //我的消息
                launch(MessageActivity.class);
                break;
            case R.id.ll_scan:
                //扫码
//                launch(CaptureActivity.class);
                //动态权限申请
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(intent, CodeUtils.RESULT_SUCCESS);
                }

                break;
            case R.id.ll_show_qrcode:
                //收款码
                ShowMyQrcodeActivity.start(getActivity());
                break;
            case R.id.tv_share:
                //我的分享
                gotoShareAct();

                break;
            case R.id.tv_customer:
                //联系客服
                String customerUrl = "http://y.fricobloc.com/?action=page.customerService.index";//正式的客服地址
//                String customerUrl = "http://192.168.1.133:81/?action=page.customerService.index";//姚辉本地的地址
                if (!TextUtils.isEmpty(Prefer.getInstance().getUserId())) {
                    customerUrl = customerUrl + "&acqid=" + Prefer.getInstance().getUserId();
                }
                CallCenterActivity.start(activity, false, "客服", customerUrl, WebUrlActivity.TYPE_MSG_SERVICE);

//                playSound(getActivity());
                //scheme 跳转到建行app
//                String navityAppScheme = "ccbapp://main.ccb.com";
//                Intent action = new Intent(Intent.ACTION_VIEW);
//                StringBuilder builder = new StringBuilder();
//                builder.append(navityAppScheme);
//                action.setData(Uri.parse(builder.toString()));
//                startActivity(action);


                break;

            case R.id.tv_my_message_list:
                //消息列表
                String messageListUrl = "http://y.fricobloc.com/DCApi/index.php/Home/Index/roomlistPage";
                if (!TextUtils.isEmpty(Prefer.getInstance().getUserId())) {
                    messageListUrl = messageListUrl + "?acqid=" + Prefer.getInstance().getUserId();
                }
                CallCenterActivity.start(activity, false, "消息列表", messageListUrl, WebUrlActivity.TYPE_MSG_LIST);
                break;
            case R.id.tv_exit:
                //退出登录
                SimpleDialog simpleDialog = new SimpleDialog(activity, "是否退出当前账号?", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
                    @Override
                    public void onNegBtnClick() {

                    }

                    @Override
                    public void onPosBtnClick() {
                        NettyHandler.getInstance().logout();
                        AnalyticsUtils.onAppExit(activity);
                        SctApp.getInstance().exitLoginActivity();
                        launch(LoginActivity.class);
                        activity.finish();
                    }
                });
                simpleDialog.show();
                break;
//            case R.id.tv_wallet_recharge:
//                //USDT充值
//                launch(BalanceTransferFromUsdtActivity.class);
//                break;
//            case R.id.tv_wallet_withdraw:
//                //提币
//                BalanceTransferWithdrawToUsdtActivity.start(getActivity());
//                break;
            default:
                break;
        }
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.REFRESH_MONEY)
            }
    )
    public void getmoney(String data) {
        getData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void gotoShareAct() {
        //  待联调联试
        if (TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            ToastUtil.showToast(getContext(), "请登录后操作");
            LoginActivity.start(getActivity());
            return;
        }

        String shareViewUrl = Constants.BASE_URL_H5 + "#/login?token=" + Prefer.getInstance().getToken();
//        String shareViewUrl = "http://www.baidu.com/";
//        String shareViewUrl = "http://192.168.1.51:8085/#/registe/";
//        String shareViewUrl = "http://192.168.1.51:8084/?token="+ Prefer.getInstance().getToken();//http://192.168.1.51:8084
//        String shareViewUrl = "http://sct.fricobloc.com/acq_register?invitecode=AAAAAA";

        WebUrlActivity.start(getActivity(), true, "邀请好友", shareViewUrl);
    }

    private void initSwitch(boolean isOpen) {
        if (isOpen) {
            switchOpen();
        } else {
            switchClose();
        }
    }

    private void initSwitchOrderAuto(boolean isOpen) {
        if (isOpen) {
            switchQureAutoBtn.setImageResource(R.drawable.open1);
        } else {
            switchQureAutoBtn.setImageResource(R.drawable.close1);
        }
    }


    private void switchOpen() {
        //开着的
        switchBtn.setImageResource(R.drawable.open1);
    }

    private void switchClose() {
        switchBtn.setImageResource(R.drawable.close1);
    }


    private Ringtone mRingtone;

    //播放自定义的声音
    public synchronized void playSound(Context context) {
        if (mRingtone == null) {
            LogUtils.e("playSound", "----------初始化铃声----------");
            String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.my_ring_552857;
            Uri no = Uri.parse(uri);
            mRingtone = RingtoneManager.getRingtone(context.getApplicationContext(), no);
        }
        if (!mRingtone.isPlaying()) {
            LogUtils.e("playSound", "--------------播放铃声---------------" + mRingtone.isPlaying());
            mRingtone.play();
        }
    }

    public synchronized void stopSound() {
        if (mRingtone != null) {
            if (mRingtone.isPlaying()) {
                mRingtone.stop();
            }
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
        switchNotifyRingBtn.setImageResource(R.drawable.open1);
    }

    private void closeRingSwitch() {
        //关闭通知铃声开关
        Prefer.getInstance().setRingSwitch(false);
        switchNotifyRingBtn.setImageResource(R.drawable.close1);
    }

    @Override
    public void onResume() {
        super.onResume();
        initMsgStatus();
        getData();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CodeUtils.RESULT_SUCCESS) {
            if (resultCode != RESULT_OK) {
                return;
            }
            if (data != null) {
                String url = data.getStringExtra(CodeUtils.RESULT_STRING);
                String code = "";
//                ToastUtil.showToast(getActivity(),"结果 = " + code);
                if(url.contains("http") && url.contains(KEY_ACQID)){
                    //如果是扫码的聚合码地址，就解析出code来
                    code = getToUserIdFromUrl(url);
                }else if(url.length() == 6){
                    //认为是老版本的二维码，就是id
                    code = url;
                }
                gotoTransfar(code);
            }
        }
    }


    //
    private String getToUserIdFromUrl(String url){
        Uri uri = Uri.parse(url);
        String type = uri.getQueryParameter(KEY_ACQID);
        return type;
    }

    private void gotoTransfar(String toUserId) {
        //转账
        BalanceTransferActivity.start(getActivity(), toUserId);
    }

}
