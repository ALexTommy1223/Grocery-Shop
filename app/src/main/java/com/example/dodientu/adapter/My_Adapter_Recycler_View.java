package com.example.dodientu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodientu.R;
import com.example.dodientu.model.FavouritesClass;
import com.squareup.picasso.Picasso;

import java.util.List;

public class My_Adapter_Recycler_View extends RecyclerView.Adapter<My_Adapter_Recycler_View.ViewHolder> {
    private List<FavouritesClass> horizontalProductModelList;

    public My_Adapter_Recycler_View(List<FavouritesClass> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @NonNull
    @Override
    public My_Adapter_Recycler_View.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull My_Adapter_Recycler_View.ViewHolder holder, int position) {
        FavouritesClass model=horizontalProductModelList.get(position);
        holder.productTitle.setText(model.getProductTitle());
        holder.productPrice.setText(model.getProductPrice());
        Picasso.get().load(model.getProuductImage()).into(holder.productImage);
        holder.checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
    }

    @Override
    public int getItemCount() {
        return horizontalProductModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage,checkBox;
        TextView productTitle,productPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.item_image);
            checkBox=itemView.findViewById(R.id.check_box);

            productTitle=itemView.findViewById(R.id.item_title);
            productPrice=itemView.findViewById(R.id.item_price);
        }
    }
}
