package com.hash.android.srijan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hash.android.srijan.fragment.DashboardFragment;
import com.hash.android.srijan.fragment.HospitalityFragment;
import com.hash.android.srijan.fragment.SubscriptionFragment;
import com.hash.android.srijan.fragment.TeamFragment;
import com.hash.android.srijan.functions.CircleTransform;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hash.android.srijan.EventsActivity.events;
import static com.hash.android.srijan.LogInActivity.mGoogleApiClient;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "MyPrefsFileNew";
    public static ArrayList<Integer> eventArrayListImage;
    public static ArrayList<String> eventArrayListTextContent;
    public static ArrayList<String> eventArrayListTextHeading;
    public static ArrayList<Integer> eventArrayListIcon;
    public static int pos;
    public static User authUser;
    public static ArrayList<Event> finalEvent;
    static String m_Text;
    private static String urlProfileImg;
    private FirebaseAuth mAuth;
    private View navHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("initToken", "Token:" + FirebaseInstanceId.getInstance().getToken());

        FirebaseMessaging.getInstance().subscribeToTopic("User");

        updateDetails();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new DashboardFragment())
                .commit();


        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        boolean dialogShown = settings.getBoolean("dialogShownFinal2", false);




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
        header.setImageResource(R.drawable.cover);




        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);

        finalEvent = new ArrayList<>();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_explore) {
            fragment = new DashboardFragment();
        } else if (id == R.id.nav_registrations) {
            fragment = new SubscriptionFragment();
        } else if (id == R.id.nav_hospitality) {
//            Toast.makeText(this, "Hospitality", Toast.LENGTH_SHORT).show();
            fragment = new HospitalityFragment();
        } else if (id == R.id.nav_sponsors) {
            Toast.makeText(this, "Sponsors", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_log_out) {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                mAuth.signOut();
                            }
                        });
            } else {
                mAuth.signOut();
            }
            startActivity(new Intent(DashboardActivity.this, LogInActivity.class));
        } else if (id == R.id.nav_team) {
            fragment = new TeamFragment();
        } else if (id == R.id.nav_about_us) {
            Toast.makeText(this, "About us", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_contact_us) {
            Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_navigation) {
            navigateTo(22.560808, 88.413224);
        }


        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

        //22.560808, 88.413224
    }

    private void navigateTo(double lat, double lng) {

        String format = "geo:0,0?q=" + lat + "," + lng + "(Jadavpur University SL Campus)";
        Uri uri = Uri.parse(format);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    private void updateDetails() {

//        Event event = new Event("Robotech","Stair Climbing Bot",getString(R.string.loremIpsum),R.drawable.codingimage,R.drawable.codemelogo);
//        event.save();

        events = new ArrayList<>();
        events.clear();
        events.add(new Event("Robotech", "Step-Up", getString(R.string.loremIpsum), R.drawable.stairimage, R.drawable.stairlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Robotech", "RoboSoccer", getString(R.string.loremIpsum), R.drawable.soccermage, R.drawable.soccerlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Robotech", "Death Race", getString(R.string.loremIpsum), R.drawable.fastimage, R.drawable.fastlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Robotech", "On-Track", getString(R.string.loremIpsum), R.drawable.lineimage, R.drawable.linelogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Robotech", "Image Processing Bot", getString(R.string.loremIpsum), R.drawable.imageimage, R.drawable.imagelogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("Gaming", "FIFA", getString(R.string.loremIpsum), R.drawable.fifaimage, R.drawable.fastlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "Counter Strike", getString(R.string.loremIpsum), R.drawable.csimage, R.drawable.cslogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "NFS Most Wanted", getString(R.string.loremIpsum), R.drawable.nfsimage, R.drawable.nfslogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "Mini Militia", getString(R.string.loremIpsum), R.drawable.minimilitiaimage, R.drawable.mlogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("Code Me", "Sherlock", getString(R.string.loremIpsum), R.drawable.codingimage, R.drawable.stairlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Code Me", "Debugging", getString(R.string.loremIpsum), R.drawable.codingimage, R.drawable.codemelogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Code Me", "H42", getString(R.string.loremIpsum), R.drawable.codingimage, R.drawable.codemelogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("ManageMania", "B Model", getString(R.string.loremIpsum), R.drawable.bplanimage, R.drawable.stairlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("ManageMania", "Case Challenge", getString(R.string.loremIpsum), R.drawable.casechallenge, R.drawable.cslogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("ManageMania", "Monopoly", getString(R.string.loremIpsum), R.drawable.monpoly, R.drawable.mlogo, getString(R.string.stairclimbingBotDesc)));

    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;
        private OnItemClickListener mListener;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                try {
                    mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                } catch (ExecutionException | InterruptedException e1) {
                    e1.printStackTrace();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position) throws ExecutionException, InterruptedException;

            void onLongItemClick(View view, int position);
        }
    }

}
