package com.spacebunny.hshen.inscat.ins;

import android.net.Uri;

public class Auth {
    public static final int REQ_CODE = 100;

    private static final String KEY_CODE = "code";
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CLIENT_SECRET = "client_secret";
    private static final String KEY_REDIRECT_URI = "redirect_uri";
    private static final String KEY_SCOPE = "scope";
    private static final String KEY_ACCESS_TOKEN = "access_token";

    private static final String CLIENT_ID = "9ee905bfe5ac4330be62e77ae558fd59";
    private static final String CLIENT_SECRET = "3dca8d78232040e38380cd7f81da0473";
    private static final String SCOPE = "basic+public_content+follower_list+comments+likes";

    private static final String URI_AUTHORIZE = "https://api.instagram.com/oauth/authorize";
    private static final String URI_TOKEN = "https://api.instagram.com/oauth/access_token";

    public static final String REDIRECT_URI = "https://www.instagram.com/";

    private static String getAuthorizeUrl() {
        return  Uri.parse(URI_AUTHORIZE).buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .appendQueryParameter(KEY_REDIRECT_URI, REDIRECT_URI)
                .appendQueryParameter(KEY_SCOPE, SCOPE)
                .build()
                .toString();
    }

    private static String getTokenUrl(String authCode) {
        return  Uri.parse(URI_TOKEN)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, CLIENT_ID)
                .appendQueryParameter(KEY_CLIENT_SECRET, CLIENT_SECRET)
                .appendQueryParameter(KEY_CODE, authCode)
                .appendQueryParameter(KEY_REDIRECT_URI, REDIRECT_URI)
                .build()
                .toString();
    }


}
