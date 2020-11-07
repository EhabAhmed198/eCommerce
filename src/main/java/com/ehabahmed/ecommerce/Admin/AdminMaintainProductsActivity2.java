package com.ehabahmed.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.model.Product;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.ehabahmed.ecommerce.sallers.SallerProducrCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminMaintainProductsActivity2 extends AppCompatActivity implements View.OnClickListener {
    private Button applyChangesBtn, deleteProductBtn;
    private EditText name, price, descript;
    private ImageView imageView;
    private String productId;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products2);

        productId = getIntent().getExtras().getString("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.PRODUCTS).child(productId);
        applyChangesBtn = findViewById(R.id.apllay_changes);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        descript = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        deleteProductBtn = findViewById(R.id.delete_product_btn);
        displaySpecificProductInfo();
        applyChangesBtn.setOnClickListener(this);
        deleteProductBtn.setOnClickListener(this);

    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Product product = snapshot.getValue(Product.class);
                    name.setText(product.getpName());
                    price.setText(product.getPrice());
                    descript.setText(product.getDescription());
                    Glide.with(AdminMaintainProductsActivity2.this).load(product.getImage()).into(imageView);

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
            case R.id.apllay_changes:

                applayChangeDataProdcut();
                break;
            case R.id.delete_product_btn:

                deleteProduct();
                break;
        }
    }

    private void deleteProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AdminMaintainProductsActivity2.this, "The Product is  deleted successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminMaintainProductsActivity2.this, SallerProducrCategoryActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void applayChangeDataProdcut() {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = descript.getText().toString();

        if (pName.equals("")) {

            Toast.makeText(this, "Write down Product Name.", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {

            Toast.makeText(this, "Write down Product Price.", Toast.LENGTH_SHORT).show();

        } else if (pDescription.equals("")) {

            Toast.makeText(this, "Write down product Description.", Toast.LENGTH_SHORT).show();

        } else {

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);
            productMap.put("pname", pName);
            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminMaintainProductsActivity2.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity2.this, SallerProducrCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });


        }


    }
}