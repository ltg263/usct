package com.frico.easy_pay.utils;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.frico.easy_pay.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

/**
 * 创建日期：2019/7/30 on 10:18
 * 作者:王红闯 hongchuangwang
 */
public class UCropUtils {
    public static void cropImg(Activity activity, Uri choosedUri, Uri outputUri) {
        //自定义裁剪样式
        UCrop uCrop = UCrop.of(choosedUri, outputUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimaryDark));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimaryDark));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1).start(activity);
    }
}
