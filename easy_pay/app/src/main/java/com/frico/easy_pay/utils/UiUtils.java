package com.frico.easy_pay.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Process;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.frico.easy_pay.SctApp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiUtils {
    public final static String UTF_8 = "utf-8";

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        return !(value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim()));
    }

    /**
     * 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true
     */
    public static boolean isEquals(String... agrs) {
        String last = null;
        for (int i = 0; i < agrs.length; i++) {
            String str = agrs[i];
            if (isEmpty(str)) {
                return false;
            }
            if (last != null && !str.equalsIgnoreCase(last)) {
                return false;
            }
            last = str;
        }
        return true;
    }

    /**
     * 返回一个高亮spannable
     *
     * @param content 文本内容
     * @param color   高亮颜色
     * @param start   起始位置
     * @param end     结束位置
     * @return 高亮spannable
     */
    public static CharSequence getHighLightText(String content, int color,
                                                int start, int end) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        start = start >= 0 ? start : 0;
        end = end <= content.length() ? end : content.length();
        SpannableString spannable = new SpannableString(content);
        CharacterStyle span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 格式化文件大小，不保留末尾的0
     */
    public static String formatFileSize(long len) {
        return formatFileSize(len, false);
    }

    /**
     * 格式化文件大小，保留末尾的0，达到长度一致
     */
    public static String formatFileSize(long len, boolean keepZero) {
        String size;
        DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
        DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
        if (len < 1024) {
            size = String.valueOf(len + "B");
        } else if (len < 10 * 1024) {
            // [0, 10KB)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
        } else if (len < 100 * 1024) {
            // [10KB, 100KB)，保留一位小数
            size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
        } else if (len < 1024 * 1024) {
            // [100KB, 1MB)，个位四舍五入
            size = String.valueOf(len / 1024) + "KB";
        } else if (len < 10 * 1024 * 1024) {
            // [1MB, 10MB)，保留两位小数
            if (keepZero) {
                size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
                        / 1024 / (float) 100))
                        + "MB";
            } else {
                size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
                        + "MB";
            }
        } else if (len < 100 * 1024 * 1024) {
            // [10MB, 100MB)，保留一位小数
            if (keepZero) {
                size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
                        / 1024 / (float) 10))
                        + "MB";
            } else {
                size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
                        + "MB";
            }
        } else if (len < 1024 * 1024 * 1024) {
            // [100MB, 1GB)，个位四舍五入
            size = String.valueOf(len / 1024 / 1024) + "MB";
        } else {
            // [1GB, ...)，保留两位小数
            size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
                    + "GB";
        }
        return size;
    }

    /**
     * 正则表达式校验手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobileNumber(String str) {
        String tel = "[1][3578]\\d{9}";
        return str.matches(tel);
    }

    /**
     * 正则表达式校验座机号、400及800电话
     *
     * @param str
     * @return
     */
    public static boolean isTelNumber(String str) {
        //String tel = "(((\\d{3,4}\\-?)|)\\d{7,8}(|([-\\u8f6c]{1}\\d{1,5})?))|(400[0-9]{7})|(800[0-9]{7})";
        //return str.trim().matches(tel);
        return !TextUtils.isEmpty(str.trim());
    }

    /**
     * 正则表达式校验中国公民身份证号码
     *
     * @param str
     * @return
     */
    public static boolean isIdcardNumber(String str) {
        String idcardNumber = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        return str.matches(idcardNumber);
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param str
     * @return
     */
    public static String hideMobileNumber(String str) {
        return str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 隐藏身份证号年月日
     *
     * @param str
     * @return
     */
    public static String hideIdCardNumber(String str) {
        return str.replaceAll("(\\d+)(\\d{10})(\\d{2}|\\dX|\\dx)", "$1**********$3");
    }

    /**
     * 只保留银行卡号最后四位
     *
     * @param str
     * @return
     */
    public static String keepBankCardNumber(String str) {
        return str.replaceAll("(\\d+)(\\d{4})", "**** **** **** $2");
    }

    /**
     * 获取字符串后四位
     *
     * @param str
     * @return
     */
    public static String keepLast4Chars(String str) {
        int length = str.length();
        if (length > 4) {
            return str.substring(length - 4);
        }
        return str;
    }

    /**
     * 格式化数据
     *
     * @param format
     * @param args
     * @return
     */
    public static String formatLocale(String format, Object... args) {
        return String.format(format, args);
    }

    /**
     * 是否包含小数
     */
    public static boolean isDecimal(String str) {
        return str.matches("^([1-9]\\d*|0)(\\.\\d+)?$");
    }

    /**
     * 把集合数据拆分拼接成String
     */
    public static String implode(String[] strs, String tag) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String str : strs) {
            sb.append(str);
            if (i != strs.length - 1) {
                sb.append(tag);
            }
            i++;
        }
        return sb.toString();
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断输入是否是正确的金额格式
     * @param s
     * @return
     */
    public static boolean isDouble(String s) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(s);
        boolean bo = match.matches();
        return bo;
    }


    /**
     * 判断字符串是否同时包含数字和字母
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }

            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isRunOnMainUiThread() {
        int myTid = Process.myTid();
        if (myTid == SctApp.getInstance().getMainThreadId()) {
            return true;
        }
        return false;
    }

    /**
     * 让其运行在主线程
     *
     * @param r
     */
    public static void runOnUiThread(Runnable r) {
        if (isRunOnMainUiThread()) {
            //是主线程就直接运行
            r.run();
        } else {
            //不是主线程，借助handle让其运行在主线程（post给looper，而轮询器是在主线程运行的）
            SctApp.getInstance().getmHandler().post(r);
        }
    }

    /**
     * 让其运行在主线程
     *
     * @param r
     */
    public static void runOnUiNomainThread(Runnable r) {
        //不是主线程，借助handle让其运行在主线程（post给looper，而轮询器是在主线程运行的）
        SctApp.getInstance().getmHandler().post(r);
    }

    /**
     * 判断是否运行在后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public static boolean isTranslucentOrFloating(Activity activity) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            TypedArray ta = activity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean)m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * 修复横竖屏 crash 的问题
     * @return
     */
    public static boolean fixOrientation(Activity activity){
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo)field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
