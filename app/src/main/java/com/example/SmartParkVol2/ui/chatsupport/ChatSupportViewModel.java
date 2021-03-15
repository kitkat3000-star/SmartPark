package com.example.SmartParkVol2.ui.chatsupport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatSupportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChatSupportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is chat support fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}