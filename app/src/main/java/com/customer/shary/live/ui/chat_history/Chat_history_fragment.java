package com.customer.shary.live.ui.chat_history;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.Contacts.ContactsList;
import com.customer.shary.live.R;
import com.customer.shary.live.ads.loadads;
import com.customer.shary.live.auth.main;
import com.customer.shary.live.ui.chat_history.Adapter.adapter;
import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Chat_history_fragment extends Fragment {

    private chat_viewmodel chathistoryviewmodel;
    private RecyclerView recyclerView ;
    private com.customer.shary.live.ui.chat_history.Adapter.adapter adapter ;
    private ProgressDialog dialog;
    private LinearLayout contacts  ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loadads.getInstance().mInterstitialAd(getActivity());

        chathistoryviewmodel =
                ViewModelProviders.of(this).get(chat_viewmodel.class);


        View root = inflater.inflate(R.layout.chat_history, container, false);


        contacts =root.findViewById(R.id.contacts);

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


            contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(getActivity(), ContactsList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);


                }
            });
            recyclerView = root.findViewById(R.id.rec);
            dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            dialog.setMessage(getResources().getString(R.string.load));
            dialog.setCancelable(false);

            dialog.show();
            chathistoryviewmodel.getText(getContext()).observe(getActivity(), new Observer<ArrayList<chat_history_user>>() {
                @Override
                public void onChanged(ArrayList<chat_history_user> chat_history_users) {


                    if (chat_history_users.size() > 0) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        init_rec(chat_history_users);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        return root;
    }

    public  void init_rec(ArrayList<chat_history_user> chat_history_users)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter =new adapter(chat_history_users,getActivity());
        recyclerView.setAdapter(adapter);

    }
}





















