package com.ehabahmed.ecommerce.sallers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ehabahmed.ecommerce.Admin.AdminCheckNewProductsActivity;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.buyers.MainActivity;
import com.ehabahmed.ecommerce.model.Product;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.ehabahmed.ecommerce.viewholder.ItemSallerViewHolder;
import com.ehabahmed.ecommerce.viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SallerHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DatabaseReference unverifiedProductRef;
   private String sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saller_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.saller_home_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        unverifiedProductRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.PRODUCTS);

       sId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        navView.setOnNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.navigation_home:
                Intent intentHome = new Intent(SallerHomeActivity.this, SallerHomeActivity.class);
                startActivity(intentHome);
                break;

            case R.id.navigation_add:
                Intent intentProductCategory = new Intent(SallerHomeActivity.this, SallerProducrCategoryActivity.class);
                startActivity(intentProductCategory);
                break;

            case R.id.navigation_logout:
                final FirebaseAuth fitrbaseAuth = FirebaseAuth.getInstance();
                fitrbaseAuth.signOut();
                Intent intent = new Intent(SallerHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(unverifiedProductRef.orderByChild("sId").equalTo(sId), Product.class).build();
        FirebaseRecyclerAdapter<Product, ItemSallerViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ItemSallerViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemSallerViewHolder holder, int position, @NonNull Product model) {

                holder.txtProductName.setText(model.getpName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice() + " $");
                holder.txtProductStatus.setText("State : "+model.getProductState());
                Glide.with(SallerHomeActivity.this).load(model.getImage()).centerCrop().into(holder.imageView);
                final Product itemClick = model;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productId = itemClick.getpId();
                        CharSequence options[] = new CharSequence[]{

                                "Yes",
                                "No"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(SallerHomeActivity.this);
                        builder.setTitle("Do you want to Delete this Product. Are you Sure?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {

                                    deleteItemProduct(productId);
                                } else if (which == 1) {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ItemSallerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saller_item_view, parent, false);
                ItemSallerViewHolder holder = new ItemSallerViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteItemProduct(String productId) {

        unverifiedProductRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SallerHomeActivity.this, "That item has been Deleted Successfully. ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}