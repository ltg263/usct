package com.frico.usct.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 转账 确认对话框
 */
public class TransferUsdtSureDialog extends BaseIncomeSureDialog {
    public TransferUsdtSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count,String transferId){
        super.initViewData("确认充值","USCT:",count,"个",
                false,null);
    }


}
