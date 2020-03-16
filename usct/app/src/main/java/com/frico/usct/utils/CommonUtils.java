package com.frico.usct.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明: 系统静态方法支持库
 */

public class CommonUtils {

    /**
     * 手机号和银行卡号的 加*显示
     */
    public enum ShowType {
        SHOW_PHONE,
        SHOW_CARD
    }

    public static String formatCon(String str, ShowType showType) {
        StringBuilder builder = new StringBuilder();
        switch (showType) {
            case SHOW_PHONE: {
                //手机显示格式化
                if (!TextUtils.isEmpty(str)) {
                    builder.append(str.substring(0, 3));
                    builder.append("****");
                    builder.append(str.substring(7, 11));
                }
            }
            break;
            case SHOW_CARD: {
                builder.append("**** ");
                builder.append("**** ");
                builder.append("**** ");
                builder.append(str.substring(str.length() - 4, str.length()));
            }
            break;
            default:
                break;
        }
        return builder.toString();
    }

    /**
     * 检测是否为身份证号码
     * @param cardStr
     * @return
     */
    public static boolean checkCard(String cardStr) {
        Pattern pattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = pattern.matcher(cardStr);
        //判断用户输入是否为身份证号
        return idNumMatcher.matches();
    }

    /**
     * 验卷，每四位自动跟横线（银行卡号 拼接空格）
     *
     * @param editText
     */
    public static void bankCardNumAddSpace(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            String beforeStr = "";
            String afterStr = "";
            String changeStr = "";
            int index = 0;
            boolean changeIndex = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                afterStr = s.toString();
                if (changeIndex)
                    index = editText.getSelectionStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()) || beforeStr.equals(afterStr)) {
                    changeIndex = true;
                    return;
                }
                changeIndex = false;
                char c[] = s.toString().replace(" ", "").toCharArray();
                changeStr = "";
                for (int i = 0; i < c.length; i++) {
                    changeStr = changeStr + c[i] + ((i + 1) % 4 == 0 && i + 1 != c.length ? " " : "");
                }
                if (afterStr.length() > beforeStr.length()) {
                    if (changeStr.length() == index + 1) {
                        index = changeStr.length() - afterStr.length() + index;
                    }
                    if (index % 5 == 0 && changeStr.length() > index + 1) {
                        index++;
                    }
                } else if (afterStr.length() < beforeStr.length()) {
                    if ((index + 1) % 5 == 0 && index > 0 && changeStr.length() > index + 1) {
                        //  index--;
                    } else {
                        index = changeStr.length() - afterStr.length() + index;
                        if (afterStr.length() % 5 == 0 && changeStr.length() > index + 1) {
                            index++;
                        }
                    }
                }
                editText.setText(changeStr);
                if (index >= 1 && index <= beforeStr.length()) {
                    editText.setSelection(index);
                }
            }
        });
    }

    /**
     * 手机号格式化 添加空格
     * @param editText
     */
    public static void phoneNumAddSpace(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;//记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '-') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = editText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == '-') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, "-");
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    editText.setText(str);
                    Editable etable = editText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    /**
     * 去掉字符中所有空格
     *
     * @param str
     * @return
     */
    public static String cleanSpaceAll(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * 格式化money + 元
     * @param money
     * @param i
     * @return
     */
    public static String obtainMoney(double money, int i) {
        BigDecimal bg = new BigDecimal(money);
        double f1 = bg.setScale(i, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1 + "元";
    }


    /**
     * 字符串去除空格
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        return str.replaceAll("\\s*", "");
    }

    /**
     * 保留两位小数
     * @param money
     * @return
     */
    public static String obtainMoneyFormat(double money) {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        double d = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return format.format(d);
    }


    /**
     * 保留四位小数
     * @param money
     * @return
     */
    public static String obtainMoney4Format(double money) {
        BigDecimal bigDecimal = new BigDecimal(money).setScale(4, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.toString();
    }

    public static String obtainTimeFormat01(long time) {
        DateFormat format;
        Date date = new Date(time);
        Calendar current = Calendar.getInstance();
        current.setTime(date);
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        if (current.after(today)) {
            format = new SimpleDateFormat("hh:mm");
            return "今天 " + format.format(date);
        } else {
            format = new SimpleDateFormat("MM:dd hh:mm");
            return format.format(date);
        }
    }

    public static String obtainDistance(double distance) {
        if (distance / 1000 >= 1) {
            double i = distance / 1000;
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(i) + "km";
        } else {
            return distance + "m";
        }
    }

    public static String obtainPicktime(long endDoortime) {
        if (endDoortime > System.currentTimeMillis()) {
            long time = endDoortime - System.currentTimeMillis();
            try {
                Integer ss = 1000;
                Integer mi = ss * 60;
                Integer hh = mi * 60;

                Long hour = (time) / hh;
                Long minute = (time - hour * hh) / mi;
                Long second = (time - hour * hh - minute * mi) / ss;

                StringBuilder builder = new StringBuilder();
                if (hour > 0) {
                    builder.append(hour + ":s");
                } else {
                    builder.append("00:");
                }
                if (minute > 0) {
                    builder.append(minute + ":");
                } else {
                    builder.append("00:");
                }
                if (second > 0) {
                    builder.append(second + "");
                } else {
                    builder.append("00");
                }

                return builder.toString();
            } catch (NumberFormatException e) {
                return "00:00:00";
            }
        } else {
            return "已超时";
        }
    }

    public static String obtainTimeFormat02(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }

    }

    public static String obtainTimeFormat08(long time) {
        DateFormat format = new SimpleDateFormat("yyyy年MM月");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }

    }

    public static String obtainTimeFormat04(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }

    }

    public static String obtainTimeFormat03(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }
    }

    public static String obtainTimeFormat05(long time) {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }
    }

    public static String obtainTimeFormat07(long time) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }
    }

    public static String obtainTimeFormat06(long time) {
        DateFormat format = new SimpleDateFormat("MM-dd");
        try {
            Date date = new Date(time);
            return format.format(date);
        } catch (Exception e) {
            return format.format(new Date());
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
        return result;
    }

    /**
     * function : 通过给定的图片路径生成对应的bitmap
     * author   : ZhaoYanDong
     * time     : 2017/3/28 0028
     * version  : 3.3.0
     * params   :
     * return   :
     */
    public static Bitmap obtainBitmap(String imgPath, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int widthSample = (int) (imageWidth / scale);
        int heightSample = (int) (imageHeight / scale);
        // 计算缩放比例
        options.inSampleSize = widthSample < heightSample ? heightSample
                : widthSample;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imgPath, options);
    }

    /**
     * 根据bitmap压缩图片质量
     *
     * @param bitmap 未压缩的bitmap
     * @return 压缩后的bitmap
     */
    public static Bitmap cQuality(Bitmap bitmap) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int beginRate = 100;
        //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        while (bOut.size() / (1024 * 1024) > 3) {  //如果压缩后大于3m，则提高压缩率，重新压缩
            beginRate -= 10;
            bOut.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, bOut);
        }
        ByteArrayInputStream bInt = new ByteArrayInputStream(bOut.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bInt);
        if (newBitmap != null) {
            return newBitmap;
        } else {
            return bitmap;
        }
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 图片压缩
     * @return
     */
    public static byte[] compressBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bao);
        byte[] buffer = bao.toByteArray();
        return buffer;
    }

    public static String compressBitmapStr(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bao);
        byte[] buffer = bao.toByteArray();
        return Base64.encodeToString(
                buffer, 0, buffer.length,
                Base64.DEFAULT
        );
    }

    /**
     * 判断男女性别
     * @param sexTag
     * @return
     */
    public static int obtainSexTag(String sexTag) {
        if ("男".equals(sexTag)) {
            return 1;
        } else if ("女".equals(sexTag)) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获取渠道名
     *
     * @param context 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            channelName = "deflaut_channel";
        }
        return channelName;
    }

    public static int obtainChannelId(Context context) {
        String channelName = getChannelName(context);
        int channeld;
        switch (channelName) {
            case "wandoujia":
                channeld = 1;
                break;

            case "baidu":
                channeld = 2;
                break;

            case "c360":
                channeld = 3;
                break;

            case "yingyongbao":
                channeld = 4;
                break;

            case "huawei":
                channeld = 5;
                break;

            case "xiaomi":
                channeld = 6;
                break;

            case "tuiguang":
                channeld = 7;
                break;
            default: {
                channeld = 0;
            }
            break;
        }
        return channeld;
    }

    /**
     * function:  判断app是否在后台
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}