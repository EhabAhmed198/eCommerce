package com.ehabahmed.ecommerce.buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.ehabahmed.ecommerce.Admin.AdminMaintainProductsActivity2;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.model.Product;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.ehabahmed.ecommerce.viewholder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    DrawerLayout drawer;
    FloatingActionButton floatingActionButton;
    View headerView;
    TextView userNameTextView;
    CircleImageView profileImageView;
    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().getString("Admin");
        }
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!type.equals("Admin")) {

                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);

                }

            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        productsRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.PRODUCTS);
        headerView = navigationView.getHeaderView(0);
        userNameTextView = (TextView) headerView.findViewById(R.id.user_profile_name);
        profileImageView = (CircleImageView) headerView.findViewById(R.id.user_profile_image);
        if (!type.equals("Admin")) {
            userNameTextView.setText(Prevalent.CURRENT_ONLINE_USER.getName());
            Glide.with(this).load(Prevalent.CURRENT_ONLINE_USER.getImage())
                    .placeholder(R.drawable.profile).into(profileImageView);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(productsRef.orderByChild("productState").equalTo("Approved"), Product.class).build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                holder.txtProductName.setText(model.getpName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price =" + model.getPrice() + " $");
                Glide.with(HomeActivity.this).load(model.getImage()).centerCrop().into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type.equals("Admin")) {

                            Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity2.class);
                            intent.putExtra("pid", model.getpId());
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getpId());
                            startActivity(intent);

                        }

                    }
                });


            }


            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_cart:
                if (!type.equals("Admin")) {
                    intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_search:
                if (!type.equals("Admin")) {
                    intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                    startActivity(intent);

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_categories:
                if (!type.equals("Admin")) {}
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_settings:
                if (!type.equals("Admin")) {
                    intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                if (!type.equals("Admin")) {
                    Paper.book().destroy();
                    intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
                drawer.closeDrawer(GravityCompat.START);
                return true;

        }
        return false;
    }


}