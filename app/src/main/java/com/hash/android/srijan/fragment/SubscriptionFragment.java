package com.hash.android.srijan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hash.android.srijan.DashboardActivity;
import com.hash.android.srijan.DetailsActivity;
import com.hash.android.srijan.Event;
import com.hash.android.srijan.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hash.android.srijan.DashboardActivity.authUser;
import static com.hash.android.srijan.DashboardActivity.finalEvent;
import static com.hash.android.srijan.EventsActivity.events;
import static com.hash.android.srijan.EventsActivity.posEvent;

/**
 * Created by Spandita Ghosh on 2/3/2017.
 */

public class SubscriptionFragment extends Fragment {

    public static RecyclerView.Adapter mAdapter;
    public static ArrayList<Event> arrayList;
    private DatabaseReference mDatabase;
    private boolean pendingAnimation;

    public SubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My registrations");
        mDatabase = FirebaseDatabase.getInstance().getReference("Events");
        pendingAnimation = true;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        FragmentTransaction tr = getFragmentManager().beginTransaction();
//        tr.replace(R.id.frame_container, new SubscriptionFragment());
//        tr.commit();
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);


        mAdapter.notifyDataSetChanged();
        arrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DashboardActivity.PREFS_NAME, 0);

        for (int i = 0; i < events.size(); i++) {
            boolean isRegistered = sharedPreferences.getBoolean(events.get(i).getHead() + authUser.getId(), false);
            if (isRegistered) {
                arrayList.add(events.get(i));
            }
        }


        final RecyclerView subscribedRecyclerView = (RecyclerView) rootView.findViewById(R.id.subscribedRecyclerView);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.subscriptionEmptyImageView);
        TextView textView = (TextView) rootView.findViewById(R.id.subscriptionTextView);

        if (arrayList.isEmpty()) {
            subscribedRecyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            subscribedRecyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        subscribedRecyclerView.setHasFixedSize(true);
        subscribedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        subscribedRecyclerView.setAdapter(mAdapter);


        if (pendingAnimation) {
            pendingAnimation = false;
            subscribedRecyclerView.setTranslationY(subscribedRecyclerView.getHeight());
            subscribedRecyclerView.setAlpha(0f);
            subscribedRecyclerView.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .alpha(1f)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }


        if (!finalEvent.isEmpty())
            finalEvent.clear();
        subscribedRecyclerView.addOnItemTouchListener(new DashboardActivity.RecyclerItemClickListener(getActivity(), subscribedRecyclerView, new DashboardActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                Event e = arrayList.get(position);
                String eventCategory = e.getCategory();
                String eventName = e.getHead();
                for (Event event : events) {
                    if (event.getCategory().equalsIgnoreCase(eventCategory)) {
                        finalEvent.add(event);
                    }
                }
                for (Event event : finalEvent) {
                    if (event.getHead().equalsIgnoreCase(eventName)) {
                        posEvent = finalEvent.indexOf(event);
                        Log.d("posEvent", String.valueOf(posEvent));

                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view.findViewById(R.id.eventDetailImageView), "sharedImage");
                        startActivity(i, optionsCompat.toBundle());
                        break;


                    }
                }

            }


            @Override
            public void onLongItemClick(View view, final int position) {
//                final Event e = arrayList.get(position);
//                new AlertDialog.Builder(getActivity())
//                        .setTitle(Html.fromHtml("Unsubcribe to event - " + "<b>" + e.getHead() + "<b>?"))
//                        .setMessage("Are you sure you want to proceed?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
//
//
//                                mDatabase.child(e.getHead()).child(authUser.getName()).child(authUser.getId()).removeValue(new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                        if (databaseError == null) {
//                                            //No error
//
//                                            SharedPreferences.Editor editor = settings.edit();
//                                            editor.putBoolean(e.getHead() + getString(R.string.finalSharedPrefs), false); //isRegistered is set to true
//
//                                            arrayList.remove(e);
//                                            mAdapter.notifyDataSetChanged();
//                                            editor.apply();
//                                        } else
//                                            Toast.makeText(getActivity(), "Failed to remove event. Try again later", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("NOPE", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                        .show();

//                String eventCategory = e.getCategory();
//                for(Event event : events) {
//                    if(event.getCategory().equalsIgnoreCase(eventCategory)){
//                        finalEvent.add(event);
//                    }
//                }
//
//                mDatabase.child(finalEvent.get(posEvent).getHead()).child(authUser.getName()).child(authUser.getId()).removeValue(new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                        if (databaseError == null) {
//                            Log.d("db", "element removed");

//                            for (Event e : arrayList) {
//                                if (e.getHead().equalsIgnoreCase(finalEvent.get(posEvent).getHead())) {
//                                    Log.d("element", e.getHead());
//                                    arrayList.remove(e);
//
//                                }
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//
//
            }
        }));

        return rootView;

    }

}