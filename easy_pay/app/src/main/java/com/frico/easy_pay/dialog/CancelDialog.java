package com.frico.easy_pay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frico.easy_pay.R;
import com.frico.easy_pay.config.Constant;
import com.frico.easy_pay.utils.ToastUtil;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public class CancelDialog extends Dialog implements View.OnClickListener {

    private OnButtonClick mButtonClick;

    TextView rightTv;
    TextView leftTv;
    LinearLayout ll_item_first;
    LinearLayout ll_item_firth;
    ImageView iv_first;
    ImageView iv_firth;

    boolean mIsClickDismiss = true;
    private String seasonType = "";
    private String seasonType_content = "";
    private Context context;

    public interface OnButtonClick {
        void onNegBtnClick();

        void onPosBtnClick(String type, String content);
    }

    public CancelDialog(Context context, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        this.context = context;
        init(context, buttonClick);
    }

    public void init(Context context, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_alert, null);

        ll_item_first = v.findViewById(R.id.ll_item_first);
        ll_item_firth = v.findViewById(R.id.ll_item_firth);

        iv_first = v.findViewById(R.id.iv_first);
        iv_firth = v.findViewById(R.id.iv_firth);

        rightTv = v.findViewById(R.id.confirm);
        leftTv = v.findViewById(R.id.cancel);

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);
        ll_item_first.setOnClickListener(this);
        ll_item_firth.setOnClickListener(this);
        setContentView(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (seasonType.equals("")) {
                    ToastUtil.showToast(context, "请选择取消原因");
                    return;
                }

                if (mButtonClick != null) {
                    mButtonClick.onPosBtnClick(seasonType, seasonType_content);
                }

                if (mIsClickDismiss) {
                    dismiss();
                }
                break;
            case R.id.cancel:
                if (mButtonClick != null) {
                    mButtonClick.onNegBtnClick();
                }
                if (mIsClickDismiss) {
                    dismiss();
                }

            case R.id.ll_item_first:
                iv_first.setImageDrawable(context.getResources().getDrawable(R.drawable.mb_address_selected2x));
                iv_firth.setImageDrawable(context.getResources().getDrawable(R.drawable.mb_address_normal2x));
                seasonType = Constant.seasonType_first;
                seasonType_content = "多拍/错拍/不想要";
                break;
            case R.id.ll_item_firth:
                iv_first.setImageDrawable(context.getResources().getDrawable(R.drawable.mb_address_normal2x));
                iv_firth.setImageDrawable(context.getResources().getDrawable(R.drawable.mb_address_selected2x));
                seasonType = Constant.seasonType_firth;
                seasonType_content = "其他";
                break;
            default:
                break;
        }

    }
}
