package com.hash.android.srijan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
        mDatabase = FirebaseDatabase.getInstance().getReference("Events");



        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Registering...");
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        boolean isRegistered = settings.getBoolean(finalEvent.get(posEvent).getHead() + getString(R.string.finalSharedPrefs), false);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!isRegistered)
            fab.setImageResource(R.drawable.ic_games_black_24dp);
        else
            fab.setImageResource(R.drawable.ic_clear_black_24dp);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean isRegistered = settings.getBoolean(finalEvent.get(posEvent).getHead() + getString(R.string.finalSharedPrefs), false);

                if (!isRegistered) {

                    new AlertDialog.Builder(DetailsActivity.this)
                            .setTitle("Confirm subscription")
                            .setMessage(Html.fromHtml("Confirm subscription for event - " + "<b>" + finalEvent.get(posEvent).getHead() + "<b>?"))
                            .setPositiveButton("Yes, I'm in!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    pd.show();
                                    mDatabase.child(finalEvent.get(posEvent).getHead()).child(authUser.getName()).child(authUser.getId()).setValue(authUser)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(DetailsActivity.this, "Subcribed succesfully!", Toast.LENGTH_SHORT).show();
                                                        SharedPreferences.Editor editor = settings.edit();
                                                        editor.putBoolean(finalEvent.get(posEvent).getHead() + getString(R.string.finalSharedPrefs), true); //isRegistered is set to true
                                                        editor.commit();
                                                        fab.setImageResource(R.drawable.ic_clear_black_24dp);
                                                        FirebaseMessaging.getInstance().subscribeToTopic(finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                                                        if (!arrayList.isEmpty()) {
                                                            arrayList.remove(finalEvent.get(posEvent));
                                                            mAdapter.notifyDataSetChanged();
                                                        }
                                                    } else
                                                        Toast.makeText(DetailsActivity.this, "Failed to subscibe.", Toast.LENGTH_SHORT).show();
                                                    pd.hide();
                                                }
                                            });

                                }
                            })
                            .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(DetailsActivity.this, "Subscription cancelled.", Toast.LENGTH_SHORT).show();
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
                                    final ProgressDialog pd1 = new ProgressDialog(DetailsActivity.this);
                                    pd1.setMessage("Unsubscribing...");
                                    pd1.show();
                                    mDatabase.child(finalEvent.get(posEvent).getHead()).child(authUser.getName()).child(authUser.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                //Deleted succesfully
                                                FirebaseMessaging.getInstance().unsubscribeFromTopic(finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                                                Log.d("Subscribed Topic:", finalEvent.get(posEvent).getHead().replaceAll("\\s+", ""));
                                                Toast.makeText(DetailsActivity.this, "Unsubcribed successfully!", Toast.LENGTH_SHORT).show();
                                                SharedPreferences.Editor editor = settings.edit();
                                                editor.putBoolean(finalEvent.get(posEvent).getHead() + getString(R.string.finalSharedPrefs), false);
                                                editor.commit();
                                                fab.setImageResource(R.drawable.ic_games_black_24dp);
                                                if (!arrayList.isEmpty()) {
                                                    arrayList.remove(finalEvent.get(posEvent));
                                                    mAdapter.notifyDataSetChanged();
                                                }

                                            } else
                                                Toast.makeText(DetailsActivity.this, "Failed to remove event. Try again later", Toast.LENGTH_SHORT).show();
                                            pd1.hide();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();


                }

            }
//                else
//                    Toast.makeText(DetailsActivity.this, "Already subscribed", Toast.LENGTH_SHORT).show();


        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().home
    }


}
