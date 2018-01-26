package com.xzs.shareelectric_second.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.dialog.CustomProgressDialog;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;
import com.xzs.shareelectric_second.utils.JsonUtil;
import com.xzs.shareelectric_second.utils.SharedUtils;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText login_edit_username;
    private EditText login_edit_password;
    private CheckBox login_cb_rememberpassword;
    private Button login_btn_login;
    private TextView login_tv_forget;
    private TextView login_tv_register;
    private List<String> permissionList=new ArrayList<>();//用于获得相关危险权限
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RequestPermissions();
        initView();
    }

    //请求相关权限
    private void RequestPermissions(){
        if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(LoginActivity.this,permissions,1);
        }else{
            initView();
        }
    }

    private void initView(){
        login_edit_username=(EditText)findViewById(R.id.login_edit_username);
        login_edit_password=(EditText)findViewById(R.id.login_edit_password);
        login_cb_rememberpassword=(CheckBox)findViewById(R.id.login_cb_rememberpassword);
        login_btn_login=(Button)findViewById(R.id.login_btn_login);
        login_tv_forget=(TextView)findViewById(R.id.login_tv_forget);
        login_tv_register=(TextView)findViewById(R.id.login_tv_register);

        if(SharedUtils.getClick(getBaseContext())){
            login_cb_rememberpassword.setChecked(true);
        }else{
            login_cb_rememberpassword.setChecked(false);
        }

        if(login_cb_rememberpassword.isChecked()){
            login_edit_username.setText(SharedUtils.getName(getBaseContext()));
            login_edit_password.setText(SharedUtils.getPass(getBaseContext()));
        }
        login_btn_login.setOnClickListener(this);
        login_tv_forget.setOnClickListener(this);
        login_tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.login_btn_login:
                //Toast.makeText(this, "login...", Toast.LENGTH_SHORT).show();
                doLogin();
                break;
            case R.id.login_tv_forget:
                Toast.makeText(this, "forger...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void doLogin(){
        final String username=login_edit_username.getText().toString().trim();
        final String password=login_edit_password.getText().toString().trim();

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                this);
        customProgressDialog.setMessage("正在登录...");
        customProgressDialog.show();

        if (username.equals("") || password.equals("")) {
            if (customProgressDialog != null
                    && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();

            }

            Toast.makeText(LoginActivity.this, "输入不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        //startActivity(new Intent(LoginActivity.this,MainActivity.class));


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils httpUtils = new HttpUtils();
                RequestParams params = new RequestParams();
                params.addBodyParameter("username", username);
                params.addBodyParameter("password", password);
                Log.d(TAG, "params: "+username+" "+password);
                httpUtils.send(HttpMethod.POST, Config.LOGIN, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        String json = responseInfo.result.trim();
                        Log.d(TAG, "json: "+json);
                        String jsonString = JsonUtil.fromJson(json);
                        Log.d(TAG, "jsonString: "+jsonString);
                        final UserEntity userEntity = GsonUtil.fromJson(jsonString, UserEntity.class);

                        MyApplication.userEntity=userEntity;
                        if (userEntity.errcode == 0) {

                            Log.i(TAG, "onSuccess: "+json);
                            if (login_cb_rememberpassword.isChecked()) {
                                SharedUtils.putName(getBaseContext(), username);
                                SharedUtils.putPass(getBaseContext(), password);
                                SharedUtils.putClick(getBaseContext(), true);
                            } else {
                                SharedUtils.putName(getBaseContext(), null);
                                SharedUtils.putPass(getBaseContext(), null);
                                SharedUtils.putClick(getBaseContext(), false);
                            }
                            if (customProgressDialog != null
                                    && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                            }
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "登录失败:" + userEntity.errmsg,
                                    Toast.LENGTH_LONG).show();
                            if (customProgressDialog != null
                                    && customProgressDialog.isShowing()) {
                                customProgressDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException exception, String msg) {
                        Toast.makeText(getApplicationContext(),
                                exception.getMessage() + " = " + msg,
                                Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onFailure: "+exception.getMessage());
                        if (customProgressDialog != null
                                && customProgressDialog.isShowing()) {
                            customProgressDialog.dismiss();
                        }
                        return;
                    }
                });


            }
        }).start();

    }
}
