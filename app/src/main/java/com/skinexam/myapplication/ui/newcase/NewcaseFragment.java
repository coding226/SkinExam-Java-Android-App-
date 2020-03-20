package com.skinexam.myapplication.ui.newcase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.skinexam.myapplication.R;

public class NewcaseFragment extends Fragment {

    private NewcaseViewModel newcaseViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_newcase, container, false);

        return root;
    }
}