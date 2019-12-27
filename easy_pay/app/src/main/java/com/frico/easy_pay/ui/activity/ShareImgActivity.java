package com.frico.easy_pay.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frico.easy_pay.R;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.Prefer;
import com.frico.easy_pay.utils.SavePhotoUtils;
import com.frico.easy_pay.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * h5分享图片页面
 */
public class ShareImgActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ShareImgActivity.class.getSimpleName();
    private static String KEY_QR_URL = "qrUrl";
    @BindView(R.id.icon_close)
    ImageView iconClose;

    @BindView(R.id.img_qr_code_img)
    ImageView imgQrCodeImg;
    @BindView(R.id.ll_bottom_lay)
    RelativeLayout llBottomLay;
    @BindView(R.id.img_btn_save_img)
    ImageView imgBtnSaveImg;
    @BindView(R.id.img_btn_share)
    ImageView imgBtnShare;
    @BindView(R.id.ll_bottom_btn_lay)
    LinearLayout llBottomBtnLay;
    @BindView(R.id.bottom_my_id_tv)
    TextView bottomMyIdTv;
    @BindView(R.id.bottom_invite_code_tv)
    TextView bottomInviteCodeTv;

    private String mQrCodeUrl;

    public static void start(BaseActivity activity, String qrCodeImgUrl) {
        Intent intent = new Intent(activity, ShareImgActivity.class);
        intent.putExtra(KEY_QR_URL, qrCodeImgUrl);
        activity.startActivity(intent);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_share_img;
    }

    @Override
    public void initTitle() {
        Intent intent = getIntent();
        mQrCodeUrl = intent.getStringExtra(KEY_QR_URL);
        imgBtnSaveImg.setOnClickListener(this);
        imgBtnShare.setOnClickListener(this);
        iconClose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Glide.with(ShareImgActivity.this).asBitmap().load(mQrCodeUrl).into(imgQrCodeImg);
        if(!TextUtils.isEmpty(Prefer.getInstance().getUserId())) {
            bottomMyIdTv.setText("我的ID:" + Prefer.getInstance().getUserId());
        }
        if(!TextUtils.isEmpty(Prefer.getInstance().getInviteCode())) {
            bottomInviteCodeTv.setText("邀请码:" + Prefer.getInstance().getInviteCode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.icon_close) {
            finish();
        } else if (vId == R.id.img_btn_share) {
            //分享
            gotoShareCurrent();
        } else if (vId == R.id.img_btn_save_img) {
            //保存到相册
            saveCurrentActView();
        }
    }

    private void gotoShareCurrent() {
        show(this, "请稍等...");
        hintBottomBtn();
        Bitmap bitmap = getCurrentShareImg();
        //截图完成就去分享
        shareImgByIntent(bitmap);
        showBttomBtn();
        dismiss();
    }


    private void saveCurrentActView() {
        show(this, "正在保存...");
        hintBottomBtn();
        saveImg(getCurrentActBitmap());
        showBttomBtn();
        ToastUtil.showToast(this, "保存相册成功！");
        dismiss();
    }

    private void saveImg(Bitmap bitmap) {
        if (bitmap == null) {
            LogUtils.e(TAG, "bitmap图片为空");
            return;
        }
        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};
        //检测是否有写的权限
        int permission = ContextCompat.checkSelfPermission(ShareImgActivity.this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(ShareImgActivity.this, PERMISSIONS, 1);
        }
        try {
            //创建savephoto类保存图片
            SavePhotoUtils savePhoto = new SavePhotoUtils(ShareImgActivity.this);
            savePhoto.saveBitmap(bitmap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void hintBottomBtn() {
        imgBtnShare.setVisibility(View.GONE);
        imgBtnSaveImg.setVisibility(View.GONE);
        iconClose.setVisibility(View.GONE);
    }

    private void showBttomBtn() {
        imgBtnSaveImg.setVisibility(View.VISIBLE);
        imgBtnShare.setVisibility(View.VISIBLE);
        iconClose.setVisibility(View.VISIBLE);
    }
}
