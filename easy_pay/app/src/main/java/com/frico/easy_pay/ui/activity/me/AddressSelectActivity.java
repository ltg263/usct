package com.frico.easy_pay.ui.activity.me;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.adapter.PopRecycleAdapter;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.response.JsonCityBean;
import com.frico.easy_pay.utils.GetJsonDataUtil;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.ToastUtil;
import com.frico.easy_pay.widget.CustomPopWindow;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressSelectActivity extends BaseActivity implements ActionBarClickListener, PopupWindow.OnDismissListener {
    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;
    @BindView(R.id.ll_select_province)
    LinearLayout llSelectProvince;
    @BindView(R.id.iv_select_address)
    ImageView ivSelectAddress;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.tv_city)
    TextView tvCity;

    private CustomPopWindow mCustomPopWindow;

    private ArrayList<JsonCityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<JsonCityBean.ChildrenBeanX>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>>> options3Items = new ArrayList<>();

    private int cityIndex,provinceIndex;

    @Override
    protected int setLayout() {
        return R.layout.activity_address_select;
    }

    @Override
    public void initTitle() {
        actionBar.setData("位置选择", R.drawable.ic_left_back2x, null, 0, null, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionBar.setStatusBarHeight(getStatusBarHeight());
        }

    }

    @Override
    protected void initData() {
        initJsonData();
        if (Prefer.getInstance().isLocal()){
            ivSelectAddress.setSelected(true);
        }
        cityIndex=Prefer.getInstance().getCityIndex();
        provinceIndex = Prefer.getInstance().getProvinceIndex();

        if (Prefer.getInstance().getCity()!=""&&Prefer.getInstance().getProvince()!=""){
            tvCity.setText(Prefer.getInstance().getProvince()+Prefer.getInstance().getCity());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_select_province,  R.id.iv_select_address, R.id.tv_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_province:
                showCityPop();
                break;
            case R.id.iv_select_address:
                changeSelectorStatue();
                break;
            case R.id.tv_commit:
                commit();
                break;
        }
    }

    private void showProvincePop() {
        //创建并显示popWindow
        View view = View.inflate(this,R.layout.recycle_pop,null);
        RecyclerView recyclerView = view.findViewById(R.id.pop_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        PopRecycleAdapter adapter = new PopRecycleAdapter(this,options1Items);
        recyclerView.setAdapter(adapter);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)
                .size(llSelectProvince.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                // .setAnimationStyle()//动画
                .enableBackgroundDark(true)//背景是否变暗
                .setBgDarkAlpha(0.7f)//调整亮度
                .setOnDissmissListener(this)//
                .create()
                .showAsDropDown(llSelectProvince);
    }
    private void showCityPop() {


        OptionsPickerView pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2).getName();
                tvCity.setText(tx);
                provinceIndex = options1;
                cityIndex = option2;

            }
        }).build();
        pvOptions.setPicker(options1Items, options2Items);
        if (Prefer.getInstance().getCityIndex()!=0&&Prefer.getInstance().getProvinceIndex()!=0){
            pvOptions.setSelectOptions(Prefer.getInstance().getProvinceIndex(),Prefer.getInstance().getCityIndex());

        }
        pvOptions.show();
    }


    private void commit() {
        Prefer.getInstance().setProvinceIndex(provinceIndex);
        Prefer.getInstance().setCityIndex(cityIndex);
        Prefer.getInstance().setProvince(options1Items.get(provinceIndex).getName());
        Prefer.getInstance().setCity(options2Items.get(provinceIndex).get(cityIndex).getName());
        Prefer.getInstance().setLocal(ivSelectAddress.isSelected());


        //看起来更像是一个网络请求而不是本地
        show(this,"提交中");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                Toast.makeText(AddressSelectActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }, 1000);//1秒后执行Runnable中的run方法

    }

    private void changeSelectorStatue() {
        if (ivSelectAddress.isSelected()){
            ivSelectAddress.setSelected(false);
        }else {
            ivSelectAddress.setSelected(true);
        }
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
      
    }
    /**
     * 初始化解析城市数据
     */
    private void initJsonData() {

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "city-data.json");//获取assets目录下的json文件数据
        ArrayList<JsonCityBean> jsonCityBean = GetJsonDataUtil.parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonCityBean;

        for (int i = 0; i < jsonCityBean.size(); i++) {//遍历省份
            ArrayList<JsonCityBean.ChildrenBeanX> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            ArrayList<String> cityStringList = new ArrayList<>();
            ArrayList<ArrayList<String>> Province_StringAreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonCityBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市
                cityStringList.add(jsonCityBean.get(i).getChildren().get(c).getName());
                CityList.add(jsonCityBean.get(i).getChildren().get(c));

                ArrayList<JsonCityBean.ChildrenBeanX.ChildrenBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                ArrayList<String> City_StringAreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonCityBean.get(i).getChildren().get(c).getChildren() == null || jsonCityBean.get(i).getChildren().get(c).getChildren().size() == 0) {
                    City_AreaList.add(null);
                    City_StringAreaList.add("");
                } else {
                    for (int d = 0; d < jsonCityBean.get(i).getChildren().get(c).getChildren().size(); d++) {//该城市对应地区所有数据
                        City_AreaList.add(jsonCityBean.get(i).getChildren().get(c).getChildren().get(d));//添加该城市所有地区数据

                        City_StringAreaList.add(jsonCityBean.get(i).getChildren().get(c).getChildren().get(d).getName());
                    }
                }

                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                Province_StringAreaList.add(City_StringAreaList);
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    @Override
    public void onDismiss() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
