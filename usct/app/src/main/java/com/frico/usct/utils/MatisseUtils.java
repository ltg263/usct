package com.frico.usct.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import com.frico.usct.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.engine.impl.MyGlideEngine;
import com.zhihu.matisse.engine.impl.MyGlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

public class MatisseUtils {
    public static void selectImg(Context mContext, int requestCode, int selectedNum) {
        Matisse.from((Activity) mContext)
                .choose(MimeType.ofAll(), false)  //约束下选择额图片格式
                //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .theme(R.style.Matisse_Zhihu)
                //是否显示数字
                .countable(true)
                //最大选择资源数量
                .maxSelectable(selectedNum)
                //添加自定义过滤器
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //设置列宽
                .gridExpectedSize(mContext.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //图片缩放比例  默认0.5f
                //.thumbnailScale(0.85f)
                //选择图片加载引擎
                .imageEngine(new MyGlideEngine())
//                .imageEngine(new GlideEngine())
//                .imageEngine(new PicassoEngine())
                //是否可以拍照
//                .capture(true)
                .capture(false)
                .captureStrategy(new CaptureStrategy(true, "com.frico.usct.fileprovider"))
                .forResult(requestCode);
    }
    public static void selectImg(Context mContext, int requestCode, int selectedNum,boolean capture) {
        Matisse.from((Activity) mContext)
                .choose(MimeType.ofAll(), false)  //约束下选择额图片格式
                //选择主题 默认是蓝色主题，Matisse_Dracula为黑色主题
                .theme(R.style.Matisse_Zhihu)
                //是否显示数字
                .countable(true)
                //最大选择资源数量
                .maxSelectable(selectedNum)
                //添加自定义过滤器
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                //设置列宽
                .gridExpectedSize(mContext.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //图片缩放比例  默认0.5f
                //.thumbnailScale(0.85f)
                //选择图片加载引擎
                .imageEngine(new MyGlideEngine())
//                .imageEngine(new GlideEngine())
//                .imageEngine(new PicassoEngine())
                //是否可以拍照
//                .capture(true)
                .capture(capture)
                .captureStrategy(new CaptureStrategy(true, "com.frico.usct.fileprovider"))
                .forResult(requestCode);
    }
}
