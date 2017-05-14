package com.hash.android.srijan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hash.android.srijan.fragment.DashboardFragment;
import com.hash.android.srijan.fragment.SubscriptionFragment;


public class TabsPagerAdapter extends FragmentStatePagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DashboardFragment();
//            case 1:
//                return new InterestedFragment();
            case 1:
                return new SubscriptionFragment();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
//        return super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Explore";
//            case 1:
//                return "Interested";
            case 1:
                return "Subscribed";
            default:
                return null;
        }
    }
}
