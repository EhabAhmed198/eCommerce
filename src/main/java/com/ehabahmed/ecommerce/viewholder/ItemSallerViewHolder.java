package com.ehabahmed.ecommerce.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.interfaces.ItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ItemSallerViewHolder extends RecyclerView.ViewHolder {
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductStatus;
    public ImageView imageView;


    public ItemSallerViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_saller_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_saller_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_saller_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.produc_sallert_price);
        txtProductStatus=(TextView)itemView.findViewById(R.id.product_saller_state);


    }






}
