package com.customer.shary.live.ui.search.chips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.shary.live.R;
import com.customer.shary.live.ui.search.searchkeyword.chipsclicked;
import com.customer.shary.live.ui.search.searchkeyword.data;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class chipsrec extends RecyclerView.Adapter<viewholder> {

    private ArrayList<data> data;
    private com.customer.shary.live.ui.search.searchkeyword.chipsclicked chipsclicked;
    public chipsrec(chipsclicked chipsclicked ,ArrayList<data> data) {
        this.data=data;
        this.chipsclicked=chipsclicked;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chips_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {


        holder.chip.setText(data.get(position).getCategories());
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipsclicked.chips(holder.chip.getText().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

class  viewholder extends RecyclerView.ViewHolder {

    Chip chip ;
    public viewholder(@NonNull View itemView) {
        super(itemView);
        chip=itemView.findViewById(R.id.chips);
    }
}
