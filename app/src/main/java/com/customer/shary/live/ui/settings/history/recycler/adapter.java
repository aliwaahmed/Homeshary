package com.customer.shary.live.ui.settings.history.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.ui.home.datamodel.apidata;

import java.util.ArrayList;

public class adapter extends  RecyclerView.Adapter<viewholder>{

    private Context mContext;
    private ArrayList<apidata>  list;

    /**
     *
     * @param mContext
     * @param list
     */
    public adapter(Context mContext, ArrayList<apidata> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder._video_title.setText(list.get(position).getName());
        holder._description.setText(list.get(position).getDetails());
        holder._fav_store_name.setText(list.get(position).getStore_name());
        holder._views.setText(list.get(position).getViews());
        Glide.with(mContext).load(list.get(position).getVideo_link()).into(holder._video_thumb);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, DisplayVideoActivity.class);
                intent.putExtra("video",        list.get(position).getMediaObject().getMedia_url());
                intent.putExtra("store_name",   list.get(position).getStore_name());
                intent.putExtra("storeimage",   list.get(position).getStore_image());
                intent.putExtra("channelname",  list.get(position).getStore_name());
                intent.putExtra("description",  list.get(position).getDetails());
                intent.putExtra("likes",        list.get(position).getLikes());
                intent.putExtra("desc",         list.get(position).getDetails());
                intent.putExtra("shares",       list.get(position).getShare());
                intent.putExtra("views",        list.get(position).getViews());
                intent.putExtra("newprice",     list.get(position).getNew_price());
                intent.putExtra("oldprice",     list.get(position).getPrice());
                intent.putExtra("productid",    list.get(position).getProduct_id());
                intent.putExtra("seller_id",    list.get(position).getSeller_id());
                intent.putExtra("product_id",   list.get(position).getProduct_id());
                intent.putExtra("category",     list.get(position).getCategory());
                intent.putExtra("product_link", list.get(position).getProduct_link());
                intent.putExtra("product_img",  list.get(position).getMediaObject().getThumbnail());
                intent.putExtra("pos", position);
                intent.putParcelableArrayListExtra("api_data", list);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
        holder._delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                list.remove(position);

                notifyDataSetChanged();
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

    public TextView _video_title,_description,_fav_store_name,_views;
    public ImageView _video_thumb,_delet;

    CardView card;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        _video_title=itemView.findViewById(R.id._video_title);
        _description=itemView.findViewById(R.id._description);
        _fav_store_name=itemView.findViewById(R.id._fav_store_name);
        _views=itemView.findViewById(R.id._views);
        _video_thumb=itemView.findViewById(R.id._video_thumb);
        _delet=itemView.findViewById(R.id._delet);
        card=itemView.findViewById(R.id.card);
    }
}
