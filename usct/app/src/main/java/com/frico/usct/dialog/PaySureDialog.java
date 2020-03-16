package com.frico.usct.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 已付款 确认对话框
 */
public class PaySureDialog extends BaseIncomeSureDialog {
    public PaySureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count){
        super.initViewData("确认付款","USCT:",count,"个",
                false,"请确保您的转账金额与订单金额一致，如未付款点击该按钮，可能会被系统列如黑名单");
    }


}
