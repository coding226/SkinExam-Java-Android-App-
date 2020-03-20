package com.skinexam.myapplication.ui.editprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.skinexam.myapplication.R;
import com.skinexam.myapplication.activityeditprofile.Step1;
import com.skinexam.myapplication.activityeditprofile.Step2;

public class PagerAdapter extends FragmentStatePagerAdapter {
    /**
     * Return the Fragment associated with a specified position.
     *
     * @param fm
     */
//    int mNoOfTabs;
    public PagerAdapter(FragmentManager fm){
        super(fm);
//        this.mNoOfTabs = NumberOfTabs;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = new DemoObjectFragment();
//        Bundle args = new Bundle();
//        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);
//        return fragment;
        switch (position){
            case 0:
                Step1 tab1 =new Step1();
                return tab1;
            case 1:
                Step2 tab2 = new Step2();
                return tab2;
//            case 2:
//                Tab3 tab3 = new Tab3();
//                return tab3;
            default:
                return  null;
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return 2;
    }

//    public CharSequence getPageTitle(int position){
//        return "OBJECT " + (position + 1);
//    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_editprofile, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            Bundle args = getArguments();
            ((TextView) view.findViewById(R.id.text_profile))
                    .setText(Integer.toString(args.getInt(ARG_OBJECT)));
        }
    }
}
