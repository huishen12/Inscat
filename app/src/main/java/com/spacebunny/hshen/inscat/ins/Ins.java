package com.spacebunny.hshen.inscat.ins;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;

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
//        if (accessToken != null) {
//            user = loadUser(context);
//        }
    }


    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void login(@NonNull Context context, @NonNull String accessToken) {
        Ins.accessToken = accessToken;
        storeAccessToken(context, accessToken);

//        Ins.user = getUser();
//        storeUser(context, user);
    }

    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
//        storeUser(context, null);

        accessToken = null;
        user = null;
    }

    public static String loadAccessToken(@NonNull Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        return sp.getString(KEY_ACCESS_TOKEN, null);
    }

    public static void storeAccessToken(@NonNull Context context, @NonNull String token) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(SP_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public static User getCurrentUser() {
        return user;
    }

    public static User loadUser(@NonNull Context context) {
        return ModelUtils.read(context, KEY_USER, new TypeToken<User>(){});
    }

//    public static User getUser() {
//        return
//    }

}
