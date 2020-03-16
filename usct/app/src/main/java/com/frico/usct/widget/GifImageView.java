package com.frico.usct.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.frico.usct.R;

/**
 * 自定义GifImageView
 */
public class GifImageView extends SimpleDraweeView {

    public GifImageView(Context context) {
        this(context, null);
    }

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int mId = 0;
        if (attrs != null) {
            TypedArray gifAttrs = context.obtainStyledAttributes(attrs, R.styleable.GifImageView);
            mId = gifAttrs.getResourceId(R.styleable.GifImageView_resId, 0);
            gifAttrs.recycle();
        }

        if (mId > 0) {
            setImageUriAndAutoPlay("res://drawable/" + mId);
        } else {
            setImageUriAndAutoPlay(null);
        }
    }

    public void setImageUriAndAutoPlay(String uri) {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true);
        if (!TextUtils.isEmpty(uri)) {
            builder.setUri(Uri.parse(uri));
        }
        setController(builder.build());
    }
}
