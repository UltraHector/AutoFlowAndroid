package com.griffith.autoflow;


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

import com.griffith.autoflow.fragment.DetailSummaryFragment;
import com.griffith.autoflow.fragment.HomeFragment;

public class UserContentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

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

        // display the friend fragment by default
        Fragment fragment = new HomeFragment();
        displayFrament(fragment);
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
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            displayFrament(fragment);
        } else if (id == R.id.nav_detailed_summary) {
            fragment = new DetailSummaryFragment();
            displayFrament(fragment);
        } else if (id == R.id.nav_usage_comparison) {

        } else if (id == R.id.nav_fixture_efficiency) {

        } else if (id == R.id.nav_leak_alert) {

        } else if (id == R.id.nav_log_out) {

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
