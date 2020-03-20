package com.skinexam.myapplication.ui.subscription;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.skinexam.myapplication.R;

public class SubscriptionFragment extends Fragment implements View.OnClickListener {

//    private SubscriptionViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        slideshowViewModel = ViewModelProviders.of(this).get(SubscriptionViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        View root = inflater.inflate(R.layout.fragment_subscription, container, false);
        return root;
    }

    @Override
    public void onClick(View view) {

    }
}