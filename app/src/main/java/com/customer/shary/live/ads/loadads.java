package com.customer.shary.live.ads;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class loadads {
    private static final loadads ourInstance = new loadads();
    private static InterstitialAd  interstitialAd;
    private static  RewardedVideoAd rewardedVideoAd;



    public static loadads getInstance() {
        return ourInstance;
    }

    private loadads() {
    }




    public static  void  mInterstitialAd(Context context)
    {
        MobileAds.initialize(context, "");
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
        interstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
    }



    public static  void loadRewardedVideoAd(Context context ){

        MobileAds.initialize(context, "");
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        AdRequest request = new AdRequest.Builder().build();
        rewardedVideoAd.loadAd("", request);
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }
}
