package com.xzs.shareelectric_second.utils;

import android.content.Context;
import android.view.WindowManager;

import com.xzs.shareelectric_second.application.MyApplication;


/**
 * Created by Administrator on 2017/4/25.
 */

public class ScreenUtil {
    public static int sysWidth()
    {
        WindowManager wm = (WindowManager) MyApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width/2;
    }
    public static int sysHeight()
    {
        WindowManager wm = (WindowManager) MyApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height/2;
    }
}
