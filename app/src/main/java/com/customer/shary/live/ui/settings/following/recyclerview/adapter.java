package com.customer.shary.live.ui.settings.following.recyclerview;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.customer.shary.live.ui.settings.following.datamodel.follow_data_model;

import java.util.ArrayList;

public class adapter extends  RecyclerView.Adapter<viewholder>{

    private ArrayList<follow_data_model>  list;
    private Context mContext;

    /***
     *
     * @param list
     * @param mContext
     */
    public adapter(ArrayList<follow_data_model> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.following_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Glide.with(mContext).load(list.get(position).getImg()).into(holder._video_thumb);

        holder._store_name.setText(list.get(position).getName());
        holder._description.setText(list.get(position).getDescription());
        holder._unfollow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class viewholder extends RecyclerView.ViewHolder
{

    ImageView _video_thumb;
    TextView _store_name,_description;
    Button _unfollow_btn;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        _video_thumb=itemView.findViewById(R.id._video_thumb);
        _store_name=itemView.findViewById(R.id._store_name);
        _description=itemView.findViewById(R.id._description);
        _unfollow_btn=itemView.findViewById(R.id._unfollow_btn);



    }
}
