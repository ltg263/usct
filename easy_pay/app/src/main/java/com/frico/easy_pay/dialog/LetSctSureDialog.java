package com.frico.easy_pay.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 放币确认对话框
 */
public class LetSctSureDialog extends BaseIncomeSureDialog {
    public LetSctSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count){
        super.initViewData("放币确认","收款金额",count,"元",
                false,"请仔细核对收款金额，确认后USCT将转到对方账户");
    }


}
