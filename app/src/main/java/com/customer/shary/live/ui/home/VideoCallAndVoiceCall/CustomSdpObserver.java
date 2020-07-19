package com.customer.shary.live.ui.home.VideoCallAndVoiceCall;


import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;


public class CustomSdpObserver implements SdpObserver {


    private String tag;

    CustomSdpObserver(String logTag) {
        tag = this.getClass().getCanonicalName();
        this.tag = this.tag + " " + logTag;
    }


    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.v(tag, "onCreateSuccess() called with: sessionDescription = [" + sessionDescription + "]");
    }

    @Override
    public void onSetSuccess() {
        Log.d(tag, "onSetSuccess() called");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.v(tag, "onCreateFailure() called with: s = [" + s + "]");
    }

    @Override
    public void onSetFailure(String s) {
        Log.v(tag, "onSetFailure() called with: s = [" + s + "]");
    }

}
