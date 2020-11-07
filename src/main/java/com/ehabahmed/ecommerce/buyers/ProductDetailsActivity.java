package com.ehabahmed.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.model.Product;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView productImage;
    private TextView productPrice, productDescription, productName;
    private ElegantNumberButton elegantNumberButton;
    private Button addToCartBtn;
    private String productId, state = "normal";
    private DatabaseReference rootProductsRef;
    private Product dataProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        productId = getIntent().getExtras().getString("pid");
        productImage = (ImageView) findViewById(R.id.product_image_details);

        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        addToCartBtn = (Button) findViewById(R.id.add_product_cart_btn);
        elegantNumberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        getProductDetails(productId);
        addToCartBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
    }

    private void getProductDetails(String productId) {

        rootProductsRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.PRODUCTS);
        rootProductsRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    dataProduct = snapshot.getValue(Product.class);
                    Glide.with(ProductDetailsActivity.this).load(dataProduct.getImage()).into(productImage);
                    productName.setText(dataProduct.getpName());
                    productDescription.setText(dataProduct.getDescription());
                    productPrice.setText(dataProduct.getPrice() + " $");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_product_cart_btn:

                if (state.equals("Order Placed") || state.equals("not shipped")) {
                     Toast.makeText(ProductDetailsActivity.this, "you can purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                } else {
                    addingToCartList();

                }
                break;
        }
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentData;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentData = currentData.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentData.format(calendar.getTime());


        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart List");
        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productId);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", dataProduct.getPrice());
        cartMap.put("data", saveCurrentData);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", elegantNumberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.CURRENT_ONLINE_USER.getPhone()).child("Products").child(productId)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    cartListRef.child("Admin View").child(Prevalent.CURRENT_ONLINE_USER.getPhone()).child("Products")
                            .child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Added to cart List", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    });

                }
            }
        });
    }

    private void checkOrderState() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CURRENT_ONLINE_USER.getPhone());
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue(String.class);
                    if (shippingState.equals("shipped")) {

                        state = "Order Shipped";
                    } else if (shippingState.equals("not shipped")) {

                        state = "Order Placed";

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}