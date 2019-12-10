package com.frico.easy_pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.frico.easy_pay.R;

public class NotificationDialog {
    private Dialog mDialog;
    private TextView dialog_message, positive, negative;
    private onButtonClickListener listener;

    public NotificationDialog(final Context context, String message, onButtonClickListener buttonClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_notification, null);

        mDialog = new Dialog(context, R.style.simpleDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        dialog_message = (TextView) view.findViewById(R.id.dialog_message);
        String contentStr ="";
        if(!TextUtils.isEmpty(message)) {
            //双斜杠替换为单斜杠的
            contentStr = message.replace("\\n", "\n");
        }
        dialog_message.setText(contentStr);

        negative = (TextView) view.findViewById(R.id.no);
        setCancelable(false);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (buttonClickListener != null) {
                    buttonClickListener.positiveClick();
                }
            }
        });
    }

    /**
     * 弹窗消失监听
     *
     * @param listener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }

    /**
     * 点击外部是否可取消
     *
     * @param flag
     */
    public void setCancelable(boolean flag) {
        mDialog.setCancelable(flag);
        if (!flag) {
            mDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public interface onButtonClickListener {
        void positiveClick();

        void negativeClick();
    }
}
