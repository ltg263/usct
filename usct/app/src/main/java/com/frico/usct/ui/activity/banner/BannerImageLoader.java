package com.frico.usct.ui.activity.banner;

import android.content.Context;
import android.widget.ImageView;

import com.frico.usct.utils.image.ImageLoaderImpl;
import com.frico.usct.utils.image.ImageLoaderOptions;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        new ImageLoaderImpl().loadImage(context, (String) path, new ImageLoaderOptions.Builder().build()).into(imageView);
    }
}
