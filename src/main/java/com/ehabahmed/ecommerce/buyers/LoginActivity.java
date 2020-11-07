package com.ehabahmed.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehabahmed.ecommerce.Admin.AdminHomeActivity;
import com.ehabahmed.ecommerce.sallers.SallerProducrCategoryActivity;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.ehabahmed.ecommerce.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private EditText inputPhone, inputPassword;
    private ProgressDialog loadingBar;
    private CheckBox rememberMe;
    TextView adminLink, notAdminLink,forgetPassword;
    String parentDbNme = Prevalent.USERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login_bin);
        inputPhone = (EditText) findViewById(R.id.login_phone_number_input);
        inputPassword = (EditText) findViewById(R.id.login_password_input_inner);
        forgetPassword=findViewById(R.id.forget_password);
        forgetPassword.setOnClickListener(this);
        rememberMe = (CheckBox) findViewById(R.id.remember_me_password);
        adminLink = (TextView) findViewById(R.id.admin_penal_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_penal_link);
        adminLink.setOnClickListener(this);
        notAdminLink.setOnClickListener(this);
        loadingBar = new ProgressDialog(this);
        Paper.init(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.login_bin:
                loginAccount();
                break;

            case R.id.admin_penal_link:
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                login.setText("Login Admin");
                parentDbNme = Prevalent.AdMINS;
                break;

            case R.id.not_admin_penal_link:
                notAdminLink.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                login.setText("Login");
                parentDbNme = Prevalent.USERS;
                break;
            case R.id.forget_password:

                Intent intent=new Intent(LoginActivity.this, RestPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
                break;
        }

    }

    private void loginAccount() {
        String phone = inputPhone.getText().toString();
        String password = inputPassword.getText().toString();
        if (phone.isEmpty()) {
            inputPhone.setError("please write phone number..");
        } else if (password.isEmpty()) {
            inputPassword.setError("please write password..");
        } else {


            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (rememberMe.isChecked()) {
                Paper.book().write(Prevalent.userPhoneKey, phone);
                Paper.book().write(Prevalent.userPasswordKey, password);
                Paper.book().write(Prevalent.TYPE_KEY,parentDbNme);
            }
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(parentDbNme).child(phone).exists()) {
                        Users usersData = snapshot.child(parentDbNme).child(phone).getValue(Users.class);
                        if (usersData.getPhone().equals(phone)) {
                            if (usersData.getPassword().equals(password)) {

                                if (parentDbNme.equals(Prevalent.AdMINS)) {
                                    Toast.makeText(LoginActivity.this, "Welcome Admin,you are logged in successfuly..", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    startActivity(intent);
                                } else if (parentDbNme.equals(Prevalent.USERS)) {
                                    Toast.makeText(LoginActivity.this, "logged in successfuly..", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                   Prevalent.CURRENT_ONLINE_USER=usersData;
                                    startActivity(intent);


                                }


                            } else {
                                loadingBar.dismiss();
                                inputPassword.setError("Password is incorrect");
                            }
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exist", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}