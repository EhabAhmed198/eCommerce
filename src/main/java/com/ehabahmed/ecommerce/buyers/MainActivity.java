package com.ehabahmed.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ehabahmed.ecommerce.Admin.AdminHomeActivity;
import com.ehabahmed.ecommerce.sallers.SallerProducrCategoryActivity;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.ehabahmed.ecommerce.model.Users;
import com.ehabahmed.ecommerce.sallers.SallerHomeActivity;
import com.ehabahmed.ecommerce.sallers.SallerRegisterationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sallerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = (Button) findViewById(R.id.main_join_now_bin);
        loginButton = (Button) findViewById(R.id.main_login_bin);
        sallerBegin = (TextView) findViewById(R.id.saller_become);
        sallerBegin.setOnClickListener(this);
        loadingBar = new ProgressDialog(this);
        loginButton.setOnClickListener(this);
        joinNowButton.setOnClickListener(this);
        Paper.init(this);
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordeKey = Paper.book().read(Prevalent.userPasswordKey);
        String typeKey = Paper.book().read(Prevalent.TYPE_KEY);
        if (userPhoneKey != "" && userPasswordeKey != "" && userPhoneKey != null & userPasswordeKey != null) {
            if (!userPhoneKey.isEmpty() && !userPasswordeKey.isEmpty()) {
                allowAcess(userPhoneKey, userPasswordeKey, typeKey);
                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please wait......");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }

        }


    }


    private void allowAcess(String userPhoneKey, String userPasswordeKey, String typekey) {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(typekey).child(userPhoneKey).exists()) {
                    Users usersData = snapshot.child(typekey).child(userPhoneKey).getValue(Users.class);
                    if (usersData.getPhone().equals(userPhoneKey)) {
                        if (usersData.getPassword().equals(userPasswordeKey)) {

                            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            if (typekey.equals("Users")) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                Prevalent.CURRENT_ONLINE_USER = usersData;
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else if (typekey.equals("Admins")) {
                                Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Account with this " + userPhoneKey + " number do not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            Intent intent=new Intent(MainActivity.this, SallerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.main_join_now_bin:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.main_login_bin:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.saller_become:
                intent=new Intent(MainActivity.this, SallerRegisterationActivity.class);
                startActivity(intent);
                break;
        }

    }
}