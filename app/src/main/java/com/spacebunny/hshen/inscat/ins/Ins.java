package com.spacebunny.hshen.inscat.ins;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.spacebunny.hshen.inscat.model.User;

public class Ins {

    private static final String TAG = "Instagram API";

    public static final int COUNT_PER_PAGE = 20;

    public static final String API_URL = "https://api.instagram.com/v1/";

    private static final String USER_END_POINT = API_URL + "users/";
    private static final String MEDIA_END_POINT = API_URL + "media";

    private static final String SP_AUTH = "auth";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user";
    private static final String KEY_POST_ID = "post_id";

    private static String accessToken;
    private static User user;


    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
    }


    public static String loadAccessToken(@NonNull Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void storeAccessToken(@NonNull Context context, @NonNull String token) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

}
