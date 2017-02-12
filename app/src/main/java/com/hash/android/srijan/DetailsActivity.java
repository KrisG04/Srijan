package com.hash.android.srijan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
