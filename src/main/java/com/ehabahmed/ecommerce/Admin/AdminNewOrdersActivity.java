package com.ehabahmed.ecommerce.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.model.AdminOrders;
import com.ehabahmed.ecommerce.viewholder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList = findViewById(R.id.order_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(orderRef, AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.userName.setText("Name: " + model.getName());
                holder.orderPhoneNumber.setText("Phone: " + model.getPhone());
                holder.orderTotalPrice.setText("TotalAmount= $ " + model.getTotalAmount());
                holder.orderDataTime.setText("Order at: " + model.getData() + " " + model.getTime());
                holder.shippingAdress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());
                holder.showOrders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        String uId = getRef(position).getKey();
                        intent.putExtra("uid", uId);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        alertDialog.setTitle("Have you shipped this order products?");
                        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    String uId=getRef(position).getKey();
                                    removerOrder(uId);

                                } else if (which == 1) {
                                    finish();
                                }
                            }
                        });
                        alertDialog.show();
                    }

                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdminOrdersViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false));
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removerOrder(String uId) {
        orderRef.child(uId).removeValue();
    }


}