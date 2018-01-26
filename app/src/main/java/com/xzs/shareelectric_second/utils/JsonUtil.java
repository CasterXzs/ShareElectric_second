package com.xzs.shareelectric_second.utils;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xzs.shareelectric_second.entity.UserEntity;

import org.json.JSONObject;


/**
 * Created by Lenovo on 2018/1/25.
 */

public class JsonUtil {

    public static String  fromJson(String json){
        String jsonString=null;
        try{
            JSONObject jsonObject=new JSONObject(json);
            Log.d("LoginActivity", "JsonUtil中的fromJson: "+jsonObject);
            jsonString=jsonObject.getJSONObject("object").toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return  jsonString;
    }
}
