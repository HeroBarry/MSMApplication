/**
 * 文 件 名:  SMSApplication.java
 * 版    权:  Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  江钰锋 00501
 * 修改时间:  16/6/8
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.vogue.sms;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.vogue.sms.brodcast.MyBroadcast;
import com.vogue.sms.service.MyServiceOne;

/**
 * <一句话功能简述>
 *
 * @author 江钰锋 00501
 * @version [版本号, 16/6/8]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SMSApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //开启系统时间广播(动态注册,不能静态注册)
        //部分机型会屏蔽时间广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//系统时间，每分钟发送一次
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);//屏幕打开（解锁），
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);//屏幕关闭
        MyBroadcast myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast, intentFilter);
        startMyService();
    }

    /**
     * 开启双服务
     */
    private void startMyService() {
        Intent serviceOne = new Intent();
        serviceOne.setClass(this, MyServiceOne.class);
        startService(serviceOne);
    }
}
