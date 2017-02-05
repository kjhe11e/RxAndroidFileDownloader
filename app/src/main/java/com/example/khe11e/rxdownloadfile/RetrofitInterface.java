package com.example.khe11e.rxdownloadfile;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by khe11e on 2/4/17.
 */

public interface RetrofitInterface {
    // Regular Retrofit 2 GET request
    /*
    @Streaming
    @GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);
    */


    // Retrofit 2 GET request for rxjava
    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFileByUrlRx(@Url String fileUrl);
}
