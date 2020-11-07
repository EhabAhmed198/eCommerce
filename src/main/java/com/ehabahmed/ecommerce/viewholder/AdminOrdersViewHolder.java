package com.ehabahmed.ecommerce.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.interfaces.ItemClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public   TextView userName,orderPhoneNumber,orderTotalPrice,orderDataTime,shippingAdress;
 public Button showOrders;
    private ItemClickListener listener;
    public AdminOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        userName=itemView.findViewById(R.id.order_user_name);
        orderPhoneNumber=itemView.findViewById(R.id.order_phone_number);
        orderTotalPrice=itemView.findViewById(R.id.order_total_price);
        shippingAdress=itemView.findViewById(R.id.order_address_city);
        orderDataTime=itemView.findViewById(R.id.order_data_time);
        showOrders=itemView.findViewById(R.id.show_all_product_btn);


    }

    public void  setItemClickListener(ItemClickListener listener){

        this.listener=listener;
    }

    @Override
    public void onClick(View v) {

    }
}
