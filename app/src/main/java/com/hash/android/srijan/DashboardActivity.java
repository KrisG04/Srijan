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
        events.add(new Event("Robotech", "Step-Up", "A special event this year, Step Up asks you to step up your game and step up your bots. \nLet your bots step up on the way to victory.", R.drawable.stairimage, R.drawable.stairlogo,
                "<h2>Design a manually controlled robot to climb stairs.</h2><br>" +
                        "<p><b>Robot Specifications:</b><br>" +
                        "1. The dimension of robot is 25cmx20cmx20cm, with 10% tolerance.<br>" +
                        "2. There’s no restriction in controlling ways, i.e., control can be wired or wireless.<br>" +
                        "3. Readymade toys/Lego kits/Hydraulic System/IC engines are not allowed.<br></p>" +
                        "<br><p><b>Team Specifications:</b><br>" +
                        "1. Team should consist maximum 4 members.<br>" +
                        "2. Teams can be formed with cross college members.<br>" +
                        "3. Only 2 persons will be allowed inside the arena.<br>" +
                        "4. Only one robot from each team is allowed inside the arena.</p><br>" +
                        "<b>Rules and Regulations:</b><br>" +
                        "1. Unethical behavior, e.g., speaking/indicating abusive in the arena, pulling a fight with coordinators/volunteers can cause suspension of the team from the event.<br>" +
                        "2. Damaging the arena may lead to direct disqualification.<br>" +
                        "3. The decision of team Step-Up is final.<br>" +
                        "4. The coordinators have all rights to take final decisions during the event.<br>" +
                        "5. The robot maybe disqualified for violating the safety measures.<br>" +
                        "<br><p><b>Event Rules:</b><br>" +
                        "1. Each team will be provided with a 220V AC – 50Hz supply. Any kind of onboard power source have to be arranged by the team.<br>" +
                        "2. There will be 2 rounds.<br>" +
                        "<br><b>Round 1:</b>: The Robot has to climb up a stair. Points will be on climbing up and time taken. The teams with more points get selected for the round 2.<br>" +
                        "<br><b>Round 2:</b> In this round the robot has to go through a path, a stair will included in it. There will be certain objectives the robot has to fulfill, which will be disclosed at the time of arena<br>" +
                        "3. Damaging the arena may cause direct disqualification.<br></p>" +
                        "<br><p><b>Point System:</b><br>" +
                        "This will be introduced at the time of event.<br></p>"));
        events.add(new Event("Robotech", "Pac-Man", "The most A-maze-ing bot chase as you dodge invisible obstacles, decode messages that can't be seen and hurry across the map to eat good food and destroy that poison.", R.drawable.casechallenge, R.drawable.soccerlogo, "" +
                "<h1>Autonomously collect food items in a grid and go to the finish line.</h1><br>" +
//                                "<br>An autonomous robot which while following a straight line in a grid should also be able to decode IR messages.<br>" +
                "<p><b>Arena Description: -</b><br> The arena will consist of:<br>" +
                "1. 128 squares (16 * 8) grid [No. of squares may change] of black colour with white border.<br>" +
                "2. The white lines of width 2.5 cm separating each square.<br>" +
                "3. Infrared LEDs in corners of each square (Intersections of white lines) emitting IR codes.<br></p>" +
                "<p><b>Event description:<br></b>" +
                "<br>1. Each IR led at a food will emit an IR-code which will give the relative position of the next food item.<br>" +
                "2. The bot has to decode the code and proceed to the next food item.<br>" +
                "3. All 4 adjacent LEDs surrounding the poison will emit an additional danger code (as in mine craft).<br>" +
                "4. Any damage to the arena will lead to immediate disqualification.<br></p>" +
                "<p><b>Rules and Regulations:</b><br>" +
                "1. The bot should be able to follow a line, i.e. it should have line following capabilities.<br>" +
                "2. The maximum dimensions of the robot is 30cm * 20cm (L * B)." +
                "<br> 3. The bot should be able to decode IR messages, for tutorial click here.<br>" +
                "There will a total of two rounds:<br><br>" +
                "<b>Round 1:</b><br>In this round the arena will contain only food (and no poison). Each food item will have a LED which will provide the relative address of the next food item.<br>" +
                "<br><b>Round 2:</b><br>Food and poison both will be present. The bot has to collect all food items while avoiding the poison. Each food item will have the relative address of its next food location. All 4 adjacent LEDs surrounding the poison will emit an additional danger code (as in mine craft).<br>" +
                "<br><b>IR Code:</b><br>" +
                "The IR LEDs will emit code modulated over standard 38 kHz frequency. The participants are advised to use TSOP1738 IR detectors for the code detection purpose. Exact details about what code is transmitted will be uploaded a week before the event, please keep checking our website www.srijanju.in/srijan for more updated details.<br>" +
                "Participants will be given scores based on the following criteria in this round:<br><br>" +
                "* The total food items collected.<br>" +
                "* The total time taken.<br>" +
                "* Negative marks for human interventions and poison encounters.<br><br>" +
                "<b>Total score</b> = (No. of (food item – poison item) collected * 100 – Time taken in seconds*5)<br>" +
                "(SCORING FORMULAE IS SUBJECT TO CHANGE)<br></p>" +
                "<p><b>General Rules:-</b><br>" +
                "1. The decision of the event co-ordinator is final in case of any conflict.<br>" +
                "2. The participants are to play fair any unfair means caught will lead to disqualification.<p><br>" +
                "This is the link to the problem statement<br>" +
                "https://onedrive.live.com/view.aspx?resid=B888735693DC1F44%215776&ithint=file%2Cdocx&app=Word&authkey=%21ANys5wPUDSPU6WI<br>"));
//        events.add(new Event("Robotech", "Kick Off", "Let your football skills take over as you try to outwit your opponent to score goals", R.drawable.fastimage, R.drawable.fastlogo, ""));
        events.add(new Event("Robotech", "Kick Off", "Welcome to Kick Off. \nLet your football skills take over as you try to outwit your opponent to score goals.", R.drawable.soccermage, R.drawable.soccerlogo,
                "<h2>Design a manually controlled robot to score goal.</h2><br>" +
                        "<p><b>Robot Specifications:</b><br>" +
                        "1. The dimension of robot should be 25 cm x 25 cm x 20 cm, with 10% tolerance.<br>" +
                        "2. Weight of the robot should not exceed from 3kg.<br>" +
                        "3. There’s no restriction in wheel size, number or type.<br>" +
                        "4. Maximum voltage input allowed is 24 VDC.<br>" +
                        "5. There’s no restriction in controlling ways, i.e., control can be wired or wireless.<br>" +
                        "6. Ready made toys/Lego kits/Hydraulic System/IC engines are not allowed.<br></p>" +
                        "<p><b>Team Specifications:</b><br>" +
                        "1. Team should consist of a maximum of 4 members.<br>" +
                        "2. Teams can be formed with cross college members.<br>" +
                        "3. Only 2 persons will be allowed inside the arena.<br>" +
                        "4. Only one robot from each team is allowed inside the arena.<br></p>" +
                        "<p><b>Rules and Regulations:</b><br>" +
                        "1. Unethical behavior, e.g., speaking/indicating abusive in the arena, pulling a fight with coordinators/volunteers can cause suspension of the team from the event.<br>" +
                        "2. Damaging the arena may lead to direct disqualification.<br>" +
                        "3. The decision of team Kick-Off is final.<br>" +
                        "4. The coordinators have all rights to take final decisions during the event.<br>" +
                        "5. The robot maybe disqualified for violating the safety measures.<br></p>" +
                        "<p><b>Event Rules:</b><br>" +
                        "1. Each team will be provided with a 220V AC – 50Hz supply. Any kind of onboard power source have to be arranged by the team.<br>" +
                        "2. In each arena, there will be solid objects, touching or damaging which won’t cost any point. However, damaging the arena may cause direct disqualification.<br>" +
                        "<p>There will be 2 rounds.<br>" +
                        "<b>Round 1:</b> Match will be in between two participant bots. Winner will be based on maximum number of goals scored. <br>" +
                        "<br><b>Round 2:</b> In this round a region will be introduced as dead area. Entering this region will cause penalty. Winner of each match in this round will be decided on the basis of points.<br><br>" +
                        "</p><p><b>Point System:</b><br>This will be introduced at the time of event.<br></p>" +
                        "<p><b>Arenas :</b><br> The pictures provided are the 2 sample arenas for round 1 and round 2.<br></p>" +
                        "Sample arenas are just there to indicate what type of arena bots will be facing. The obstacles may change. They are not drawn to scale.<br>" +
                        "Photo Link - https://www.facebook.com/events/212415755830061/permalink/212417455829891/<br><br>"));
        events.add(new Event("Robotech", "On-Track", getString(R.string.loremIpsum), R.drawable.lineimage, R.drawable.linelogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Robotech", "Image Processing Bot", getString(R.string.loremIpsum), R.drawable.imageimage, R.drawable.imagelogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("Gaming", "FIFA", getString(R.string.loremIpsum), R.drawable.fifaimage, R.drawable.fastlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "Counter Strike", getString(R.string.loremIpsum), R.drawable.csimage, R.drawable.cslogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "NFS Most Wanted", getString(R.string.loremIpsum), R.drawable.nfsimage, R.drawable.nfslogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Gaming", "Mini Militia", getString(R.string.loremIpsum), R.drawable.minimilitiaimage, R.drawable.mlogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("Code Me", "Sherlock", getString(R.string.loremIpsum), R.drawable.codingimage, R.drawable.stairlogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Code Me", "Debugging", getString(R.string.loremIpsum), R.drawable.codingimage, R.drawable.codemelogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Code Me", "H42", "Are you a coding geek? Do you think you have what it takes to be #1 and tackle all sorts of algorithmic challenges? <br>  Show off your skills as you take on an army of problems equipped with C/C++/Java as your weapon.", R.drawable.codingimage, R.drawable.codemelogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("ManageMania", "B Model", getString(R.string.loremIpsum), R.drawable.bplanimage, R.drawable.stairlogo,
                "<h2>B-Model Competition at Srijan’17 will consist of two rounds<h2><br>" +
                        "<p><b>Round 1 (Preliminary)</b><br>" +
                        "• Participants are required to email a soft copy of the Elevator Pitch of the Business Idea in not more than 6 slides and a short video in which a team member explains the business idea. Refer to the submission details for more information on content, format and deadline of the submission.<br>" +
                        "• In case you are not able to mail the submission or finding it difficult to do so, in that case please upload your submission on the Google drive and share the downloadable link to the email id mentioned in the submission details.<br>" +
                        "• This is an elimination round and the teams will be shortlisted by a jury panel and will be invited for the second round. The list of the shortlisted teams will be released by 3rd week of February 2017.<br>" +
                        "• Shortlisted teams will be informed well before hand about the second round and will be communicated the same by mail.<br></p>" +
                        "<p><b>Round 2 (Final)</b><br>" +
                        "• The shortlisted teams will present their Business idea along with the prototype (if applicable) in front of distinguished panel of judges, followed by Q&A, to compete for the cash prize for the winner and the runner up.<br>" +
                        "• If the Jadavpur University Entrepreneurship Cell finds any idea very interesting and scalable, they may further get the teams mentored to develop their idea into a business and get them incubated under the aegis of Jadavpur University Entrepreneurship Cell.<br>" +
                        "Submission Guidelines:<br>" +
                        "• Submission to be sent to: bmodel.srijan17@gmail.com<br>" +
                        "• Subject line of the Email: <Team Name>_Round 1_BModel<br>" +
                        "• Submissions for Round 1 to be sent on or before: 11:59 PM, 20th Feb, 2017<br></p>" +
                        "<p><br><b>Elevator Pitch (Round 1):</b><br>" +
                        "• Adhere to the 6 slide limit<br>" +
                        "• First slide should contain information about the team and the participants<br>" +
                        "• The remaining 5 slides should contain the following:<br><br>" +
                        "o Summary of the idea<br>" +
                        "o Customer perspective<br>" +
                        "o USP<br>" +
                        "o Market Size<br>" +
                        "o Product/Service Detail<br>" +
                        "o Revenue Model and Financials<br>" +
                        "o Sales and Distribution Model<br>" +
                        "o SWOT Analysis<br>" +
                        "• Submission Format: PPT, PDF<br>" +
                        "• Naming of file: <Team Name>_Presentation_Round 1_BModel<br></p>" +
                        "<b><p>Video:</b><br>" +
                        "• The video is for better understanding of the idea and ease the judging<br>" +
                        "• Normal video quality would suffice<br>" +
                        "• Duration of the video shouldn’t exceed 5 min<br>" +
                        "• Submission Formats: MP4,MPG,WMV,AVI<br>" +
                        "• Naming of the file: <Team Name>_Video_Round 1_BModel<br></p></p>" +
                        "<p><b>Round 2 Format:</b><br>" +
                        "For the Round 2, each team needs to make a 6-10 Slides Presentation, which may contain the following:<br>" +
                        "• Summary of the idea<br>" +
                        "• Customer Perspective<br>" +
                        "• Product/Service Details<br>" +
                        "• USP<br>" +
                        "• Market size<br>" +
                        "• Revenue Model<br>" +
                        "• Sales Model<br>" +
                        "• SWOT Analysis<br>" +
                        "• Financials<br>" +
                        "The teams are requested to come along with their prototype during the Round 2, if ready, having a prototype if applicable would fetch more points.<br>" +
                        "Each team needs to send their presentation to us at bmodel.srijan17@gmail.com <br>" +
                        "Each team in the Round 2 will have 10 minutes of presentation time followed by 5 minutes of Q&A <br>" +
                        "Each team needs to give their confirmation for Round 2 on the following by 22nd February 2017, failure to do so may lead to your spot being offered to the waitlisted teams:<br>" +
                        "• Participation in Round 2 of the B Model Competition at Srijan’17<br>" +
                        "• Name and contact details of the team members attending the event<br></p>" +
                        "<br><p><b>Rules:</b><br>" +
                        "• This competition is open to students pursuing undergraduate and postgraduate degree and to start-ups who are in the initial stages. The eligibility criteria below apply to all business ideas entering the contest.<br>" +
                        "• Teams applying should not have received venture capital financing for their business plan.<br>" +
                        "• A team can have a maximum of 4 members and also, a member can be a part of only one team.<br>" +
                        "• A team cannot submit more than one business idea. If found so in any stage of the competition, it would lead to the disqualification of the team.<br></p>" +
                        "<p><b>Benefits:</b><br>" +
                        "• The top 4 teams will get certificate of recognition.<br>" +
                        "• The top 2 teams selected by the independent jury will get cash prize and certificate of recognition and coverage on Jadavpur University media channels.<br>" +
                        "• The team (if any) selected by Jadavpur University Entrepreneurship Cell will have an opportunity to be mentored by experts and get incubated.<br></p>" +
                        "<p><b>Terms and Conditions:</b><br>" +
                        "• All decisions in matters of eligibility, authenticity and final judgments will be with organizing committee at Srijan’17 and Jadavpur University Entrepreneurship Cell.<br>" +
                        "• The format and structure of events is subject to change before the actual beginning of the event.<br>" +
                        "• The top 2 teams qualifying for the cash prize will be identified by an independent jury. These teams may or may not overlap with the teams identified by Jadavpur University Entrepreneurship Cell for mentoring and incubation.<br>" +
                        "• The conditions for mentoring and incubation lies with the Entrepreneurship Cell of Jadavpur University and would be communicated to the teams selected for the same.</p>"));
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
