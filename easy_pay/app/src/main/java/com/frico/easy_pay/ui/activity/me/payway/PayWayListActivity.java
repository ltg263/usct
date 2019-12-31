package com.frico.easy_pay.ui.activity.me.payway;

import android.content.Intent;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.dialog.SimpleDialog;
import com.google.gson.Gson;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.frico.easy_pay.R;
import com.frico.easy_pay.SctApp;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.core.BasePayWayListItemBean;
import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.BankVO;
import com.frico.easy_pay.core.entity.PayWayListVO;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.utils.BusAction;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.adapter.PayWayListAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.SwipeItemLayout;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PayWayListActivity extends BaseActivity implements ActionBarClickListener {
    public static final int PAY_WAY_BANK = 1;
    public static final int PAY_WAY_ZFB = 2;
    public static final int PAY_WAY_WX = 3;
    public static final int PAY_WAY_YSF = 4;
    public static final int PAY_WAY_ZSM = 5;

    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.ry_record)
    RecyclerView ryRecord;
    @BindView(R.id.tv_add)
    TextView tvAdd;

    private PayWayListAdapter payWayListAdapter;
    private List<PayWayListVO.ListBean> listBeanList = new ArrayList<PayWayListVO.ListBean>();
    private List<BankVO.ListBean> banklistBeanList = new ArrayList<BankVO.ListBean>();
    List<String> listDeleteIds = new ArrayList<>();
    List<String> listDeleteTypes = new ArrayList<>();
    @Override
    protected int setLayout() {
        return R.layout.activity_pay_way_list;
    }

    @Override
    public void initTitle() {
        actionbar.setData("收款方式", R.drawable.ic_left_back2x, null, 0, "编辑", this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }
    }

    @Override
    protected void initData() {
        ryRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ryRecord.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(PayWayListActivity.this));
        initRecyData();
        getPayCodeList();

        //添加收款方式
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.w(listDeleteIds.toString()+";"+listDeleteTypes.toString());

                if(tvAdd.getText().toString().equals("删除")){
                    if(listDeleteIds.size()==0){
                        new SimpleDialog(PayWayListActivity.this,"删除项不为空").show();
                        return;
                    }
                    SimpleDialog simpleDialog = new SimpleDialog(PayWayListActivity.this,
                            "是否确定删除?", "提示", "取消", "确定", new SimpleDialog.OnButtonClick() {
                        @Override
                        public void onNegBtnClick() {

                        }

                        @Override
                        public void onPosBtnClick() {
                            show(PayWayListActivity.this, "删除中...");
                            deleteBanks(listDeleteIds.get(0),listDeleteTypes.get(0),0);
                        }
                    });
                    simpleDialog.setCanceledOnTouchOutside(false);
                    simpleDialog.show();
                    return;
                }
                launch(AddPayWayActivity.class);
            }
        });
    }

    /**
     * 初始化列表样式
     */
    private void initRecyData() {
        payWayListAdapter = new PayWayListAdapter(R.layout.item_pay_list_layout);
        payWayListAdapter.bindToRecyclerView(ryRecord);
        //设置显示空布局, 一定要放在 适配器与recyclerView绑定 下面, 方式获取不到recyclerView对象
        payWayListAdapter.setEmptyView(R.layout.loading_layout);

        //控件的点击事件
        payWayListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            BasePayWayListItemBean listBean = payWayListAdapter.getData().get(position);
            switch (view.getId()) {
                case R.id.view:
                    switchBtn(listBean);
                    break;
                case R.id.ll_pay_bg:
                    if(listBean.getType() == BasePayWayListItemBean.TYPE_BANK){
                        //银行
                        int bankItemIndex = position;
                        if(listBeanList.size() > 0){
                            //列表前面是 二维码支付方式
                            bankItemIndex = position - listBeanList.size();
                        }
                        BankVO.ListBean bankBeanItem = banklistBeanList.get(bankItemIndex);
                        startActivity(new Intent(PayWayListActivity.this, AddBankActivity.class).putExtra("listBean", bankBeanItem));
                    }else{
                        PayWayListVO.ListBean  listBeanItem = listBeanList.get(position);
                        startActivity(new Intent(PayWayListActivity.this, AddZfbActivity.class).putExtra(Constant.TITLE,
                                listBean.getType() == 2 ? "编辑支付宝码" : "编辑微信码").putExtra("listBean", listBeanItem));
                    }
                    break;
                case R.id.img_pay_delete:
                    //删除支付方式
//                    show(this,"删除"+listBean.getNickName());
//                    deleteBank(listBean.getId()+"");
                    break;
                case R.id.tv_set_normal:
                    //设置默认
                    if(listBean.getType() == BasePayWayListItemBean.TYPE_CODE_ALIPAY || listBean.getType() == BasePayWayListItemBean.TYPE_CODE_WEIXIN) {
                        PayWayListVO.ListBean  listBeanItem = listBeanList.get(position);
                        boolean isAliPay = listBean.getType() == 2;
                        setNormalCode(listBeanItem.getId(),isAliPay);
                    }
                    break;
                case R.id.cb_select_del:
                    if(listDeleteIds.contains(listBean.getId())){
                        listDeleteIds.remove(listBean.getId());
                        listDeleteTypes.remove(listBean.getType());
                    }else{
                        listDeleteIds.add(listBean.getId());
                        if(listBean.getType()==1){
                            listDeleteTypes.add(listBean.getType()+"");
                        }else{
                            listDeleteTypes.add("2");
                        }
                    }
                    break;
            }
            return true;
        });
    }

    private void switchBtn(BasePayWayListItemBean listBean) {
        if(listBean.getType() == BasePayWayListItemBean.TYPE_BANK){
            switchBankBtn(listBean);
        }else{
            switchCodeBtn(listBean);
        }
    }

    /**
     * 点击开关
     *
     * @param listBean
     */
    private void switchCodeBtn(BasePayWayListItemBean listBean) {
        show(PayWayListActivity.this, listBean.getPayWayStatus().equals("normal") ? "关闭中..." : "打开中...");
        RetrofitUtil.getInstance().apiService()
                .paycodeswitch(listBean.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            getPayCodeList();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 点击开关
     *
     * @param listBean
     */
    private void switchBankBtn(BasePayWayListItemBean listBean) {
        show(PayWayListActivity.this, listBean.getPayWayStatus().equals("normal") ? "关闭中..." : "打开中...");
        RetrofitUtil.getInstance().apiService()
                .bankswitch(listBean.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            getPayCodeList();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    /**
     * 获取收款方式列表
     */
    private void getPayCodeList() {
        RetrofitUtil.getInstance().apiService()
                .paycodelist()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<PayWayListVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<PayWayListVO> result) {
                        LogUtils.e("--收款码--" + new Gson().toJson(result));
                        if (result.getCode() == 1) {
                            payWayListAdapter.getData().clear();
                            listBeanList = result.getData().getList();
                            payWayListAdapter.addData(createAdapterListFromCodeList(listBeanList));

                            if (payWayListAdapter.getItemCount() <= 1) {
                                payWayListAdapter.setEmptyView(R.layout.empty_layout);
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                        //获取银行卡列表
                        getBankList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                        getBankList();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 必须在 二维码支付方式到达后，再请求银行卡列表  否则，listitem 点击事件无法定位数据
     * 获取银行卡列表
     */
    private void getBankList() {
        RetrofitUtil.getInstance().apiService()
                .banklist()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result<BankVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<BankVO> result) {
                        if (result.getCode() == 1) {

                             banklistBeanList = result.getData().getList();
                            payWayListAdapter.addData(createAdapterListFromBankList(banklistBeanList));

                            if (payWayListAdapter.getItemCount() == 0) {
                                payWayListAdapter.setEmptyView(R.layout.empty_layout);
                            }
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 根据消息刷新数据
     *
     * @param data
     */
    @Subscribe(
            tags = {
                    @Tag(BusAction.PAY_WAY_LIST)
            }
    )
    public void getpayList(String data) {
        ryRecord.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPayCodeList();
            }
        }, 500);
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    public String getActionBarText(){
        return actionbar.getRightText();
    }

    @Override
    public void onRightClick() {
        if(listBeanList==null || listBeanList.size()==0){
            return;
        }
        if (actionbar.getRightText().equals("编辑")) {
            actionbar.setRight("取消");
            tvAdd.setText("删除");
        } else {
            actionbar.setRight("编辑");
            tvAdd.setText("+添加");
        }
        payWayListAdapter.getData().clear();
        payWayListAdapter.addData(createAdapterListFromCodeList(listBeanList));
        payWayListAdapter.addData(createAdapterListFromBankList(banklistBeanList));
    }

    private List<BasePayWayListItemBean> createAdapterListFromBankList(List<BankVO.ListBean> list){
        List<BasePayWayListItemBean> resultList = new ArrayList<>();
        if(list != null){
            BankVO.ListBean tempBean = null;
             tempBean = null;
            for(int i =0;i< list.size();i++){
                tempBean = list.get(i);
                BasePayWayListItemBean showItemBean = new BasePayWayListItemBean();
                showItemBean.setId(tempBean.getId());
                showItemBean.setType(BasePayWayListItemBean.TYPE_BANK);
                showItemBean.setNickName(tempBean.getAccountname());
                showItemBean.setAliasName(tempBean.getAlias());
                showItemBean.setCurrentSctCount(tempBean.getReceivemoney());
                showItemBean.setVerifyStatus(tempBean.getVerifystatus());
                showItemBean.setPayWayStatus(tempBean.getStatus());
                resultList.add(showItemBean);
            }
        }
        return resultList;
    }

    private List<BasePayWayListItemBean> createAdapterListFromCodeList(List<PayWayListVO.ListBean> list){
        List<BasePayWayListItemBean> resultList = new ArrayList<>();
        if(list != null){
            PayWayListVO.ListBean tempBean = null;
             tempBean = null;
            for(int i =0;i< list.size();i++){
                tempBean = list.get(i);
                BasePayWayListItemBean showItemBean = new BasePayWayListItemBean();
                showItemBean.setId(tempBean.getId());
                showItemBean.setType(tempBean.getCodetype());
                showItemBean.setNickName(tempBean.getAccountname());
                showItemBean.setAliasName(tempBean.getAlias());
                showItemBean.setCurrentSctCount(tempBean.getReceivemoney());
                showItemBean.setVerifyStatus(tempBean.getVerifystatus());
                showItemBean.setAlipayPID(tempBean.getAlipayPid());
                showItemBean.setPayWayStatus(tempBean.getStatus());
                showItemBean.setIsdefault(tempBean.getIsDefault());
                resultList.add(showItemBean);
            }
        }
        return resultList;
    }

    private void deleteBanks(String id,String type,int pos){
        RetrofitUtil.getInstance().apiService()
                .bankdel(id,type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if(pos < listDeleteIds.size()-1){
                            deleteBanks(listDeleteIds.get(pos+1),listDeleteTypes.get(pos+1),pos+1);
                            return;
                        }
                        dismiss();
                        if (result.getCode() == 1) {
                            if(pos == listDeleteIds.size()-1){
                                ToastUtil.showToast(PayWayListActivity.this, "删除成功");
                            }
                            getPayCodeList();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    private void setNormalCode(String id,boolean isAlipay) {
        String codeType = isAlipay ? "2":"3";
        show(PayWayListActivity.this, "提交中...");
        RetrofitUtil.getInstance().apiService()
                .setDefaultCode(id,codeType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        dismiss();
                        if (result.getCode() == 1) {
                            ToastUtil.showToast(PayWayListActivity.this, "操作成功");
                            getPayCodeList();
                        } else if (result.getCode() == 2) {
                            ToastUtil.showToast(PayWayListActivity.this, "登录失效，请重新登录");
                            SctApp.getInstance().gotoLoginActivity();
                            finish();
                        } else {
                            ToastUtil.showToast(PayWayListActivity.this, result.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismiss();
                        ToastUtil.showToast(PayWayListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
