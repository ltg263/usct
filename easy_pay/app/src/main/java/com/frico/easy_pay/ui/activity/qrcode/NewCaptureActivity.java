package com.frico.easy_pay.ui.activity.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frico.easy_pay.R;
import com.frico.easy_pay.impl.ActionBarClickListener;
import com.frico.easy_pay.ui.activity.base.BaseActivity;
import com.frico.easy_pay.ui.activity.me.setting.CertificationActivity;
import com.frico.easy_pay.utils.LogUtils;
import com.frico.easy_pay.utils.MatisseUtils;
import com.frico.easy_pay.widget.CustomPopWindow;
import com.frico.easy_pay.widget.TranslucentActionBar;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zhihu.matisse.Matisse;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chargepail.qz.www.qzzxing.activity.CaptureActivity;
import chargepail.qz.www.qzzxing.activity.CaptureFragment;
import chargepail.qz.www.qzzxing.activity.CodeUtils;
import chargepail.qz.www.qzzxing.camera.CameraManager;
import pub.devrel.easypermissions.EasyPermissions;

public class NewCaptureActivity extends BaseActivity implements ActionBarClickListener {
    @BindView(R.id.action_bar)
    TranslucentActionBar actionBar;
    @BindView(R.id.fl_zxing_container)
    FrameLayout flZxingContainer;
    @BindView(R.id.iv_open_light)
    ImageView ivOpenLight;

    private final int IMAGE_SELECT = 222;

    @Override
    protected int setLayout() {
        return R.layout.activity_capture_new;
    }

    @Override
    public void initTitle() {
        actionBar.setData("扫一扫", R.drawable.ic_left_back2x, null,
               // R.drawable.icon_menu,
                0,
                null, this);
        //actionbar.setNeedTranslucent(true,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionBar.setStatusBarHeight(getStatusBarHeight());
        }
        initView();
    }

    private void initView() {
        CaptureFragment captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
        captureFragment.setCameraInitCallBack(new CaptureFragment.CameraInitCallBack() {
            @Override
            public void callBack(Exception e) {
                if (e == null) {

                } else {
                    Log.e("TAG", "callBack: ", e);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_open_light)
    public void onViewClicked() {
        openOrClose();
    }

    @Override
    public void onLeftClick() {
        finish();
    }

    @Override
    public void onRightClick() {
        //showPop();
    }
    private CustomPopWindow popWindow;
    private static final int PERMISSION_CAMERA = 110;

    private void showPop() {
        View view = View.inflate(this,R.layout.pop_bottom,null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvSelectPhoto = view.findViewById(R.id.tv_select_photo);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dissmiss();
            }
        });
        tvSelectPhoto.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(NewCaptureActivity.this, params)) {

                    //具备权限 直接进行操作
                    MatisseUtils.selectImg(NewCaptureActivity.this, IMAGE_SELECT, 1);
                } else {
                    //权限拒绝 申请权限
                    EasyPermissions.requestPermissions(NewCaptureActivity.this, "为了您更好使用本应用，请允许应用获取以下权限", PERMISSION_CAMERA, params);
                }
            }
        });

        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                //     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .enableOutsideTouchableDissmiss(true)
                .size(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(0.5f)
                .create()//创建PopupWindow
                .showAtLocation(actionBar, Gravity.BOTTOM,0,0);//显示PopupWindow

    }

    private Camera camera;
    private Camera.Parameters parameter;
    private boolean isOpen = false;

    private void openOrClose(){
        //获取到ZXing相机管理器创建的camera
        camera = CameraManager.get().getCamera();
        parameter = camera.getParameters();

        if (isOpen) {
            ivOpenLight.setImageResource(R.drawable.icon_flashlight_open);
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
            isOpen = false;
        } else {  // 关灯
            ivOpenLight.setImageResource(R.drawable.icon_flashlight_close);
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
            isOpen = true;
        }
    }
    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            NewCaptureActivity.this.setResult(RESULT_OK, resultIntent);
            NewCaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            NewCaptureActivity.this.setResult(RESULT_OK, resultIntent);
            NewCaptureActivity.this.finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case IMAGE_SELECT:
                        Uri uri = Matisse.obtainResult(data).get(0);

                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + requestCode);
                }
            }

        }
    }

    /*private void handleAlbumPic(Intent data) {
        //获取选中图片的路径
        final Uri uri = data.getData();



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Result result = scanningImage(uri);
                if (result != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.INTENT_EXTRA_KEY_QR_SCAN ,result.getText());

                    resultIntent.putExtras(bundle);
                    CaptureActivity.this.setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(NewCaptureActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    *//**
     * 扫描二维码图片的方法
     * @param path
     * @return
     *//*
    public Result scanningImage(Uri uri) {
        if (uri == null) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //设置二维码内容的编码

        scanBitmap = BitmapUtil.decodeUri(this, uri, 500, 500);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}
