package com.spacebunny.hshen.inscat.view.comment_list;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.spacebunny.hshen.inscat.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView authorPhoto;
    TextView authorName;
    TextView commentText;

    public CommentViewHolder(View itemView) {
        super(itemView);
        authorPhoto = (SimpleDraweeView) itemView.findViewById(R.id.comment_image);
        authorName = (TextView) itemView.findViewById(R.id.comment_full_name);
        commentText = (TextView) itemView.findViewById(R.id.comment_text);
    }
}
