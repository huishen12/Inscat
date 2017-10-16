package com.spacebunny.hshen.inscat.view.profile_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.AndroidDatabaseManager;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.PostCounts;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.model.UserCounts;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.utils.UIUtils;
import com.spacebunny.hshen.inscat.view.base.SpaceItemDecoration;
import com.spacebunny.hshen.inscat.view.post_list.PostListAdapter;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileFragment extends Fragment{
    public static final String TAG = "Profile Fragment";
    public static final String KEY_USER = "user";

    private PostListAdapter adapter;
    User user;

    public static ProfileFragment newInstance(@NonNull Bundle args) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.profile_recycler_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout profileInfoView = (LinearLayout) view.findViewById(R.id.profile_info);
        user = ModelUtils.toObject(getArguments().getString(KEY_USER), new TypeToken<User>() {
        });
        Log.d(TAG, "User is " + user.full_name);

        ImageView profilePhoto = (ImageView) profileInfoView.findViewById(R.id.profile_photo);
        profilePhoto.setImageURI(Uri.parse(user.profile_picture));

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

        TextView profileName = (TextView) profileInfoView.findViewById(R.id.profile_name);
        profileName.setText(user.full_name);

        TextView profileFollowed = (TextView) profileInfoView.findViewById(R.id.profile_follower);
        profileFollowed.setText(user.counts.followed_by);

        TextView profileFollowing = (TextView) profileInfoView.findViewById(R.id.profile_following);
        profileFollowing.setText(user.counts.follows);

        TextView profileFollowedText = (TextView) profileInfoView.findViewById(R.id.profile_follower_text);
        profileFollowedText.setText("followers");

        TextView profileFollwingText = (TextView) profileInfoView.findViewById(R.id.profile_following_text);
        profileFollwingText.setText("following");

        TextView profileBio = (TextView) profileInfoView.findViewById(R.id.profile_bio);
        profileBio.setText(user.bio);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_post_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));
        adapter = new PostListAdapter(new ArrayList<Post>(), new UIUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadProfilePostTask(adapter.getDataCount() / Ins.COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private class LoadProfilePostTask extends AsyncTask<Void, Void, List<Post>> {
        int page;

        public LoadProfilePostTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Post> doInBackground(Void... voids) {
            try {
                return Ins.getPostListUser(user.id);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Post> posts) {
            if (posts != null) {
                adapter.append(posts);
                adapter.setShowLoading(posts.size() == Ins.COUNT_PER_PAGE);
            }
            else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }



    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.profile_recycler_view, container, false);
//        return view;
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        user = ModelUtils.toObject(getArguments().getString(KEY_USER), new TypeToken<User>() {
//        });
//        user.counts = new UserCounts();
//        user.counts.followed_by = "2121";
//        user.counts.follows = "1212";
//        Log.d(TAG, "User is " + user.full_name);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_medium)));
//
//        adapter = new ProfileAdapter(fakeUser(), fakeData(), new UIUtils.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                Log.d(TAG, "On load more");
//            }
//        });
//    }
//        ProfileAdapter adapter = new ProfileAdapter(user, fakeData());
//        recyclerView.setAdapter(adapter);

//        AsyncTask<Void, Void, User> newuser = AsyncTaskCompat.executeParallel(new LoadProfileTask(user.id));

//        adapter = new ProfileAdapter(user, new ArrayList<Post>(), new UIUtils.LoadMoreListener() {
//            @Override
//            public void onLoadMore() {
////                AsyncTaskCompat.executeParallel(new LoadProfileTask(user.id));
//                AsyncTaskCompat.executeParallel(new LoadPostTask(adapter.getDataCount() / Ins.COUNT_PER_PAGE + 1, user.id));
//            }
//        });
//        recyclerView.setAdapter(adapter);
//    }

//    private class LoadProfileTask extends AsyncTask<Void, Void, User> {
//        String userid;
//
//        public LoadProfileTask(String userid) {
//            this.userid = userid;
//        }
//
//        @Override
//        protected User doInBackground(Void... params) {
//            try {
//                User curuser =  Ins.getUser(userid);
//                return curuser;
//            } catch (IOException | JsonSyntaxException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(User curuser) {
//            if (curuser != null) {
//                user = curuser;
//            }
//            else {
//                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
//            }
//        }
//    }

//    class LoadPostTask extends AsyncTask<Void, Void, List<Post>> {
//        int page;
//        String userid;
//
//        public LoadPostTask(int page, String userid) {
//            this.page = page;
//            this.userid = userid;
//            Log.d(TAG, "Page number is " + page);
//            Log.d(TAG, "User id is " + userid);
//        }
//
//        @Override
//        protected List<Post> doInBackground(Void... params) {
//            try {
//                List<Post> posts = Ins.getPostListUser(userid);
//                Log.d(TAG, "Posts are " + posts);
//                return posts;
//            } catch (IOException | JsonSyntaxException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(List<Post> posts) {
//            if (posts != null) {
//                adapter.append(posts);
//                adapter.setShowLoading(posts.size() > Ins.COUNT_PER_PAGE);
//            } else {
//                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
//            }
//        }
//    }


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
            post.category_count = random.nextInt(10000);
            post.likes = new PostCounts();
            post.likes.count = random.nextInt(200);
            post.comments = new PostCounts();
            post.comments.count = random.nextInt(50);
            post.caption.text = "This is a description";

            postList.add(post);
        }
        return postList;
    }
}
