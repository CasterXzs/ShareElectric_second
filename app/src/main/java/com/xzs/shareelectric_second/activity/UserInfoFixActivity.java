package com.xzs.shareelectric_second.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.dialog.CustomProgressDialog;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;

import java.net.URLEncoder;


public class UserInfoFixActivity extends AppCompatActivity {

    private EditText userinfofix_et_nickname;
    private Button userinfofix_btn_birthday;
    private RadioGroup userinfofix_rg_sex;
    private Button userinfofix_btn;
    private String sex;
    private static final String TAG = "UserInfoFixActivity";
    private String birthday;
    private String nickname;
    private UserEntity userEntity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_fix);
        init();
    }

    private void init(){
        userEntity= MyApplication.userEntity;
        userinfofix_et_nickname=(EditText)findViewById(R.id.userinfofix_et_nickname);
        userinfofix_btn_birthday=(Button)findViewById(R.id.userinfofix_btn_birthday);
        userinfofix_rg_sex=(RadioGroup)findViewById(R.id.userinfofix_rg_sex);
        userinfofix_btn=(Button)findViewById(R.id.userinfofix_btn);
        userinfofix_btn.setOnClickListener(new MyOnClickListener());
        userinfofix_rg_sex.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        userinfofix_btn_birthday.setOnClickListener(new MyOnClickListener());
    }


    private class MyOnCheckedChangeListener  implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup,  int i) {
            switch (i){
                case R.id.userinfofix_rg_sex_male:
                    try{
                        sex="男".toString();
                        //sex=new String(sex1.getBytes("iso8859-1"),"UTF-8");
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    break;
                case R.id.userinfofix_rg_sex_female:
                    try{
                        sex="女".toString();
                        //String sex1=URLEncoder.encode(URLEncoder.encode(sex, "UTF-8"), "UTF-8");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            nickname=userinfofix_et_nickname.getText().toString().trim();

            if(view.getId()==R.id.userinfofix_btn){
                if(TextUtils.isEmpty(nickname)||TextUtils.isEmpty(birthday)||TextUtils.isEmpty(sex)){
                    Log.d(TAG, "nickname: "+nickname+" birthday="+birthday+" sex="+sex);
                    Toast.makeText(getApplicationContext(), "请输入相关参数", Toast.LENGTH_SHORT).show();
                }else{
                    final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                            UserInfoFixActivity.this);
                    customProgressDialog.setMessage("正在修改...");
                    customProgressDialog.show();
                    Toast.makeText(UserInfoFixActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onClick: "+nickname+" "+birthday+" "+sex);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpUtils httpUtils = new HttpUtils();
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("username", userEntity.getUsername());
                            params.addBodyParameter("nickname", nickname);
                            params.addBodyParameter("sex", sex);
                            params.addBodyParameter("birthday", birthday);
                            httpUtils.send(HttpMethod.POST, Config.UPDATA, params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {

                                    String json = responseInfo.result.trim();
                                    Log.d(TAG, "json: "+json);
                                    final UserEntity userEntity = GsonUtil.fromJson(json, UserEntity.class);
                                    MyApplication.userEntity=userEntity;
                                    Log.d(TAG, "修改完成后返回来的json: "+json);
                                    if (userEntity.errcode == 0) {

                                        Log.i(TAG, "onSuccess: "+json);
                                        if (customProgressDialog != null
                                                && customProgressDialog.isShowing()) {
                                            customProgressDialog.dismiss();
                                        }
                                        Intent intent=new Intent();
                                        setResult(RESULT_OK, intent);
                                        finish();
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
            if(view.getId()==R.id.userinfofix_btn_birthday){
                Toast.makeText(getApplicationContext(), "点击了生日", Toast.LENGTH_SHORT).show();
                new DatePickerDialog(UserInfoFixActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        birthday=String.format("%d-%d-%d",year,month+1,day);
                        Toast.makeText(UserInfoFixActivity.this, "year,month,day="+year+" "+month+" "+day, Toast.LENGTH_SHORT).show();
                        userinfofix_btn_birthday.setText(year+"-"+(month+1)+"-"+day);
                    }
                },2000,1,1).show();
            }
        }
    }


    /*
    public  void change(){
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
    }
    */
}
