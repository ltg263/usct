package com.frico.easy_pay.ui.activity.banner;

import android.content.Context;
import android.widget.ImageView;

import com.frico.easy_pay.utils.image.ImageLoaderImpl;
import com.frico.easy_pay.utils.image.ImageLoaderOptions;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        new ImageLoaderImpl().loadImage(context, (String) path, new ImageLoaderOptions.Builder().build()).into(imageView);
    }
}
