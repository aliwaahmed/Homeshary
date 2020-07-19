package com.customer.shary.live;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.cart.Cart_Activity;
import com.customer.shary.live.notification.notifcation;

import com.customer.shary.live.ui.home.datamodel.apidata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import ro.andreidobrescu.emojilike.Emoji;
import ro.andreidobrescu.emojilike.EmojiConfig;
import ro.andreidobrescu.emojilike.EmojiLikeTouchDetector;
import ro.andreidobrescu.emojilike.IActivityWithEmoji;
import ro.andreidobrescu.emojilike.OnEmojiSelectedListener;

public class MainActivity extends AppCompatActivity implements IActivityWithEmoji, OnEmojiSelectedListener {

    EmojiLikeTouchDetector emojiLikeTouchDetector;
    FloatingActionButton floatingActionButton,cartActionButton;
    public static Activity activity;
    public ArrayList<apidata> cart_product_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activity = this;
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_CONTACTS},
                1);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.settings,R.id.live, R.id.search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        emojiLikeTouchDetector = new EmojiLikeTouchDetector();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                if (sharedPreferences.getString("status", "-1").equals("-1")) {


                    BottomNavigationView navigation =
                            (BottomNavigationView) findViewById(R.id.nav_view);
                    navigation.setSelectedItemId(R.id.navigation_home);

                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else {

                    Intent intent = new Intent(getApplicationContext(), notifcation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });


        Intent serviceIntent = new Intent(this, Service.class);
        serviceIntent.putExtra("inputExtra", "App is online");
        ContextCompat.startForegroundService(this, serviceIntent);


        cartActionButton=findViewById(R.id.cartActionButton);
        cartActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.getString("status", "-1").equals("-1")) {
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {


                    Intent in = new Intent(getApplicationContext(), Cart_Activity.class);
                    in.putParcelableArrayListExtra("products", cart_product_list);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean shouldCallSuper = emojiLikeTouchDetector.dispatchTouchEvent(event);
        if (shouldCallSuper)
            return super.dispatchTouchEvent(event);
        return false;
    }

    @Override
    public void configureEmojiLike(EmojiConfig config) {
        emojiLikeTouchDetector.configure(config);
    }

    @Override
    public void onEmojiSelected(Emoji emoji) {

    }
}