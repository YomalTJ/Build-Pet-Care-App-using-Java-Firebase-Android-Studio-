package com.example.pet_care_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.pet_care_app.Activity.DetailActivity;
import com.example.pet_care_app.Domain.Pets;
import com.example.pet_care_app.R;

import java.util.ArrayList;

public class BestPetsAdapter extends RecyclerView.Adapter<BestPetsAdapter.viewholder>{
    ArrayList<Pets> items;
    Context context;

    public BestPetsAdapter(ArrayList<Pets> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BestPetsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_pets, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BestPetsAdapter.viewholder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.priceTxt.setText("$"+items.get(position).getPrice());
        holder.timeTxt.setText(items.get(position).getTimeValue()+" min");
        holder.starTxt.setText(""+items.get(position).getStar());

        Glide.with(context).load(items.get(position).getImagePath()).transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, starTxt, timeTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.txtTitle);
            priceTxt = itemView.findViewById(R.id.txtPrice);
            starTxt = itemView.findViewById(R.id.txtStar);
            timeTxt = itemView.findViewById(R.id.txtTime);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
