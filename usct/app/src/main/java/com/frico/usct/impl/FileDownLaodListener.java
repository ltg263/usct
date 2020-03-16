package com.frico.usct.impl;

/**
 * [文件下载]
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public interface FileDownLaodListener {
    /**
     * 保存成功
     */
    void onSuccess();


    /**
     * 失败
     */
    void onFail();
}
