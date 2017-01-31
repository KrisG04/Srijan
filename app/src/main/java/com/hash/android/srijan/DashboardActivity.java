package com.hash.android.srijan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hash.android.srijan.functions.CircleTransform;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static String urlProfileImg;
    private FirebaseAuth mAuth;
    private Handler mHandler;

    private View navHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        urlProfileImg = String.valueOf(user.getPhotoUrl());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navHeader = navigationView.getHeaderView(0);
        TextView txtName = (TextView) navHeader.findViewById(R.id.name);
        TextView emailTextView = (TextView) navHeader.findViewById(R.id.website);
        ImageView header = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        ImageView profileImage = (ImageView) navHeader.findViewById(R.id.img_profile);


        txtName.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(header);

        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explore) {
            Toast.makeText(this, "Explore", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_registrations) {
            Toast.makeText(this, "Explore", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_hospitality) {
            Toast.makeText(this, "Hospitality", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_sponsors) {
            Toast.makeText(this, "Sponsors", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_log_out) {
            mAuth.signOut();
            startActivity(new Intent(DashboardActivity.this, LogInActivity.class));
        } else if (id == R.id.nav_srijan) {
            Toast.makeText(this, "Srijan", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_journal) {
            Toast.makeText(this, "The JU Journal", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_ecell) {
            Toast.makeText(this, "Entrepreneurship Cell", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about_us) {
            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact_us) {
            Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
