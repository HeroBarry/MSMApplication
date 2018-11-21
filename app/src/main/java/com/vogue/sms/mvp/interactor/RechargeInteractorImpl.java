package com.vogue.sms.mvp.interactor;

import android.content.Context;
import android.util.Log;


import com.vogue.sms.net.R;
import com.vogue.sms.mvp.listener.BaseSingleLoadedListener;
import com.vogue.sms.mvp.listener.CommonSingleInteractor;
import com.vogue.sms.net.Retrofit2See;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class RechargeInteractorImpl extends Retrofit2See implements CommonSingleInteractor {
    BaseSingleLoadedListener loadedListener;

    public RechargeInteractorImpl(Context context, BaseSingleLoadedListener loadedListener) {
        super(context);
        this.loadedListener = loadedListener;
    }
    public RechargeInteractorImpl(Context context, BaseSingleLoadedListener loadedListener,String url,String token) {
        super(context,url,token);
        this.loadedListener = loadedListener;
    }

    @Override
    public void getCommonSingleData(String address,String time,String body) {//address,time,body
        Call<R> call = seeApi.recharge(address,time,body);
        call.enqueue(new Callback<R>() {
            @Override
            public void onResponse(Call<R> call, Response<R> response) {
                Log.i("single", response.code() + "");
                if (response.body() != null) {
                    loadedListener.onSuccess(response.body());
                } else {
                    loadedListener.onFailure("请求失败");
                }
            }

            @Override
            public void onFailure(Call<R> call, Throwable t) {
                loadedListener.onFailure("网络连接错误，请稍后重试");
            }
        });
    }
}
