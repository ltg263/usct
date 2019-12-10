package com.frico.easy_pay.ui.activity.base;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;

/**
 * Created by d on 2016/12/2.
 */

public class BaseFragment extends Fragment {

    public String[] params = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        if (getSystemVersion() >= 19) {
            //获取status_bar_height资源的ID
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                return getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }


    public void launch(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
}
