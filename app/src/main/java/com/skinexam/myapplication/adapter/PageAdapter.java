package com.skinexam.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.skinexam.myapplication.activityeditprofile.Step1;
import com.skinexam.myapplication.activityeditprofile.Step2;

public class PageAdapter extends FragmentPagerAdapter {
    /**
     * Constructor for {@link FragmentPagerAdapter} that sets the fragment manager for the adapter.
     * This is the equivalent of calling  and
     * passing in {@link #BEHAVIOR_SET_USER_VISIBLE_HINT}.
     *
     * <p>Fragments will have {@link Fragment#setUserVisibleHint(boolean)} called whenever the
     * current Fragment changes.</p>
     *
     * @param fm fragment manager that will interact with this adapter
     * @deprecated use  with
     * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */

    private int numOfTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Step1();
            case 1:
                return new Step2();
            default:
                return null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
