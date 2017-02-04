package com.hash.android.srijan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.android.srijan.DashboardActivity;
import com.hash.android.srijan.EventRecyclerAdapter;
import com.hash.android.srijan.EventsActivity;
import com.hash.android.srijan.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.hash.android.srijan.DashboardActivity.eventArrayListIcon;
import static com.hash.android.srijan.DashboardActivity.eventArrayListImage;
import static com.hash.android.srijan.DashboardActivity.eventArrayListTextContent;
import static com.hash.android.srijan.DashboardActivity.eventArrayListTextHeading;
import static com.hash.android.srijan.DashboardActivity.pos;

/**
 * Created by Spandita Ghosh on 2/3/2017.
 */
public class DashboardFragment extends android.support.v4.app.Fragment {

//    static ArrayList<Integer> eventArrayListImage;




    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Explore");

    }

    /**
     * Get the root view for the fragment's layout (the one returned by {@link #onCreateView}),
     * if provided.
     *
     * @return The fragment's root view, or null if it has no layout.
     */
    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        eventArrayListTextHeading = new ArrayList<String>();
        eventArrayListImage = new ArrayList<Integer>();
        eventArrayListIcon = new ArrayList<Integer>();
        eventArrayListTextContent = new ArrayList<String>();

        updateContent();
        RecyclerView eventsRecyclerView = (RecyclerView) rootView.findViewById(R.id.exploreRecyclerView);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventsRecyclerView.setAdapter(new EventRecyclerAdapter());

        eventsRecyclerView.addOnItemTouchListener(new DashboardActivity.RecyclerItemClickListener(getActivity(), eventsRecyclerView, new DashboardActivity.RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
                Intent i = new Intent(getActivity(), EventsActivity.class);
//                i.putExtra("position", position);
                pos = position;
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;
//        return  relativeLayout;
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


}
