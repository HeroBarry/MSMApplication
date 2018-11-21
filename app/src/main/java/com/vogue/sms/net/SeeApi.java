package com.vogue.sms.net;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public interface SeeApi {

    @POST("recharge/post")
    Call<R> recharge(@Query("phone") String phone,@Query("date") String date, @Query("body") String body);

}
