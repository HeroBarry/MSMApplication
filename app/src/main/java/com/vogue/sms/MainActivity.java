package com.vogue.sms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vogue.sms.mvp.presenter.RechargePresenterImpl;
import com.vogue.sms.mvp.view.RechargeView;
import com.vogue.sms.tools.CommonUtils;
import com.vogue.sms.tools.SmsObserver;
import com.vogue.sms.tools.SmsResponseCallback;
import com.vogue.sms.tools.VerificationCodeSmsFilter;
import com.vogue.sms.tools.XmlDB;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SmsResponseCallback, RechargeView {

    int REQUEST_CALL_READ_SMS_PERMISSION = 0;
    @Bind(R.id.smsCode)
    TextView smsCode;
    @Bind(R.id.smsUrl)
    EditText smsUrl;
    @Bind(R.id.smsToken)
    EditText smsToken;

    private SmsObserver smsObserver;
    RechargePresenterImpl rechargePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

       String url= XmlDB.getInstance(getApplicationContext()).getKeyString("url","服务器地址");
       String token=XmlDB.getInstance(getApplicationContext()).getKeyString("token","交互token");
        if (CommonUtils.isEmpty(url)){
            smsUrl.setText(url);
        }
        if (CommonUtils.isEmpty(token)){
            smsToken.setText(token);
        }
        if (checkPermission(this, Manifest.permission.READ_SMS, getPackageName())) {
            smsObserver = new SmsObserver(this, this, new VerificationCodeSmsFilter(null));
            smsObserver.registerSMSObserver();
        }
        rechargePresenter = new RechargePresenterImpl(getApplicationContext(), this);

    }

    @Override
    public void recharge(com.vogue.sms.net.R data) {
        Log.d("OOOO", data.getMsg());
        Toast.makeText(getApplicationContext(),data.getMsg(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCallbackSmsContent(String address, String time, String body) {
        smsCode.setText("手机号：" + address + "  短信内容:" + body);
        Log.e("长才", "手机号：" + address + "  短信内容:" + body + " 时间：" + time);

            String url=smsUrl.getText().toString();
            String token=smsToken.getText().toString();
            if (CommonUtils.isEmpty(url)){
                Toast.makeText(getApplicationContext(),"URL不能为空",Toast.LENGTH_LONG).show();
                return;
            }
            if (CommonUtils.isEmpty(token)){
                Toast.makeText(getApplicationContext(),"TOKEN不能为空",Toast.LENGTH_LONG).show();
                return;
            }
            XmlDB.getInstance(getApplicationContext()).saveKey("url",url);
            XmlDB.getInstance(getApplicationContext()).saveKey("token",token);
            rechargePresenter = new RechargePresenterImpl(getApplicationContext(), this,url,token);
            rechargePresenter.recharge(address, time, body);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsObserver.unregisterSMSObserver();
    }

    private boolean checkPermission(Context context, String permName, String pkgName) {
        PackageManager pm = context.getPackageManager();
        if (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permName, pkgName)) {
            System.out.println(pkgName + "has permission : " + permName);
            return true;
        } else {
            //PackageManager.PERMISSION_DENIED == pm.checkPermission(permName, pkgName)
            System.out.println(pkgName + "not has permission : " + permName);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, REQUEST_CALL_READ_SMS_PERMISSION);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CALL_READ_SMS_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //执行自己的业务逻辑
                Toast.makeText(this, "获取了权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "拒绝了权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
            exit();
        }else if(keyCode == KeyEvent.KEYCODE_HOME){
            exit();
        }
        return true;
    }

    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
