package com.spacebunny.hshen.inscat.view.profile_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileFragment extends Fragment{
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));
        ProfileAdapter adapter = new ProfileAdapter(fakeUser(), fakeData());
        recyclerView.setAdapter(adapter);
    }

    private User fakeUser(){
        User user = new User();
        user.full_name = "Bunny";
//        user.description = "Bunny Profile description";
//        user.follower = 1212;
//        user.following = 2121;
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
            post.likes_count = random.nextInt(200);
            post.comments_count = random.nextInt(50);
            post.description = "This is a description";

            postList.add(post);
        }
        return postList;
    }
}
