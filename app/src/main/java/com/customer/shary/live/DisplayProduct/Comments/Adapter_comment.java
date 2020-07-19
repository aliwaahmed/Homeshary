package com.customer.shary.live.DisplayProduct.Comments;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.shary.live.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_comment  extends RecyclerView.Adapter<viewholder>{


    private ArrayList<datamodelComment> comments;
    private Context mContext;
    SharedPreferences prefs;
    String name ,img;
    public  Adapter_comment (ArrayList<datamodelComment> comments, Context mContext) {
        this.comments = comments;
        this.mContext = mContext;
       prefs = mContext.getSharedPreferences("login", MODE_PRIVATE);
        name =prefs.getString("name","");
        img=prefs.getString("image","");


    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new viewholder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.comments_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        if (comments.get(position).getImg() == null) {
            comments.get(position).setName(name);
            comments.get(position).setImg(img);

        }
//            if (comments.get(position).getId().equals(prefs.getString("login_id", "-1"))) {
//                holder._lin_edite.setVisibility(View.INVISIBLE);
//            }

            Glide.with(mContext).load(comments.get(position).getImg()).into(holder.user_img);
            holder.comment.setText(comments.get(position).getContent());
            holder.name.setText(comments.get(position).getName());

            if (position == comments.size() - 1) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up);
                holder.lin.setAnimation(animation);
                animation.start();
                Log.e("tag", String.valueOf(position));
            }



    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

class  viewholder extends RecyclerView.ViewHolder
{

    public  TextView name,comment;
    public  ImageView user_img;
    public LinearLayout lin,_lin_edite;

    public viewholder(@NonNull View itemView) {

        super(itemView);

        name =itemView.findViewById(R.id._name);
        comment=itemView.findViewById(R.id._comment);
        user_img=itemView.findViewById(R.id._img);
        lin=itemView.findViewById(R.id._lin);
        _lin_edite=itemView.findViewById(R.id._lin_edite);


    }
}


