package com.example.dodientu.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.dodientu.R;
import com.example.dodientu.model.FavouritesClass;
import com.example.dodientu.model.HorizontalProductModel;
import com.example.dodientu.ui.ProductActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridLayoutAdapter extends BaseAdapter {
    List<HorizontalProductModel> horizontalProductModelList;
    List<FavouritesClass> favourites;
    ImageView productImage;
    TextView productTitle,productPrice;
    ImageView checkBox;
    ConstraintLayout container;
    Context context;

    public GridLayoutAdapter(List<HorizontalProductModel> horizontalProductModelList, List<FavouritesClass> favourites, Context context) {
        this.horizontalProductModelList = horizontalProductModelList;
        this.favourites = favourites;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView==null){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item,null);
            container=view.findViewById(R.id.mainProductId);
            productImage=view.findViewById(R.id.item_image);
            productTitle=view.findViewById(R.id.item_title);
            productPrice=view.findViewById(R.id.item_price);
            checkBox=view.findViewById(R.id.check_box);

            Picasso.get().load(horizontalProductModelList.get(position).getProductImage()).into(productImage);
            productPrice.setText(horizontalProductModelList.get(position).getProductTitle());
            boolean isFavorite=false;

            for (int i=0;i<favourites.size();i++){
                if(horizontalProductModelList.get(position).getProductTitle().equals(favourites.get(i).getProductTitle())){
                    isFavorite=true;
                    horizontalProductModelList.get(position).setChecked(true);
                    break;
                }
            }
            if(isFavorite){
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
            else {
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
            }


        }
        else{
            view=convertView;
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("favorites")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        ;
                HorizontalProductModel hz=horizontalProductModelList.get(position);

                if(!(horizontalProductModelList.get(position).isChecked())){
                    horizontalProductModelList.get(position).setChecked(true);
                    checkBox=v.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
                    ref.child(horizontalProductModelList.get(position).getProductTitle()).setValue(hz);
                }
                else{
                    horizontalProductModelList.get(position).setChecked(true);
                    checkBox=v.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    ref.child(horizontalProductModelList.get(position).getProductTitle()).setValue(null);
                }

            }
        });
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProductActivity.class);
                intent.putExtra("Product Name",horizontalProductModelList.get(position).getProductTitle());
                intent.putExtra("Product Price",horizontalProductModelList.get(position).getProductPrice());
                intent.putExtra("Product ExpiryDate",horizontalProductModelList.get(position).getExpiredDate());
                intent.putExtra("Product IsFavorite",horizontalProductModelList.get(position).isChecked());
                intent.putExtra("Is offerd","no");

                context.startActivity(intent);
            }
        });
        return view;
    }
}
