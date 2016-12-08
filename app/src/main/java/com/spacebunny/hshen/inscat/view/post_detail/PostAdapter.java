package com.spacebunny.hshen.inscat.view.post_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.database.DBReaderHelper;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.comment_list.CommentListActivity;
import com.spacebunny.hshen.inscat.view.comment_list.CommentListFragment;
import com.spacebunny.hshen.inscat.view.favorite_list.CategoryListFragment;
import com.spacebunny.hshen.inscat.view.favorite_list.ChooseCategoryActivity;
import com.spacebunny.hshen.inscat.view.like_list.LikeListActivity;
import com.spacebunny.hshen.inscat.view.like_list.LikeListFragment;
import com.spacebunny.hshen.inscat.view.profile_detail.ProfileActivity;
import com.spacebunny.hshen.inscat.view.profile_detail.ProfileFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PostAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_POST_IMAGE = 0;
    private static final int VIEW_TYPE_POST_INFO = 1;

    private final PostFragment postFragment;
    private final Post post;
    private ArrayList<String> collectedCategoryIds;

    public PostAdapter(@NonNull PostFragment postFragment, @NonNull Post post) {
        this.postFragment = postFragment;
        this.post = post;
        this.collectedCategoryIds = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_POST_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image, parent, false);
                return new ImageViewHolder(view);
            case VIEW_TYPE_POST_INFO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_info, parent, false);
                return new InfoViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_POST_IMAGE:
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                                                .setUri(Uri.parse(post.getImageUrl()))
                                                .setAutoPlayAnimations(true)
                                                .build();
                ((ImageViewHolder) holder).image.setController(controller);
                break;
            case VIEW_TYPE_POST_INFO:
                final User user = post.user;
                InfoViewHolder postDetailViewHolder = (InfoViewHolder) holder;
                postDetailViewHolder.likeCount.setText(String.valueOf(post.likes.count));
                postDetailViewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = holder.itemView.getContext();
                        Intent intent = new Intent(context, LikeListActivity.class);
                        intent.putExtra(LikeListFragment.KEY_POST, ModelUtils.toString(post, new TypeToken<Post>(){

                        }));
                        context.startActivity(intent);
                    }
                });
                postDetailViewHolder.commentCount.setText(String.valueOf(post.comments.count));
                postDetailViewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = holder.itemView.getContext();
                        Intent intent = new Intent(context, CommentListActivity.class);
                        intent.putExtra(CommentListFragment.KEY_POST, ModelUtils.toString(post, new TypeToken<Post>() {
                        }));
                        context.startActivity(intent);
                    }
                });
//                postDetailViewHolder.categoryCount.setText(String.valueOf(post.category_count));
//                Drawable categoryDrawable = post.categoryed
//                        ? ContextCompat.getDrawable(postDetailViewHolder.itemView.getContext(), R.drawable.ic_inbox_pink_300_18dp)
//                        : ContextCompat.getDrawable(postDetailViewHolder.itemView.getContext(), R.drawable.ic_inbox_black_18dp);
//                postDetailViewHolder.categoryButton.setImageDrawable(categoryDrawable);

                postDetailViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        share(view.getContext());
                    }
                });

                postDetailViewHolder.categoryButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        category(view.getContext());
                    }
                });

                new LoadUserTask(holder.itemView.getContext(), postDetailViewHolder, user.id).execute();
                new LoadPostCategoryTask(holder.itemView.getContext(), postDetailViewHolder, post.id).execute();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position){
        if (position == 0) {
            return VIEW_TYPE_POST_IMAGE;
        }
        else {
            return VIEW_TYPE_POST_INFO;
        }
    }

    public void setUserDetail (final Context curContext, InfoViewHolder postDetailViewHolder, @NonNull final User postUser) {
        postDetailViewHolder.authorName.setText(postUser.full_name);
        postDetailViewHolder.authorPhoto.setImageURI(Uri.parse(postUser.profile_picture));
        postDetailViewHolder.authorBio.setText(postUser.bio);
        postDetailViewHolder.authorPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(curContext, ProfileActivity.class);
                intent.putExtra(ProfileFragment.KEY_USER, ModelUtils.toString(postUser, new TypeToken<User>() {
                }));
                curContext.startActivity(intent);
            }
        });
    }

    public void setPostCategory (final Context context, InfoViewHolder postDetailViewHolder, @NonNull int categoryCount ) {
        postDetailViewHolder.categoryCount.setText(String.valueOf(categoryCount));
        if (categoryCount == 0)
            post.categoryed = false;
        else
            post.categoryed = true;
        Drawable categoryDrawable = post.categoryed
                ? ContextCompat.getDrawable(postDetailViewHolder.itemView.getContext(), R.drawable.ic_inbox_pink_300_18dp)
                : ContextCompat.getDrawable(postDetailViewHolder.itemView.getContext(), R.drawable.ic_inbox_black_18dp);
        postDetailViewHolder.categoryButton.setImageDrawable(categoryDrawable);
    }

    private class LoadUserTask extends AsyncTask<Void, Void, User> {
        String userid;
        InfoViewHolder mViewHolder;
        Context mContext;
        public LoadUserTask(Context context, InfoViewHolder viewHolder, String userid) {
            this.mContext = context;
            this.mViewHolder = viewHolder;
            this.userid = userid;
        }

        @Override
        protected User doInBackground(Void... voids) {
            try {
                return Ins.getUser(userid);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(User postUser) {
            setUserDetail(mContext, mViewHolder, postUser);
        }
    }

    private class LoadPostCategoryTask extends AsyncTask<Void, Void, Integer> {
        Context context;
        String postid;
        InfoViewHolder mViewHolder;

        public LoadPostCategoryTask(Context context, InfoViewHolder viewHolder, String postid) {
            this.mViewHolder = viewHolder;
            this.context = context;
            this.postid = postid;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            DBReaderHelper dbHelper = new DBReaderHelper(context);
            if (dbHelper.getPostCategoryCount(postid) != null) {
                post.category_count = dbHelper.getPostCategoryCount(postid);
            }
            else {
                dbHelper.insertPost(postid, 0);
                post.category_count = 0;
            }
            return post.category_count;
        }

        @Override
        protected void onPostExecute(Integer postCategory) {
            setPostCategory(context, mViewHolder, postCategory);
        }
    }

    public List<String> getReadOnlyCollectedCategoryIds() {
        return Collections.unmodifiableList(collectedCategoryIds);
    }

    public void updateCollectedCategoryIds(@NonNull List<String> categoryIds) {
        if (collectedCategoryIds == null) {
            collectedCategoryIds = new ArrayList<String>();
        }

        collectedCategoryIds.clear();
        collectedCategoryIds.addAll(categoryIds);

        post.categoryed = !categoryIds.isEmpty();
        notifyDataSetChanged();
    }

    public void updateCollectedCategoryIds(@NonNull List<String> addedIds, @NonNull List<String> removedIds) {
        if (collectedCategoryIds == null) {
            collectedCategoryIds = new ArrayList<String>();
        }

        collectedCategoryIds.addAll(addedIds);
        collectedCategoryIds.removeAll(removedIds);

        post.categoryed = !collectedCategoryIds.isEmpty();
        post.category_count += addedIds.size() - removedIds.size();
        notifyDataSetChanged();
    }

    private void share(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getImageUrl());
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_post)));
    }

    private void category(Context context) {
        if (collectedCategoryIds != null) {
            Intent intent = new Intent(context, ChooseCategoryActivity.class);
            intent.putStringArrayListExtra(CategoryListFragment.KEY_CHOSEN_CATEGORY_IDS, collectedCategoryIds);
            postFragment.startActivityForResult(intent, PostFragment.REQ_CODE_CATEGORY);
        }
    }

//    // Returns the URI path to the Bitmap displayed in specified ImageView
//    public Uri getLocalBitmapUri(SimpleDraweeView imageView) {
//        // Extract Bitmap from ImageView drawable
//        Drawable drawable = imageView.getDrawable();
//        Bitmap bmp = null;
//        if (drawable instanceof BitmapDrawable){
//            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        } else {
//            return null;
//        }
//        // Store image to default external storage directory
//        Uri bmpUri = null;
//        try {
//            // Use methods on Context to access package-specific directories on external storage.
//            // This way, you don't need to request external read/write permission.
//            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
//            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
//            FileOutputStream out = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.close();
//            bmpUri = Uri.fromFile(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bmpUri;
//    }
//
//    private File getExternalFilesDir(String directoryPictures) {
//
//    }
}
