package com.frico.usct.ui.activity.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frico.usct.R;


/**
 * @date 2018/11/08
 * <p>描述:自定义下载进度条
 */
public class CustomUpdateProgressDialog extends Dialog {
    private Context context;
    private TextView mProgress, downloadFaile;
    private ProgressBar mProgressBar;
    private int mCurrentProgress, mMaxProgress;
    private UpdateFaileListener updateFaileListener;

    public CustomUpdateProgressDialog(Context context) {
        super(context, R.style.CustomDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_progress, null, false);
        mProgressBar = view.findViewById(R.id.pb_dialog_progress);
        mProgress = view.findViewById(R.id.tv_dialog_progress);
        downloadFaile = view.findViewById(R.id.download_faile);
        if (mCurrentProgress != 0) {
            mProgressBar.setProgress(mCurrentProgress);
        }
        if (mMaxProgress != 0) {
            mProgressBar.setMax(mMaxProgress);
        } else {
            mProgressBar.setMax(100);
        }
        setProgress(mCurrentProgress);

        downloadFaile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateFaileListener != null){
                    updateFaileListener.gotoBrowser();
                }
            }
        });

        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = d.widthPixels; // 高度设置为屏幕的高
        lp.height = d.heightPixels; // 高度设置为屏幕的宽
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        dialogWindow.setAttributes(lp);
    }

    /**
     * 设置进度值
     *
     * @param progress int
     */
    public void setProgress(int progress) {
        mCurrentProgress = progress;
        if (mProgressBar != null) {
            mProgressBar.setProgress(progress);
            mProgress.setText(mCurrentProgress * 100 / mMaxProgress + "%");
        }
    }

    /**
     * 设置进度最大值
     *
     * @param progress int
     */
    public void setMaxProgress(int progress) {
        mMaxProgress = progress;
        if (mProgressBar != null) {
            mProgressBar.setMax(progress);
        }
    }

    public void setUpdateFaileListener(UpdateFaileListener updateFaileListener) {
        this.updateFaileListener = updateFaileListener;
    }

    public interface UpdateFaileListener {
        /**
         * 浏览器下载
         */
        void gotoBrowser();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
