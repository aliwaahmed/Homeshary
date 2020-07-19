package com.customer.shary.live.ui.search.rec;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.ui.home.datamodel.apidata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class adapter extends RecyclerView.Adapter<viewholder> {


    private ArrayList<apidata>  list ;
    private ArrayList<apidata> temp;;
    private Context mContext;

    public adapter(ArrayList<apidata> list, Context mContext) {
        this.list = list;
        this.temp=list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        Glide.with(mContext).load(list.get(position).getMediaObject().getThumbnail()).into(holder.search_productimage);
        holder._search_productname.setText(list.get(position).getName());
        holder._search_productdescription.setText(list.get(position).getDetails());
        holder._search_newprice.setText(list.get(position).getNew_price());
        holder._search_oldprice.setText(list.get(position).getPrice());
        holder._search_q_number.setText(list.get(position).getQuantity());
        holder._search_sellername.setText(list.get(position).getStore_name());
        holder._lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DisplayVideoActivity.class);

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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void filterList(ArrayList<apidata> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

}
class viewholder extends RecyclerView.ViewHolder
{

    public ImageView search_productimage;
    public TextView _search_productname,_search_productdescription,
    _search_sellername,_search_q_number,_search_newprice,_search_oldprice;

    LinearLayout _lin;

    public viewholder(@NonNull View itemView) {
        super(itemView);
        search_productimage=itemView.findViewById(R.id.search_productimage);
        _search_productname=itemView.findViewById(R.id._search_productname);
        _search_productdescription=itemView.findViewById(R.id._search_productdescription);
        _search_sellername=itemView.findViewById(R.id._search_sellername);
        _search_q_number=itemView.findViewById(R.id._search_q_number);
        _search_newprice=itemView.findViewById(R.id._search_newprice);
        _search_oldprice=itemView.findViewById(R.id._search_oldprice);
        _lin=itemView.findViewById(R.id._lin);


    }
}
