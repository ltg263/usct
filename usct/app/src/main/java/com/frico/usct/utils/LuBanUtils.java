package com.frico.usct.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class LuBanUtils {
    /**
     * 处理Uri类型
     *
     * @param mContext
     * @param uri
     * @param compressListener
     */
    public static void compressImg(Context mContext, Uri uri, final OnMyCompressListener compressListener) {
        Luban.with(mContext).load(new File(PathConvertUtils.uri2StringPath(mContext, uri)))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        compressListener.onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        compressListener.onError(e);
                    }
                })
                .launch();
    }

    /**
     * 处理String类型
     *
     * @param mContext
     * @param path
     * @param compressListener
     */
    public static void compressPathImg(Context mContext, String path, final OnMyCompressListener compressListener) {
        Luban.with(mContext).load(path)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        compressListener.onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        compressListener.onError(e);
                    }
                })
                .launch();
    }

    public interface OnMyCompressListener {
        /**
         * 压缩成功
         *
         * @param file
         */
        void onSuccess(File file);

        /**
         * 压缩失败
         *
         * @param e
         */
        void onError(Throwable e);
    }
}
