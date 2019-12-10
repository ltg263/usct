package com.frico.easy_pay.ui.activity.update;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.frico.easy_pay.core.api.RetrofitUtil;
import com.frico.easy_pay.core.entity.Result;
import com.frico.easy_pay.core.entity.UpdateVO;
import com.frico.easy_pay.dialog.UpdateDialog;
import com.frico.easy_pay.core.entity.MbAppVersionVO;
import com.frico.easy_pay.utils.AppUtil;
import com.frico.easy_pay.utils.FixedToastUtils;
import com.frico.easy_pay.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * @date 2018/11/07
 * <p>描述:自动升级相关
 * **更新策略为强制更新**
 */
public class AutoUpgradeClient {
    private static final String TAG = "AutoUpgradeClient";
    /**
     * 请求读写权限的request code
     */
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10001;

    private static Context sContext;

    private static boolean isDownSuc = false;

    /**
     * 检查升级
     */
    public static void checkUpgrade(Context context, UpdateVO updateVO) {
        sContext = context;
        if(updateVO == null){
            return;
        }

        MbAppVersionVO mbAppVersionVO = new MbAppVersionVO();
        mbAppVersionVO.setPlatform("ANDROID");
        mbAppVersionVO.setVersion(getVersionCode());

        if (updateVO.getVersioncode() > getVersionCode()) {
            showUpdate(updateVO);
        }
    }

    /**
     * 提示升级
     */
    private static void showUpdate(UpdateVO mbAppVersionVO) {
        boolean isForce;
        if (mbAppVersionVO.getEnforce() == 1) {
            isForce = true;
        } else {
            isForce = false;
        }

        String apkUrl = mbAppVersionVO.getDownloadurl();
        String content = mbAppVersionVO.getContent();
        String newversion = mbAppVersionVO.getNewversion();

        UpdateDialog updateDialog = new UpdateDialog(sContext, content, "", isForce, new UpdateDialog.onButtonClickListener() {
            @Override
            public void positiveClick() {
                startDownload(newversion, apkUrl, content);
            }

            @Override
            public void negativeClick() {
                release();
            }
        });

        updateDialog.show();
    }

    /**
     * 统计新版本下载量
     *
     * @param newversion
     */
    private static void downCount(String newversion) {
        RetrofitUtil.getInstance().apiService()
                .downloadnum(newversion)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        LogUtils.e("--统计下载量--" + result.getCode());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 开始下载apk
     */
    private static void startDownload(String newversion, String apkUrl, String content) {
        if (sContext == null) {
            return;
        }

        //android 23 权限适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查权限
            if (ContextCompat.checkSelfPermission(sContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                ActivityCompat.requestPermissions((Activity) sContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                return;
            }
        }
        File file = getDownloadDir();
        final CustomUpdateProgressDialog progressDialog = new CustomUpdateProgressDialog(sContext);
        progressDialog.setMaxProgress(100);
        progressDialog.setCancelable(false);
        progressDialog.show();

        progressDialog.setUpdateFaileListener(new CustomUpdateProgressDialog.UpdateFaileListener() {
            @Override
            public void gotoBrowser() {
                if (isDownSuc) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(apkUrl);
                    intent.setData(content_url);
                    sContext.startActivity(intent);

                    release();
                } else {
                    if (sContext != null) {
                        FixedToastUtils.show(sContext, "下载中，不可操作");
                    }
                }

            }
        });
//
        OkHttpUtils//
                .get()//
                .url(apkUrl)//
                .build()//
                .execute(new FileCallBack(file.getAbsolutePath(), getSaveFileName(apkUrl))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e(TAG, "onError :" + e.getMessage());
                        LogUtils.e("自动升级 : download apk onFailure");
                        isDownSuc = true;
                        if (sContext != null) {
                            FixedToastUtils.show(sContext, "下载失败，点击提示文字信息进入浏览器下载apk");
                        }
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        LogUtils.e("download", "onResponse :" + file.getAbsolutePath());

                        downCount(newversion);
                        LogUtils.e("--下载完成，开始安装-- onDone: ");
                        progressDialog.dismiss();
                        File apkFile = new File(file,getSaveFileName(apkUrl));
                        installApk(apkFile.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total , int id)
                    {
                        progressDialog.setProgress((int) (100 * progress));
                    }
                });
    }

    /**
     * 安装应用
     */
    private static void installApk(String outPath) {
        LogUtils.e("----安装apk地址---" + outPath);
        if (sContext != null) {
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            File file = new File(outPath);
            //版本在7.0以上是不能直接通过uri访问的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                String providerAction = AppUtil.getPackage() +".fileprovider";//com.nmw.sct
                Uri apkUri = FileProvider.getUriForFile(sContext, providerAction, file);
                installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                installIntent.addCategory("android.intent.category.DEFAULT");
                installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            sContext.startActivity(installIntent);
        }
    }

    /**
     * 释放context对象
     */
    private static void release() {
        sContext = null;
    }

    /**
     * 获取版本号与版本名
     *
     * @return
     */
    public static synchronized Integer getVersionCode() {
        try {
            PackageManager manager = sContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(sContext.getApplicationContext().getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 文件保存名称
     *
     * @param downloadUrl
     * @return
     */
    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "stc.apk";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    /**
     * 获取文件名
     */
    private static File getDownloadDir() {
        File downloadDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            downloadDir = new File(Environment.getExternalStorageDirectory(), "update");
        } else {
            downloadDir = new File(sContext.getCacheDir(), "update");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }

}
