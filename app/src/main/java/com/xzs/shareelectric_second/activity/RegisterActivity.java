package com.xzs.shareelectric_second.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.dialog.CustomProgressDialog;
import com.xzs.shareelectric_second.entity.BaseEntity;
import com.xzs.shareelectric_second.utils.Base64Util;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;
import com.xzs.shareelectric_second.utils.TelephoneUtils;


import cn.bmob.newsmssdk.BmobSMS;
import cn.bmob.newsmssdk.exception.BmobException;
import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
import cn.bmob.newsmssdk.listener.SMSCodeListener;
import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_edit_username;//用户名
    private EditText register_edit_password;//密码
    private EditText register_edit_password2;//确认密码
    private EditText register_edit_phone;//手机号码
    private EditText register_edit_verifyingcode;//输入验证码
    private TextView register_tv_verifyingcode;//获取验证码
    private TimeCount timeCount;//验证码倒计时
    private Button register_btn_registe;//注册按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        //BmobSMS.initialize(RegisterActivity.this,"dd86255f183f52039c8f40ed8f043ab9",new MySMSCodeListener());//bmob短信发送功能初始化
        register_edit_username = (EditText) findViewById(R.id.register_edit_username);
        register_edit_password = (EditText) findViewById(R.id.register_edit_password);
        register_edit_password2 = (EditText) findViewById(R.id.register_edit_password2);
        register_edit_phone = (EditText) findViewById(R.id.register_edit_phone);
        register_edit_verifyingcode = (EditText) findViewById(R.id.register_edit_verifyingcode);
        register_tv_verifyingcode = (TextView) findViewById(R.id.register_tv_verifyingcode);
        timeCount = new TimeCount(60000, 1000);
        register_btn_registe = (Button) findViewById(R.id.register_btn_registe);

        //监听点击事件：1.点击注册按钮 2.点击获取验证码
        register_btn_registe.setOnClickListener(this);
        register_tv_verifyingcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn_registe://点击注册按钮
                //Toast.makeText(this, "跳转到登录页面", Toast.LENGTH_SHORT).show();
                doRegister();//进行注册
                break;
            case R.id.register_tv_verifyingcode://点击 获取验证码
                String phone=register_edit_phone.getText().toString().trim();
                if(phone.length()!=11||!TelephoneUtils.isMobileNO(phone)){
                    Toast.makeText(this, "请输入正确的联系方式", Toast.LENGTH_SHORT).show();

                }
                else{
                    timeCount.start();//倒计时启动
                    //VerifyPhone();//给指定的手机号发送验证码
                }

                break;
        }
    }

    private void doRegister() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在注册...");
        customProgressDialog.show();

        final String username = register_edit_username.getText().toString();
        final String password = register_edit_password.getText().toString();
        String password2 = register_edit_password2.getText().toString();
        final String phone = register_edit_phone.getText().toString();
        String verifyingcode = register_edit_verifyingcode.getText().toString();

        if (username.equals("") || password.equals("") || phone.equals("") || verifyingcode.equals("")) {
            Toast.makeText(getApplicationContext(), "输入不能为空!", Toast.LENGTH_SHORT).show();
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            return;
        }

        if (!password.equals(password2)) {
            Toast.makeText(getApplicationContext(), "密码输入不同!", Toast.LENGTH_SHORT).show();
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            return;
        }

        //验证短信验证码
        BmobSMS.verifySmsCode(this, phone, verifyingcode, new VerifySMSCodeListener() {
            @Override
            public void done(BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//短信验证码已验证成功
                    Log.i("bmob", "验证通过");
                } else {
                    Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    if (customProgressDialog != null
                            && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                    }
                    return;
                }
            }
        });

        //使用默认头像
        Bitmap decodeResource = BitmapFactory.decodeResource(this.getResources(), R.mipmap.register_icon);
        //将BitMap转换成Base64
        final String image = Base64Util.bitmapToBase64(decodeResource);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //将用户的相关信息上传至服务器：xUtils 2
                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("username", username);
                params.addBodyParameter("password", password);
                params.addBodyParameter("phone", phone);
                params.addBodyParameter("image", image);
                httpUtils.send(HttpMethod.POST, Config.REGISTER, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //从服务器返回是否注册成功的信息
                        String json = responseInfo.result;
                        BaseEntity baseEntity = GsonUtil.fromJson(json, BaseEntity.class);
                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }

                        if (baseEntity.errcode == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "注册成功",
                                    Toast.LENGTH_LONG).show();
                            finish();//注册成功后关闭当前页面
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "注册失败:" + baseEntity.errmsg,
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage() + " = " + s,
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).start();
    }

    private void VerifyPhone() {
        String phone = register_edit_phone.getText().toString();
        BmobSMS.requestSMSCode(RegisterActivity.this, phone, "Electric", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {//验证码发送成功
                    Log.i("bmob", "短信id: " + integer);//用于查询本次短信发送详情
                }
            }
        });
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //register_tv_verifyingcode.setTextColor(Color.parseColor("#cedbf6"));
            register_tv_verifyingcode.setClickable(false);
            register_tv_verifyingcode.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {

            register_tv_verifyingcode.setText("重新获取验证码");
            register_tv_verifyingcode.setClickable(true);
            register_tv_verifyingcode.setTextColor(Color.parseColor("#fbe0a6"));
        }
    }

    class MySMSCodeListener implements SMSCodeListener {

        @Override
        public void onReceive(String content) {
            if (register_edit_verifyingcode != null) {
                register_edit_verifyingcode.setText(content);
            }
        }

    }

}
