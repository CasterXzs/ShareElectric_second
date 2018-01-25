package com.xzs.shareelectric_second.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private NavigationView user_view;
    private View headerView;
    private ImageView userheader_circleimageview;
    private TextView user_tv_phone;
    private UserEntity userEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        user_view=(NavigationView)findViewById(R.id.user_view);
        user_view.setNavigationItemSelectedListener(new MyNavigationItemSelectedListener());
        headerView=user_view.getHeaderView(0);
        user_tv_phone=headerView.findViewById(R.id.user_tv_phone);
        userheader_circleimageview=headerView.findViewById(R.id.userheader_circleimageview);
        userEntity= MyApplication.userEntity;
        user_tv_phone.setText(hidePhone(userEntity.getPhone()));
        userheader_circleimageview.setOnClickListener(new MyClickListener());

    }

    private class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,UserInfoActivity.class));
        }
    }

    private String hidePhone(String phone){
        String phoneNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        return phoneNumber;
    }


    private class MyNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.user_menu_record:
                    Toast.makeText(MainActivity.this, "checked user_menu_record", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,UserRecordActivity.class));
                    break;
                case R.id.user_menu_wallet:
                    Toast.makeText(MainActivity.this, "checked user_menu_wallet", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,UserWalletActivity.class));
                    break;
                case R.id.user_menu_invent:
                    Toast.makeText(MainActivity.this, "checked user_menu_invent", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.user_menu_service:
                    Toast.makeText(MainActivity.this, "checked user_menu_service", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }

}
