package com.frico.easy_pay.ui.activity.me;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.MbpUserVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.netty.NettyHandler;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.core.utils.Constants;
import com.frico.easy_pay.dialog.SimpleDialog;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.LoginActivity;
import com.frico.easy_pay.ui.activity.MainActivity;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.ui.activity.me.agency.MyAgencyActivity;
import com.frico.easy_pay.ui.activity.me.group.MyGroupActivity;
import com.frico.easy_pay.ui.activity.me.info.MyAccountInfoActivity;
import com.frico.easy_pay.ui.activity.me.payway.PayWayListActivity;
import com.frico.easy_pay.ui.activity.me.setting.SettingActivity;
import com.frico.easy_pay.ui.activity.me.wallet.BalanceTransferActivity;
import com.frico.easy_pay.ui.activity.me.wallet.NewWalletActivity;
import com.frico.easy_pay.utils.AnalyticsUtils;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.utils.Util;
import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chargepail.qz.www.qzzxing.activity.CodeUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class NewMeFragment extends BaseFragment implements ActionBarClickListener {

    private static String KEY_ACQID = "acqid";
    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.iv_fragment_me_head)
    ImageView ivFragmentMeHead;
    @BindView(R.id.tv_fragment_me_name)
    TextView tvFragmentMeName;
    @BindView(R.id.tv_fragment_me_id)
    TextView tvFragmentMeId;
    @BindView(R.id.tv_fragment_me_all_coin)
    TextView tvFragmentMeAllCoin;
    @BindView(R.id.tv_fragment_me_usable_coin)
    TextView tvFragmentMeUsableCoin;
    @BindView(R.id.tv_fragment_me_today_gain)
    TextView tvFragmentMeTodayGain;
    @BindView(R.id.tv_fragment_me_auto)
    TextView tvFragmentMeAuto;
    @BindView(R.id.tv_fragment_me_team)
    TextView tvFragmentMeTeam;
    @BindView(R.id.tv_fragment_me_wallet)
    TextView tvFragmentMeWallet;
    @BindView(R.id.tv_fragment_me_pay_way)
    TextView tvFragmentMePayWay;
    @BindView(R.id.tv_fragment_me_share)
    TextView tvFragmentMeShare;
    @BindView(R.id.tv_fragment_me_service)
    TextView tvFragmentMeService;
    @BindView(R.id.tv_fragment_me_exit)
    TextView tvFragmentMeExit;
    @BindView(R.id.iv_fragment_me_switch)
    ImageView ivFragmentMeSwitch;
    @BindView(R.id.tv_fragment_me_lv)
    TextView tvFragmentMeLv;
    @BindView(R.id.tv_fragment_me_address)
    TextView tvFragmentMeAddress;
    @BindView(R.id.iv_is_vip)
    ImageView ivIsVIp;


    private View view;
    private Unbinder unbinder;
    private MainActivity activity;

    private MbpUserVO mUserInfoData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me_new, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        initTitle();
        return view;
    }

    private void initTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        actionbar.setData("我的", 0, null, R.drawable.icon_fragment_me_message, null, this);
        actionbar.setTvTitleColor(getResources().getColor(R.color.colorWhite));
        actionbar.addTvRight2(R.drawable.icon_fragment_me_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(SettingActivity.class);
            }
        });
        actionbar.setColorBackground(Color.parseColor("#FF70A0FF"));
    }

    public static NewMeFragment newInstance() {
        Bundle args = new Bundle();
        NewMeFragment fragment = new NewMeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }

    private void getData() {
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
                            LogUtils.e(result.getData().toString());
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

    private void initViewData(MbpUserVO mUserInfoData) {
        tvFragmentMeLv.setText(mUserInfoData.getLeveltext());
        tvFragmentMeAllCoin.setText(mUserInfoData.getTotal_amount());
        tvFragmentMeUsableCoin.setText(mUserInfoData.getAvailable_money());
        tvFragmentMeTodayGain.setText(mUserInfoData.getTotal_profit_money());

        //initSwitch(mUserInfoData.isPayWayOpen());
        initSwitchOrderAuto(mUserInfoData.isOrderAutoOpen());
        String nickName = "--";
        if (!mUserInfoData.getIs_setUserName()) {
            nickName = "未设置昵称";
        } else {
            nickName = TextUtils.isEmpty(mUserInfoData.getUsername()) ? "--" : mUserInfoData.getUsername();
        }
        LogUtils.e(mUserInfoData.toString());
        if (null!=mUserInfoData.getIsVip()&&mUserInfoData.getIsVip().equals("0")){
         //不是vip
            ivIsVIp.setVisibility(View.GONE);
        }else if (null!=mUserInfoData.getIsVip()&&mUserInfoData.getIsVip().equals("1")){
            //vip
            ivIsVIp.setVisibility(View.VISIBLE);
        }
        tvFragmentMeName.setText(nickName);
        tvFragmentMeId.setText("ID:" + mUserInfoData.getAcqid());
        setSpan(mUserInfoData.getTotal_amount(), tvFragmentMeAllCoin, 16, 12);
        setSpan(mUserInfoData.getAvailable_money(), tvFragmentMeUsableCoin, 16, 12);
        setSpan(mUserInfoData.getProfit_money(), tvFragmentMeTodayGain, 16, 12);
        if (TextUtils.isEmpty(mUserInfoData.getmHeaderImgUrl())) {
            new ImageLoaderImpl().loadImageCircle(getActivity(), R.drawable.header_default).into(ivFragmentMeHead);
        } else {
            new ImageLoaderImpl().loadImageCircle(getActivity(), mUserInfoData.getmHeaderImgUrl()).into(ivFragmentMeHead);
        }
        initMsgStatus();

    }

    private void initSwitchOrderAuto(boolean isOpen) {
        if (isOpen) {
            ivFragmentMeSwitch.setImageResource(R.drawable.open1);
        } else {
            ivFragmentMeSwitch.setImageResource(R.drawable.close1);
        }
    }

    public void initMsgStatus() {
       /* if (SctApp.mIsHaveMsgMember) {
            tvMyMessageList.setText("消息列表(有新消息)");
        } else {
            tvMyMessageList.setText("消息列表");
        }*/

        if (SctApp.mIsHaveMsgService) {
            tvFragmentMeService.setText("联系客服(有新消息)");
        } else {
            tvFragmentMeService.setText("联系客服");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_fragment_me_address,R.id.iv_fragment_me_head, R.id.tv_fragment_me_name,
            R.id.tv_fragment_me_auto, R.id.iv_fragment_me_switch, R.id.tv_fragment_me_team,
            R.id.tv_fragment_me_wallet, R.id.tv_fragment_me_pay_way, R.id.tv_fragment_me_share,
            R.id.tv_fragment_me_service, R.id.tv_fragment_me_exit,R.id.ll_agency_partner,
            R.id.ll_agency_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_me_auto:

                break;
            case R.id.tv_fragment_me_address:
                launch(AddressSelectActivity.class);
                break;
            case R.id.iv_fragment_me_switch:
                //自动收款开关
                if (SctApp.mUserInfoData.isOrderAutoStatusOpen()) {
                    changeOrderAutoStatusClose();
                } else {
                    changeOrderAutoStatusOpen();
                }
                break;
            case R.id.tv_fragment_me_team:
                //我的团队
                MyGroupActivity.start(getActivity(), true, Prefer.getInstance().getUserId(), null);
                break;
            case R.id.tv_fragment_me_wallet:
                //钱包
                launch(NewWalletActivity.class);
                break;
            case R.id.tv_fragment_me_pay_way:
                //收款方式
                launch(PayWayListActivity.class);
                break;
            case R.id.tv_fragment_me_share:
                //分享
                gotoShareAct();
                break;

            case R.id.ll_agency_partner:
                //代理合伙人
                launch(MyAgencyActivity.class);
                break;
            case R.id.ll_agency_vip:
                //代理VIP
                launch(MyAgencyActivity.class);
                break;
            case R.id.tv_fragment_me_service:
                //联系客服
                String customerUrl = "http://y.fricobloc.com/?action=page.customerService.index";//正式的客服地址
//                String customerUrl = "http://192.168.1.133:81/?action=page.customerService.index";//姚辉本地的地址
                if (!TextUtils.isEmpty(Prefer.getInstance().getUserId())) {
                    customerUrl = customerUrl + "&acqid=" + Prefer.getInstance().getUserId();
                }
                CallCenterActivity.start(activity, false, "客服", customerUrl, WebUrlActivity.TYPE_MSG_SERVICE);

                break;
            case R.id.tv_fragment_me_exit:
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
            case R.id.iv_fragment_me_head:
                // 个人信息页面
                MyAccountInfoActivity.start(getActivity(), mUserInfoData);
                break;
            case R.id.tv_fragment_me_name:
                // 个人信息页面
                MyAccountInfoActivity.start(getActivity(), mUserInfoData);
                break;
        }
    }

    private void changeOrderAutoStatusClose() {
        changeOrderAutoStatus("hidden");
    }

    private void changeOrderAutoStatusOpen() {
        changeOrderAutoStatus("normal");
    }

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

  /*  private void switchOpen() {
        //开着的
        ivFragmentMeSwitch.setImageResource(R.drawable.open1);
    }

    private void switchClose() {
        ivFragmentMeSwitch.setImageResource(R.drawable.close1);
    }*/


    private void initSwitch(boolean isOpen) {
        if (isOpen) {
            //switchOpen();
        } else {
            //switchClose();
        }
    }


    @Override
    public void onLeftClick() {

    }

    @Override
    public void onRightClick() {

    }

    private void gotoShareAct() {
        //  待联调联试
        if (TextUtils.isEmpty(Prefer.getInstance().getToken())) {
            ToastUtil.showToast(getContext(), "请登录后操作");
            LoginActivity.start(getActivity());
            return;
        }

        String shareViewUrl = Constants.BASE_URL_H5 + "/share_acq.html?invitecode=" + mUserInfoData.getRegcode();
//        String shareViewUrl = "http://www.baidu.com/";
//        String shareViewUrl = "http://192.168.1.51:8085/#/registe/";
//        String shareViewUrl = "http://192.168.1.51:8084/?token="+ Prefer.getInstance().getToken();//http://192.168.1.51:8084
//        String shareViewUrl = "http://sct.fricobloc.com/acq_register?invitecode=AAAAAA";

        LogUtils.e(shareViewUrl);
        WebUrlActivity.start(getActivity(), true, "邀请好友", shareViewUrl);
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

    /**
     * 将 @param target 分成两种大小的文字布局在@param textView上
     *
     * @param target
     * @param textView
     * @param firstDip
     * @param secondDip
     */

    private void setSpan(String target, TextView textView, int firstDip, int secondDip) {
        String str1 = Util.splitSctCount(target)[0];
        String str2 = Util.splitSctCount(target)[1];
        if (str2.length()>=2){
            str2 = str2.substring(0,2);
            target = str1+"."+str2;
        }else if (str2.length()==1){
            str2 = str2+"0";
            target = str1+"."+str2;
        }

        Spannable sp = new SpannableString(target);
        sp.setSpan(new AbsoluteSizeSpan(firstDip, true), 0, str1.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(secondDip, true), str1.length(), target.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(sp);
    }

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
                if (url.contains("http") && url.contains(KEY_ACQID)) {
                    //如果是扫码的聚合码地址，就解析出code来
                    code = getToUserIdFromUrl(url);
                } else if (url.length() == 6) {
                    //认为是老版本的二维码，就是id
                    code = url;
                }
                gotoTransfer(code);
            }
        }
    }

    private void gotoTransfer(String toUserId) {
        //转账
        BalanceTransferActivity.start(getActivity(), toUserId);
    }


    private String getToUserIdFromUrl(String url) {
        Uri uri = Uri.parse(url);
        String type = uri.getQueryParameter(KEY_ACQID);
        return type;
    }


}
