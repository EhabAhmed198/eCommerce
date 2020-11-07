package com.ehabahmed.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.buyers.HomeActivity;
import com.ehabahmed.ecommerce.buyers.MainActivity;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logoutBtn, checkOrdersBtn, maintainProductsBtn, checkApprovedNewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkApprovedNewProducts = findViewById(R.id.admin_check_approved_products_btn);
        checkOrdersBtn = (Button) findViewById(R.id.admin_check_orders_btn);
        maintainProductsBtn = findViewById(R.id.admin_maintain_btn);
        checkApprovedNewProducts.setOnClickListener(this);
        maintainProductsBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        checkOrdersBtn.setOnClickListener(this);
        Paper.init(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {

            case R.id.admin_logout_btn:
                Paper.book().destroy();
                intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.admin_check_orders_btn:
                intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
                break;
            case R.id.admin_maintain_btn:
                intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                break;

            case R.id.admin_check_approved_products_btn:

                    intent=new Intent(AdminHomeActivity.this,AdminCheckNewProductsActivity.class);
                    startActivity(intent);
                break;
        }
    }
}
