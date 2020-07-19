package com.customer.shary.live.DisplayProduct.Seller_Product_And_Related;

import android.content.Context;
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
import com.customer.shary.live.ui.home.datamodel.apidata;

import java.util.ArrayList;

public class Adapter_saller  extends RecyclerView.Adapter<viewholder>{


    private ArrayList<apidata> related;
    private Context mContext;
    private videoscallback videoscallback;

    public  Adapter_saller (videoscallback videoscallback ,ArrayList<apidata> related, Context mContext) {
        this.related = related;
        this.mContext = mContext;
        this.videoscallback=videoscallback;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new viewholder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.display_videos_raw, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Log.e("Video", String.valueOf(related.get(position).getImage_link()));
        Glide.with(mContext).load(related.get(position).getImage_link()).centerCrop().into(holder.dis_image);
        holder.dis_channel_name.setText(related.get(position).getStore_name());
        holder.dis_numViews.setText(related.get(position).getViews());
        holder.dis_title.setText(related.get(position).getName());

        holder.dis_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoscallback.video_clicked_saller(related.get(position).getVideo_link(),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return related.size();
    }
}

class  viewholder extends RecyclerView.ViewHolder
{

    public ImageView dis_image;
    public TextView dis_title,dis_channel_name,dis_numViews;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        dis_image=itemView.findViewById(R.id.dis_image);
        dis_title=itemView.findViewById(R.id.dis_title);
        dis_channel_name=itemView.findViewById(R.id.dis_channel_name);
        dis_numViews=itemView.findViewById(R.id.dis_numViews);

    }
}


