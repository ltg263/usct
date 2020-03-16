package com.frico.usct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.frico.usct.R;

public class UpdateDialog {
    private Dialog mDialog;
    private TextView dialog_message, positive, negative;
    private onButtonClickListener listener;

    public UpdateDialog(final Context context, String message, String title, final boolean isForce, onButtonClickListener buttonClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_update, null);

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

        positive = (TextView) view.findViewById(R.id.yes);
        negative = (TextView) view.findViewById(R.id.no);

        //是否需要强制更新
        if (isForce) {
            setCancelable(false);
            negative.setVisibility(View.GONE);
        } else {
            setCancelable(true);
            negative.setVisibility(View.VISIBLE);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (buttonClickListener != null) {
                        buttonClickListener.negativeClick();
                    }
                }
            });
        }

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDialog.dismiss();
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
