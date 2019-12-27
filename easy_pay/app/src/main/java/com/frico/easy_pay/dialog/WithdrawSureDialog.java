package com.frico.easy_pay.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 收单确认对话框
 */
public class WithdrawSureDialog extends BaseIncomeSureDialog {
    public WithdrawSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count){
        super.initViewData("提现确认","提现数量",count,"USCT",false,
                "");
    }


}
