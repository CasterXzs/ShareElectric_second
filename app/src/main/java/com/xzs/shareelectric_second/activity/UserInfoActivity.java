package com.xzs.shareelectric_second.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.xzs.shareelectric_second.dialog.UserInfoDialog;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.Base64Util;
import com.xzs.shareelectric_second.utils.Config;
import com.xzs.shareelectric_second.utils.GsonUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private UserEntity userEntity;
    private Toolbar userinfo_tb;
    private CircleImageView userinfo_circleimageview;
    private TextView userinfo_tv_nickname;
    private TextView userinfo_tv_birthday;
    private TextView userinfo_tv_sex;
    private TextView userinfo_tv_qq;
    private TextView userinfo_tv_wx;
    private static final String TAG = "UserInfoActivity";
    public static  final int FIXBACK=1;

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
        userinfo_circleimageview=(CircleImageView)findViewById(R.id.userinfo_circleimageview);

        userinfo_circleimageview.setImageBitmap(Base64Util.base64ToBitmap(userEntity.getHeadImage()));

        userinfo_tv_nickname=(TextView)findViewById(R.id.userinfo_tv_nickname);
        userinfo_tv_nickname.setText(userEntity.getNickname());

        userinfo_tv_birthday=(TextView)findViewById(R.id.userinfo_tv_birthday);
        userinfo_tv_birthday.setText(userEntity.getBirthday());

        userinfo_tv_sex=(TextView)findViewById(R.id.userinfo_tv_sex);
        userinfo_tv_sex.setText(userEntity.getSex());

        userinfo_tv_qq=(TextView)findViewById(R.id.userinfo_tv_qq);
        userinfo_tv_wx=(TextView)findViewById(R.id.userinfo_tv_wx);

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
        }
    }
}
