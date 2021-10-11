package com.iu.open311.ui.issues;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IssuessViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IssuessViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is issues fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}