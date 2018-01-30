package com.xzs.shareelectric_second.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.dialog.CustomProgressDialog;
import com.xzs.shareelectric_second.dialog.SelectHeadImageDialog;
import com.xzs.shareelectric_second.dialog.UserInfoDialog;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.Base64Util;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;
import com.xzs.shareelectric_second.utils.SharedUtils;

import java.io.File;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserInfoActivity extends AppCompatActivity {

    private UserEntity userEntity;
    private Toolbar userinfo_tb;
    private CircleImageView userinfo_circleimageview;
    private TextView userinfo_tv_nickname;
    private TextView userinfo_tv_birthday;
    private TextView userinfo_tv_sex;
    private TextView userinfo_tv_phone;
    private TextView userinfo_tv_qq;
    private TextView userinfo_tv_wx;
    private static final String TAG = "UserInfoActivity";
    public static  final int FIXBACK=1;
    private SelectHeadImageDialog shid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    private void initView(){
        userEntity= MyApplication.userEntity;
        userinfo_tb=(Toolbar)findViewById(R.id.userinfo_tb);
        setSupportActionBar(userinfo_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userinfo_tv_phone=(TextView)findViewById(R.id.userinfo_tv_phone);
        userinfo_tv_phone.setText(MyApplication.hidePhone(userEntity.getPhone()));

        userinfo_circleimageview=(CircleImageView)findViewById(R.id.userinfo_circleimageview);
        userinfo_circleimageview.setImageBitmap(Base64Util.base64ToBitmap(userEntity.getHeadImage()));
        userinfo_circleimageview.setOnClickListener(new MyOnClickListener());

        userinfo_tv_nickname=(TextView)findViewById(R.id.userinfo_tv_nickname);
        userinfo_tv_nickname.setText(userEntity.getNickname());

        userinfo_tv_birthday=(TextView)findViewById(R.id.userinfo_tv_birthday);
        userinfo_tv_birthday.setText(userEntity.getBirthday());

        userinfo_tv_sex=(TextView)findViewById(R.id.userinfo_tv_sex);
        userinfo_tv_sex.setText(userEntity.getSex());

        userinfo_tv_qq=(TextView)findViewById(R.id.userinfo_tv_qq);
        userinfo_tv_wx=(TextView)findViewById(R.id.userinfo_tv_wx);

    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.userinfo_circleimageview:
                    shid=new SelectHeadImageDialog(UserInfoActivity.this,R.style.ActionSheetDialogStyle);
                    //shid.setTitle("选择方式");
                    //获取当前Activity所在的窗体
                    Window dialogWindow = shid.getWindow();
                    //设置Dialog从窗体底部弹出
                    dialogWindow.setGravity( Gravity.BOTTOM);
                    //获得窗体的属性
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.y = 20;//设置Dialog距离底部的距离
                    //将属性设置给窗体
                    dialogWindow.setAttributes(lp);
                    shid.show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userinfo_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.userinfo_fixinfo:
                Toast.makeText(this, "you clicked userinfo_fixinfo", Toast.LENGTH_SHORT).show();
                /*
                UserInfoDialog dialog=new UserInfoDialog(UserInfoActivity.this);
                dialog.show();
                dialog.change();
                */
                Intent intent=new Intent(UserInfoActivity.this,UserInfoFixActivity.class);
                startActivityForResult(intent,FIXBACK);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case FIXBACK:
                userEntity=MyApplication.userEntity;
                Log.d(TAG, "修改完成回来了: ");
                final CustomProgressDialog customProgressDialog = new CustomProgressDialog(
                        this);
                customProgressDialog.setMessage("正在更新...");
                customProgressDialog.show();
                if(resultCode==RESULT_OK){
                    Log.d(TAG, "回到userinfoactivity里面的 "+userEntity.getNickname()+" "+userEntity.getBirthday());
                    userinfo_tv_nickname.setText(userEntity.getNickname());
                    userinfo_tv_birthday.setText(userEntity.getBirthday());
                    userinfo_tv_sex.setText(userEntity.getSex());
                }
                if (customProgressDialog != null
                        && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
                break;

            case SelectHeadImageDialog.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    try{

                        final CustomProgressDialog customProgressDialog1 = new CustomProgressDialog(
                                this);
                        customProgressDialog1.setMessage("正在更新...");
                        customProgressDialog1.show();

                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(SelectHeadImageDialog.imageUri));
                        final String headimage=Base64Util.bitmapToBase64(bitmap);


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                HttpUtils httpUtils = new HttpUtils();
                                RequestParams params = new RequestParams();
                                params.addBodyParameter("username", userEntity.getUsername());
                                params.addBodyParameter("base64", headimage);
                                httpUtils.send(HttpMethod.POST, Config.MODIFYHEADIMAGE, params, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        String json = responseInfo.result.trim();
                                        Log.d(TAG, "json: "+json);
                                        final UserEntity userEntity = GsonUtil.fromJson(json, UserEntity.class);
                                        Log.d(TAG, "userEntity.headImage: "+userEntity.getHeadImage());
                                        Log.d(TAG, "userEntity.username: "+userEntity.getUsername());
                                        MyApplication.userEntity=userEntity;
                                        if (userEntity.errcode == 0) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    userinfo_circleimageview.setImageBitmap(Base64Util.base64ToBitmap(userEntity.getHeadImage()));
                                                }
                                            });

                                            if (customProgressDialog1 != null
                                                    && customProgressDialog1.isShowing()) {
                                                customProgressDialog1.dismiss();
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(),
                                                    "修改头像失败:" + userEntity.errmsg,
                                                    Toast.LENGTH_LONG).show();
                                            if (customProgressDialog1 != null
                                                    && customProgressDialog1.isShowing()) {
                                                customProgressDialog1.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(HttpException exception, String msg) {
                                        Toast.makeText(getApplicationContext(),
                                                exception.getMessage() + " = " + msg,
                                                Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "onFailure: "+exception.getMessage());
                                        if (customProgressDialog1 != null
                                                && customProgressDialog1.isShowing()) {
                                            customProgressDialog1.dismiss();
                                        }
                                        return;
                                    }
                                });


                            }
                        }).start();
                        if (shid != null
                                && shid.isShowing()) {
                            shid.dismiss();
                        }

                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
