package com.example.dodientu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodientu.R;
import com.example.dodientu.model.AdminProduct;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHodler> {
    private Context context;
    private List<AdminProduct> list;
    private AdminOfferAdapter.onLongClickListener longClickListener;
    private AdminOfferAdapter.onItemClickListener itemClickListener;

    public AdminProductAdapter(Context context, List<AdminProduct> list) {
        this.context = context;
        this.list = list;
    }

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onLongClickListener(int pos);
    }

    public void setLongClickListener(AdminOfferAdapter.onLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setItemClickListener(AdminOfferAdapter.onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void addList(List<AdminProduct> listProduct){
        list.clear();
        Collections.copy(list,listProduct);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdminProductAdapter.ProductViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_list,parent,false);

        return new ProductViewHodler(view,itemClickListener,longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductAdapter.ProductViewHodler holder, int position) {
        AdminProduct adminProduct=list.get(position);
        holder.image.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        Picasso.get().load(adminProduct.getImage()).centerCrop().fit().into(holder.image);
        holder.name.setText(adminProduct.getName());
        holder.category.setText("Category " + adminProduct.getCategory());
        holder.quantity.setText("Available Amounts: "+ adminProduct.getQuantity());
        holder.price.setText("Price:" +adminProduct.getPrice());

        if(adminProduct.getExpired().equalsIgnoreCase("null"))
            holder.expire.setVisibility(View.GONE);
        else holder.expire.setVisibility(View.VISIBLE);

            holder.expire.setText("Expiry Date: "+ adminProduct.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHodler extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name,category,quantity,price,expire;
        CardView cardView;
        public ProductViewHodler(@NonNull View itemView,final AdminOfferAdapter.onItemClickListener itemListener,final AdminOfferAdapter.onLongClickListener longClickListener) {
            super(itemView);

            image=itemView.findViewById(R.id.adapterProductImage);
            name=itemView.findViewById(R.id.adapterProductName);
            category=itemView.findViewById(R.id.adapterProductCategory);
            quantity=itemView.findViewById(R.id.adapterProductQuantity);
            price=itemView.findViewById(R.id.adapterProductExpire);
            cardView=itemView.findViewById(R.id.productCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            itemListener.onItemCLick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(longClickListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            longClickListener.onItemLongCLick(position);
                        }
                    }
                    return false;
                }

            });
        }
    }

}
