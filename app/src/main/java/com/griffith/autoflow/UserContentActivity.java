package com.griffith.autoflow;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.griffith.autoflow.constant.Constant;
import com.griffith.autoflow.fragment.DetailSummaryFragment;
import com.griffith.autoflow.fragment.HomeFragment;

public class UserContentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private TextView usernameTv, useremailTv;

    private Fragment homeFragment, detailSummaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_content);

        // Initiate the  tool bar
        toolbar = (Toolbar) findViewById(R.id.activity_user_content_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.activity_user_content_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initiate the left-slide navigation menu
        navigationView = (NavigationView) findViewById(R.id.activity_user_content_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        // display the friend fragment by default
        homeFragment = new HomeFragment();
        detailSummaryFragment = new DetailSummaryFragment();
        displayFrament(homeFragment);

        // username and email in the navimenu
        usernameTv = (TextView)headerView.findViewById(R.id.activity_user_content_header_username_tv);
        useremailTv = (TextView)headerView.findViewById(R.id.activity_user_content_header_email_tv);

        SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String username = sharedPref.getString(Constant.SHARED_PREFERENCE_USERNAME, "");
        String useremail = sharedPref.getString(Constant.SHARED_PREFERENCE_USEREMAIL, "");
        usernameTv.setText(username);
        useremailTv.setText(useremail);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayFrament(homeFragment);
        } else if (id == R.id.nav_detailed_summary) {
            displayFrament(detailSummaryFragment);
        } else if (id == R.id.nav_usage_comparison) {

        } else if (id == R.id.nav_fixture_efficiency) {

        } else if (id == R.id.nav_leak_alert) {

        } else if (id == R.id.nav_log_out) {
            // clean login details
            SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
            //redirect to login page
            Intent loginIntent = new Intent(UserContentActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayFrament(Fragment fragment){
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_user_content_frame_content, fragment).commit();
        } else {
            // error in creating fragment
            Log.e("UserContentActivity", "Error in creating fragment");
        }
    }




}
