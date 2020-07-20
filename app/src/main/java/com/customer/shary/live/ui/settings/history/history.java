package com.customer.shary.live.ui.settings.history;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.R;
import com.customer.shary.live.ui.home.datamodel.apidata;
import com.customer.shary.live.ui.settings.history.recycler.adapter;
import com.customer.shary.live.ui.settings.history.viewmodel.viewmodel;
import com.customer.shary.live.ui.settings.settingViewModel;

import java.util.ArrayList;

public class history extends AppCompatActivity {

    private LinearLayoutManager  linearLayoutManager;
    private adapter mAdapter;
    private com.customer.shary.live.ui.settings.history.viewmodel.viewmodel viewmodel;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        ImageView backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id._orders_recycler);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());

        viewmodel=  ViewModelProviders.of(this).get(viewmodel.class);
        viewmodel.getvideos(getApplicationContext()).observe(this, new Observer<ArrayList<apidata>>() {
            @Override
            public void onChanged(ArrayList<apidata> apidata) {
                init(apidata);
            }
        });

    }

    public void init(ArrayList<apidata> historymodels)
    {
        mAdapter=new adapter(getApplicationContext(),historymodels);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }
}






