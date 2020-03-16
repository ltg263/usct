package com.frico.usct.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收单/放币/确认付款 输入密码确认弹框
 */
public class BaseIncomeSureDialog extends Dialog {

    @BindView(R.id.tv_dialog_title)
    TextView tvDialogTitle;
    @BindView(R.id.tv_top_count_intro)
    TextView tvTopCountIntro;
    @BindView(R.id.tv_income_notify_money)
    TextView tvIncomeNotifyMoney;
    @BindView(R.id.tv_income_count_unit)
    TextView tvIncomeCountUnit;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.img_income_read_notification_check)
    CheckBox imgIncomeReadNotificationCheck;
    @BindView(R.id.tv_notification_red_content_txt)
    TextView tvNotificationRedContentTxt;
    @BindView(R.id.ll_income_read_notification_conent_lay)
    LinearLayout llIncomeReadNotificationConentLay;
    @BindView(R.id.tv_income_notify_cancel_btn)
    Button tvIncomeNotifyCancelBtn;
    @BindView(R.id.tv_income_notify_sure_btn)
    Button tvIncomeNotifySureBtn;

    private Activity mContext;
    private NotifyDialogListener mListener;


    public BaseIncomeSureDialog(@NonNull Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_income_notify_pw_input);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        Display display = mContext.getWindowManager().getDefaultDisplay();
        params.width = (int)(display.getWidth() * 0.9); //设置宽度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_income_notify_pw_input);
    }

    @Override
    public void show() {
        super.show();
        initView();
    }

    private void initView(){
        tvIncomeNotifyCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                if(mListener != null){
                    mListener.onCancel();
                }
                dismiss();
                clearInputPW();
            }
        });
        tvIncomeNotifySureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认
                onClickSureBtn();
                dismiss();
            }
        });
        imgIncomeReadNotificationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //是否显示确认
                showSureBtnStatus(b);
            }
        });

    }

    /**
     *  初始化对话框的内容
     * @param title                 标题
     * @param countIntro            数量介绍  ：例如：“收款金额”、USCT等
     * @param count                 数量
     * @param countUnit             数量单位 例如：元、个
     * @param isNeedCheckBox        是否需要checkbox
     * @param redNotifyContent      红色的提示文字内容
     */
    public void initViewData(String title,String countIntro,String count,String countUnit,boolean isNeedCheckBox,String redNotifyContent){
        tvDialogTitle.setText(title);
        tvTopCountIntro.setText(countIntro);
        tvIncomeNotifyMoney.setText(count+"");
        tvIncomeCountUnit.setText(countUnit);
        if(isNeedCheckBox){
            imgIncomeReadNotificationCheck.setChecked(false);
            tvIncomeNotifySureBtn.setEnabled(false);
        }else{
            imgIncomeReadNotificationCheck.setVisibility(View.GONE);
            tvIncomeNotifySureBtn.setEnabled(true);
        }
        if(!TextUtils.isEmpty(redNotifyContent)) {
            tvNotificationRedContentTxt.setText(redNotifyContent);
        }else{
            tvNotificationRedContentTxt.setText("");
        }
    }


    public void setOnNotifyListener(NotifyDialogListener listener){
        mListener = listener;
    }


    private void onClickSureBtn(){
        String pw = getInputPw();
        if(TextUtils.isEmpty(pw)){
            ToastUtil.showToast(mContext,"请输入密码");
            return;
        }
        if(mListener != null){
            mListener.onSure(pw);
        }
        clearInputPW();
    }



    private String getInputPw(){
        return etInputPassword.getText().toString().trim();
    }

    private void clearInputPW(){
        etInputPassword.setText("");
    }

    private void showSureBtnStatus(boolean isCanClick){
        tvIncomeNotifySureBtn.setEnabled(isCanClick);
    }




    public interface NotifyDialogListener{
        void onCancel();
        void onSure(String pw);
    }


}
