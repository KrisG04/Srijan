package com.hash.android.srijan.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.android.srijan.DashboardActivity;
import com.hash.android.srijan.Event;
import com.hash.android.srijan.R;
import com.hash.android.srijan.SubscribedEventRecyclerAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hash.android.srijan.EventsActivity.events;

/**
 * Created by Spandita Ghosh on 2/3/2017.
 */

public class SubscriptionFragment extends Fragment {


    public static ArrayList<Event> arrayList;

    public SubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);


        arrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(DashboardActivity.PREFS_NAME, 0);

        for (int i = 0; i < events.size(); i++) {
            boolean isRegistered = sharedPreferences.getBoolean(events.get(i).getHead() + getString(R.string.finalSharedPrefs), false);
            if (isRegistered) {
                arrayList.add(events.get(i));
            }
        }

        RecyclerView subscribedRecyclerView = (RecyclerView) rootView.findViewById(R.id.subscribedRecyclerView);
        subscribedRecyclerView.setHasFixedSize(true);
        subscribedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribedRecyclerView.setAdapter(new SubscribedEventRecyclerAdapter());


        subscribedRecyclerView.addOnItemTouchListener(new DashboardActivity.RecyclerItemClickListener(getActivity(), subscribedRecyclerView, new DashboardActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                Event e = arrayList.get(position);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;

    }

}