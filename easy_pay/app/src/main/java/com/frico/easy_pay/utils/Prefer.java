package com.frico.easy_pay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.frico.easy_pay.SctApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghongchuang
 * on 2016/8/25.
 * email:844285775@qq.com
 */
public class Prefer {
    private static final String TAG = "Prefer";
    private static final String PREFERENCE_FILE = "prefer_config";

    private static Prefer mInstance;
    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;

    private final String KEY_MOBILE = "KEY_MOBILE";
    private final String KEY_TOKEN = "KEY_TOKEN";
    private final String KEY_TRADE_PWD = "KEY_TRADE_PWD";
    private final String KEY_USERID = "KEY_USERID";
    private final String KEY_MBMID = "KEY_MBMID";
    private final String KEY_AUTH = "KEY_AUTH";
    private final String KEY_BIND_STATUS = "KEY_BIND_STATUS";
    private final String KEY_ACCOUNT_STATUS = "KEY_ACCOUNT_STATUS";
    private final String KEY_CHECK_STOCK_STATUS = "KEY_CHECK_STOCK_STATUS";
    private final String KEY_AUTH_STATUS = "KEY_AUTH_STATUS";
    private final String KEY_WX = "KEY_WX";
    private final String KEY_SHARE_URL = "KEY_SHARE_URL";
    private final String KEY_INVITE_CODE = "KEY_INVITE_CODE";
    private final String KEY_INVITE_ID = "KEY_INVITE_ID";
    private final String KEY_NEED_GUIDE = "KEY_NEED_GUIDE";
    private final String KEY_MEMBER_LEVEL = "KEY_MEMBER_LEVEL";
    private final String KEY_REDUCE_MEMBER_LEVEL = "KEY_REDUCE_MEMBER_LEVEL";
    public final String KEY_ADDRESS_BEAN = "KEY_ADDRESS_BEAN";
    private final String KEY_RONG_TOKEN = "KEY_RONG_TOKEN";
    private final String KEY_RONG_CUSTOMER_ID = "KEY_RONG_CUSTOMER_ID";
    private final String KEY_USER_NAME = "KEY_USER_NAME";
    private final String KEY_LOGIN_TIME = "KEY_LOGIN_TIME";
    private final String KEY_EXPIRE_TIME = "KEY_LOGIN_TIME";//Expiretime
    private final String KEY_USER_AVATAR = "KEY_USER_AVATAR";
    private final String kEY_MIN_RECHARGE_AMOUNT = "kEY_MIN_RECHARGE_AMOUNT";
    private final String kEY_MY_POP_LINE = "kEY_MY_POP_LINE";
    private final String kEY_CART_LIST = "kEY_CART_LIST";
    private final String kEY_AD_IMAGE = "kEY_AD_IMAGE";
    private final String KEY_HAS_AD_IMAGE = "KEY_HAS_AD_IMAGE";
    private final String KEY_GOODS_CONTENT = "KEY_GOODS_CONTENT";
    private final String KEY_ROOM_ID = "KEY_ROOM_ID";
    private final String KEY_YB_STATUS = "KEY_YB_STATUS";
    private final String KEY_SWITCH_RING = "KEY_SWITCH_RING";
    private final String KEY_SOCKET_IP = "KEY_SOCKET_IP";       //socket的ip

    private final String CITY = "CITY_CITY_CITY";
    private final String PROVINCE = "PROVINCE_PROVINCE_PROVINCE";
    private final String CITY_INDEX = "CITY";
    private final String PROVINCE_INDEX = "PROVINCE";
    private final String LOCAL = "CITY-WIDE";

    public Boolean isLocal(){
        return mPref.getBoolean(LOCAL,false);
    }

    public void setLocal(boolean isLocal){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(LOCAL, isLocal);
        editor.commit();
    }


    public String getCity() {
        return mPref.getString(CITY, "");
    }

    public String getProvince() {
        return mPref.getString(PROVINCE, "");
    }

    public int getCityIndex() {
        return mPref.getInt(CITY_INDEX, 0);
    }

    public int getProvinceIndex() {
        return mPref.getInt(PROVINCE_INDEX, 0);
    }

    public void setCity(String city) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(CITY, city);
        editor.commit();
    }

    public void setProvince(String province) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(PROVINCE, province);
        editor.commit();
    }

    public void setCityIndex(int cityIndex) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(CITY_INDEX, cityIndex);
        editor.commit();
    }

    public void setProvinceIndex(int provinceIndex) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(PROVINCE_INDEX, provinceIndex);
        editor.commit();
    }


    private final String KEY_NOTIFICATION_CACHE = "KEY_NOTIFICATION_CACHE"; //缓存的没发出去的通知消息


    public static Prefer getInstance() {
        if (null == mInstance) {
            mInstance = new Prefer();
        }
        return mInstance;
    }

    private Prefer() {
        mPref = SctApp.getInstance().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }

    /**
     * 用户手机号
     */
    public void setMobile(String mobile) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_MOBILE, mobile);
        editor.commit();
    }

    public String getMobile() {
        return mPref.getString(KEY_MOBILE, "");
    }

    /**
     * 用户唯一标识token
     */
    public void setToken(String token) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return mPref.getString(KEY_TOKEN, "");
    }

    /**
     * 用户等级
     */
    public void setMemberLevel(String memberLevel) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_MEMBER_LEVEL, memberLevel);
        editor.commit();
    }

    public String getMemberLevel() {
        return mPref.getString(KEY_MEMBER_LEVEL, "");
    }

    /**
     * 降级等级
     */
    public void setReduceMemberLevel(String reduceMemberLevel) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_REDUCE_MEMBER_LEVEL, reduceMemberLevel);
        editor.commit();
    }

    public String getReduceMemberLevel() {
        return mPref.getString(KEY_REDUCE_MEMBER_LEVEL, "");
    }

    /**
     * 我的上线
     */
    public void setMyPopLine(String myPopLine) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(kEY_MY_POP_LINE, myPopLine);
        editor.commit();
    }

    public String getMyPopLine() {
        return mPref.getString(kEY_MY_POP_LINE, "");
    }


    /**
     * 是否需要引导页
     */
    public void setNeedGuide(boolean neeDGuide) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_NEED_GUIDE, neeDGuide);
        editor.commit();
    }

    public boolean getNeedGuide() {
        return mPref.getBoolean(KEY_NEED_GUIDE, true);
    }

    /**
     * 用户ID
     *
     * @param userId
     */
    public void setUserId(String userId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_USERID, userId);
        editor.commit();
    }

    public String getUserId() {
        return mPref.getString(KEY_USERID, "");
    }

    /**
     * 分享的链接头部
     */
    public void setShareUrl(String shareUrl) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_SHARE_URL, shareUrl);
        editor.commit();
    }

    public String getShareUrl() {
        return mPref.getString(KEY_SHARE_URL, "");
    }

    /**
     * socket链接的ip
     */
    public void setSocketIp(String socketIp) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_SOCKET_IP, socketIp);
        editor.commit();
    }

    public String getSocketIp() {
        return mPref.getString(KEY_SOCKET_IP, "");
    }

    /**
     * 邀请码Id
     */
    public void setInviteId(String inviteId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_INVITE_ID, inviteId);
        editor.commit();
    }

    public String getInviteId() {
        return mPref.getString(KEY_INVITE_ID, "");
    }

    /**
     * 通知铃声的开关
     */
    public void setRingSwitch(boolean ringSwitchIsOpen) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_SWITCH_RING, ringSwitchIsOpen);
        editor.commit();
    }

    public boolean getRingSwitchIsOpen() {
        return mPref.getBoolean(KEY_SWITCH_RING, false);
    }

    /**
     * 邀请码
     */
    public void setInviteCode(String inviteCode) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_INVITE_CODE, inviteCode);
        editor.commit();
    }

    public String getInviteCode() {
        return mPref.getString(KEY_INVITE_CODE, "");
    }

    /**
     * 是否已设置交易密码
     */
    public void setTradePwd(boolean tradePwd) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_TRADE_PWD, tradePwd);
        editor.commit();
    }

    public boolean getTradePwd() {
        return mPref.getBoolean(KEY_TRADE_PWD, false);
    }

    /**
     * 星球Id
     */
    public void setMbmId(String mbmId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_MBMID, mbmId);
        editor.commit();
    }

    public String getMbmId() {
        return mPref.getString(KEY_MBMID, "");
    }

    /**
     * 是否实名（1实名认证 0没有实名认证）
     */
    public void setAuth(String auth) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_AUTH, auth);
        editor.commit();
    }

    public String getAuth() {
        return mPref.getString(KEY_AUTH, "0");
    }

    /**
     * 实名认证状态
     */
    public void setAuthStatus(String authStatus) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_AUTH_STATUS, authStatus);
        editor.commit();
    }

    public String getAuthStatus() {
        return mPref.getString(KEY_AUTH_STATUS, "");
    }

    /**
     * 是否绑卡（0:未绑定 1:已绑定）
     */
    public void setBindStatus(String bindStatus) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_BIND_STATUS, bindStatus);
        editor.commit();
    }

    public String getBindStatus() {
        return mPref.getString(KEY_BIND_STATUS, "0");
    }

    /**
     * 账户状态（1正常 2锁定）
     */
    public void setAccountStatus(String accountStatus) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_ACCOUNT_STATUS, accountStatus);
        editor.commit();
    }

    public String getAccountStatus() {
        return mPref.getString(KEY_ACCOUNT_STATUS, "");
    }

    /**
     * 申请盘点状态（1申请盘点 0未申请）
     */
    public void setCheckStockStatus(String checkStockStatus) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_CHECK_STOCK_STATUS, checkStockStatus);
        editor.commit();
    }

    public String getCheckStockStatus() {
        return mPref.getString(KEY_CHECK_STOCK_STATUS, "");
    }

    /**
     * 最小充值金额
     */
    public void setMinRechargeAmount(String minRechargeAmount) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(kEY_MIN_RECHARGE_AMOUNT, minRechargeAmount);
        editor.commit();
    }

    public String getMinRechargeAmount() {
        return mPref.getString(kEY_MIN_RECHARGE_AMOUNT, "0");
    }

    /**
     * 是否关联微信
     */
    public void setRelevantWX(boolean relevantWX) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_WX, relevantWX);
        editor.commit();
    }

    public boolean getTRelevantWX() {
        return mPref.getBoolean(KEY_WX, false);
    }

    /**
     * 融云token
     */
    public void setRongToken(String rongToken) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_RONG_TOKEN, rongToken);
        editor.commit();
    }

    public String getRongToken() {
        return mPref.getString(KEY_RONG_TOKEN, "");
    }

    /**
     * 广告图片的链接
     */
    public void setAdImageUrl(String adImageUrl) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(kEY_AD_IMAGE, adImageUrl);
        editor.commit();
    }

    public String getAdImageUrl() {
        return mPref.getString(kEY_AD_IMAGE, "");
    }

    /**
     * 是否本地存在广告图片
     */
    public void setisHasAd(boolean relevantWX) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_HAS_AD_IMAGE, relevantWX);
        editor.commit();
    }

    public boolean getIsHasAd() {
        return mPref.getBoolean(KEY_HAS_AD_IMAGE, false);
    }

    /**
     * 融云客服Id
     */
    public void setRongCustomerId(String customerId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_RONG_CUSTOMER_ID, customerId);
        editor.commit();
    }

    public String getRongCustomerId() {
        return mPref.getString(KEY_RONG_CUSTOMER_ID, "");
    }

    /**
     * 用户name
     */
    public void setUserName(String userName) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() {
        return mPref.getString(KEY_USER_NAME, "");
    }


    /**
     * 用户登录时间（s）
     */
    public void setLoginTime(String userName) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_LOGIN_TIME, userName);
        editor.commit();
    }

    public String getLoginTime() {
        return mPref.getString(KEY_LOGIN_TIME, "");
    }

    /**
     * 用户登录时间（s）
     */
    public void setExpireTime(String userName) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_EXPIRE_TIME, userName);
        editor.commit();
    }

    public String getExpireTime() {
        return mPref.getString(KEY_EXPIRE_TIME, "");
    }

    /**
     * 保存通知栏消息上传失败的内容（json格式）
     */
    public void saveNotificationMes(String allMsg) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_NOTIFICATION_CACHE, allMsg);
        editor.commit();
    }

    /**
     * 获取缓存的没上传成功的消息
     *
     * @return
     */
    public String getAllNotificationMsges() {
        return mPref.getString(KEY_NOTIFICATION_CACHE, "");
    }

    /**
     * 商品图文详情
     */
    public void setGoodsContent(String content) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_GOODS_CONTENT, content);
        editor.commit();
    }

    public String getGoodsContent() {
        return mPref.getString(KEY_GOODS_CONTENT, "");
    }

    /**
     * 购物车下单list
     */
    public void setCartList(String cartList) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(kEY_CART_LIST, cartList);
        editor.commit();
    }

    public String getCartList() {
        return mPref.getString(kEY_CART_LIST, "");
    }

    /**
     * 融云客服Id
     */
    public void setUserAvatar(String userAvatar) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_USER_AVATAR, userAvatar);
        editor.commit();
    }

    public String getUserAvatar() {
        return mPref.getString(KEY_USER_AVATAR, "");
    }

    public String getRoomId() {
        return mPref.getString(KEY_ROOM_ID, "");
    }

    /**
     * 融云客服Id
     */
    public void setRoomId(String roomId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_ROOM_ID, roomId);
        editor.commit();
    }


    public String getYbStatus() {
        return mPref.getString(KEY_YB_STATUS, "0");
    }

    /**
     * 融云客服Id
     */
    public void setYbStatus(String roomId) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(KEY_YB_STATUS, roomId);
        editor.commit();
    }


    /**
     * 向SharedPreference 中保存信息<br>
     *
     * @param obj 类型object
     */
    public void saveObjectToShared(Object obj, String key) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(obj);
            String value = new String(Base64.encode(out.toByteArray(), Base64.DEFAULT));

            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从SharedPreference 中读取保存的信息<br>
     *
     * @return 返回读取的信息
     * Value 为读取内容，类型为String,如果Key未找到对应的数据，则返回null
     */
    public Object getSharedToObject(String key) {
        String value = mPref.getString(key, null);

        if (value != null) {
            byte[] valueBytes = Base64.decode(value, Base64.DEFAULT);
            ByteArrayInputStream bin = new ByteArrayInputStream(valueBytes);
            try {
                ObjectInputStream oin = new ObjectInputStream(bin);
                return oin.readObject();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 保存StringList
     *
     * @param tag
     * @param datalist
     */
    public void setDataList(String tag, List<String> datalist) {
        if (null == datalist)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);

        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取StringList
     *
     * @param tag
     * @return
     */
    public List<String> getDataList(String tag) {
        List<String> datalist = new ArrayList<>();
        String strJson = mPref.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
        }.getType());
        return datalist;
    }

    /**
     * 退出登录后清除缓存数据
     */
    public void clearData() {
        String mobile = getMobile();
        boolean needGuide = getNeedGuide();

        //清除数据
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();

        //重新写入
        setMobile(mobile);
        setNeedGuide(needGuide);
    }
}
