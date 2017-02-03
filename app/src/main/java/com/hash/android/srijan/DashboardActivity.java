package com.hash.android.srijan;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hash.android.srijan.fragment.DashboardFragment;
import com.hash.android.srijan.fragment.SubscriptionFragment;
import com.hash.android.srijan.functions.CircleTransform;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "MyPrefsFileNew";
    static ArrayList<Integer> eventArrayListImage;
    static ArrayList<String> eventArrayListTextContent;
    static ArrayList<String> eventArrayListTextHeading;
    static ArrayList<Integer> eventArrayListIcon;
    static int pos;
    static User authUser;
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

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.frame_container, new DashboardFragment())
//                .commit();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialogShown = settings.getBoolean("dialogShownfinal", false);

        //TODO: Remove this dailog box and embed it into the login screen or make a slider activity for it.
        if (!dialogShown) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Contact No.");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text = input.getText().toString();
                }
            });
            builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            if (m_Text != null)
                Log.d("sharedprefs:", m_Text);
            else
                Log.d("sharedprefs:", "Fail");
            builder.show();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShownfinal", true);
            editor.putString("phone", m_Text);
            editor.commit();
        }




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

        String phoneNumber = settings.getString("phone", "null");
        Log.d("phone", phoneNumber);
        authUser = new User();
        authUser.setName(user.getDisplayName());
        authUser.setEmail(user.getEmail());
        authUser.setId(user.getUid());
        authUser.setPhoneNumber(phoneNumber);
        authUser.saveUser();


        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImage);


        eventArrayListTextHeading = new ArrayList<String>();
        eventArrayListImage = new ArrayList<Integer>();
        eventArrayListIcon = new ArrayList<Integer>();
        eventArrayListTextContent = new ArrayList<String>();

        updateContent();
        RecyclerView exploreRecyclerView = (RecyclerView) findViewById(R.id.exploreRecyclerView);
        exploreRecyclerView.setHasFixedSize(true);
        exploreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exploreRecyclerView.setAdapter(new EventRecyclerAdapter());

        exploreRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, exploreRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                Log.d("postion", String.valueOf(position));
                Intent i = new Intent(DashboardActivity.this, EventsActivity.class);
//                i.putExtra("position", position);
                pos = position;
                startActivity(i);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void updateContent() {

        addEvent(getString(R.string.robticsEventName), R.drawable.roboticsimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.robotics);
        addEvent(getString(R.string.codeMe), R.drawable.codingimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.codemelogo);
        addEvent(getString(R.string.gaming), R.drawable.gamingimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.gaminglogo);
        addEvent("Manage Mania", R.drawable.manageeimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.managelogo);
        addEvent("Exhibition", R.drawable.exhiimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.exhilogo);
        addEvent("Workshop", R.drawable.workshopimage, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.workshoplogo);
        addEvent("Official Merchandise", R.drawable.merch2, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan feugiat ipsum id imperdiet. In sed turpis odio.", R.drawable.merchlogo);

    }

    private void addEvent(String robotech, int codingimage, String s, int robotics) {
        eventArrayListTextHeading.add(robotech);
        eventArrayListImage.add(codingimage);
        eventArrayListTextContent.add(s);
        eventArrayListIcon.add(robotics);
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

        Fragment fragment = null;
        if (id == R.id.nav_explore) {
            fragment = new DashboardFragment();
            // Handle the camera action
        } else if (id == R.id.nav_registrations) {
            fragment = new SubscriptionFragment();
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
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
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
