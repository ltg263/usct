package com.frico.easy_pay.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 收单确认对话框
 */
public class IncomeSureDialog extends BaseIncomeSureDialog {
    public IncomeSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count){
        super.initViewData("收款确认","收款金额",count,"元",true,"请仔细核对收款金额，否则造成的损失平台概不负责！");
    }


}
