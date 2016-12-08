package com.spacebunny.hshen.inscat.view.favorite_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Category;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.UIUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_CATEGORY = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private final CategoryListFragment categoryListFragment;
    private List<Category> data;
    private UIUtils.LoadMoreListener mLoadMoreListener;
    private boolean isChoosingMode;
    private boolean showLoading;

    public CategoryListAdapter(CategoryListFragment categoryListFragment, @NonNull List<Category> data, @NonNull UIUtils.LoadMoreListener loadMoreListener, boolean isChoosingMode) {
        this.categoryListFragment = categoryListFragment;
        this.data = data;
        this.mLoadMoreListener = loadMoreListener;
        this.isChoosingMode = isChoosingMode;
        this.showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CATEGORY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_loading, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) {
            mLoadMoreListener.onLoadMore();
        } else {
            final Category category = data.get(position);
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;

            Context context = holder.itemView.getContext();
            String categoryPostCountString = MessageFormat.format(context.getResources().getString(R.string.post_count), category.post_count);

            categoryViewHolder.categoryName.setText(category.name);
            categoryViewHolder.categoryPostCount.setText(categoryPostCountString);

            if (isChoosingMode) {
                categoryViewHolder.categoryChosen.setVisibility(View.VISIBLE);
                categoryViewHolder.categoryChosen.setImageDrawable(
                        category.isChoosing ? ContextCompat.getDrawable(context, R.drawable.ic_check_box_black_24dp)
                                            : ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank_black_24dp));
                categoryViewHolder.categoryLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        category.isChoosing = !category.isChoosing;
                        notifyItemChanged(position);
                    }
                });
            } else {
                categoryViewHolder.categoryChosen.setVisibility(View.GONE);
                categoryViewHolder.categoryLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        categoryPostList(view.getContext(), category);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size() ? VIEW_TYPE_CATEGORY : VIEW_TYPE_LOADING;
    }

    public void append(@NonNull List<Category> moreCategories) {
        data.addAll(moreCategories);
        notifyDataSetChanged();
    }

    public void prepend(@NonNull List<Category> morePreCategories) {
        this.data.addAll(0, morePreCategories);
        notifyDataSetChanged();
    }

    public int getDataCount() {
        return data.size();
    }

    @NonNull
    public ArrayList<String> getSelectedCategoryIds() {
        ArrayList<String> selectedCategoryIds = new ArrayList<>();
        for (Category category : data) {
            if (category.isChoosing) {
                selectedCategoryIds.add(category.id);
            }
        }
        return selectedCategoryIds;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    private void categoryPostList(Context context, Category category) {
        if (category != null) {
            Intent intent = new Intent(context, CategoryPostListActivity.class);
            intent.putExtra(CategoryPostListFragment.KEY_USER_ID, Ins.getCurrentUser().id);
            intent.putExtra(CategoryPostListFragment.KEY_CATEGORY_ID, category.id);
            intent.putExtra(CategoryPostListFragment.KEY_CATEGORY_NAME, category.name);
            context.startActivity(intent);
        }
    }

}
