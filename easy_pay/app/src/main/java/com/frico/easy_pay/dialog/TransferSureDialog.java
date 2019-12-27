package com.frico.easy_pay.dialog;

import android.app.Activity;
import androidx.annotation.NonNull;

/**
 * 转账 确认对话框
 */
public class TransferSureDialog extends BaseIncomeSureDialog {
    public TransferSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count,String transferId){
        super.initViewData("确认转账","USCT:",count,"个",
                false,"确认向"+ transferId +"转账？");
    }


}
