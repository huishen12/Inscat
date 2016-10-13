package com.spacebunny.hshen.inscat.ins;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Ins {

    private static final String TAG = "Instagram API";

    public static final int COUNT_PER_PAGE = 20;

    public static final String API_URL = "https://api.instagram.com/v1/";

    private static final String USER_END_POINT = API_URL + "users/";
    private static final String MEDIA_END_POINT = API_URL + "media/";

    private static final String SP_AUTH = "auth";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER = "user";
    private static final String KEY_POST_ID = "post_id";
    private static final String KEY_USER_ID = "user_id";

    private static final TypeToken<List<Post>> POST_LIST_TYPE = new TypeToken<List<Post>>(){};
    private static final TypeToken<Post> POST_TYPE = new TypeToken<Post>(){};
    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){};

    private static OkHttpClient client = new OkHttpClient();

    private static String accessToken;
    private static User user;


    private static Request.Builder authRequestBuilder(String url) {
        url = url + "?access_token=" + accessToken;
        return new Request.Builder().url(url);
    }

    private static Response makeRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        Log.d(TAG, "X-RateLimit-Remaining: " + response.header("X-RateLimit-Remaining"));
        return response;
    }

    private static Response makeGetRequest(String url) throws IOException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }

    private static Response makePostRequest(String url, RequestBody requestBody) throws IOException {
        Request request = authRequestBuilder(url)
                .post(requestBody)
                .build();
        return makeRequest(request);
    }

    private static Response makePutRequest(String url, RequestBody requestBody) throws IOException {
        Request request = authRequestBuilder(url)
                .put(requestBody)
                .build();
        return makeRequest(request);
    }

    private static Response makeDeleteRequest(String url, RequestBody requestBody) throws IOException {
        Request request = authRequestBuilder(url)
                .delete(requestBody)
                .build();
        return makeRequest(request);
    }

    private static <T> T parseResponse(Response response, TypeToken<T> typeToken) throws IOException, JsonSyntaxException {
        String responseString = response.body().string();
        Log.d(TAG, responseString);
        JsonElement jelement = new JsonParser().parse(responseString);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("data");
        Log.d(TAG, jobject.toString());
        return ModelUtils.toObject(jobject.toString(), typeToken);
    }

    private static void checkStatusCode(Response response, int statusCode) throws IOException {
        if (response.code() != statusCode) {
            throw new IOException((response.message()));
        }
    }

    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    public static void login(@NonNull Context context, @NonNull String accessToken) throws IOException, JsonSyntaxException {
        Ins.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Ins.user = getUserSelf();
        Log.d(TAG, "User is " + user);
        Log.d(TAG, "User self name is " + user.full_name);
        storeUser(context, user);
    }

    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

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

    public static void storeUser(@NonNull Context context, User user) {
         ModelUtils.save(context, KEY_USER, user);
    }

    public static User getUserSelf() throws IOException, JsonSyntaxException {
        String userSelfUrl = USER_END_POINT + "self/";
        Log.v(TAG, "Get user self url " + userSelfUrl);
        return parseResponse(makeGetRequest(userSelfUrl), USER_TYPE);
    }

    public static User getUser(String userId) throws IOException, JsonSyntaxException {
        String userUrl = USER_END_POINT + userId + "/";
        return parseResponse(makeGetRequest(userUrl), USER_TYPE);
    }
}
