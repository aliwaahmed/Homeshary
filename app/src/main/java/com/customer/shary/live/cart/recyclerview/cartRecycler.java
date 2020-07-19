package com.customer.shary.live.cart.recyclerview;

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
import com.customer.shary.live.DisplayProduct.DisplayVideoActivity;
import com.customer.shary.live.R;
import com.customer.shary.live.Sockets.SignallingClient;
import com.customer.shary.live.cart.cart_sumation;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class cartRecycler extends RecyclerView.Adapter<holder> {


    private Context mContext;
    private ArrayList<cartmodel> list;
    private SharedPreferences prefs;
    private cart_sumation cart_sumation;
    private Double sumation = 0.0;

    /**
     * @param mContext
     * @param list
     */
    public cartRecycler(cart_sumation cart_sumation, Context mContext, ArrayList<cartmodel> list) {
        this.mContext = mContext;
        this.list = list;
        this.cart_sumation = cart_sumation;
        prefs = mContext.getSharedPreferences("login", MODE_PRIVATE);

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(mContext).inflate(R.layout.cart_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {


        Glide.with(mContext).load(list.get(position).getMediaObject().getThumbnail()).into(holder.cart_productimage);
        holder.cart_productname.setText(list.get(position).getName());
        holder.cart_productdescription.setText(list.get(position).getDetails());
        holder.cart_sellername.setText(list.get(position).getStore_name());
        holder._item_price.setText(list.get(position).getNew_price());

        holder.cart_q_number.setText(list.get(position).getQuantity());


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Log.e("add", "add");

                    int qty = Integer.valueOf(list.get(position).getQuantity());
                    int new_qty = qty + 1;
                    list.get(position).setQuantity(String.valueOf(new_qty));

                    //TODO :SEND TO SOCKET ADD
                    SignallingClient.getInstance(mContext).AddCart(String.valueOf(list.get(position).getProduct_id()),
                            prefs.getString("login_id", "-1"), "1");

                    cart_sumation.sumation(list);
                    notifyDataSetChanged();

                    DisplayVideoActivity.cart_product += 1;

                    DisplayVideoActivity.cart_number.setText(String.valueOf(DisplayVideoActivity.cart_product));
                } catch (Exception e) {

                }


            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Log.e("remove", "remove");
                    int qty = Integer.valueOf(list.get(position).getQuantity());
                    int new_qty = qty - 1;
                    if (new_qty == 0) {

                        SignallingClient.getInstance(mContext).RemoveFromCart(prefs.getString("login_id", ""),
                                String.valueOf(list.get(position).getProduct_id()));
                        list.remove(position);
                        cart_sumation.sumation(list);
                        notifyDataSetChanged();


                    } else {

                        list.get(position).setQuantity(String.valueOf(new_qty));
                        //TODO :SEND TO SOCKET Update
                        SignallingClient.getInstance(mContext).Edite_cart(prefs.getString("login_id", ""),
                                String.valueOf(list.get(position).getProduct_id()), String.valueOf(new_qty));

                        cart_sumation.sumation(list);
                        notifyDataSetChanged();


                    }

                    DisplayVideoActivity.cart_product -= 1;

                    DisplayVideoActivity.cart_number.setText(String.valueOf(DisplayVideoActivity.cart_product));


                } catch (Exception e) {

                }

            }
        });
        holder.cart_deleteproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    int qty = Integer.valueOf(list.get(position).getQuantity());
                    int new_qty = qty - 1;
                    //TODO :SEND TO SOCKET Delet

                    SignallingClient.getInstance(mContext).RemoveFromCart(prefs.getString("login_id", ""),
                            String.valueOf(list.get(position).getProduct_id()));
                    list.remove(position);
                    cart_sumation.sumation(list);

                    notifyDataSetChanged();

                    DisplayVideoActivity.cart_product -= 1;

                    DisplayVideoActivity.cart_number.setText(String.valueOf(DisplayVideoActivity.cart_product));

                } catch (Exception e) {

                }
            }
        });

        holder.cart_newprice.setText(String.valueOf(Integer.valueOf(list.get(position).getQuantity()) * Integer.valueOf(list.get(position).getNew_price())));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class holder extends RecyclerView.ViewHolder {

    public ImageView cart_productimage, cart_deleteproduct;
    public TextView cart_productname, cart_productdescription,
            cart_sellername, _item_price, cart_q_number, cart_newprice, add, remove;

    public holder(@NonNull View itemView) {
        super(itemView);

        cart_productimage = itemView.findViewById(R.id.cart_productimage);
        cart_productname = itemView.findViewById(R.id.cart_productname);
        cart_productdescription = itemView.findViewById(R.id.cart_productdescription);
        cart_sellername = itemView.findViewById(R.id.cart_sellername);
        _item_price = itemView.findViewById(R.id._item_price);
        cart_q_number = itemView.findViewById(R.id.cart_q_number);
        cart_newprice = itemView.findViewById(R.id.cart_newprice);
        cart_deleteproduct = itemView.findViewById(R.id.cart_deleteproduct);
        add = itemView.findViewById(R.id.add);
        remove = itemView.findViewById(R.id.remove);
    }
}
