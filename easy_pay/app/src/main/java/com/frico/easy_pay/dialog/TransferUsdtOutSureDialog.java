package com.frico.easy_pay.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 提币 确认对话框
 */
public class TransferUsdtOutSureDialog extends BaseIncomeSureDialog {
    public TransferUsdtOutSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count,String transferId){
        super.initViewData("确认提币","USCT:",count,"个",
                false,null);
    }


}
