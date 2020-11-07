package com.ehabahmed.ecommerce.viewholder;

import android.view.View;
import android.widget.TextView;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.interfaces.ItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

    public   TextView productName,productQuantity,productPrice;
    private ItemClickListener listener;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productName=itemView.findViewById(R.id.cart_product_name);
        productQuantity=itemView.findViewById(R.id.cart_product_quantity);
        productPrice=itemView.findViewById(R.id.cart_product_price);
    }

  public void  setItemClickListener(ItemClickListener listener){

        this.listener=listener;
  }
    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
