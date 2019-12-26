package com.frico.easy_pay.ui.activity.me.agency;

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

import com.frico.easy_pay.R;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.AdVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.ui.activity.base.BaseFragment;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
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

        initNetData(0);
        return view;
    }


    /**
     * 获取底部列表数据数据
     *
     * @param page
     */
    private void initNetData(int page) {
        LogUtils.w("---------------------------");
        if (true) {
            ll.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            switch (page){
                case 0://未审核
                    ll.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    submit.setText("提交申请");
                    break;
                case 1://审核中
                    iv.setImageResource(R.drawable.ic_agency_shz);
                    text.setText("您的商户合伙人申请正在审核中 \n请耐心等待...");
                    break;
                case 2://审核通过
                    iv.setImageResource(R.drawable.ic_agency_yes);
                    text.setText("恭喜您! \n您的商户合伙人申请审核已通过~");
                    break;
                case 3://审核失败
                    iv.setImageResource(R.drawable.ic_agency_no);
                    text.setText("很抱歉~ \n您的商户合伙人申请审核未通过");
                    submit.setVisibility(View.VISIBLE);
                    submit.setText("重新申请");
                    break;
            }
            return;
        }
        RetrofitUtil.getInstance().apiService()
                .advert(1, 0, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<AdVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<AdVO> result) {

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
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        if(submit.getText().toString().equals("重新申请")){

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
    }

}
