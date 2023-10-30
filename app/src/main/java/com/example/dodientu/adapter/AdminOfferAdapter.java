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
import com.example.dodientu.model.AdminOffers;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AdminOfferAdapter extends RecyclerView.Adapter<AdminOfferAdapter.ViewHolder> {
    private Context context;
    private List<AdminOffers> list;
    private onItemClickListener itemListener;
    private onLongClickListener longListener;

    public interface  onItemClickListener{
        void onItemCLick(int pos);
    }
    public interface onLongClickListener{
        void onItemLongCLick(int pos);
    }

    public void setItemListener(onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setLongListener(onLongClickListener longListener) {
        this.longListener = longListener;
    }

    public AdminOfferAdapter(Context context, List<AdminOffers> list) {
        this.context = context;
        this.list = list;
    }
    public void addList(List<AdminOffers> offers){
        list.clear();
        Collections.copy(list,offers);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdminOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_options_list,parent,false);

        return new ViewHolder(view,itemListener,longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOfferAdapter.ViewHolder holder, int position) {
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        holder.img.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));

        Picasso.get().load(list.get(position).getOfferImg()).centerCrop().fit().into(holder.img);
        holder.name.setText(list.get(position).getOfferName());
        holder.description.setText(list.get(position).getOfferDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,description;
        CardView cardView;
        public ViewHolder(@NonNull View itemView,final onItemClickListener itemListener,final onLongClickListener longClickListener) {
            super(itemView);
            img=itemView.findViewById(R.id.adapterOfferImage);
            name=itemView.findViewById(R.id.adapterOfferName);
            description=itemView.findViewById(R.id.adapterOfferDescripton);
            cardView=itemView.findViewById(R.id.offerCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){
                        int positiion=getAdapterPosition();
                        if(positiion!=RecyclerView.NO_POSITION){
                            itemListener.onItemCLick(positiion);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongCLick(position);
                        }
                    }
                    return false;
                }
            });

        }
    }
}
