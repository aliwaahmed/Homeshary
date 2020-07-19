package com.customer.shary.live.Contacts.rec;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.Contacts.contacts_model.contact_api_model;
import com.customer.shary.live.Contacts.model;
import com.customer.shary.live.R;
import com.customer.shary.live.ui.chat_history.chat.chat;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter  extends RecyclerView.Adapter<Adapter.viewholder> {


    private ArrayList<model> models ;
    private Context context;
    private String link;
    HashMap<Integer, contact_api_model> map;
    HashMap<Integer, String>      map1;
    ArrayList<contact_api_model> contact_api;

    public Adapter(ArrayList<contact_api_model> contact_api, HashMap<Integer, contact_api_model> map, HashMap<Integer, String> map1, String link, ArrayList<model> models, Context context) {
        this.models = models;
        this.context = context;
        this.link =link;
        this.map1=map1;
        this.map=map;
        this.contact_api=contact_api;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.name.setText(models.get(position).getName());
        holder.phone.setText(models.get(position).getPhone());
        holder._invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, link);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(Intent.createChooser(i, context.getResources().getString(R.string.shareapp))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        if(map.containsKey(position))
        {
            if(map.get(position)!=null)
            {

                Log.e("ss",map.get(position).getLink()+map.get(position).getImage());
                Glide.with(context).load(map.get(position).getLink()+map.get(position).getImage()).into(holder._img);
            }
        }

        if(map1.containsKey(position)) {
            if(map.get(position)!=null)
            {
                holder._invite.setVisibility(View.INVISIBLE);
                Log.e("exist", "exist");
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, chat.class);
                        intent.putExtra("products", "");
                        intent.putExtra("seller_room", map.get(position).getRoom_name());
                        intent.putExtra("seller_id",map.get(position).getId());
                        Log.e("seller_id",map.get(position).getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
                });


            } else {
                holder._invite.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class viewholder extends RecyclerView.ViewHolder
    {

        TextView  name,phone ,_invite;
        LinearLayout linearLayout ;
        ImageView _img;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id._contact_name);
            phone=itemView.findViewById(R.id._contact_number);
            _invite=itemView.findViewById(R.id._invite);
            _img=itemView.findViewById(R.id._img);
            linearLayout =itemView.findViewById(R.id.lin);
        }
    }
}
