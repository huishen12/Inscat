package com.spacebunny.hshen.inscat.view.favorite_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spacebunny.hshen.inscat.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    View categoryLayout;
    TextView categoryName;
    TextView categoryPostCount;
    ImageView categoryChosen;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryLayout = itemView.findViewById(R.id.category_layout);
        categoryName = (TextView) itemView.findViewById(R.id.category_name);
        categoryPostCount = (TextView) itemView.findViewById(R.id.category_post_count);
        categoryChosen = (ImageView) itemView.findViewById(R.id.category_post_chosen);
    }
}
