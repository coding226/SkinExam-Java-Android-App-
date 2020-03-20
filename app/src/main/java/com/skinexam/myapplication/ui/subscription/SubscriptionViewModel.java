package com.skinexam.myapplication.ui.subscription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubscriptionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SubscriptionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Subscription fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}