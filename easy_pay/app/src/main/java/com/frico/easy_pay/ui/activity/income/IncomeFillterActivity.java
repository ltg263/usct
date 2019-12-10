package com.frico.easy_pay.ui.activity.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.TimeUtil;
import com.frico.easy_pay.widget.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收单记录筛选
 */
public class IncomeFillterActivity extends BaseActivity implements View.OnClickListener {
    public static final int TYPE_REQUEST_CODE = 1000;
    public static final int TYPE_WX = 3;
    public static final int TYPE_ZFB = 2;
    public static final int TYPE_LSM = 12;
    public static final int TYPE_ZSM = 5;
    public static final int TYPE_YSF = 4;
    public static final int TYPE_YHK = 1;
    public static final int TIME_TODAY = 20;
    public static final int TIME_YES = 21;
    public static final int TIME_THREE = 22;
    public static final int TIME_WEEK = 23;
    public static final int STATUS_SUCCESS = 3;
    public static final int STATUS_OVER_TIME = 0;

    public static String KEY_TYPE = "type";
    public static String KEY_TIME = "time";
    public static String KEY_STATUS = "status";

    public static String KEY_RESULT_START = "result_start";
    public static String KEY_RESULT_END = "result_end";
    public static String KEY_RESULT_TYPE = "result_type";
    public static String KEY_RESULT_STATUS = "result_status";



    @BindView(R.id.actionbar)
    TranslucentActionBar actionbar;
    @BindView(R.id.tv_income_type_wx)
    TextView tvIncomeTypeWx;
    @BindView(R.id.tv_income_type_zfb)
    TextView tvIncomeTypeZfb;
    @BindView(R.id.tv_income_type_lsm)
    TextView tvIncomeTypeLsm;
    @BindView(R.id.tv_income_type_zsm)
    TextView tvIncomeTypeZsm;
    @BindView(R.id.tv_income_type_ysf)
    TextView tvIncomeTypeYsf;
    @BindView(R.id.tv_income_type_yhk)
    TextView tvIncomeTypeYhk;
    @BindView(R.id.tv_income_time_today)
    TextView tvIncomeTimeToday;
    @BindView(R.id.tv_income_time_yesterday)
    TextView tvIncomeTimeYesterday;
    @BindView(R.id.tv_income_time_three)
    TextView tvIncomeTimeThree;
    @BindView(R.id.tv_income_time_week)
    TextView tvIncomeTimeWeek;
    @BindView(R.id.tv_income_status_success)
    TextView tvIncomeStatusSuccess;
    @BindView(R.id.tv_income_status_over_time)
    TextView tvIncomeStatusOverTime;
    @BindView(R.id.reset)
    TextView reset;
    @BindView(R.id.sure)
    TextView sure;

    private List<TextView> mIncomeTypeViewList = new ArrayList<>();
    private List<TextView> mIncomeTimeViewList= new ArrayList<>();
    private List<TextView> mIncomeStatusViewList= new ArrayList<>();

    private int mTypeFillter;
    private int mTimeFillter;
    private int mStatusFillter;

    private String mResultStartTime;
    private String mResultEndTime;
    private int mResultType;
    private int mResultStatus;


    public static void start(Activity activity){
        activity.startActivityForResult(new Intent(activity,IncomeFillterActivity.class),TYPE_REQUEST_CODE);
    }

    @Override
    protected int setLayout() {
        return R.layout.act_fillter_income_order;
    }

    @Override
    public void initTitle() {
        actionbar.setData("筛选", R.drawable.ic_left_back2x, null, 0, null, new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionbar.setStatusBarHeight(getStatusBarHeight());
        }

        mIncomeTypeViewList.add(tvIncomeTypeWx);
        mIncomeTypeViewList.add(tvIncomeTypeZfb);
        mIncomeTypeViewList.add(tvIncomeTypeLsm);
        mIncomeTypeViewList.add(tvIncomeTypeZsm);
        mIncomeTypeViewList.add(tvIncomeTypeYsf);
        mIncomeTypeViewList.add(tvIncomeTypeYhk);
        mIncomeTimeViewList.add(tvIncomeTimeToday);
        mIncomeTimeViewList.add(tvIncomeTimeYesterday);
        mIncomeTimeViewList.add(tvIncomeTimeThree);
        mIncomeTimeViewList.add(tvIncomeTimeWeek);
        mIncomeStatusViewList.add(tvIncomeStatusSuccess);
        mIncomeStatusViewList.add(tvIncomeStatusOverTime);
        sure.setOnClickListener(this);
        reset.setOnClickListener(this);
        setClickListener();
    }

    private void setClickListener(){
        for(int i = 0;i< mIncomeTypeViewList.size();i++){
            mIncomeTypeViewList.get(i).setOnClickListener(this);
        }
        for(int i = 0;i < mIncomeTimeViewList.size();i++){
            mIncomeTimeViewList.get(i).setOnClickListener(this);
        }

        for(int i = 0;i< mIncomeStatusViewList.size();i++){
            mIncomeStatusViewList.get(i).setOnClickListener(this);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if(vId == R.id.tv_income_type_wx){
            //微信
            setTypeSelect(view);
            mTypeFillter = TYPE_WX;
            mResultType = TYPE_WX;
        }else if(vId == R.id.tv_income_type_zfb){
            //支付宝
            setTypeSelect(view);
            mTypeFillter = TYPE_ZFB;
            mResultType = TYPE_ZFB;
        }else if(vId == R.id.tv_income_type_lsm){
            //农商码
            setTypeSelect(view);
            mTypeFillter = TYPE_LSM;
            mResultType = TYPE_LSM;
        }else if(vId == R.id.tv_income_type_zsm){
            //赞赏码
            setTypeSelect(view);
            mTypeFillter = TYPE_ZSM;
            mResultType = TYPE_ZSM;
        }else if(vId == R.id.tv_income_type_ysf){
            //云闪付
            setTypeSelect(view);
            mTypeFillter = TYPE_YSF;
            mResultType = TYPE_YSF;
        }else if(vId == R.id.tv_income_type_yhk){
            //银行卡
            setTypeSelect(view);
            mTypeFillter = TYPE_YHK;
            mResultType = TYPE_YHK;
        }else if(vId == R.id.tv_income_time_today){
            //当天
            setTimeSelect(view);
            mTimeFillter = TIME_TODAY;
        }else if(vId == R.id.tv_income_time_yesterday){
            //昨天
            setTimeSelect(view);
            mTimeFillter = TIME_YES;
        }else if(vId == R.id.tv_income_time_three){
            //最近三天
            setTimeSelect(view);
            mTimeFillter = TIME_THREE;
        }else if(vId == R.id.tv_income_time_week){
            //一周内
            setTimeSelect(view);
            mTimeFillter = TIME_WEEK;
        }else if(vId == R.id.tv_income_status_success){
            //成功
            setStatusSelect(view);
            mStatusFillter = STATUS_SUCCESS;
            mResultStatus = STATUS_SUCCESS;
        }else if(vId == R.id.tv_income_status_over_time){
            //超时
            setStatusSelect(view);
            mStatusFillter = STATUS_OVER_TIME;
            mResultStatus = STATUS_OVER_TIME;
        }else if(vId == R.id.reset){
            //重置
            resetSelect();
        }else if(vId == R.id.sure){
            //确定
            // 确定筛选
            commitSearchResult();
            setSearchResult();
        }
    }


    private void commitSearchResult(){
        initTimeParams();
    }

    private void setSearchResult(){
        Intent intent = new Intent();
        intent.putExtra(KEY_RESULT_START, mResultStartTime);
        intent.putExtra(KEY_RESULT_END, mResultEndTime);
        intent.putExtra(KEY_RESULT_TYPE,mResultType);
        intent.putExtra(KEY_RESULT_STATUS, mResultStatus);
        setResult(TYPE_REQUEST_CODE,intent);
        finish();
    }
    
    private void resetSelect(){
        resetSelectType();
        resetSelectTime();
        resetSelectStatus();
    }

    private void resetSelectStatus(){
        setListViewSelectedStatus(mIncomeStatusViewList,null);
    }

    private void setStatusSelect(View view){
        TextView textView = (TextView) view;
        setListViewSelectedStatus(mIncomeStatusViewList,textView);
    }
    
    private void resetSelectTime(){
        setListViewSelectedStatus(mIncomeTimeViewList,null);
    }

    private void setTimeSelect(View view){
        TextView textView = (TextView) view;
        setListViewSelectedStatus(mIncomeTimeViewList,textView);
    }

    private void resetSelectType(){
        setListViewSelectedStatus(mIncomeTypeViewList,null);
    }
    
    private void setTypeSelect(View view){
        TextView textView = (TextView) view;
        setListViewSelectedStatus(mIncomeTypeViewList,textView);
    }

    
    
    private void setListViewSelectedStatus(List<TextView> viewList,TextView selectedTv){
        for(int i=0;i< viewList.size();i++){
            setViewNormal(viewList.get(i));
        }
        if(selectedTv != null) {
            setViewSelected(selectedTv);
        }
    }

    private void setViewSelected(TextView tv){
        tv.setBackgroundResource(R.drawable.bg_fillter_item_s);
    }

    private void setViewNormal(TextView tv){
        tv.setBackgroundResource(R.drawable.bg_fillter_income_item);
    }


    private void initTimeParams(){
        String startTimeHms = " 00:00:00";
        String endTimeHms = " 23:59:59";
        String today = TimeUtil.getToday();
        mResultEndTime = today + startTimeHms;
        if(mTimeFillter == TIME_TODAY){
            mResultStartTime = today + endTimeHms;
        }else if(mTimeFillter == TIME_YES){
            String endData = TimeUtil.dateToTime3(TimeUtil.getOldDate(-1));
            mResultStartTime = endData + endTimeHms;
        }else if(mTimeFillter == TIME_THREE){
            String endData = TimeUtil.dateToTime3(TimeUtil.getOldDate(-3));
            mResultStartTime = endData + endTimeHms;
        }else if(mTimeFillter == TIME_WEEK){
            String endData = TimeUtil.dateToTime3(TimeUtil.getOldDate(-7));
            mResultStartTime = endData + endTimeHms;
        }
    }




}
