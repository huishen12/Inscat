package com.spacebunny.hshen.inscat.view.like_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.spacebunny.hshen.inscat.R;

public class LikeViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView authorPhoto;
    TextView authorName;
    TextView authorBio;

    public LikeViewHolder(View itemView) {
        super(itemView);
        authorPhoto = (SimpleDraweeView) itemView.findViewById(R.id.like_image);
        authorName = (TextView) itemView.findViewById(R.id.like_full_name);
        authorBio = (TextView) itemView.findViewById(R.id.like_bio);
    }
}
