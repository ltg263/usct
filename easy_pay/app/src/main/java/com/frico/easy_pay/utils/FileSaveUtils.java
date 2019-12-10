package com.frico.easy_pay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.frico.easy_pay.dialog.LoadDialog;
import com.frico.easy_pay.impl.FileDownLaodListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.Call;

public class FileSaveUtils {

    private static int needDownloadSize = 0;//需要下载的图片数量
    private static int downloadedSize = 0;//已经下载的图片数量
    private static LoadDialog loadDialog;

    //系统相册目录
    public static String galleryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zxx/image";

    /**
     * 下载图片
     */
    public static void startDownSourceImg(Activity context, int size) {
        needDownloadSize = size;
        downloadedSize = 0;

        //先下载图片
        loadDialog = new LoadDialog(context, "保存中...");
        if (!((Activity) context).isFinishing()) {
            loadDialog.show();
        }
    }


    /**
     * 下载多张图片到本地相册
     */
    public static void saveMultipleImageToPhotos(Context context, String imgUrl, FileDownLaodListener laodListener) {
        String fileNmae = System.currentTimeMillis() + ".jpg";
        OkHttpUtils
                .get()
                .url(imgUrl)
                .build()
                .execute(new FileCallBack(galleryPath, fileNmae) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        needDownloadSize--;
                        if (downloadedSize >= needDownloadSize) {//所有图片下载完成
                            if (loadDialog != null)
                                loadDialog.dismiss();
                            laodListener.onSuccess();
                        }
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        downloadedSize++;
                        insertImageToSystem(context, response.getAbsolutePath(), response, fileNmae);
                        if (downloadedSize >= needDownloadSize) {//所有图片下载完成
                            if (loadDialog != null)
                                loadDialog.dismiss();
                            laodListener.onSuccess();
                        }
                    }
                });
    }


    /**
     * 下载单张图片到本地相册
     */
    public static void saveSingleImageToPhotos(Context context, String imgUrl, FileDownLaodListener laodListener) {
        String fileNmae = System.currentTimeMillis() + ".jpg";
        OkHttpUtils
                .get()
                .url(imgUrl)
                .build()
                .execute(new FileCallBack(galleryPath, fileNmae) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (loadDialog != null)
                            loadDialog.dismiss();
                        laodListener.onFail();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        insertImageToSystem(context, response.getAbsolutePath(), response, fileNmae);

                        if (loadDialog != null)
                            loadDialog.dismiss();
                        laodListener.onSuccess();
                    }
                });
    }

    /**
     * 图片插入相册
     *
     * @param context
     * @param imagePath
     * @return
     */
    public static String insertImageToSystem(Context context, String imagePath, File file, String fileName) {
        String url = "";
        try {
            if (context != null) {
                url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, fileName, null);
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

}