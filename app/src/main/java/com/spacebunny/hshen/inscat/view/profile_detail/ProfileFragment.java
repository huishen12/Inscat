package com.spacebunny.hshen.inscat.view.profile_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.PostCounts;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.model.UserCounts;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileFragment extends Fragment{
    public static final String KEY_USER = "user";

    public static ProfileFragment newInstance(@NonNull Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        User user = ModelUtils.toObject(getArguments().getString(KEY_USER), new TypeToken<User>() {});
        Bundle arguments
                = getArguments();
        String userid = getArguments().getString(KEY_USER);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));
        ProfileAdapter adapter = new ProfileAdapter(fakeUser(), fakeData());
        recyclerView.setAdapter(adapter);
    }

    private User fakeUser(){
        User user = new User();
        user.full_name = "Bunny";
        user.counts = new UserCounts();
        user.counts.follows = "1212";
        user.counts.followed_by = "2121";
        return user;
    }

    private List<Post> fakeData() {
        List<Post> postList = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i < 21; ++i) {
            Post post = new Post();
            User user = fakeUser();
            post.user = user;
            post.title = "post" + i;
            post.views_count = random.nextInt(10000);
            post.likes = new PostCounts();
            post.likes.count = random.nextInt(200);
            post.comments = new PostCounts();
            post.comments.count = random.nextInt(50);
            post.caption = "This is a description";

            postList.add(post);
        }
        return postList;
    }
}
