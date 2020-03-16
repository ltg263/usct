package com.frico.usct.ui.activity.me.agency;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.SctApp;
import com.frico.usct.core.api.RetrofitUtil;
import com.frico.usct.core.entity.ApplyInfo;
import com.frico.usct.core.entity.Result;
import com.frico.usct.ui.activity.base.BaseFragment;
import com.frico.usct.utils.LogUtils;
import com.frico.usct.utils.ToastUtil;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HhrFragment extends BaseFragment {


    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.rl)
    RelativeLayout rl;
    private View view;
    private Unbinder unbinder;
    private MyAgencyActivity activity;

    public static HhrFragment newInstance() {
        HhrFragment adFragment = new HhrFragment();
        return adFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hhr, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        activity = (MyAgencyActivity) getActivity();
        activity.show(getActivity(), "获取信息中...");
        initNetData(activity.getType());
        return view;
    }


    /**
     * 获取底部列表数据数据
     */
    private void initNetData(int type) {
        RetrofitUtil.getInstance().apiService()
                .applyInfo(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<ApplyInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<ApplyInfo> result) {
                        activity.dismiss();
                        if (result.getCode() == 1) {
                            appleySita(result);
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            ToastUtil.showToast(getActivity(), result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void appleySita(Result<ApplyInfo> result) {
        if(result.getData()==null){
            //未审核
            ll.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            submit.setText("提交申请");
            return;
        }
        switch (result.getData().getStatus()){
            case 0://审核中
            case 3://审核中
                ll.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                iv.setImageResource(R.drawable.ic_agency_shz);
                if(activity.getType()==1){
                    text.setText("您的商户合伙人申请正在审核中 \n请耐心等待...");
                }else{
                    text.setText("您的超级VIP申请正在审核中 \n请耐心等待...");
                }
                break;
            case 5://审核通过
                ll.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                iv.setImageResource(R.drawable.ic_agency_yes);
                if(activity.getType()==1){
                    text.setText("恭喜您! \n您的商户合伙人申请审核已通过~");
                }else{
                    text.setText("恭喜您! \n您的超级VIP审核已通过~");
                }
                break;
            case 6://审核失败
                ll.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_agency_no);
                if(activity.getType()==1){
                    text.setText("很抱歉~ \n您的商户合伙人申请审核未通过");
                }else{
                    text.setText("很抱歉~ \n您的超级VIP申请审核未通过");
                }
                submit.setVisibility(View.VISIBLE);
                submit.setText("重新申请");
                break;
        }
    }


    @OnClick(R.id.submit)
    public void onViewClicked() {
        if(submit.getText().toString().equals("重新申请")){
            ll.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            submit.setText("提交申请");
            return;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.showToast(getContext(), "请输入昵称");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtil.showToast(getContext(), "请输入手机号");
            return;
        }
        LogUtils.w(etName.getText().toString()+";"+etPhone.getText().toString()+";"+activity.getType()+"");
        activity.show(getActivity(),"提交信息中...");
        RetrofitUtil.getInstance().apiService()
                .agentApply(etName.getText().toString(),etPhone.getText().toString(),activity.getType())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 1) {
                            initNetData(activity.getType());
                        } else if (result.getCode() == 2) {
                            activity.dismiss();
                            ToastUtil.showToast(getActivity(), "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                        } else {
                            activity.dismiss();
                            ToastUtil.showToast(getActivity(), result.getMsg());
                        }
                    }
//
                    @Override
                    public void onError(Throwable e) {
                        activity.dismiss();
                        ToastUtil.showToast(activity, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
