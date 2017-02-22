package com.hash.android.srijan.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hash.android.srijan.R;
import com.hash.android.srijan.adapter.SponsorsRecyclerViewAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class SponsorsFragment extends android.support.v4.app.Fragment {
    public static ArrayList<String> sponsorsUrl;

    public SponsorsFragment() {
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Sponsors");
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_sponsors, container, false);

        updateResources();


        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefsFileNew", 0);

        SharedPreferences.Editor editor = settings.edit();
        if (settings.getBoolean("firstLoad", true)) {
            editor.putBoolean("firstLoad", false);
            editor.apply();
            Toast.makeText(getActivity(), "Loading resources...", Toast.LENGTH_SHORT).show();
        }
        RecyclerView sponsorsRecyclerView = (RecyclerView) rootView.findViewById(R.id.sponsorsRecyclerView);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mLayoutManager.setItemPrefetchEnabled(false);
        sponsorsRecyclerView.setHasFixedSize(true);
        sponsorsRecyclerView.setItemAnimator(new SlideInUpAnimator());
        SponsorsRecyclerViewAdapter sponsorsRecyclerViewAdapter = new SponsorsRecyclerViewAdapter();
//        sponsorsRecyclerViewAdapter.setHasStableIds(true);
        sponsorsRecyclerView.setLayoutManager(mLayoutManager);
        sponsorsRecyclerView.setAdapter(sponsorsRecyclerViewAdapter);


        return rootView;
    }

    private void updateResources() {
        sponsorsUrl = new ArrayList<>();
        sponsorsUrl.clear();
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/siemens.png?alt=media&token=e1e75e8f-1e28-43fc-adfa-161e1ed5f3db");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/tcs.png?alt=media&token=db1f7e2d-77ce-4b8a-b304-fd8a03639896");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/you.png?alt=media&token=8879d110-150d-4c62-8d20-84f82db3bf63");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/lic.png?alt=media&token=f505c16c-413a-4da9-be73-14145295c6a1"); //LIC
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/jain.png?alt=media&token=e3dbeb9a-7654-4340-87e4-87960dc4989a"); //Jain
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/holidayinn.png?alt=media&token=4c5c8ad3-baad-428c-9a4b-6ea00dc20ce3");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/airindia.png?alt=media&token=6074c475-0c77-4992-bf44-999b14903bd7");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/indialabs.png?alt=media&token=889da50c-21c7-4524-9bb7-556155a52e0a");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/roboversity.png?alt=media&token=296d7562-1827-4bc9-b4ea-63c9c75d9794");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/friends.png?alt=media&token=828a91ee-49fc-47ce-9b7f-00c4536b804e");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/redbull.png?alt=media&token=c40606ae-82dd-4f17-835b-9add2b804b90");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/everglow.png?alt=media&token=86a17c0e-3457-414e-a5e5-c90c05996a61");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/jio.png?alt=media&token=f975ccf2-1c19-4b68-bdcb-34b178d95989");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/iyouthmag.png?alt=media&token=1332cdb0-b117-4def-b125-63e057b4bb4d");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/gandhi.png?alt=media&token=23901af3-1d8c-472f-93c0-29613f325a1b");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/satya.png?alt=media&token=0698acf9-67b4-4719-ae7c-7b4963de2d0a");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/teachforindia.png?alt=media&token=3d24fd52-5c5e-42da-8aa7-3a4065117835");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/princeton.png?alt=media&token=f1661613-caa2-4f28-8f94-2ce771a19f39");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/campusfrance.png?alt=media&token=8ad0e4c3-172c-45c7-b926-7c78392f60f1");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/jamboree.png?alt=media&token=f7151aa2-e692-43bd-997f-c058130b46f2");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/idp.png?alt=media&token=02144c36-5447-4da1-a705-910399150ad6");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/gateforum.png?alt=media&token=49083a00-b924-485f-9680-b72df8ec8eb5");
        sponsorsUrl.add("https://firebasestorage.googleapis.com/v0/b/srijan-17-e257c.appspot.com/o/ace.png?alt=media&token=f15c0bb6-d05b-4f30-9aa1-f12541bb5a4d");

    }
}
