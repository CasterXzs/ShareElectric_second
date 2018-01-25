package com.xzs.shareelectric_second.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.entity.UserEntity;

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
        userinfo_tv_nickname=(TextView)findViewById(R.id.userinfo_tv_nickname);
        userinfo_tv_birthday=(TextView)findViewById(R.id.userinfo_tv_birthday);
        userinfo_tv_sex=(TextView)findViewById(R.id.userinfo_tv_sex);
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
            default:
                break;
        }
        return true;
    }

}
