package com.customer.shary.live.ui.chat_history.Adapter;

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
import com.customer.shary.live.ui.chat_history.chat.chat;
import com.customer.shary.live.ui.chat_history.chat_user_his.chat_history_user;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<viewholder>  {

    private ArrayList<chat_history_user>  users ;
    private Context mContext ;

    public adapter(ArrayList<chat_history_user> users, Context mContext) {
        this.users = users;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(mContext).inflate(R.layout.chat_user_history_raw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.name.setText(users.get(position).getAdmin_name());
        holder.lastmsg.setText(users.get(position).getLatest_message());
        Glide.with(mContext).load(users.get(position).getAdmin_image()).into(holder.img);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                

                Intent in = new Intent(mContext, chat.class);

                in.putExtra("products", "");
                in.putExtra("seller_room", users.get(position).getRoom());
                in.putExtra("seller_id",users.get(position).getSaller_id());
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(in);


            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

class viewholder extends RecyclerView.ViewHolder
{


    public  LinearLayout linearLayout ;

    public TextView name ,lastmsg;
    public ImageView img;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        name =itemView.findViewById(R.id._chats_name);
        lastmsg=itemView.findViewById(R.id._last_msg);
        img=itemView.findViewById(R.id._chats_img);
        linearLayout=itemView.findViewById(R.id._lin);
    }
}
