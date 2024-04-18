package com.example.pet_care_app.Adapter;

import android.content.Context;
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
import com.example.pet_care_app.Domain.Pets;
import com.example.pet_care_app.Helper.ChangeNumberItemsListener;
import com.example.pet_care_app.Helper.ManagementCart;
import com.example.pet_care_app.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Pets> list;
    private ManagementCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<Pets> list, Context context, ChangeNumberItemsListener changeNumberItemsListener){
        this.list = list;
        managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }
    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.feeEachItem.setText("$" + (list.get(position).getNumberInCart() * list.get(position).getPrice()));
        holder.totalEachItem.setText(list.get(position).getNumberInCart() + "*$" +(
                list.get(position).getPrice()));
        holder.num.setText(list.get(position).getNumberInCart()+ "");

        Glide.with(holder.itemView.getContext())
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.plusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.minusNumberItem(list, position, new ChangeNumberItemsListener() {
                    @Override
                    public void change() {
                        notifyDataSetChanged();
                        changeNumberItemsListener.change();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            pic = itemView.findViewById(R.id.pic);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
