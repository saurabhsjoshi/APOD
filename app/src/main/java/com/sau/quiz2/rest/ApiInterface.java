package com.sau.quiz2.rest;

import com.sau.quiz2.model.Picture;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by saurabh on 2017-03-30.
 */

public interface ApiInterface {
    @GET("planetary/apod")
    Call<Picture> getPictureOfTheDay(@Query("api_key") String api_key);

    @GET("planetary/apod")
    Observable<Picture> getPictureForDateObservable(@Query("api_key") String api_key, @Query("date") String date);

    @GET("planetary/apod")
    Call<Picture> getPictureForDate(@Query("api_key") String api_key, @Query("date") String date);
}
