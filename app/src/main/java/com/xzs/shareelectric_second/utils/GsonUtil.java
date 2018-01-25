package com.xzs.shareelectric_second.utils;


import com.google.gson.Gson;
import com.xzs.shareelectric_second.entity.UserEntity;

public class GsonUtil {

    public static <T> T fromJson(String json, Class<T> clazz) {

        Gson gson = new Gson();
        return gson.fromJson(json, clazz);

    }

}
