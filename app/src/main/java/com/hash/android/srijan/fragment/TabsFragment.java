package com.hash.android.srijan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.android.srijan.R;
import com.hash.android.srijan.adapter.TabsPagerAdapter;

import static com.hash.android.srijan.fragment.SubscriptionFragment.mAdapter;


public class TabsFragment extends android.support.v4.app.Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public int int_items = 3;

    public TabsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

        View x = inflater.inflate(R.layout.tab_layout, null);
        getActivity().setTitle("Srijan '17");
        tabLayout = (TabLayout) x.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) x.findViewById(R.id.vpPager);

        /**
         *Set an Adapter for the View Pager
         */
        viewPager.setAdapter(new TabsPagerAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return x;

    }


}

