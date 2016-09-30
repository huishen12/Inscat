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
    TextView likeCount;
    TextView viewCount;
    TextView commentCount;
    ImageButton likeButton;
    ImageButton commentButton;
    TextView shareButton;

    public InfoViewHolder(View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.post_description);
        authorPhoto = (ImageView) itemView.findViewById(R.id.post_author_photo);
        authorName = (TextView) itemView.findViewById(R.id.post_author_name);
        likeCount = (TextView) itemView.findViewById(R.id.post_like_count);
        viewCount = (TextView) itemView.findViewById(R.id.post_view_count);
        commentCount = (TextView) itemView.findViewById(R.id.post_comment_count);
        likeButton = (ImageButton) itemView.findViewById(R.id.post_action_like);
        commentButton = (ImageButton) itemView.findViewById(R.id.post_action_comment);
        shareButton = (TextView) itemView.findViewById(R.id.post_action_share);
    }
}
