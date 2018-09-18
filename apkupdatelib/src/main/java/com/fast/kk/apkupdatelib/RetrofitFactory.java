package com.fast.kk.apkupdatelib;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static RetrofitService retrofitDownloadService;

    /**
     * 下载专用
     *
     * @return 返回OkHttpClient单例
     */
    public static OkHttpClient getDownloadInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
//                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();

    }

    public static RetrofitService getDownloadService() {
        if (retrofitDownloadService == null) {
            retrofitDownloadService = new Retrofit.Builder()
                    .baseUrl("http://test")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getDownloadInstance())
                    .build()
                    .create(RetrofitService.class);
        }
        return retrofitDownloadService;
    }


}
