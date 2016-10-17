package com.spacebunny.hshen.inscat.model;

import android.support.annotation.NonNull;

public class Post {
    public String type;
    public String link;
    public String title;

    public String id;
    public User user;
    public String caption;

    public int views_count;
    public PostCounts likes;
    public PostCounts comments;

    public PostImages images;

    @NonNull
    public String getImageUrl() {
        if (images == null) {
            return "";
        }

        String url;
        if (images.standard_resolution.url != null)
            url = images.standard_resolution.url;
        else if (images.low_resolution.url != null)
            url = images.low_resolution.url;
        else
            url = images.thumbnail.url;

        return url;
    }
}
