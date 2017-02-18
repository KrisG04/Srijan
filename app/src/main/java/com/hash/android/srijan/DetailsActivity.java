package com.hash.android.srijan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.hash.android.srijan.DashboardActivity.PREFS_NAME;
import static com.hash.android.srijan.DashboardActivity.authUser;
import static com.hash.android.srijan.DashboardActivity.finalEvent;
import static com.hash.android.srijan.EventsActivity.posEvent;
import static com.hash.android.srijan.fragment.SubscriptionFragment.arrayList;
import static com.hash.android.srijan.fragment.SubscriptionFragment.mAdapter;

public class DetailsActivity extends AppCompatActivity {

    Context context;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    private SharedPreferences settings;

    private FloatingActionButton fab;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MySafetyMethod();
//        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fa);
        setContentView(R.layout.activity_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageResource(finalEvent.get(posEvent).getImage());
//        getWindow().getSharedElementEnterTransition().
        collapsingToolbarLayout.setTitle(finalEvent.get(posEvent).getHead());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

        TextView detailsTextView = (TextView) findViewById(R.id.detailsTextView);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            detailsTextView.setText(Html.fromHtml(finalEvent.get(posEvent).getDetailedDescription(), Html.FROM_HTML_MODE_COMPACT));
//        }

//        else{
        detailsTextView.setText(Html.fromHtml(finalEvent.get(posEvent).getDetailedDescription()));
//        detailsTextView.setText(getText(R.string.stairclimbingBotDesc));
//        }

//detailsTextV

        firebaseAuth = FirebaseAuth.getInstance();


        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Registering...");
        settings = getSharedPreferences(PREFS_NAME, 0);

        boolean isRegistered = settings.getBoolean(finalEvent.get(posEvent).getHead() + authUser.getId(), false);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!isRegistered)
            fab.setImageResource(R.drawable.ic_games_black_24dp);
        else
            fab.setImageResource(R.drawable.ic_clear_black_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean isRegistered = settings.getBoolean(finalEvent.get(posEvent).getHead() + authUser.getId(), false);

                if (!isRegistered) {

                    new AlertDialog.Builder(DetailsActivity.this)
                            .setTitle("Confirm subscription")
                            .setMessage(Html.fromHtml("Confirm subscription for event - " + "<b>" + finalEvent.get(posEvent).getHead() + "<b>?"))
                            .setPositiveButton("Yes, I'm in!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    subscribe();
                                }
                            })


                            .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Toast.makeText(DetailsActivity.this, "Subscription cancelled.", Toast.LENG).show();
                                }
                            })
                            .show();
                } else {

                    new AlertDialog.Builder(DetailsActivity.this)
                            .setTitle(Html.fromHtml("Unsubcribe to event - " + "<b>" + finalEvent.get(posEvent).getHead() + "<b>?"))
                            .setMessage("Are you sure you want to proceed?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    unsubscribe();
                                }
                            })
                            .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Ignore
                                }
                            })
                            .show();


                }

            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Modifies the standard behavior to allow results to be delivered to fragments.
     * This imposes a restriction that requestCode be <= 0xffff.
     *
     * @param intent
     * @param requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private void MySafetyMethod() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                System.out.println("inside the process of handling exceptions");
                System.err.println("inside the process of handling exceptions");
                throwable.printStackTrace();
                System.exit(2);

                startActivity(new Intent(context, LogInActivity.class));
                finish();
            }
        });
    }

    private void subscribe() {

        //Method to subscribe to the specific topic
        final ProgressDialog pd = new ProgressDialog(DetailsActivity.this);
        pd.setMessage("Subscribing...");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                pd.show();
                FirebaseDatabase.getInstance().getReference("Events").child(finalEvent.get(posEvent).getHead()).child(authUser.getName()).setValue(authUser)
//
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putBoolean(finalEvent.get(posEvent).getHead() + authUser.getId(), true); //isRegistered is set to true
                                    editor.commit();
                                    fab.setImageResource(R.drawable.ic_clear_black_24dp);
                                    FirebaseMessaging.getInstance().subscribeToTopic(finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                                    if (!arrayList.isEmpty()) {
                                        arrayList.remove(finalEvent.get(posEvent));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    pd.hide();
                                    Snackbar.make(findViewById(R.id.rootViewDetails), "Subscribed succesfully!", Snackbar.LENGTH_SHORT)
                                            .setAction("UNDO", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    unsubscribe();
                                                }
                                            })
                                            .setActionTextColor(getColor(R.color.colorAccent))
                                            .show();
                                } else {
                                    pd.hide();
                                    Snackbar.make(findViewById(R.id.rootViewDetails), "Failed to subscribe.", Snackbar.LENGTH_SHORT)
                                            .setActionTextColor(getColor(R.color.colorAccent))
                                            .setAction("RETRY", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    subscribe();
                                                }
                                            })
                                            .show();

//                                    Toast.makeText(DetailsActivity.this, "Failed to subscribe.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void unsubscribe() {

        final ProgressDialog pd1 = new ProgressDialog(DetailsActivity.this);
        pd1.setMessage("Un-registering...");
        pd1.show();
        FirebaseDatabase.getInstance().getReference("Events").child(finalEvent.get(posEvent).getHead()).child(authUser.getName()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    //Deleted succesfully
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                    Log.d("Subscribed Topic:", finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                    Snackbar.make(findViewById(R.id.rootViewDetails), "Unregistered succesfully!", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    subscribe();
//                                    pd1.hide();

                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .show();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(finalEvent.get(posEvent).getHead() + authUser.getId(), false);
                    editor.commit();
                    fab.setImageResource(R.drawable.ic_games_black_24dp);
                    if (!arrayList.isEmpty()) {
                        arrayList.remove(finalEvent.get(posEvent));
                        mAdapter.notifyDataSetChanged();
                    }
                    pd1.hide();
                } else {
                    Snackbar.make(findViewById(R.id.rootViewDetails), "Failed to remove event. Try again later", Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getColor(R.color.colorAccent))
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    unsubscribe();
//                                    pd1.hide();

                                }
                            }).show();
//                    Toast.makeText(DetailsActivity.this, "Failed to remove event. Try again later", Toast.LENGTH_SHORT).show();
                    pd1.hide();
                }
            }
        });
    }


}

/*Problem Statement: \n
<h1>Design a manually controlled robot to climb stairs.</h2> \n
<b>Robot Specifications:</b> \n
1. The dimension of robot is 25cmx20cmx20cm, with 10% tolerance. \n
2. There’s no restriction in controlling ways, i.e., control can be wired or wireless.\n
3. Readymade toys/Lego kits/Hydraulic System/IC engines are not allowed.\n
<b>Team Specifications:<b>\n
1. Team should consist maximum 4 members.\n
2. Teams can be formed with cross college members.\n
3. Only 2 persons will be allowed inside the arena.\n
4. Only one robot from each team is allowed inside the arena.\n
<b>Rules and Regulations:</b>\n
1. Unethical behavior, e.g., speaking/indicating abusive in the arena, pulling a fight with coordinators/volunteers can cause suspension of the team from the event.\n
2. Damaging the arena may lead to direct disqualification.\n
3. The decision of team Step-Up is final.\n
4. The coordinators have all rights to take final decisions during the event.\n
5. The robot maybe disqualified for violating the safety measures.\n\n
Event Rules:\n
1. Each team will be provided with a 220V AC – 50Hz supply. Any kind of onboard power source have to be arranged by the team.
2. There will be 2 rounds.
Round 1: The Robot has to climb up a stair. Points will be on climbing up and time taken. The teams with more points get selected for the round 2.
Round 2: In this round the robot has to go through a path, a stair will included in it. There will be certain objectives the robot has to fulfill, which will be disclosed at the time of arena.
3. Damaging the arena may cause direct disqualification.
Point System: This will be introduced at the time of event.
Stair Details: The stairs will be like this*/
