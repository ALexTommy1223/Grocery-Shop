package com.example.dodientu.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dodientu.R;
import com.example.dodientu.model.Model;
import com.squareup.picasso.Picasso;

import java.util.List;

public class My_Adapter extends PagerAdapter {
    private Context context;
    private List<Model> modelList;
    private LayoutInflater layoutInflater;
    private String productName,productPrice,productImage,productnExpiryDate,productIsFavorite;

    public My_Adapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {// dùng để làm việc với adapter
       layoutInflater=LayoutInflater.from(context);
       View view=layoutInflater.inflate(R.layout.item,container,false);

        ImageView imageView;
        TextView title,desc;
        CardView offerCardContainer;

        offerCardContainer=view.findViewById(R.id.offerCardContainer);
        imageView=view.findViewById(R.id.contentImage);
        title=view.findViewById(R.id.contenttitle);
        desc=view.findViewById(R.id.contentDecs);

        Picasso.get().load(modelList.get(position).getImage()).into(imageView);

        title.setText(modelList.get(position).getTitle()+"Offer");
        desc.setText(modelList.get(position).getDecs());
        container.addView(view);
        offerCardContainer.setOnClickListener(view1 ->{
            productIsFavorite="false";
            getData(modelList.get(position).getTitle());
        });


       return view;
    }

    private void getData(String title) {

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
