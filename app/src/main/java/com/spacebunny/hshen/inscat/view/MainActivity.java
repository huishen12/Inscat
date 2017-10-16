package com.spacebunny.hshen.inscat.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.User;
import com.spacebunny.hshen.inscat.utils.ModelUtils;
import com.spacebunny.hshen.inscat.view.favorite_list.CategoryListFragment;
import com.spacebunny.hshen.inscat.view.post_list.LikeListFragment;
import com.spacebunny.hshen.inscat.view.post_list.PostListFragment;
import com.spacebunny.hshen.inscat.view.profile_detail.ProfileActivity;
import com.spacebunny.hshen.inscat.view.profile_detail.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, PostListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = PostListFragment.newInstance();
                        setTitle("Inscat");
                        break;
                    case R.id.drawer_item_likes:
                        fragment = LikeListFragment.newInstance();
                        setTitle("Likes");
                        break;
                    case R.id.drawer_item_categories:
                        fragment = CategoryListFragment.newInstance(false, null);
                        setTitle("Categories");
                        break;
                }

                drawerLayout.closeDrawers();
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }

                return false;
            }
        });
        setupNavHeader();
    }

    private void setupNavHeader() {
        View headerView = navigationView.getHeaderView(0);

        //TODO: change to real name
        ((TextView) headerView.findViewById(R.id.nav_header_user_name)).setText(Ins.getCurrentUser().full_name);

        //TODO: change to real photo
        final SimpleDraweeView userPhoto = ((SimpleDraweeView) headerView.findViewById(R.id.nav_header_user_photo));
        userPhoto.setImageURI(Uri.parse(Ins.getCurrentUser().profile_picture));
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = userPhoto.getContext();
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileFragment.KEY_USER, ModelUtils.toString(Ins.getCurrentUser(), new TypeToken<User>(){
                }));
                context.startActivity(intent);
            }
        });

        TextView logOutButton = (TextView) headerView.findViewById(R.id.nav_header_logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ins.logout(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}

