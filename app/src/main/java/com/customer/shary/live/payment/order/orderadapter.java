package com.customer.shary.live.payment.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.R;
import com.customer.shary.live.payment.order.ordermodel.Datum;
import com.customer.shary.live.payment.orderSteper.OrderSteper;

import java.util.ArrayList;

public class orderadapter extends RecyclerView.Adapter<viewholder> {

    private Context context;
    private ArrayList<Datum> data;

    public orderadapter(Context context, ArrayList<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(context).inflate(R.layout.my_orders_raw, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder._qty.setText(data.get(position).getQuantity());
        holder._price.setText(data.get(position).getPrice());
        holder._sellername.setText(data.get(position).getAddress());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OrderSteper.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

class viewholder extends RecyclerView.ViewHolder {


    TextView _sellername;
    TextView _price;
    TextView _qty;

    Button view;


    public viewholder(@NonNull View itemView) {
        super(itemView);
        _sellername = itemView.findViewById(R.id._sellername);
        _price = itemView.findViewById(R.id._price);
        _qty = itemView.findViewById(R.id._qty);
        view = itemView.findViewById(R.id._view);


    }


}