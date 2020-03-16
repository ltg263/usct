package com.frico.usct.core.api;

import com.frico.usct.core.utils.Constants;
import com.frico.usct.utils.Prefer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private static RetrofitUtil util;
    private ApiService apiService;

    public static RetrofitUtil getInstance() {
        if (util == null) {
            synchronized (RetrofitUtil.class) {
                if (util == null) {
                    util = new RetrofitUtil();
                }
            }
        }
        return util;
    }

    private RetrofitUtil() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                Request build = builder.addHeader("token", Prefer.getInstance().getToken()).build();
                return chain.proceed(build);
            }
        });


        httpClientBuilder.addInterceptor(new MyInterceptor());//添加日志打印

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService apiService() {
        return apiService;
    }
}
