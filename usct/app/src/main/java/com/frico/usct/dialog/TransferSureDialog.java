package com.frico.usct.dialog;

import android.app.Activity;

import androidx.annotation.NonNull;

/**
 * 转账 确认对话框
 */
public class TransferSureDialog extends BaseIncomeSureDialog {
    public TransferSureDialog(@NonNull Activity context) {
        super(context);
    }

    public void initViewData(String count, String transferId,String mToUserId) {
        if (mToUserId !=null && mToUserId.contains("wxp://")) {
            super.initViewData("确认转账", "USCT:", count, "个",
                    false, "确认向微信转账？");
            return;
        }
        if (mToUserId !=null && mToUserId.contains("qr.alipay.com")) {
            super.initViewData("确认转账", "USCT:", count, "个",
                    false, "确认向支付宝转账？");
            return;
        }

        super.initViewData("确认转账", "USCT:", count, "个",
                false, "确认向" + transferId + "转账？");
    }


}
