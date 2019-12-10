package com.frico.easy_pay.core.api;


import com.frico.easy_pay.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CreateInterceptor implements Interceptor {
    public static final int HTTP_CODE_ACCEPT = 202;                     //请求成功，但没有处理

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());  //如果401了，会先执行TokenAuthenticator

        LogUtils.e("CreateInterceptor request url "+response.request().url());
        LogUtils.e("CreateInterceptor  response code "+response.code());
        if (response.code() == HTTP_CODE_ACCEPT) {
            CreateInterceptorExceptioin interceptorExceptioin = new CreateInterceptorExceptioin();

            interceptorExceptioin.setErrorCode(HTTP_CODE_ACCEPT);
            interceptorExceptioin.setRetry_after(response.header("Retry-After"));
            throw  interceptorExceptioin;
        }
        return response;
    }



    public class CreateInterceptorExceptioin extends Error{
        private int errorCode;
        private String retry_after;


        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getRetry_after() {
            return retry_after;
        }

        public void setRetry_after(String retry_after) {
            this.retry_after = retry_after;
        }
    }

}