package com.example.vera_liu.twitter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.vera_liu.twitter.MentionsFragment;
import com.example.vera_liu.twitter.TweetsFragment;

/**
 * Created by vera_liu on 4/16/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    public TweetsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Log.d("position", Integer.toString(position));
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return TweetsFragment.newInstance();
            case 1:
                return MentionsFragment.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return "Tweets";
            case 1:
                return "Mentions";
            default:
                return null;
        }
    }


}