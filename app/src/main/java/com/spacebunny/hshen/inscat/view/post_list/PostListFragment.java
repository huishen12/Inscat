package com.spacebunny.hshen.inscat.view.post_list;

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

public class PostListFragment extends Fragment {

    public static PostListFragment newInstance() {
        return new PostListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));

        //TO DO change to real data
        PostListAdapter adapter = new PostListAdapter(fakeData());
        recyclerView.setAdapter(adapter);
    }

    private List<Post> fakeData() {
        List<Post> postList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; ++i) {
            Post post = new Post();
            User user = new User();
            user.full_name = "Bunny";
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
