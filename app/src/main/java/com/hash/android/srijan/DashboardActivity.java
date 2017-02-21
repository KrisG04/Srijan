package com.hash.android.srijan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hash.android.srijan.fragment.AboutUsFragment;
import com.hash.android.srijan.fragment.ContactUsFragment;
import com.hash.android.srijan.fragment.DashboardFragment;
import com.hash.android.srijan.fragment.SponsorsFragment;
import com.hash.android.srijan.fragment.SubscriptionFragment;
import com.hash.android.srijan.functions.CircleTransform;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hash.android.srijan.EventsActivity.events;
import static com.hash.android.srijan.LogInActivity.mGoogleApiClient;
import static com.hash.android.srijan.fragment.SubscriptionFragment.arrayList;

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
        arrayList = new ArrayList<>();
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

        assert user != null;
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_explore) {
            fragment = new DashboardFragment();
        } else if (id == R.id.nav_registrations) {
            fragment = new SubscriptionFragment();
        } else if (id == R.id.nav_sponsors) {
            fragment = new SponsorsFragment();
        } else if (id == R.id.nav_log_out) {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                // ...
                                mAuth.signOut();
                            }
                        });
            } else {
                mAuth.signOut();
            }
            startActivity(new Intent(DashboardActivity.this, LogInActivity.class));
        } else if (id == R.id.nav_team_ecell) {
            String url = "https://www.facebook.com/juslecell";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_team_journal) {
            String url = "http://www.jujournal.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_team_srijan) {
            String url = "http://www.srijanju.in";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_team_sc) {
            String url = "http://juscofficial.wixsite.com/home";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } else if (id == R.id.nav_about_us) {
            fragment = new AboutUsFragment();
        } else if (id == R.id.nav_contact_us) {
            fragment = new ContactUsFragment();
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
        events.add(new Event("Robotech", "Step-Up", "A special event this year, Step Up asks you to step up your game and step up your bots. \nLet your bots step up on the way to victory.", R.drawable.stairimage, R.drawable.s,
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
        events.add(new Event("Robotech", "Pac-Man", "The most A-maze-ing bot chase as you dodge invisible obstacles, decode messages that can't be seen and hurry across the map to eat good food and destroy that poison.", R.drawable.pacbot, R.drawable.p, "" +
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
        events.add(new Event("Robotech", "Kick Off", "Welcome to Kick Off. \nLet your football skills take over as you try to outwit your opponent to score goals.", R.drawable.soccermage, R.drawable.k,
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
        events.add(new Event("Robotech", "Death Race", "Welcome to the Death Race- A Race Against Death on the Highway to Hell!", R.drawable.fastimage, R.drawable.d,
                "<h2>Design a manually controlled wired robot capable of travelling in various types of terrain.</h2><br>" +
                        "<b><p>Robot Specifications:</b><br>" +
                        "1. The dimension of robot is 25cmx25cmx20cm, with 10% tolerance.<br>" +
                        "2. Weight of the robot should not exceed from 2.5kg.<br>" +
                        "3. There’s no restriction in wheel size, number or type.<br>" +
                        "4. Maximum voltage input allowed is 24 V DC.<br>" +
                        "5. There’s no restriction in controlling ways, i.e., control can be wired or<br>" +
                        "wireless.<br>" +
                        "6. Readymade toys/Lego kits/Hydraulic System/IC engines are not allowed.<br></p>" +
                        "<p><b>Team Specifications:</b><br>" +
                        "1. Team should consist maximum 4 members.<br>" +
                        "2. Teams can be formed with cross college members.<br>" +
                        "3. Only 2 person will be allowed inside the arena.<br></p>" +
                        "<p><b>Rules and Regulations:</b><br>" +
                        "1. Unethical behavior, e.g., speaking/indicating abusive in the arena, pulling<br>" +
                        "a fight with coordinators/volunteers can cause suspension of the team<br>" +
                        "from the event.<br>" +
                        "2. Damaging the arena may lead to direct disqualification.<br>" +
                        "3. The decision of team Death-Race is final.<br>" +
                        "4. The coordinators have all rights to take final decisions during the event.<br>" +
                        "5. The robot maybe disqualified for violating the safety measures.<br></p>" +
                        "<p><b>Event Rules:</b><br>" +
                        "1. Each team will be provided with a 220V AC – 50Hz supply. Any kind of<br>" +
                        "onboard power source have to be arranged by the team.<br>" +
                        "<p>2. There will be 3 rounds.<br>" +
                        "<b>Round 1:</b> 50% of the participating team will be able to enter the second<br>" +
                        "round. The qualification will be based on points.<br>" +
                        "<b>Round 2:</b> 2 teams will be able to reach the final round from this round.<br>" +
                        "<b>Round 3:</b> Teams will be introduced to a different arena, which will be " +
                        "disclosed on the commencement of the final round. No extra addition is " +
                        "required in the bot to participate in this round. However, teams will be " +
                        "given additional 30 minutes to modify their robot.</p>" +
                        "3. In each arena, there will be ways, travelling through which will cause<br>" +
                        "extra bonus points. Also, there will be such obstacles/areas,<br>" +
                        "touching/entering those will cause the deduction in points.<br></p>" +
                        "Point System: This will be introduced at the time of event."));
        events.add(new Event("Robotech", "Snakes and Ladders", "This Srijan come play snakes and ladders with your bots. \nA classic game reinvented have fun gathering points and evading poison.", R.drawable.snakesandladder, R.drawable.s,
                "<h2>Design a manually controlled robot to move and pick/grab the things.</h2><br>" +
                        "<p><b>Robot specification:</b><br>" +
                        "1. Dimension 25×20×20 (cm) with 10% tolerance.<br>" +
                        "2. Control should be wireless.<br>" +
                        "3. Readymade toy/lego kits/hydraulic system/IC engines are not allowed.<br>" +
                        "4. Robot should have a body of combination of slant and flat surface so that dice can roll down to the flat surface from the slant surface and dice must not go out of the body<br>" +
                        "5. there should be a vertical stick of 6 inches attached to the body of robot to put in rings in such a way that robot should be balanced.<br>" +
                        "</p><p><b>Players.</b><br>" +
                        "1. Each player will have his/her individual robot.<br>" +
                        "2. At a time 4 robots will be allowed in arena.<br></p>" +
                        "<p><b>Rules and Regulations:</b><br>" +
                        "1. Unethical behavior e.g. speaking/indicating abusive in the arena etc. can cause suspension of candidate form the event.<br>" +
                        "2. Damaging arena can cause direct disqualification.<br>" +
                        "3 decision of coordinating team will be final.<br>" +
                        "4. Coordinators have all rights to take final decision during the event.<br>" +
                        "5. The robot maybe disqualified for violating the safety measures.</p><br>" +
                        "<p><b>Event rules:</b><br>" +
                        "1. Each player will be provided with a 220 V AC - 50 Hz supply. Any type of onboard power supply should be arranged by the player.<br>" +
                        "2. The game is just like the traditional snake ladder game with slight modification.<br>" +
                        "3. Robot will pick a dice and release it to the slant part of the board which leads the dice to roll down to the flat surface on board itself.<br>" +
                        "4. Robot will move as per the no. shown by dice.<br>" +
                        "5. In each box of layout there will be certain no. of rings of different colors. Robot will pick one ring of specific color from each box where it goes in every move and put it into the stick on the body.<br>" +
                        "6. To pick up the ring robot will have two chances else it has to leave that ring.<br>" +
                        "7. Each ring will have a credit point of '2'. E.g. If one bot picks 10 rings till the end then it will get a point of '20'<br>" +
                        "Moreover who reaches first to the end will get '50' game points second 40 and third 30.<br>" +
                        "8. At last the player having largest sum of his/her (game point+ ring credit point) will win the game.<br>" +
                        "9. To collect more no of rings one can leave the advantage of ladder or can take the ladder to reach faster to the end to grab highest game points. It’s up to the player's decision.</p>"));
        events.add(new Event("Robotech", "Destination Seeker 2.0", "\"I chose the path less travelled by, and that has made all the difference\".\nWelcome to the line following event of Srijan.", R.drawable.lineimage, R.drawable.d,
                "<h2>Design a autonomous robot to follow the track as closely as possible</h2><br>" +
                        "<p><b>Rules and Regulations & Arena description</b><br>" +
                        "• Bot has to be self-efficient and autonomous ,no external computation unit can be used <br>" +
                        "• Commands can’t be send manually into the bot once it starts the run <br>" +
                        "• Prelims arena will contain only white background but in final arena will contain both white and black background. <br>" +
                        "• Line width be 2.5cm <br>" +
                        "• All the crossover points will be at right angle to each other <br>" +
                        "• Decisions taken by the event coordinators in favour of the event will be final. <br>" +
                        "• External standard power lines will be provided on the arena <br>" +
                        "• The arena will be randomly generated, dead ends, straight paths and multiple path way based nodes will be there in the arena. <br>" +
                        "• Run will restart if the bot fails to stick to basic fundamental of following the line <br>" +
                        "• Jumping or skipping or walking bot is not allowed to take part in the event<br></p>" +
                        "<p><b>Prelims:</b><br>" +
                        "• The line will be black in colour on a white background <br>" +
                        "• The bot has to reach the destination " +
                        "node from a particular starting node <br>" +
                        "• The bot with minimum time of run leads the score board <br>" +
                        "• Total 5 minutes of time will be allocated per team <br>" +
                        "• A team can take as many as possible runs in their allocated time of 5 minutes <br>" +
                        "• Each new Run has to begin from the particular starting node <br>" +
                        "• No previous run history can be directly used in the proceeding runs <br>" +
                        "• Between two consecutive runs the clock doesn’t pause <br>" +
                        "• Only one 2-minute technical time out is allowed, and then the clock resumes the count down <br>" +
                        "• Once the bot starts its run operator can’t touch their bot unless it has been restarted<br>" +
                        "*No loop path ways will be there in this round*<br></p>" +
                        "<b><p>Finals:</b><br>" +
                        "• There will be several partitions on arena. Some sub-arenas will contain white lines on black background and others will contain black lines on white background. The bot has to make out the transition from black to white background and the opposite and to follow the distinct line <br>" +
                        "• Bot has to start from a particular starting node then reach the flag node & return to its starting node <br>" +
                        "• The flag node will be a square of 18*18, the bot has to detect the square and start the reverse journey <br>" +
                        "• The bot with minimum time of run leads the score board <br>" +
                        "• Total 10 minutes of time will be allocated per team <br>" +
                        "• A team can take as many as possible runs in their allocated time of 10 minutes <br>" +
                        "• Each new Run has to begin from the particular starting node <br>" +
                        "• No previous run history can be directly used in the proceeding runs <br>" +
                        "• Between two consecutive runs the clock doesn’t pause <br>" +
                        "• Only one 2-minute technical time out is allowed, and then the clock resumes the count down <br>" +
                        "• Once the bot starts its run operator can’t touch their bot unless it has been restarted<br>" +
                        "* Loop or alternative paths between " +
                        "two nodes will be there in this round *<br>" +
                        "<p><b>To be Noted:</b><br>" +
                        "• Bot dimension must be such that it fits in the cube 18*18*18 cc throughout the run<br>" +
                        "• Damage to the arena will lead to disqualification<br>" +
                        "• Modifying any base rules of the event under satisfactory logical reason by the event coordinators is allowed<br>" +
                        "• Bot can’t leave behind any part of it after the run<br>" +
                        "• Bot has to work as a single integrated unit</p></p><br>" +
                        "Sample Arena - https://www.facebook.com/events/996011600500545/permalink/996020700499635/"));
        events.add(new Event("Robotech", "Bandits of the Sea 2.0", "Ahoy, mates !! Buckle your swords and itch your beards. \nThe Bandits is back by popular demand. Sail those waters in the Bandits of the Sea 2.0!", R.drawable.banditsofthesea, R.drawable.b,
                "<h2>Design a manually controlled robot to traverse in surface of water.</h2><br>" +
                        "<p><b>Robot Specifications:</b><br>" +
                        "1. The dimension of robot is 40cmx40cm, with 10% tolerance.<br>" +
                        "2. There is no restriction in height and weight of the bot.<br>" +
                        "3. There’s no restriction in controlling ways, i.e., control can be wired or wireless.<br>" +
                        "4. Readymade toys/Lego kits are not allowed.<br></p>" +
                        "<p><b>Team Specifications:</b><br>" +
                        "1. Team should consist maximum 4 members.<br>" +
                        "2. Teams can be formed with cross college members.<br>" +
                        "3. Only 2 persons will be allowed inside the arena.<br>" +
                        "4. Only one robot from each team is allowed inside the arena.<br></p>" +
                        "<p><b>Rules and Regulations:</b><br>" +
                        "1. Unethical behavior, e.g., speaking/indicating abusive in the arena, pulling a fight with coordinators/volunteers can cause suspension of the team from the event.<br>" +
                        "2. Damaging the arena may lead to direct disqualification.<br>" +
                        "3. The decision of team Bandits of The Sea is final.<br>" +
                        "4. The coordinators have all rights to take final decisions during the event.<br>" +
                        "5. The robot maybe disqualified for violating the safety measures.<br></b>" +
                        "<p><b>Event Rules:</b><br>" +
                        "1. Each team will be provided with a 220V AC – 50Hz supply. Any kind of onboard power source have to be arranged by the team.<br>" +
                        "2. Damaging the arena may cause direct disqualification.<br>" +
                        "<p>" +
                        "<b>Round 1:</b><br> The robot has to traverse through the arena avoiding obstacles.<br>" +
                        "There will be negative point on touching the obstacle. However, there is no negative in touching the wall of the arena. The teams with less scores moves for second round.<br>" +
                        "<br><b>Round 2:</b><br> This will be an one-on-one round, where one bot has to defend a ball, while other has to grab/touch the ball somehow. When the attacking bot touches the ball, he becomes the defender and the defending one becomes the attacking one. And thus the game goes until a time limit.<br>" +
                        "</p></p>" +
                        "<p><b>Point System:</b><br> This will be introduced at the time of event.</p>"));

        events.add(new Event("Gaming", "FIFA", "Get ready for non stop FIFA against your friends and stand a chance to win some amazing prizes.", R.drawable.fifaimage, R.drawable.f, "Event details will be decided on spot. For further details ask queries in the <b>Contact Us</b> section in the app."));
        events.add(new Event("Gaming", "Counter Strike", "May the odds be ever in your favour. Play against your friends in a chance to win amazing prizes.", R.drawable.csimage, R.drawable.c, "Event details will be decided on spot. For further details ask queries in the <b>Contact Us</b> section in the app."));
        events.add(new Event("Gaming", "NFS Most Wanted", "Race against time, on a highway to hell! Play one of the most loved racing games and win amazing prizes.", R.drawable.nfsimage, R.drawable.n, "Event details will be decided on spot. For further details ask queries in the <b>Contact Us</b> section in the app."));
        events.add(new Event("Gaming", "Mini Militia", "Team up with your friends for an extreme combat challenge. This addictive game has won the heart of millions. ", R.drawable.minimilitiaimage, R.drawable.m, "Event details will be decided on spot. For further details ask queries in the <b>Contact Us</b> section in the app."));

        events.add(new Event("Code Me", "Sher-Locked", "Sher-Locked', an event that is a perfect infusion of brainstorming and exploring out of the box.", R.drawable.coding1, R.drawable.s,
                "<h2>The fastest one to crack the code wins!</h2>" +
                        "Mr. Drumpf has been brutally murdered and the onus is on Detective Mobama(you) to find out who the real killer is. Suspicion is on Mr. Drumpf's five friends, one of whom is the killer. All we could gather as evidence was Mr. Drumpf's personal laptop which may contain information about who the killer is.<br>" +
                        "<p><b>Round 1</b><br>" +
                        "The laptop is password protected. The aim is to crack the password. The team will be given 15 logical puzzles/questions which they would need to solve within a stipulated time of 1 hour. The answers to these 15 questions will help you to figure out the password. 15 teams will move on to the next round.<br>" +
                        "</p><b><p>Round 2</b><br>" +
                        "Now that you have figured out the password, you have access to Mr Drumpf's laptop. The laptop contains 5 files(password-protected), each for each of the subjects. Teams will be given 5 coding problems. There will be one test case for each of the problem. The answer to the test case is the password to the suspect's file. Teams need to open all the files and with the information provided , you need to figure out the real killer.<br>" +
                        "<br>Time duration is 2 hours.</p><br>" +
                        "<p><b>Event Rules:</b><br>" +
                        "• This is a team event.<br>" +
                        "• Each team should consist of 3 members<br>" +
                        "• The decision of the event co-ordinators is final and binding.</p>"));
        events.add(new Event("Code Me", "Bugger On", "Do you like resolving bugs in code? Of course not. But are you an expert at editing code someone messed up at the last minute?", R.drawable.coding2, R.drawable.b,
                "<h2>Resolve bugs as fast and efficiently you can!<br>" +
                        "<p>Euler forgot to lock his screen on his way to lunch. When he returned, his work was ruined. His cat had been dancing on the keyboard and playing with the mouse, leaving a tangled mess of characters in its wake.<br>" +
                        "Do you like resolving bugs in code? Of course not. But are you an expert at editing code someone messed up at the last minute? Have you wasted a lot of time hunting bugs for others, wondering how it would ever help you? Srijan'17 brings you this opportunity to use all your experience to debuggify Euler's code and win exciting prizes. Bugger On!<br>" +
                        "</p><b>Pattern:</b> Online questions with different sections based on language. A set of questions have to be solved combining all languages.<br>" +
                        "<br><b>Time-duration:</b> 1hr<br><br>" +
                        "<b>Language used:</b> C++, Java, Python<br><br>" +
                        "<p><b>Prelims:</b> 24th and 25th Feb<br>" +
                        "<b>Finals:</b> 26th Feb</p>"));
        events.add(new Event("Code Me", "H42", "Are you a coding geek? Do you think you have what it takes to be #1 and tackle all sorts of algorithmic challenges?", R.drawable.codingimage, R.drawable.h,
                "<h2>\"Talk is cheap. Show me the code.\"</h2><br>" +
                        "<p>Are you a coding geek? Do you think you have what it takes to be #1 and tackle all sorts of algorithmic challenges? Show off your skills as you take on an army of problems equipped with C/C++/Java as your weapon.</p><br>" +
                        "<p><b>Rules</b> - <br>" +
                        "Participation in this contest will be as a team of maximum three. There will be two rounds, first round being online and the second round offline. The first round will have 3 questions to be solved in 2 hours and the second round will have 5 questions to be solved in 3 hours.<br>" +
                        "<p><b>Round 1:</b><br>" +
                        "The prelims of H42 will be an online contest to be held on Wednesday,22nd February 2017 from 8pm onwards on Hackerrank platform.<br>" +
                        "Link: https://www.hackerrank.com/h42-prelims-srijan17<br>" +
                        "Remember to fill up the form before registering else you will be disqualified. It is a team contest so you have to make a team following the steps given in the Info section." +
                        "</p>" +
                        "<p><b>Rounds 2 and 3:</b><br>" +
                        "Top teams qualifying the first round will be called for the offline round at Jadavpur University campus. " +
                        "The decision of the event coordinators are final.<br></p>" +
                        "<p><b>Contact:</b><br>" +
                        "Arnab Ghatak - 9007514107<br>" +
                        "Rajat Kumar - 7278754372<br>" +
                        "Anik Dasgupta - 9163436746</p>"));

        events.add(new Event("ManageMania", "B Model", "B-Model Competition is slightly different than the B-Plan competition. \nIn a B-Model competition you need to have not just an idea but also an prototype.", R.drawable.bplanimage, R.drawable.b,
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
        events.add(new Event("ManageMania", "Epiphony", "Have you ever felt that you could sense the business acumen in you telling you how to run a succesful company. \nEver felt the need for that entrepreneurial spirit that lends itslef to success.", R.drawable.casechallenge, R.drawable.e,
                "<p><b>Participants:</b><br>" +
                        "10-12 teams, consisting of four students each.<br>" +
                        "<p><b>Pool System</b><br>" +
                        "All the 12 teams participating are called on one by one to pick up a number from a bowl of papers having numbers 1-4. Pool A is designated by 1, Pool B by 2, Pool C by 3 and Pool D by 4. Each team is then given an envelope based on their pool number. The envelopes contain 12 different cases for the 12 teams participating. No team is allowed to disclose their case details until Day 3. If found guilty of disclosing, particular team would be disqualified from EPIPHANY.<br>" +
                        "</p><p><b>The Case</b><br>" +
                        "The competition is centered on a three day-long “case period”, which unfolds on the last days of EPIPHANY. The case challenges students with a business problem. Each team must come up with a strategy and organize their findings into a presentation. The participants work in the allotted room for three days. They move to the presentation site on the final day to rehearse for the last two hours.<br>" +
                        "</p></p><p><b>Presentations:</b><br>" +
                        "The presentations happen on the last day of EPIPHANY and are open to the general public. After two and a half days of work, the participants are asked to present their strategies and take questions from the judges. All twelve teams are required to present once in the morning and the four lucky teams that get selected for the second round based on the points are required to present once more in the afternoon session; each time facing a different panel of judges. There may be unexpected pathfalls in your second round.<br>" +
                        "Round 1 - They have up to 15 minutes to convince their audience, followed by a 3 minute Q&A.<br>" +
                        "Round 2 - They have up to 10 minutes to convince their audience, followed by a 5-6 minutes of Q&A.<br>" +
                        "</p><p><b>Judges:</b><br>" +
                        "The competition will be judged by executives from various businesses and fields of expertise. They come from banking, insurance, retailing, accounting and publishing companies. They are senior managers, analysts, directors, consultants and entrepreneurs, who all participate as volunteers.<br>" +
                        "</p><p><b>Evaluation:</b><br>" +
                        "The teams are evaluated on their overall content, presentation and structure by the judges. A list of criteria is available (70% on content, 30% on presentation). However, these are only guidelines for the judges. <br>" +
                        "Round 1 - The judges are asked to give scores to the teams during a deliberation session. The participating teams are allowed to give points on the other team's presentation. Judges' scores carry a weightage of 80% and rival team's scores carry a weightage of 20%. Four winners eventually emerge from the evaluation of the scores, one from each pool. <br>" +
                        "Round 2 - The judges are asked to rank the teams during a deliberation session. The judges are only asked to rank the teams during a deliberation session. The session ends only when a consensus is reached. The winner eventually emerges from these rankings. The winner reflects which team the judges would have hired as their consultants.<br>" +
                        "A particularity is that, in order to win at in EPIPHANY, a team must actually deliver not one, but two solid presentations. They must convince the panel on both instances, or else they cannot rank favorably.<br>" +
                        "<p><b>Rankings:</b><br>" +
                        "Trophies for first, second and third place are awarded during closing ceremonies. Although all 12 teams may receive a ranking during deliberation, their order remains undisclosed. Only the top 3 are announced.</p>"));
//        events.add(new Event("ManageMania", "Monopoly", getString(R.string.loremIpsum), R.drawable.monpoly, R.drawable.mlogo, getString(R.string.stairclimbingBotDesc)));

        events.add(new Event("Mixed Bag", "Quiz It", getString(R.string.loremIpsum), R.drawable.workshopimage, R.drawable.q, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Mixed Bag", "Math-E-Magician", "You don't have to be a mathematician to have a feel for numbers", R.drawable.workshopimage, R.drawable.workshoplogo,
                "<h2>Airlift your magic wand and be the jolly king</h2> <br>" +
                        "<p>“You don't have to be a mathematician to have a feel for numbers</i>\"<br>" +
                        "- John Nash<br></p>" +
                        "<p>On its 11th edition, F.E.T.S.U. presents Srijan'17 brings you the opportunity to be the KHILADI of numbers. Come only if you see mathematics as a game and you like to play with numbers.<br>Airlift your ideas, swing your magic wand and compete with young minds.<br>" +
                        "So guys, gear up!<br></p>" +
                        "<p><b>Registration is free of cost.</b></p>"));
        events.add(new Event("Mixed Bag", "Junkyard Wars", "Junkyard Wars brings to you a wasteathon. The objective is simple.\n" +
                "We human beings produce a lot of waste. Waste that might be recycled but isn’t.", R.drawable.workshopimage, R.drawable.workshoplogo,
                "<h2>The mantra is three pronged.<br>" +
                        "Recycle-Reuse-Reproduce <br></h2>" +
                        "<p><b>• Recycle</b><br>" +
                        "Bring Your Own Waste.<br>" +
                        "Recycle the waste that you normally would discard. Help keep India cleaner by 1 waste article at least.<br>" +
                        "<b>• Reuse</b><br>" +
                        "Design and make an innovative solution to a real life problem using waste that is a by-product of day to day life.<br>" +
                        "<b>• Reproduce</b><br>" +
                        "Prepare a presentation on how you believe your model will be a step above the rest.<br></p>" +
                        "<p><b>Rules</b><br>" +
                        "• 5 participants in one team. Cross college participants in one team are allowed.<br>" +
                        "• Event consists of 2 rounds.<br>" +
                        "• Round 1 consists of a set of questions that test the logical aptitude of participating teams. Top 5 teams move to the next round. <br>" +
                        "• No electronic devices or computation aids are allowed in round 1.<br>" +
                        "• Round 2 will be of minimum 5 hours duration. Teams have to start their model, create an innovative solution and finally pitch their presentation in front of judges.<br>" +
                        "• The decision of event co-ordinators in this event is absolute.<br>" +
                        "• Some special ingredients will be provided which they must incorporate in their model. Teams are encouraged to bring their own waste.<br>" +
                        "• Teams scoring better in prelims will get an advantage in the second round.<br>" +
                        "• Use of laptops, electronic gadgets for information and research purposes in the second round, is allowed.<br>" +
                        "• May the force be with you.</p><br>" +
                        "<p><b>Contact them for any queries:</b><br>" +
                        "Partho - 9883245361<br>" +
                        "Aranyo - 8961133064<br>" +
                        "Nandita - 7044732298<br></p>"));
        events.add(new Event("Mixed Bag", "Electroniche", getString(R.string.loremIpsum), R.drawable.workshopimage, R.drawable.workshoplogo, getString(R.string.stairclimbingBotDesc)));
        events.add(new Event("Mixed Bag", "Bridge The Gap", "Design creates culture.\nCulture shapes value.\nValue determines the future.", R.drawable.workshopimage, R.drawable.workshoplogo,
                "<p>Bridge the Gap is an on-spot event. It has only one round. Some instructions that should be followed during the making of the model bridge-<br>" +
                        "1. Minimum free span (Distance between two piers) should be not more than 40 inches.<br>" +
                        "2. The deck width may vary between a minimum of 6 inches to a maximum of 9 inches.<br>" +
                        "3. Height of Bridge should not exceed 12 inches.<br>" +
                        "4. Clear distance between deck and ground should not be less than 4 inches.<br>" +
                        "</p><p><b>Materials provided:</b><br>" +
                        "Popsicle sticks, Thread<br></p>" +
                        "<p><b>Scoring and Judging Criterion:</b><br>" +
                        "1. Maximum Load (in Kg) at which the structure fails: 50%<br>" +
                        "2. Aesthetics: 40%<br>" +
                        "3. Time taken to complete the design: 10%<br></p>" +
                        "<p><b>Rules:</b><br>" +
                        "1. No extra time will be given.<br>" +
                        "2. Participants have to bring adhesive gum and any type of cutting tool they prefer.<br>" +
                        "3. No extra materials will be given.<br>" +
                        "4. Decision made by the judges will be final and binding.</p>"));
        events.add(new Event("Mixed Bag", "Paint and Wear", "“The public is more familiar with bad design than good design. It is, in effect, conditioned to prefer bad design, because that is what it lives with. The new becomes threatening, the old reassuring.”", R.drawable.workshopimage, R.drawable.p, "" +
                "<p><b>Event Specifications:</b> <br>" +
                "The event comprises of two rounds.<br>" +
                "First will be on designing the logo of the company on paper.<br>" +
                "Secondly the designing will be done on t-shirt.<br></p>" +
                "<p><i>Participants just need to bring their colours, rest shall be provided.</i></p><br>" +
                "<p><b>Team Specifications:</b><br>" +
                "Team should consist of exactly two members.<br>" +
                "Teams cannot be formed with cross college members.<br></p>" +
                "<p><b>Rules and Regulations:</b><br>" +
                "Unethical behaviour, e.g., speaking/indicating abusive in the event spot, pulling a quarrel with the coordinators / volunteers / other team members will cause suspension of the team from the event.<br>" +
                "The decision of the judges’ panel will be final and no cross questioning will be entertained.<br>" +
                "The coordinators have all rights to take final decision during the events.<br>" +
                "Mobile phones must be submitted to the coordinators before starting with the event.<br></p>" +
                "<p><b>Event Rules:</b><br><p><b>Round 1 :</b> <br>" +
                "1. Each team will be provided with a drawing sheet and a A4 sheet.<br>" +
                "2. On the drawing sheet, participants must draw the logo on the specified company.<br>" +
                "3. The A4 sheet must be filled with details on why they designed the logo in the manner they did on at most 60 words.<br>" +
                "4. Timing: 30 minutes to draw. 15 minutes to write the details.<br></p>" +
                "<p><b>Round 2:</b><br>" +
                "1. Each team will be provided with a blank t-shirt.<br>" +
                "2. They must design a logo and paint it on the t-shirt.<br>" +
                "3. Rough sheet will be provided, which must be return with the t-shirt for invigilation.<br>" +
                "4. Timing: 1 hour and 15 minutes to complete the basic structure, extra 15 minutes to make the required touch ups.<br>" +
                "5. Participants must bring their own colours. Only Water colours, poster colours and fabric colours can be used for Round 1. Only poster colours and fabric colours for Round 2.<br>" +
                "6. Participants must bring their own drawing kits such as pencil, eraser, drawing board, sharpener etc.<br>" +
                "7. Scales, no other colours apart from the above mentioned and sketch pens are not allowed.<br>" +
                "8. If found disregarding the rules, the team will be disqualified.<br></p>" +
                "<p><b>Point Systems:</b><br>" +
                "This will be disclosed before the event.<br>"
        ));
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
