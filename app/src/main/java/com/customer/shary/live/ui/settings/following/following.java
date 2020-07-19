package com.customer.shary.live.ui.settings.following;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.R;
import com.customer.shary.live.ui.settings.following.datamodel.follow_data_model;
import com.customer.shary.live.ui.settings.following.recyclerview.adapter;
import com.customer.shary.live.ui.settings.following.viewmodel.viewmodel;

import java.util.ArrayList;

public class following extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager ;
    private RecyclerView recyclerView ;
    private viewmodel viewmodel;
    private adapter adapter;
    private ImageView backbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.following_layout);
        linearLayoutManager =new LinearLayoutManager(this);
        recyclerView=findViewById(R.id._saller_following);
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewmodel = new ViewModelProvider(this).get(viewmodel.class);
        viewmodel.getfollowed(getApplicationContext()).observe(this,
                new Observer<ArrayList<follow_data_model>>() {
            @Override
            public void onChanged(ArrayList<follow_data_model> follow_data_models) {

                init_rec(follow_data_models);
            }
        });
    }
    public void init_rec(ArrayList<follow_data_model> follow_data_models)
    {
        adapter=new adapter(follow_data_models,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}
