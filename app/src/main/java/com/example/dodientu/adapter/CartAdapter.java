package com.example.dodientu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodientu.R;
import com.example.dodientu.model.CartItemModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter{

    private List<CartItemModel> cartItemModelsList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onDeletCLick(int positon);
        void updateTotalPrice(String str);
    }

    public CartAdapter(List<CartItemModel> cartItemModelsList) {
        this.cartItemModelsList = cartItemModelsList;
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cartItemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
        return new CartItemViewHoler(cartItemView,mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return cartItemModelsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelsList.get(position).getType()){
            case 0:
                return CartItemModel.cart_item;
            default:
                return -1;
        }
    }
    class CartItemViewHoler extends  RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productTitle;
        private TextView freecoupon,productPrice, cuttedPrice,offerApplied,couponApplied,productQuantity;
        private ImageView couponIcon,plusIcon,minusIcon,cartItemDelete;
        private boolean deletedItem=false;

        //
        int totalPriceval;
        DatabaseReference root;
        String currentUser;
        private FirebaseAuth mAuth;


        public CartItemViewHoler(@NonNull View itemView, final CartAdapter.OnItemClickListener mListener) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_title);

            freecoupon=itemView.findViewById(R.id.coupon_txt);
            productPrice=itemView.findViewById(R.id.cut_price);
            offerApplied=itemView.findViewById(R.id.offertxt);
            couponApplied=itemView.findViewById(R.id.couponApplied);
            productQuantity=itemView.findViewById(R.id.quan);
            couponIcon=itemView.findViewById(R.id.coupon);
            plusIcon=itemView.findViewById(R.id.plusIcon);
            minusIcon=itemView.findViewById(R.id.minusIcon);

            cartItemDelete=itemView.findViewById(R.id.cart_ItemDelete);
            root= FirebaseDatabase.getInstance().getReference();
            mAuth=FirebaseAuth.getInstance();
            currentUser=mAuth.getCurrentUser().getUid();

            cartItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }


}
