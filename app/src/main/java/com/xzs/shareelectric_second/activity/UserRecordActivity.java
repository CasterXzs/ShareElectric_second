package com.xzs.shareelectric_second.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.adapter.UserRecordAdapter;
import com.xzs.shareelectric_second.entity.UserRecordEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserRecordActivity extends AppCompatActivity {


    private Toolbar userrecord_tb;
    private RecyclerView userrecord_recyclerview;

    private UserRecordEntity[] userRecordEntities = {
            new UserRecordEntity("2017-08-25 18:29:38", "0.0", "14902519"),
            new UserRecordEntity("2017-08-11 08:11:57", "12.9", "12345678"),
            new UserRecordEntity("2017-08-24 08:13:21", "1.8", "47946271"),
            new UserRecordEntity("2017-08-25 18:29:38", "0.0", "14902519"),
            new UserRecordEntity("2017-08-25 18:29:38", "0.0", "14902519"),
    };

    private List<UserRecordEntity> userRecordEntityList = new ArrayList<>();
    private UserRecordAdapter userRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);
        initView();
    }

    private void initView() {
        userrecord_tb = (Toolbar) findViewById(R.id.userrecord_tb);
        setSupportActionBar(userrecord_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initUserRecordEntityList();
        userrecord_recyclerview = (RecyclerView) findViewById(R.id.userrecord_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userrecord_recyclerview.setLayoutManager(layoutManager);
        userRecordAdapter = new UserRecordAdapter(userRecordEntityList);
        userrecord_recyclerview.setAdapter(userRecordAdapter);

    }

    private void initUserRecordEntityList() {
        userRecordEntityList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(userRecordEntities.length);
            userRecordEntityList.add(userRecordEntities[index]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
