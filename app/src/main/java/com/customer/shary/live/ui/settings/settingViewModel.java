package com.customer.shary.live.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class settingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public settingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }
    public void post()
    {
        mText.setValue("This idssddss dashboard fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}