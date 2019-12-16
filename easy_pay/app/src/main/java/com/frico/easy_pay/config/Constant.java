package com.frico.easy_pay.config;

import com.frico.easy_pay.BuildConfig;

/**
 * 公共常量类
 */
public class Constant {
    public static boolean DEBUG_DEV = BuildConfig.DEBUG;
    public static String seasonType_first = "1";
    public static String seasonType_firth = "5";

    public static String URL = "url";
    public static String TITLE = "title";

    public static String START_ITEM_POSITION   = "start_item_position";//初始的Item位置
    public static String START_IAMGE_POSITION = "start_item_image_position"; //初始的图片位置

    public static String CURRENT_ITEM_POSITION   = "current_item_position";
    public static String CURRENT_IAMGE_POSITION = "current_item_image_position";

    //支付宝和微信的android app 包名
    public static final String LISTENING_TARGET_WX_PKG = "com.tencent.mm";
    public static final String LISTENING_TARGET_MMS_PKG = "com.android.mms";
    public static final String LISTENING_TARGET_ALI_PKG = "com.eg.android.AlipayGphone";
    public static final String LISTENING_TARGET_YSF_PKG = "com.unionpay";//云闪付

    public static final double RATE = 1;//USCT的汇率是1

    public static final String BULY_ID="11722feca8";
}
