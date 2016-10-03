package com.spacebunny.hshen.inscat.view.profile_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacebunny.hshen.inscat.R;

public class ProfileInfoViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePhoto;
    TextView profileName;
    TextView profileDescription;
    TextView profileFollower;
    TextView profileFollowing;

    public ProfileInfoViewHolder(View itemView) {
        super(itemView);
        profilePhoto = (ImageView) itemView.findViewById(R.id.profile_photo);
        profileName = (TextView) itemView.findViewById(R.id.profile_name);
        profileDescription = (TextView) itemView.findViewById(R.id.profile_description);
        profileFollower = (TextView) itemView.findViewById(R.id.profile_follower);
        profileFollowing = (TextView) itemView.findViewById(R.id.profile_following);
    }
}
