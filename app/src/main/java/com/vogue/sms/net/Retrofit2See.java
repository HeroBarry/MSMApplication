package com.vogue.sms.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Retrofit2See {
    //protected String baseUrl = "https://www.eluchn.com/";
    //protected String baseUrl = "http://driver.tunnel.echomod.cn/";
    protected String baseUrl = "http://vogue.ngrok.xiaomiqiu.cn/";

    protected Retrofit retrofit;
    public SeeApi seeApi;
    private static Context mContext = null;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public Retrofit2See(Context context,String url ,String authorization) {
        mContext = context;
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            /**
             * 设置头
             */
            builder.addInterceptor(addHeaderInterceptor(authorization));
            /**
             * 设置公共参数
             */
            builder.addInterceptor(addQueryParameterInterceptor());

            /**
             * 设置缓存
             */
            File cacheFile = new File(mContext.getExternalCacheDir(), "RetrofitCache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            builder.cache(cache).addInterceptor(addCacheInterceptor());

            /**
             * 错误重连
             */
            builder.retryOnConnectionFailure(true);
            /**
             * okhttp3
             */
            OkHttpClient client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        seeApi = retrofit.create(SeeApi.class);
    }
    public Retrofit2See(Context context,String authorization ) {
        mContext = context;
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            /**
             * 设置头
             */
            builder.addInterceptor(addHeaderInterceptor(authorization));
            /**
             * 设置公共参数
             */
            builder.addInterceptor(addQueryParameterInterceptor());

            /**
             * 设置缓存
             */
            File cacheFile = new File(mContext.getExternalCacheDir(), "RetrofitCache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            builder.cache(cache).addInterceptor(addCacheInterceptor());

            /**
             * 错误重连
             */
            builder.retryOnConnectionFailure(true);
            /**
             * okhttp3
             */
            OkHttpClient client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        seeApi = retrofit.create(SeeApi.class);
    }



    public Retrofit2See(Context context) {
        mContext = context;
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            /**
             * 设置头
             */
            builder.addInterceptor(addHeaderInterceptor());
            /**
             * 设置公共参数
             */
            builder.addInterceptor(addQueryParameterInterceptor());

            /**
             * 设置缓存
             */
            File cacheFile = new File(mContext.getExternalCacheDir(), "RetrofitCache");
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            builder.cache(cache).addInterceptor(addCacheInterceptor());

            /**
             * 错误重连
             */
            builder.retryOnConnectionFailure(true);
            /**
             * okhttp3
             */
            OkHttpClient client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        seeApi = retrofit.create(SeeApi.class);

    }

    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor() {
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        // Provide your custom header here
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }
    /**
     * 设置头
     */
    private static Interceptor addHeaderInterceptor(final String authorization ) {
        Log.d("设置头",authorization+"");
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        // Provide your custom header here
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization",authorization)
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        return headerInterceptor;
    }

    /**
     * 设置公共参数
     */
    private static Interceptor addQueryParameterInterceptor() {
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("platform", "android")
                        .addQueryParameter("version", "1.0.0")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        return addQueryParameterInterceptor;
    }

    /**
     * 设置缓存
     */
    private static Interceptor addCacheInterceptor() {
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkAvailable(mContext)) {
                    request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                }
                Response response = chain.proceed(request);
                if (isNetworkAvailable(mContext)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("nyn")
                            .build();
                }
                return response;
            }
        };
        return cacheInterceptor;
    }


    /**
     * 判断网络
     */
    public static boolean isNetworkAvailable(Context ct) {
        Context context = ct.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
