package com.customer.shary.live.notification.rec;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.notification.datamodel;
import com.customer.shary.live.ui.home.datamodel.apidata;

import java.util.ArrayList;

public class adapte extends RecyclerView.Adapter<viewholder> {


    private Context mContext;
    private ArrayList<datamodel> list;

    public adapte(Context mContext, ArrayList<datamodel> list) {
        this.mContext = mContext;
        this.list = list;

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.notifications_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int i) {

        Glide.with(mContext).load(list.get(i).getSeller_image()).into(holder._store_image);
        Glide.with(mContext).load(list.get(i).getProduct_imag()).into(holder._product_img);
        holder._store_name.setText(list.get(i).getTitle());
        holder._notification_type.setText(list.get(i).getMessage());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, DisplayVideoActivity.class);
                intent.putExtra("video", list.get(i).getMediaObject().getMedia_url());
                intent.putExtra("store_name", list.get(i).getStore_name());
                intent.putExtra("storeimage", list.get(i).getStore_image());
                intent.putExtra("channelname", list.get(i).getStore_name());
                intent.putExtra("description", list.get(i).getDetails());
                intent.putExtra("likes", list.get(i).getLikes());
                intent.putExtra("desc", list.get(i).getDetails());
                intent.putExtra("shares", list.get(i).getShare());
                intent.putExtra("views", list.get(i).getViews());
                intent.putExtra("newprice", list.get(i).getNew_price());
                intent.putExtra("oldprice", list.get(i).getPrice());
                intent.putExtra("productid", list.get(i).getProduct_id());
                intent.putExtra("seller_id", list.get(i).getSeller_id());
                intent.putExtra("product_id", list.get(i).getProduct_id());
                intent.putExtra("category", list.get(i).getCategory());
                intent.putExtra("product_link", list.get(i).getProduct_link());
                intent.putExtra("product_img", list.get(i).getMediaObject().getThumbnail());
                intent.putExtra("seller_room",list.get(i).getSeller_room());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class  viewholder extends RecyclerView.ViewHolder
{
    ImageView _store_image,_product_img;
    TextView _store_name,_notification_type,_notification_date;
    Button view;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        _store_image=itemView.findViewById(R.id._store_image);
        _product_img=itemView.findViewById(R.id._product_img);
        _store_name=itemView.findViewById(R.id._store_name);
        _notification_type=itemView.findViewById(R.id._notification_type);
        view=itemView.findViewById(R.id.view);
    }
}


























