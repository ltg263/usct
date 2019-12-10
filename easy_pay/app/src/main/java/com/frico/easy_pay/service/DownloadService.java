//package com.nmw.sct.service;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.IBinder;
//import android.provider.ContactsContract;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//
//import com.aliyun.common.utils.L;
//
//import java.io.File;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import retrofit2.HttpException;
//
///**
// * 下载文件服务
// */
//public class DownloadService extends Service {
//    private static final String TAG = "UpdateService";
//    private static final String KEY_DOWNLOAD_URL = "downloadUrl";
//    private static final String KEY_LOCAL_PATH = "localPath";
//    private static final String ACTION_DOWNLOAD = "com.qz.qian.DownloadService";
//    private static boolean sIsDoanloading;
//
//    /**
//     * 开启下载服务
//     *
//     * @param context
//     * @param downloadUrl 下载路径
//     * @param localPath 本地保存路径
//     */
//    public static void start(Context context, String downloadUrl, String localPath) {
//        if (TextUtils.isEmpty(downloadUrl)) {
//            throw new IllegalArgumentException("Download Url cannot be null");
//        } else if (TextUtils.isEmpty(localPath)) {
//            throw new IllegalArgumentException("Local Path cannot be null");
//        }
//
//        Intent intent = new Intent(context, DownloadService.class);
//        intent.setAction(ACTION_DOWNLOAD);
//        intent.putExtra(KEY_DOWNLOAD_URL, downloadUrl);
//        intent.putExtra(KEY_LOCAL_PATH, localPath);
//        context.startService(intent);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null) {
//            String action = intent.getAction();
//            if (TextUtils.equals(ACTION_DOWNLOAD, action)) {
//                String downloadUrl = intent.getStringExtra(KEY_DOWNLOAD_URL);
//                String localPath = intent.getStringExtra(KEY_LOCAL_PATH);
//                startDownload(downloadUrl, localPath);
//                return START_REDELIVER_INTENT;
//            }
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    /**
//     * 开始下载APP文件
//     */
//    private void startDownload(final String downloadUrl, final String localPath) {
//        if (TextUtils.isEmpty(downloadUrl) || TextUtils.isEmpty(localPath)) {
//            return;
//        }
//        if(sIsDoanloading) {
//            return;
//        }
//        // 下载中
//        sIsDoanloading = true;
//        /**
//         * 检查本地文件是否下载完成，没下载则重新下载
//         */
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    URL netUrl = new URL(downloadUrl);
//                    HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();
//                    // 服务器文件长度
//                    int netFileLength = connection.getContentLength();
//                    // 本地文件
//                    File localFile = new File(localPath);
//                    if (localFile.exists()) {
//                        int localFileLength = (int) localFile.length();
//                        // 文件长度匹配
//                        if (localFileLength == netFileLength) {
//                            Profile.setFileDownloadStatus(downloadUrl, UpdateFileBean.STATUS_SUCCESS);
//                        } else {
//                            Profile.setFileDownloadStatus(downloadUrl, "");
//                            download(downloadUrl, localPath);
//                        }
//                    } else {
//                        download(downloadUrl, localPath);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    /**
//     * 下载文件
//     *
//     * @param url
//     * @param localPath
//     */
//    private void download(final String url, final String localPath) {
//        HttpUtils httpUtils = new HttpUtils();
//        httpUtils.download(url, localPath, new RequestCallBack<File>() {
//            /**
//             * 开始下载，展示通知
//             */
//            @Override
//            public void onStart() {
//                super.onStart();
//                // 下载中
//                sIsDoanloading = true;
//
//                UpdateReceiver.sendUpdateStartBroadcast(DownloadService.this);
//                ContactsContract.Profile.setFileDownloadStatus(url, "");
//
//                new UpdateNotification(DownloadService.this).show();
//            }
//
//            /**
//             * 更新进度
//             * @param total
//             * @param current
//             * @param isUploading
//             */
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                super.onLoading(total, current, isUploading);
//                // 下载中
//                sIsDoanloading = true;
//
//                // 百分比
//                int progress = (int) (((double) current / (double) total) * 100);
//                UpdateReceiver.sendUpdateProgressBroadcast(DownloadService.this, progress);
//
//                if (!TextUtils.equals(Profile.getFileDownloadStatus(url), UpdateFileBean.STATUS_DOWNLOADING)) {
//                    Profile.setFileDownloadStatus(url, UpdateFileBean.STATUS_DOWNLOADING);
//                }
//
//                L.i(TAG, "current = " + current + ", total = " + total);
//            }
//
//            /**
//             * 下载成功
//             * @param responseInfo
//             */
//            @Override
//            public void onSuccess(ResponseInfo<File> responseInfo) {
//                L.i(TAG, "download success");
//                sIsDoanloading = false;
//
//                UpdateReceiver.sendUpdateSuccessBroadcast(DownloadService.this, localPath);
//                Profile.setFileDownloadStatus(url, UpdateFileBean.STATUS_SUCCESS);
//
//                // 下载完成跳转到安装界面
//                Intent installIntent = IntentUtils.getInstallIntent(localPath);
//                if(installIntent != null) {
//                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(installIntent);
//                }
//            }
//
//            /**
//             * 下载失败
//             * @param e
//             * @param s
//             */
//            @Override
//            public void onFailure(HttpException e, String s) {
//                L.i(TAG, "download failure");
//                sIsDoanloading = false;
//
//                String failureMsg = "下载失败...";
//                UpdateReceiver.sendUpdateFailureBroadcast(DownloadService.this, failureMsg);
//                ContactsContract.Profile.setFileDownloadStatus(url, UpdateFileBean.STATUS_FAILURE);
//            }
//
//            @Override
//            public void onCancelled() {
//                L.i(TAG, "download cancel");
//                sIsDoanloading = false;
//
//                super.onCancelled();
//                ContactsContract.Profile.setFileDownloadStatus(url, UpdateFileBean.STATUS_FAILURE);
//            }
//        });
//    }
//
//    public static boolean isDownloading() {
//        return sIsDoanloading;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//}
