package com.example.timecapsule.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpServices {

    @GET("index")
    Observable<HttpResponse<DataBean>> getNewsList(
            @Query("type") String type, @Query("key") String key
    );


    @GET("content")
    Observable<HttpResponse<DataBean>> getNewsDetails(
            @Query("uniquekey") String uniquekey, @Query("key") String key
    );


}
