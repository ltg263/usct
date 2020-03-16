package com.frico.usct.ui.activity.me.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.MbpUserVO;
import com.frico.usct.core.entity.Result;
import com.frico.usct.ui.activity.base.BaseActivity;
import com.frico.usct.ui.activity.me.info.MyAccountInfoActivity;
import com.frico.usct.ui.activity.me.payway.AddPayWayActivity;
import com.frico.usct.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 做任务页面
 */
public class DoTaskActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_icon_set_pw)
    ImageView imgIconSetPw;
    @BindView(R.id.tv_set_login_pw)
    TextView tvSetLoginPw;
    @BindView(R.id.tv_set_login_pw_give_sct)
    TextView tvSetLoginPwGiveSct;
    @BindView(R.id.ll_content_set_login_pw_lay)
    LinearLayout llContentSetLoginPwLay;
    @BindView(R.id.iv_set_login_pw_togo)
    ImageView ivSetLoginPwTogo;
    @BindView(R.id.img_icon_set_pay_pw)
    ImageView imgIconSetPayPw;
    @BindView(R.id.tv_set_pay_pw)
    TextView tvSetPayPw;
    @BindView(R.id.tv_set_pay_pw_give_sct)
    TextView tvSetPayPwGiveSct;
    @BindView(R.id.ll_content_set_pay_pw_lay)
    LinearLayout llContentSetPayPwLay;
    @BindView(R.id.iv_set_pay_pw_togo)
    ImageView ivSetPayPwTogo;
    @BindView(R.id.img_icon_set_email)
    ImageView imgIconSetEmail;
    @BindView(R.id.tv_set_email)
    TextView tvSetEmail;
    @BindView(R.id.tv_set_email_give_sct)
    TextView tvSetEmailGiveSct;
    @BindView(R.id.ll_content_set_email_lay)
    LinearLayout llContentSetEmailLay;
    @BindView(R.id.iv_set_email_togo)
    ImageView ivSetEmailTogo;
    @BindView(R.id.img_icon_set_pay_way)
    ImageView imgIconSetPayWay;
    @BindView(R.id.tv_set_pay_way)
    TextView tvSetPayWay;
    @BindView(R.id.tv_set_pay_way_give_sct)
    TextView tvSetPayWayGiveSct;
    @BindView(R.id.ll_content_set_pay_way_lay)
    LinearLayout llContentSetPayWayLay;
    @BindView(R.id.iv_set_pay_way_togo)
    ImageView ivSetPayWayTogo;
    @BindView(R.id.iv_title_bar_left_back)
    ImageView ivTitleBarLeftBack;
    @BindView(R.id.img_icon_set_nick)
    ImageView imgIconSetNick;
    @BindView(R.id.tv_set_nick)
    TextView tvSetNick;
    @BindView(R.id.tv_set_nick_give_sct)
    TextView tvSetNickGiveSct;
    @BindView(R.id.ll_content_set_nick_lay)
    LinearLayout llContentSetNickLay;
    @BindView(R.id.iv_set_nick_togo)
    ImageView ivSetNickTogo;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, DoTaskActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_do_task;
    }

    @Override
    public void initTitle() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initView();
        getUserInfo();
    }

    private void initView() {
        ivSetLoginPwTogo.setOnClickListener(this);
        ivSetPayPwTogo.setOnClickListener(this);
        ivSetEmailTogo.setOnClickListener(this);
        ivSetPayWayTogo.setOnClickListener(this);
        ivTitleBarLeftBack.setOnClickListener(this);
        ivSetNickTogo.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.iv_title_bar_left_back) {
            finish();
        } else if (vId == R.id.iv_set_login_pw_togo) {
            //登录密码
            ChangeLoginPwdActivity.startSetLoginPW(DoTaskActivity.this);
        } else if (vId == R.id.iv_set_pay_pw_togo) {
            //支付密码
            launch(SetPayPwdActivity.class);
        } else if (vId == R.id.iv_set_email_togo) {
            //邮箱
            launch(ChangeMailActivity.class);
        } else if (vId == R.id.iv_set_pay_way_togo) {
            //支付方式
            launch(AddPayWayActivity.class);
        }else if(vId == R.id.iv_set_nick_togo){
            //设置头像和昵称
            MyAccountInfoActivity.start(this,null);
        }
    }

    /**
     * 获取用户信息
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
                        if (result.getCode() == 1) {
                            MbpUserVO data = result.getData();
                            if (data != null) {
                                //更新用户信息
                                updateViewData(data);
                                SctApp.mUserInfoData = result.getData();

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(DoTaskActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateViewData(MbpUserVO data) {
        updateSetedMail(data.getIs_setemail(), data.getGive_setemail());
        updateSetLoginPw(data.getIs_setpassword(), data.getGive_setpassword());
        updateSetedPayPw(data.getIs_setpaypassword(), data.getGive_setpaypassword());
        updatePayListStatus(data.getIs_setpayway(), data.getGive_setpayway());
        boolean isSetNicked = data.getIs_setHeader() && data.getIs_setUserName();
        int giveCount = data.getGive_setavater() + data.getGive_setusername();
        updateSetNickStatus(isSetNicked,giveCount);
    }


    private void updateSetedMail(boolean isSetedMail, int giveSctCount) {
        updatePayListItemStatus(ivSetEmailTogo, isSetedMail);
        tvSetEmailGiveSct.setVisibility(View.VISIBLE);
        tvSetEmailGiveSct.setText(getGiveSctIntro(giveSctCount));
    }


    private void updateSetedPayPw(boolean isSetedPayPw, int giveSctCount) {
        updatePayListItemStatus(ivSetPayPwTogo, isSetedPayPw);
        tvSetPayPwGiveSct.setVisibility(View.VISIBLE);
        tvSetPayPwGiveSct.setText(getGiveSctIntro(giveSctCount));
    }

    /**
     * 登录密码状态
     */
    private void updateSetLoginPw(boolean isSettedPW, int giveSctCount) {
        updatePayListItemStatus(ivSetLoginPwTogo, isSettedPW);
        tvSetLoginPwGiveSct.setVisibility(View.VISIBLE);
        tvSetLoginPwGiveSct.setText(getGiveSctIntro(giveSctCount));
    }


    /**
     * 更新支付方式状态
     */
    private void updatePayListStatus(boolean isSetedPayWay, int giveSctCount) {
        updatePayListItemStatus(ivSetPayWayTogo, isSetedPayWay);
        tvSetPayWayGiveSct.setVisibility(View.VISIBLE);
        tvSetPayWayGiveSct.setText(getGiveSctIntro(giveSctCount));
    }

    /**
     * 更新头像昵称设置状态
     */
    private void updateSetNickStatus(boolean isSetedNick, int giveSctCount) {
        updatePayListItemStatus(ivSetNickTogo, isSetedNick);
        tvSetNickGiveSct.setVisibility(View.VISIBLE);
        tvSetNickGiveSct.setText(getGiveSctIntro(giveSctCount));
    }



    private String getGiveSctIntro(int sctCount) {
        return "+" + sctCount + "USCT";
    }


    private void updatePayListItemStatus(ImageView textView, boolean isOver) {
        int drawableId = R.drawable.icon_task_do_togo;
        if (isOver) {
            drawableId = R.drawable.icon_task_do_over;
//            textView.setClickable(false);
        }
        textView.setImageResource(drawableId);
    }

}
