package com.customer.shary.live.ui.home.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.customer.shary.live.CustomRecyclerView.MediaObject;
import com.customer.shary.live.MainActivity;
import com.customer.shary.live.R;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.andreidobrescu.emojilike.EmojiLikeView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    //vars
    public Context context;
    public FrameLayout media_container;
    public TextView title, description, profileName, num_view, num_likes, num_dislikes,
            num_share, created_at, followers;
    public ImageView thumbnail, volumeControl, like_btn, dislike_btn, share_btn, follow;
    public ProgressBar progressBar;
    public CircleImageView profileImg;
    public View parent;
    public RequestManager requestManager;

    public TextView you, _comment_num, _Rate;

    public ImageView media, _Fav, Rate;
    public EmojiLikeView emojiLikeView;
    private AdView mAdView;
    CardView cardView;
    Button contact_us;


    public VideoPlayerViewHolder(@NonNull final View itemView, Context context) {
        super(itemView);

        this.context = context;
        parent = itemView;
        media = itemView.findViewById(R.id.media);
        emojiLikeView = itemView.findViewById(R.id.emojiView);
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.homeTitle);
        _comment_num = itemView.findViewById(R.id._comment_num);
        profileImg = itemView.findViewById(R.id.homeImg);
        profileName = itemView.findViewById(R.id.homeName);
        description = itemView.findViewById(R.id.homeDesc);
        progressBar = itemView.findViewById(R.id.progressBar);
        num_view = itemView.findViewById(R.id.numViews);
        num_likes = itemView.findViewById(R.id.numLikes);
        num_share = itemView.findViewById(R.id.numShare);
        share_btn = itemView.findViewById(R.id.shareBtn1);
        like_btn = itemView.findViewById(R.id.likesBtn);
        _Fav = itemView.findViewById(R.id._Fav);
        Rate = itemView.findViewById(R.id.Rate);
        _Rate = itemView.findViewById(R.id._RateT);


        if (itemView.findViewById(R.id.adView) != null) {
            mAdView = itemView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

        }
        if (itemView.findViewById(R.id.id_native_ad) != null) {
            MobileAds.initialize(context);

            AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            //the native ad will be available inside this method  (unifiedNativeAd)

                            UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView)
                                    MainActivity.activity.getLayoutInflater().inflate(R.layout.unifiednativeadview, null);

                            mapUnifiedNativeAdToLayout(unifiedNativeAd, unifiedNativeAdView);

                            FrameLayout nativeAdLayout = itemView.findViewById(R.id.id_native_ad);
                            nativeAdLayout.removeAllViews();
                            nativeAdLayout.addView(unifiedNativeAdView);
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }

        if(itemView.findViewById(R.id.adv_card)!=null)
        {
            cardView =itemView.findViewById(R.id.adv_card);
            contact_us=itemView.findViewById(R.id.contact_us);

        }


    }

    public void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {
        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));
        myAdView.setBodyView(myAdView.findViewById(R.id.ad_body));
        myAdView.setCallToActionView(myAdView.findViewById(R.id.ad_call_to_action));
        myAdView.setIconView(myAdView.findViewById(R.id.ad_icon));
        myAdView.setPriceView(myAdView.findViewById(R.id.ad_price));
        myAdView.setStarRatingView(myAdView.findViewById(R.id.ad_rating));
        myAdView.setStoreView(myAdView.findViewById(R.id.ad_store));
        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getBody() == null) {
            myAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getBodyView()).setText(adFromGoogle.getBody());
        }

        if (adFromGoogle.getCallToAction() == null) {
            myAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((Button) myAdView.getCallToActionView()).setText(adFromGoogle.getCallToAction());
        }

        if (adFromGoogle.getIcon() == null) {
            myAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) myAdView.getIconView()).setImageDrawable(adFromGoogle.getIcon().getDrawable());
        }

        if (adFromGoogle.getPrice() == null) {
            myAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getPriceView()).setText(adFromGoogle.getPrice());
        }

        if (adFromGoogle.getStarRating() == null) {
            myAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) myAdView.getStarRatingView()).setRating(adFromGoogle.getStarRating().floatValue());
        }

        if (adFromGoogle.getStore() == null) {
            myAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getStoreView()).setText(adFromGoogle.getStore());
        }

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);

    }


    public void onBind(MediaObject mediaObject, RequestManager requestManager) {

        if (mediaObject != null) {
            this.requestManager = requestManager;
            parent.setTag(this);

            this.requestManager
                    .load(mediaObject.getThumbnail())
                    .into(thumbnail);

        }
    }
}
