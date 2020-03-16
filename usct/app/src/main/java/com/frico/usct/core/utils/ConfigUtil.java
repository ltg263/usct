package com.frico.usct.core.utils;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class ConfigUtil {
    public static void initConfig(Application c) {
        try {
            ApplicationInfo appInfo = c.getPackageManager()
                    .getApplicationInfo(c.getPackageName(),
                            PackageManager.GET_META_DATA);
            Constants.BASE_URL = appInfo.metaData.getString("DEFAULT_HTTP_SERVER_URL");
            Constants.APP_HOST = appInfo.metaData.getString("APP_HOST");
            Constants.APP_PORT = appInfo.metaData.getInt("APP_PORT");
            Constants.BASE_URL_H5 = appInfo.metaData.getString("DEFAULT_H5_SERVER_URL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetSocketIp(String appSocketHost){
        Constants.APP_HOST = appSocketHost;
    }
}
