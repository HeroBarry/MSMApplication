package com.vogue.sms.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vogue.sms.net.R;
import com.vogue.sms.mvp.interactor.RechargeInteractorImpl;
import com.vogue.sms.mvp.listener.BaseSingleLoadedListener;
import com.vogue.sms.mvp.view.RechargeView;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class RechargePresenterImpl implements BaseSingleLoadedListener<R> {
    Context mContext = null;
    RechargeInteractorImpl loginInteractor;
    RechargeView rechargeView;
    public RechargePresenterImpl(Context mContext, RechargeView rechargeView ) {
        this.mContext = mContext;
        this.rechargeView = rechargeView;
        this.loginInteractor = new RechargeInteractorImpl(mContext, this);
    }
    public RechargePresenterImpl(Context mContext, RechargeView rechargeView ,String url,String token) {
        this.mContext = mContext;
        this.rechargeView = rechargeView;
        this.loginInteractor = new RechargeInteractorImpl(mContext, this,url,token);
    }
    public void recharge(String address,String time,String body) {
        Log.e("惆怅","手机号："+address+"  短信内容:" + body+" 时间："+time);
        loginInteractor.getCommonSingleData(address,time,body);
    }

    @Override
    public void onSuccess(R data) {
            if (data.getCode()==0) {
                rechargeView.recharge(data);
            } else {
                Toast.makeText(mContext,"失败",Toast.LENGTH_LONG);
            }

    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(mContext,"失败",Toast.LENGTH_LONG);
    }


}
