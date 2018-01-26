package com.xzs.shareelectric_second.application;

import android.app.Application;

import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.utils.ZXingLibrary;

/**
 * Created by Lenovo on 2018/1/25.
 */

public class MyApplication extends Application {
    public static UserEntity userEntity;

    private static MyApplication myApplication;
    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        ZXingLibrary.initDisplayOpinion(this);
    }
}
