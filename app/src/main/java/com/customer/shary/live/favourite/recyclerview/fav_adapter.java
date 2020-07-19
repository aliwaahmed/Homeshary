package com.customer.shary.live.favourite.recyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class fav_adapter  extends  RecyclerView.Adapter<viewholder>{

    private Context mContext;
    private ArrayList<favmodel> mFavmodels ;
    SharedPreferences prefs;

    public fav_adapter(Context mContext, ArrayList<favmodel> mFavmodels) {
        this.mContext = mContext;
        this.mFavmodels = mFavmodels;
        prefs = mContext.getSharedPreferences("login", MODE_PRIVATE);


    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(mContext)
                .inflate(R.layout.favourite_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        Glide.with(mContext).load(mFavmodels.get(position).getMediaObject().getThumbnail()).into(holder.imageView);
        holder._video_title.setText(mFavmodels.get(position).getName());
        holder._fav_store_name.setText(mFavmodels.get(position).getStore_name());
        holder._description.setText(mFavmodels.get(position).getDetails());
        holder._views.setText(mFavmodels.get(position).getViews());
        holder._delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignallingClient.getInstance(mContext).delet_fav(String.valueOf(mFavmodels.get(position).getProduct_id()),prefs.getString("login_id", "-1"));
                mFavmodels.remove(position);
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mFavmodels.size();
    }
}

class viewholder extends RecyclerView.ViewHolder
{


    public ImageView imageView  ,_delet;
    public TextView _video_title,_fav_store_name,_description,_views;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id._video_thumb);
        _video_title=itemView.findViewById(R.id._video_title);
        _fav_store_name=itemView.findViewById(R.id._fav_store_name);
        _description=itemView.findViewById(R.id._description);
        _views=itemView.findViewById(R.id._views);
        _delet =itemView.findViewById(R.id._delet);
    }
}
