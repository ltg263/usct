package com.frico.easy_pay.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.frico.easy_pay.ui.activity.response.JsonCityBean;
import com.frico.easy_pay.ui.activity.response.JsonRouterBean;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * TODO<读取Json文件的工具类>
 *
 * @date: 2017/3/16 16:22
 */
public class GetJsonDataUtil {

    /**
     * Gson解析字符串
     *
     * @param result
     * @return
     */
    public static ArrayList<JsonCityBean> parseData(String result) {//Gson 解析
        ArrayList<JsonCityBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonCityBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonCityBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /**
     * Gson解析字符串
     *
     * @param result
     * @return
     */
    public static ArrayList<JsonRouterBean> parseRouterData(String result) {//Gson 解析
        ArrayList<JsonRouterBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonRouterBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonRouterBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    /**
     * 获取Json字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Json 转为Map集合
     *
     * @param str_json
     * @return
     */
    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }

}

