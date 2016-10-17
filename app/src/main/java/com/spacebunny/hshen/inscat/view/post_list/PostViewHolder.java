package com.spacebunny.hshen.inscat.view.post_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.spacebunny.hshen.inscat.R;


public class PostViewHolder extends RecyclerView.ViewHolder  {
    public View cover;
    public TextView likeCount;
    public TextView commentCount;
    public TextView viewCount;
    public SimpleDraweeView image;

    public PostViewHolder(View itemView) {
        super(itemView);
        cover = (View) itemView.findViewById(R.id.post_clickable_cover);
        likeCount = (TextView) itemView.findViewById(R.id.post_like_count);
        commentCount = (TextView) itemView.findViewById(R.id.post_comment_count);
        viewCount = (TextView) itemView.findViewById(R.id.post_view_count);
        image = (SimpleDraweeView) itemView.findViewById(R.id.post_image);
    }
}
