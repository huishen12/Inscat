package com.spacebunny.hshen.inscat.view.post_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacebunny.hshen.inscat.R;

public class InfoViewHolder extends RecyclerView.ViewHolder {

    TextView description;
    ImageView authorPhoto;
    TextView authorName;
    TextView authorBio;
    TextView likeCount;
    TextView commentCount;
    TextView categoryCount;
    ImageButton likeButton;
    ImageButton commentButton;
    ImageButton categoryButton;
    TextView shareButton;

    public InfoViewHolder(View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.post_description);
        authorPhoto = (ImageView) itemView.findViewById(R.id.post_author_photo);
        authorName = (TextView) itemView.findViewById(R.id.post_author_name);
        authorBio = (TextView) itemView.findViewById(R.id.post_author_bio);
        likeCount = (TextView) itemView.findViewById(R.id.post_like_count);
        categoryCount = (TextView) itemView.findViewById(R.id.post_category_count);
        commentCount = (TextView) itemView.findViewById(R.id.post_comment_count);
        likeButton = (ImageButton) itemView.findViewById(R.id.post_action_like);
        commentButton = (ImageButton) itemView.findViewById(R.id.post_action_comment);
        categoryButton = (ImageButton) itemView.findViewById(R.id.post_action_category);
        shareButton = (TextView) itemView.findViewById(R.id.post_action_share);
    }
}
