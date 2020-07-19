package com.customer.shary.live.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.customer.shary.live.R;
import com.customer.shary.live.cart.Cart_Activity;
import com.customer.shary.live.notification.rec.adapte;
import com.customer.shary.live.notification.viewmodel.notificationviewmodel;
import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class notifcation extends AppCompatActivity {

    private datamodel datamodel;
    private ArrayList<datamodel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

        recyclerView = findViewById(R.id._notification_list);
        backbtn = findViewById(R.id.backbtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        notificationviewmodel notificationviewmodel = new ViewModelProvider(this).get(notificationviewmodel.class);

        final Observer<ArrayList<datamodel>> nameObserver = new Observer<ArrayList<datamodel>>() {
            @Override
            public void onChanged(ArrayList<datamodel> s) {
                adapte adapte = new adapte(getApplicationContext(), s);
                recyclerView.setAdapter(adapte);
                adapte.notifyDataSetChanged();

            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

        notificationviewmodel.getText(this).observe(this, nameObserver);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }



}



