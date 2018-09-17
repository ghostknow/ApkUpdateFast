package com.fast.kk.apkupdatelib;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {

    @Streaming
    @GET
    Flowable<ResponseBody> downLoadFile(@Url String url);
}
