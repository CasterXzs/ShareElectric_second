package com.xzs.shareelectric_second.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xzs.shareelectric_second.R;

public class UserWalletActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar userwallet_tb;
    private LinearLayout userwallet_ll_redpackage;
    private LinearLayout userwallet_ll_coupon;
    private LinearLayout userwallet_ll_electriccard;
    private TextView userwallet_tv_restmoney;
    private TextView userwallet_tv_redpackage;
    private static final String TAG = "UserWalletActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);
        initView();
    }

    private void initView(){
        userwallet_tb=(Toolbar)findViewById(R.id.userwallet_tb);
        setSupportActionBar(userwallet_tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userwallet_ll_redpackage=(LinearLayout)findViewById(R.id.userwallet_ll_redpackage);
        userwallet_ll_coupon=(LinearLayout)findViewById(R.id.userwallet_ll_coupon);
        userwallet_ll_electriccard=(LinearLayout)findViewById(R.id.userwallet_ll_electriccard);
        userwallet_tv_restmoney=(TextView)findViewById(R.id.userwallet_tv_restmoney);
        userwallet_tv_redpackage=(TextView)findViewById(R.id.userwallet_tv_redpackage);
        //userwallet_tv_redpackage.setText("22");
        userwallet_ll_redpackage.setOnClickListener(this);
        userwallet_ll_coupon.setOnClickListener(this);
        userwallet_ll_electriccard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.userwallet_ll_redpackage:
                String redpackage=userwallet_tv_redpackage.getText().toString();
                Log.d(TAG, "redpackage: "+redpackage);
                final int redpackage1=Integer.parseInt(redpackage);
                Log.d(TAG, "redpackage1: "+redpackage1);
                Toast.makeText(this, "click userwallet_ll_redpackage", Toast.LENGTH_SHORT).show();


                break;
            case R.id.userwallet_ll_coupon:
                Toast.makeText(this, "click userwallet_ll_coupon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.userwallet_ll_electriccard:
                Toast.makeText(this, "click userwallet_ll_electriccard", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
