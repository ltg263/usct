package com.frico.usct.utils;

import android.content.pm.PackageInfo;

import com.frico.usct.SctApp;

public class AppUtil {

    /**
     * 获取App安装包信息
     */
    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = SctApp.getInstance().getPackageManager().getPackageInfo(SctApp.getInstance().getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    public static String getPackage(){
        return getPackageInfo().packageName;
    }


}
