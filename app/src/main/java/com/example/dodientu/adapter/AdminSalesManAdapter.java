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
import com.example.dodientu.model.AdminSalesMan;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdminSalesManAdapter extends RecyclerView.Adapter<AdminSalesManAdapter.SalesManViewHolder> {
    private Context context;
    private List<AdminSalesMan> list;
    private AdminOfferAdapter.onItemClickListener itemListener;
    private AdminOfferAdapter.onLongClickListener longListener;

    public AdminSalesManAdapter(Context context, List<AdminSalesMan> list) {
        this.context = context;
        this.list = list;
    }

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onLongClickListener(int pos);
    }

    public void setItemListener(AdminOfferAdapter.onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setLongListener(AdminOfferAdapter.onLongClickListener longListener) {
        this.longListener = longListener;
    }

    public void addList(List<AdminSalesMan> listMan){
        list.clear();
        Collections.copy(list,listMan);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdminSalesManAdapter.SalesManViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_options_list,parent,false);
        return new SalesManViewHolder(view ,itemListener,longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminSalesManAdapter.SalesManViewHolder holder, int position) {
        AdminSalesMan salesMan=list.get(position);
        holder.image.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));
        Picasso.get().load(salesMan.getImg()).centerCrop().fit().into(holder.image);
        holder.name.setText("Name: "+salesMan.getName());
        holder.salary.setText("Salary "+salesMan.getSalary()+ "USD");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SalesManViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name,salary;
        CardView cardView;
        public SalesManViewHolder(@NonNull View itemView, final AdminOfferAdapter.onItemClickListener itemListener,final AdminOfferAdapter.onLongClickListener longListener) {
            super(itemView);
            image=itemView.findViewById(R.id.adapterSalesManImage);
            name=itemView.findViewById(R.id.adapterSalesManName);
            salary=itemView.findViewById(R.id.adapterSalesManSalary);
            cardView=itemView.findViewById(R.id.salesManCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListener!=null){
                        int positon=getAdapterPosition();
                        if(positon!=RecyclerView.NO_POSITION){
                            itemListener.onItemCLick(positon);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(longListener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            longListener.onItemLongCLick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
