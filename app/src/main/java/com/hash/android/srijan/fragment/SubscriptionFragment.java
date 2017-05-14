package com.hash.android.srijan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hash.android.srijan.DashboardActivity;
import com.hash.android.srijan.DetailsActivity;
import com.hash.android.srijan.Event;
import com.hash.android.srijan.R;
import com.hash.android.srijan.User;
import com.hash.android.srijan.adapter.SubscribedEventRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.hash.android.srijan.DashboardActivity.updateDetails;
import static com.hash.android.srijan.EventsActivity.events;
//import static com.hash.android.srijan.EventsActivity.posEvent;

//import static com.hash.android.srijan.DashboardActivity.authUser;

/**
 * Created by Spandita Ghosh on 2/3/2017.
 */

public class SubscriptionFragment extends Fragment {

    public static RecyclerView.Adapter mAdapter;
    public static ArrayList<Event> arrayList;
    SharedPreferences sharedPreferences;
    int posEvent;
    RecyclerView subscribedRecyclerView;
    TextView textView;
    ImageView imageView, QRCodeImageView;
    private User authUser;
    private ArrayList<Event> finalEvent;


    public SubscriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().setTitle("My registrations");
//        mDatabase = FirebaseDatabase.getInstance().getReference("Events");
//        pendingAnimation = true;
//        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        FragmentTransaction tr = getFragmentManager().beginTransaction();
//        tr.replace(R.id.frame_container, new SubscriptionFragment());
//        tr.commit();
        View rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false);


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user == null) {
//            startActivity(new Intent(getActivity(), LogInActivity.class));
//            getActivity().finish();
//        }

        finalEvent = new ArrayList<>();
        if (events == null) {
            updateDetails();
        }

        arrayList = new ArrayList<>();

        authUser = getActivity().getIntent().getExtras().getParcelable("authUser");
        sharedPreferences = getActivity().getSharedPreferences(DashboardActivity.PREFS_NAME, 0);

        for (int i = 0; i < events.size(); i++) {
            boolean isRegistered = sharedPreferences.getBoolean(events.get(i).getHead() + authUser.getId(), false);
            if (isRegistered) {
                arrayList.add(events.get(i));
            }
        }
        mAdapter = new SubscribedEventRecyclerAdapter(arrayList);
        mAdapter.notifyDataSetChanged();

        subscribedRecyclerView = (RecyclerView) rootView.findViewById(R.id.subscribedRecyclerView);
        imageView = (ImageView) rootView.findViewById(R.id.subscriptionEmptyImageView);
        textView = (TextView) rootView.findViewById(R.id.subscriptionTextView);

        updateNullSettings();

        subscribedRecyclerView.setHasFixedSize(true);
        subscribedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        subscribedRecyclerView.setAdapter(mAdapter);

//        QRCodeImageView = (ImageView) rootView.findViewById(R.id.qrCode);
        try {

            JSONObject userClass = new JSONObject();
            userClass.put("id", authUser.getId());
            userClass.put("name", authUser.getName());
            userClass.put("phone", authUser.getPhoneNumber());
            userClass.put("email", authUser.getEmail());

            Bitmap qr = encodeAsBitmap(userClass.toString());
//            QRCodeImageView.setImageBitmap(qr);

        } catch (WriterException | JSONException e) {
            e.printStackTrace();
        }


//        if (pendingAnimation) {
//            pendingAnimation = false;
//            subscribedRecyclerView.setTranslationY(subscribedRecyclerView.getHeight());
//            subscribedRecyclerView.setAlpha(0f);
//            subscribedRecyclerView.animate()
//                    .translationY(0f)
//                    .setDuration(300)
//                    .alpha(1f)
//                    .setInterpolator(new AccelerateDecelerateInterpolator())
//                    .start();
//        }


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

//                        Bundle bundle = getActivity().getIntent().getExtras();
                        Bundle bundle = getActivity().getIntent().getExtras();
                        bundle.putParcelableArrayList("finalEvent", finalEvent);
                        bundle.putInt("posEvent", posEvent);
                        i.putExtras(bundle);
//                        i.putExtras(getActivity().getIntent().getExtras());
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

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                arrayList.clear();
                for (int i = 0; i < events.size(); i++) {
                    boolean isRegistered = sharedPreferences.getBoolean(events.get(i).getHead() + authUser.getId(), false);
                    if (isRegistered) {
                        arrayList.add(events.get(i));
                    }
                }


//                subscribedRecyclerView.setAdapter(mAdapter);
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        mAdapter.notifyDataSetChanged();
                        updateNullSettings();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }.start();
//                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;

    }

    private void updateNullSettings() {

        if (arrayList.isEmpty()) {
//            subscribedRecyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
//            subscribedRecyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 150, 150, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 150, 0, 0, w, h);
        return bitmap;
    }

}