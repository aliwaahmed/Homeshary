package com.customer.shary.live.ui.live.recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;
import com.customer.shary.live.ui.live.MyVideoCall;
import com.customer.shary.live.ui.live.datamodel.livemodel;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<viewholder>  {


    private Context mContext;
    private ArrayList<livemodel> list;

    public adapter(Context mContext, ArrayList<livemodel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate( R.layout.live_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        Glide.with(mContext).load(list.get(position).getSeller_image()).into(holder._live_store_img);
        holder._live_store_name.setText(list.get(position).getSeller_name());
        holder._lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(mContext, MyVideoCall.class);
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
class viewholder extends RecyclerView.ViewHolder
{

    ImageView _live_store_img;
    TextView _live_store_name;
    LinearLayout _lin;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        _live_store_img=itemView.findViewById(R.id._live_store_img);
        _live_store_name=itemView.findViewById(R.id._live_store_name);
        _lin=itemView.findViewById(R.id._lin);
    }
}
