package com.frico.easy_pay.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtil {
    public static String getToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date());
        return dateString;
    }

    public static String timeLeft(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


        long timeStamp = date.getTime();
        Date currentTime = new Date();
        long currentTimeStamp = currentTime.getTime();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;
        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (days > 0) {
            leftTime = days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (hours > 0) {
            leftTime = hours + "小时" + minutes + "分" + seconds + "秒";
        } else if (minutes > 0) {
            leftTime = minutes + "分" + seconds + "秒";
        } else if (seconds > 0) {
            leftTime = seconds + "秒";
        } else {
            leftTime = "0秒";
        }

        return leftTime;

    }

    public static String timeCountDown(String timeStr) {
        long timeStamp = Long.parseLong(timeStr);
        long currentTimeStamp = System.currentTimeMillis();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (hours > 0) {
            if (minutes < 10) {
                if (seconds < 10) {
                    leftTime = hours + ":0" + minutes + ":0" + seconds;
                } else {
                    leftTime = hours + ":0" + minutes + ":" + seconds;
                }
            } else {
                if (seconds < 10) {
                    leftTime = hours + ":" + minutes + ":0" + seconds;
                } else {
                    leftTime = hours + ":" + minutes + ":" + seconds;
                }
            }

        } else if (minutes > 0) {
            if (minutes < 10) {
                if (seconds < 10) {
                    leftTime = "00:0" + minutes + ":0" + seconds;
                } else {
                    leftTime = "00:0" + minutes + ":" + seconds;
                }
            } else {
                if (seconds < 10) {
                    leftTime = "00:" + minutes + ":0" + seconds;
                } else {
                    leftTime = "00:" + minutes + ":" + seconds;
                }
            }
        } else {
            if (seconds < 10) {
                leftTime = "00:00:" + "0" + seconds;
            } else {
                leftTime = "00:00:" + "" + seconds;
            }
        }

        return leftTime;

    }

    //倒计时0秒了
    public static boolean isCountDownSecond(String timeStr) {
        long timeStamp = Long.parseLong(timeStr);
        long currentTimeStamp = System.currentTimeMillis();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param timeStr 毫秒值
     * @return
     */
    public static String timeCountDownMinuteSecond(String timeStr) {
        return timeCountDownMinuteSecond(Long.parseLong(timeStr));
    }

    /**
     *
     * @param timeLong  毫秒值
     * @return
     */
    public static String timeCountDownMinuteSecond(long timeLong) {
        long timeStamp = timeLong;
        long currentTimeStamp = System.currentTimeMillis();

        long total_seconds = (timeStamp - currentTimeStamp) / 1000;

        if (total_seconds <= 0) {
            return "00:00";
        }

        long days = Math.abs(total_seconds / (24 * 60 * 60));

        long hours = Math.abs((total_seconds - days * 24 * 60 * 60) / (60 * 60));
        long minutes = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60) / 60);
        long seconds = Math.abs((total_seconds - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60));
        String leftTime;
        if (hours > 0) {
            if (minutes < 10) {
                if (seconds < 10) {
                    leftTime = hours + ":0" + minutes + ":0" + seconds;
                } else {
                    leftTime = hours + ":0" + minutes + ":" + seconds;
                }
            } else {
                if (seconds < 10) {
                    leftTime = hours + ":" + minutes + ":0" + seconds;
                } else {
                    leftTime = hours + ":" + minutes + ":" + seconds;
                }
            }

        } else if (minutes > 0) {
            if (minutes < 10) {
                if (seconds < 10) {
                    leftTime = "0" + minutes + ":0" + seconds;
                } else {
                    leftTime = "0" + minutes + ":" + seconds;
                }
            } else {
                if (seconds < 10) {
                    leftTime = "" + minutes + ":0" + seconds;
                } else {
                    leftTime = "" + minutes + ":" + seconds;
                }
            }
        } else {
            if (seconds < 10) {
                leftTime = "00:" + "0" + seconds;
            } else {
                leftTime = "00:" + "" + seconds;
            }
        }
        return leftTime;
    }


    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static int timeToage(String time) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(time);
            return getAge(date);
        } catch (Exception e) {
            e.printStackTrace();
            return -0;
        }
    }

    /**
     * 计算年龄
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        }

        return age;
    }

    /**
     * 当周第一天
     *
     * @return
     */
    public static String getFirstDayfWeek(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//设置周一
        gcLast.setFirstDayOfWeek(Calendar.SUNDAY);
        String day_first = df.format(gcLast.getTime());
        return day_first.toString();

    }

    /**
     * 当周最后一天
     *
     * @return
     */
    @SuppressLint("WrongConstant")
    public static String getLastDayofWeek(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.setFirstDayOfWeek(Calendar.SUNDAY);
        gcLast.set(Calendar.DAY_OF_WEEK, gcLast.getFirstDayOfWeek() + 6); // Sunday
        String day_first = df.format(gcLast.getTime());
        return day_first.toString();

    }

    /**
     * 当月第一天
     *
     * @return
     */
    public static String getFirstDayofMonth(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        day_first += " 00:00:00";
        return day_first.toString();

    }

    /**
     * 获取某月的最后一天
     *
     * @throws
     * @Title:getLastDayOfMonth
     * @Description:
     * @param:@param year
     * @param:@param month
     * @param:@return
     * @return:String
     */
    public static String getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        lastDayOfMonth += " 23:59:59";

        return lastDayOfMonth;
    }


    /**
     * 某年第一天
     *
     * @return
     */
    public static String getFirstDayofYear(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_YEAR, 1);
        String day_first = df.format(gcLast.getTime());
        day_first += " 00:00:00";
        return day_first.toString();

    }

    /**
     * 获取某年的最后一天
     *
     * @throws
     * @Title:getLastDayOfMonth
     * @Description:
     * @param: date
     * @return:String
     */
    public static String getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_YEAR, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        lastDayOfMonth += " 23:59:59";

        return lastDayOfMonth;
    }

    /**
     * 某一个月第一天和最后一天
     *
     * @param date
     * @return
     */
    private static int getDayOfMonth(Date date) {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.setTime(date);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;

    }


    /**
     * ******* Time *********
     */

    public static long currentTimeInMillis() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

    public static String cal(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        return unitFormat(h) + ":" + unitFormat(d) + ":" + unitFormat(s);
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * @param timeStr
     * @return yyyy-MM-dd HH:mm
     */
    public static String time(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 根据时间获取周几
     *
     * @param pTime
     * @return
     */
    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        String week = null;
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    //返回格式 yyyy/MM/dd
    public static String formatTimeYMD(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    //返回格式 yyyy.MM.dd
    public static String formatTimePointYMD(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    //返回格式 yyyy.MM.dd
    public static String formatTimePointYMD_Birth(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    //返回格式 MM/dd
    public static String formatTimeMD(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        String dateString = formatter.format(date);
        return dateString;
    }


  //返回格式 MM-dd HH:mm
    public static String formatTimeMD2(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    //返回格式  HH:mm
    public static String formatTimeMD3(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }


    public static String formatTimeYMDM(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    //返回格式 MM/dd HH:mm
    public static String formatTimeMDHM(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }



    /**
     *
     * @param timeStr  yyyy-MM-dd HH:mm:ss
     * @param formateStr  例如 MM-dd HH:mm:ss
     * @return  根据上面的格式返回
     */
    public static String formatTimeByFormate(String timeStr,String formateStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formateStr);
        String dateString = formatter.format(date);
        return dateString;
    }


    //返回格式 yyyy/MM/dd HH:mm
    public static String formatTimeYMDHM(String timeStr) {
        return formatTimeByFormate(timeStr, "yyyy/MM/dd HH:mm");
    }

    //返回格式 yyyy/MM/dd HH:mm

    /**
     *
     * @param timeStr  yyyy-MM-dd HH:mm:ss
     * @return  HH:mm:ss
     */
    public static String formatTimeHMS(String timeStr) {
        return formatTimeByFormate(timeStr, "HH:mm:ss");
    }

    //返回格式 MM/dd HH:mm:ss
    public static String formatTimeHms(String timeStr) {
        return formatTimeByFormate(timeStr, "MM/dd HH:mm:ss");
    }

    public static String formatVideoTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    public static String formatVideoTimeHMS(long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date(time));
    }

    public static String dateformatTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * return XX月XX日
     *
     * @param date
     * @return
     */
    public static String dateformatTime2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * @param timeStr
     * @return -1 过去时间
     * 0 今天
     * 1 将来时间
     */
    public static int getTimeAgo(String timeStr) {
        long oneDay = 24 * 60 * 60 * 1000; // 一天时间

        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return 1;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateString = formatter.format(date);

        // 当前时间，从当天0时算起
        Date todayDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayTime = format.format(todayDate);
        Date today = null;
        try {
            today = format.parse(todayTime);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 目标时间
        Date targetDate = null;
        try {
            SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            targetDate = formatTime.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return 1;
        }

        if (targetDate.getTime() - today.getTime() >= 0 && targetDate.getTime() - today.getTime() < oneDay) {
            return 0;
        } else if (targetDate.getTime() - today.getTime() >= oneDay) {
            return 1;
        } else if (targetDate.getTime() - today.getTime() < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * 根据时间获取周几
     *
     * @param pTime
     * @return
     */
    public static String dayForWeekStr(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        String week = null;
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    public static String getMonth(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(pTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int month = date.getMonth() + 1;//月

        return month + "";
    }

    public static String getDay(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(pTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int day = date.getDate();//日
        return day + "";
    }

    //返回格式 MM-dd
    public static String formatHistoryTime(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static Long timeTotime(Long starTime) {
        System.currentTimeMillis();
        Long s = (System.currentTimeMillis() - starTime);
        return s;

    }

    public static String dateToTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ZZZ");
        String dateString = formatter.format(date.getTime());
        return dateString;
    }

    /**
     * @param date
     * @return yyyy/MM/dd
     */
    public static String dateToTime2(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(date.getTime());
        return dateString;
    }
    /**
     * @param date
     * @return yyyy/MM/dd
     */
    public static String dateToTime4(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM.dd HH:mm");
        String dateString = formatter.format(date.getTime());
        return dateString;
    }

    /**
     * @param date
     * @return yyyy-MM-dd
     */
    public static String dateToTime3(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date.getTime());
        return dateString;
    }


    public static String stampToDate(String s) {
        if (s.length() < 13) {
            s = s + "000";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String timeToTime(String timeStr) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss zzzzz");
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String formatDate(String timeStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(timeStr);
    }

    public static String getDateToString(long time) {
        Date d = new Date(time * 1000);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    public static Calendar getStringToCalendar(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date timeToDate(String timeStr) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取两个Date的时间差 一年之内
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean getTimeInterval(Date startDate, Date endDate, Integer type) {
        long time = endDate.getTime() - startDate.getTime();// 得出的时间间隔是毫秒
        if (type == 1 ? time / 86400000 <= 90 : time / 86400000 <= 365) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取两个Date的时间大小
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean getTimeSize(Date startDate, Date endDate) {
        if (endDate.getTime() >= startDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static Date getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtils.e("==前后天数==" + dft.format(endDate));
        return endDate;
    }

    /**
     * 获取当前日期 Date 格式
     *
     * @return
     */
    public static Date getCurDate() {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE));
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtils.e("==当前日期==" + dft.format(endDate));
        return endDate;
    }

    /**
     * @return 时间戳
     */
    public static long timeTotime1(String timeStr) {
        long timeStemp = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(timeStr);
            timeStemp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStemp;
    }

    /**
     * 时间格式
     *
     * @param time
     * @return
     */
    public static String converToSimpleStrDate(String time) {

        long date = timeTotime1(time);
        long current = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat;
        long offSet = (current - date) / 1000;
        Date dt = new Date(date);
        String returnData;
        if (offSet < 5 * 60) {
            returnData = "刚刚";
        } else if (offSet >= 5 * 60 && offSet < 60 * 60) {
            returnData = offSet / 60 + "分钟前";
        } else if (offSet >= 60 * 60 && offSet < 60 * 60 * 24) {
            returnData = (offSet / 60 / 60) + "小时前";
        } else if (offSet >= 60 * 60 * 24 && offSet < (60 * 60 * 24 * 30 * 365)) {
            //几天前
            simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            returnData = simpleDateFormat.format(dt);
        } else {
            //一年前
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            returnData = simpleDateFormat.format(dt);
        }
        return returnData;
    }


    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }



}
