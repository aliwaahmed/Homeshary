package com.customer.shary.live.ui.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.customer.shary.live.BuildConfig;
import com.customer.shary.live.R;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.favourite.favourite_Activity;
import com.customer.shary.live.payment.order.myorder;
import com.customer.shary.live.ui.settings.edite_my_info.edite_info;
import com.customer.shary.live.ui.settings.edite_my_info.model.userdatamodel;
import com.customer.shary.live.ui.settings.edite_my_info.viewmodel.viewmodel;
import com.customer.shary.live.ui.settings.history.history;
import com.customer.shary.live.ui.settings.notificationsetting.notificationsetting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class settingFragment extends Fragment {

    private settingViewModel dashboardViewModel;

    private LinearLayout _history,_edit_info,_fav,_notifications,_my_orders;
    private ImageView _profile_img;
    private TextView _profile_name;
    private LinearLayout _share_app;


    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                SharedPreferences prefs =context.getSharedPreferences("login", MODE_PRIVATE);
                if(prefs!=null) {
                    Glide.with(context).load(prefs.getString("image", "2")).into(_profile_img);
                    _profile_name.setText(prefs.getString("name", "-2"));
                }
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loadads.getInstance().loadRewardedVideoAd(getActivity());

        dashboardViewModel =
                ViewModelProviders.of(this).get(settingViewModel.class);
        View root = inflater.inflate(R.layout.settings_layout, container, false);

        _share_app=root.findViewById(R.id._share_app);
        _my_orders=root.findViewById(R.id._my_orders);
        _my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), myorder.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);


            }
        });




        root.findViewById(R.id._about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), About.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        root.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getContext(),
                        privacy.class);
                startActivity(browserIntent);
            }
        });

        root.findViewById(R.id._terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(getContext(),
                        terms.class);
                startActivity(browserIntent);
            }
        });






        _share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Shary));
                    String shareMessage= getString(R.string.recommend_app);
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });


        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("login",MODE_PRIVATE);

        if(sharedPreferences.getString("status","-1").equals("-1"))
        {


            BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.nav_view);
            navigation.setSelectedItemId(R.id.navigation_home);
            Intent intent =new Intent(getActivity(), main.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);


        }
        else {

            dashboardViewModel.getText().observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {


                }
            });

            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getActivity());
            lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

            _history = root.findViewById(R.id._history);
            _edit_info = root.findViewById(R.id._edit_info);
            _fav = root.findViewById(R.id._fav);
            _notifications = root.findViewById(R.id._notifications);
            _profile_img = root.findViewById(R.id._profile_img);
            _profile_name = root.findViewById(R.id._profile_name);


            SharedPreferences prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);

            Picasso.Builder builder = new Picasso.Builder(getActivity());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    _profile_img.setImageResource(R.drawable.avatar);
                }
            });

           com.customer.shary.live.ui.settings.edite_my_info.viewmodel.viewmodel viewmodel;

            viewmodel = ViewModelProviders.of(this).get(viewmodel.class);

            viewmodel.load(getContext()).observe(getViewLifecycleOwner(), new Observer<userdatamodel>() {
                @Override
                public void onChanged(userdatamodel userdatamodel) {

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("login", MODE_PRIVATE).edit();

                    editor.putString("image",userdatamodel.getImg());
                    editor.putString("name",userdatamodel.getName());
                    editor.apply();
                    editor.commit();

                    Glide.with(getActivity()).load(userdatamodel.getImg()).into(_profile_img);
                }
            });




            _profile_name.setText(prefs.getString("name", "-2"));

            _edit_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadads.getInstance().loadRewardedVideoAd(getActivity());

                    Intent _history = new Intent(getActivity(), edite_info.class);
                    _history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(_history);
                }
            });

            _history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadads.getInstance().loadRewardedVideoAd(getActivity());

                    Intent _history = new Intent(getActivity(), history.class);
                    _history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(_history);
                }
            });

//            following1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent _history = new Intent(getActivity(), following.class);
//                    _history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(_history);
//                }
//            });

            _fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadads.getInstance().loadRewardedVideoAd(getActivity());

                    Intent _history = new Intent(getActivity(), favourite_Activity.class);
                    _history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(_history);
                }
            });

            _notifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadads.getInstance().loadRewardedVideoAd(getActivity());
                    Intent _history = new Intent(getActivity(), notificationsetting.class);
                    _history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(_history);
                }
            });

        }

        return root;
    }
}