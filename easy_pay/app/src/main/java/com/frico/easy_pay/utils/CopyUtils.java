package com.frico.easy_pay.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;

/**
 * 复制内容到系统剪贴板
 */
public final class CopyUtils {
    /**
     * 复制文字
     *
     * @param label
     * @param text
     */
    public static void copyText(Context context, String label, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); //获取剪贴板管理器：
        ClipData clipData = ClipData.newPlainText(label, text);
        cm.setPrimaryClip(clipData);
    }

    /**
     * 复制url
     *
     * @param label
     * @param url
     */
    public static void copyUrl(Context context, String label, String url) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE); //获取剪贴板管理器：
        ClipData clipData = ClipData.newRawUri(label, Uri.parse(url));
        cm.setPrimaryClip(clipData);
    }
}
