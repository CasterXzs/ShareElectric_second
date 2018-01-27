package com.xzs.shareelectric_second.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.adapter.MessageAdapter;
import com.xzs.shareelectric_second.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2018/1/19.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView yaoqing;
    private ImageView gongyizhongxin;
    private ImageView youhuijuan;
    private ImageView back;
    private List<Message> messages=new ArrayList<Message>();
    private ListView listView;
    RecyclerViewBanner recyclerViewBanner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        initview();
        setOnclick();
        final List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("http://img02.tooopen.com/images/20160408/tooopen_sy_158723161481.jpg"));
        banners.add(new Banner("http://d.5857.com/tc_170411/001.jpg"));
        banners.add(new Banner("http://img06.tooopen.com/images/20160921/tooopen_sy_179583447187.jpg"));
        banners.add(new Banner("http://www.quanjing.com/image/2017index/lx3.png"));
        banners.add(new Banner("http://img02.tooopen.com/images/20160509/tooopen_sy_161967094653.jpg"));
        recyclerViewBanner.setIndicatorInterval(2000);
        recyclerViewBanner.setRvBannerData(banners);
        recyclerViewBanner.setOnSwitchRvBannerListener(new RecyclerViewBanner.OnSwitchRvBannerListener() {
            @Override
            public void switchBanner(int position, AppCompatImageView bannerView) {
                Glide.with(bannerView.getContext())
                        .load(banners.get(position).getUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(bannerView);
            }
        });
        recyclerViewBanner.setOnRvBannerClickListener(new RecyclerViewBanner.OnRvBannerClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MessageActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        initMessage();
        MessageAdapter messageAdapter=new MessageAdapter(MessageActivity.this,messages);
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MessageActivity.this,MessageContentActivity.class));
            }
        });
    }
    private void initview(){
        recyclerViewBanner = (RecyclerViewBanner) findViewById(R.id.rv_banner_1);
        listView=(ListView)findViewById(R.id.listview);
        gongyizhongxin=(ImageView)findViewById(R.id.gongyi);
        yaoqing=(ImageView)findViewById(R.id.yaoqing);
        youhuijuan=(ImageView)findViewById(R.id.youhuijuan);
        back=(ImageView)findViewById(R.id.message_back);
    }
    private void setOnclick(){
        gongyizhongxin.setOnClickListener(this);
        yaoqing.setOnClickListener(this);
        youhuijuan.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    private void initMessage(){
        Message message=new Message("低碳排放",R.mipmap.ic_launcher,"低碳排放，从我做起");
        messages.add(message);
        Message message1=new Message("节能减排",R.mipmap.ic_launcher,"从现在起，节约每一天的电111111111111111111111111111111111111111111111111111111111111111");
        messages.add(message1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.youhuijuan:
                startActivity(new Intent(MessageActivity.this,YouhuijuanActivity.class));
                break;
            case R.id.yaoqing:
                startActivity(new Intent(MessageActivity.this,YaoqingActivity.class));
                break;
            case R.id.gongyi:
                startActivity(new Intent(MessageActivity.this,GongyizhongxinActivity.class));
                break;
            case R.id.message_back:
                startActivity(new Intent(MessageActivity.this,MainActivity.class));
                break;
            default:
                break;
        }
    }

    private class Banner {

        String url;

        public Banner(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}
