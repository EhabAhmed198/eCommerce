package com.ehabahmed.ecommerce.sallers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.buyers.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SallerRegisterationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sallerLoginBeign, registerButton;
    private EditText nameInput, phoneInput, emailInput, passwordInput, addressInput;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saller_registeration);
        sallerLoginBeign = findViewById(R.id.saller_already_have_account_btn);
        sallerLoginBeign.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);
        registerButton = (Button) findViewById(R.id.saller_registeration_btn);
        nameInput = (EditText) findViewById(R.id.saller_name);
        phoneInput = (EditText) findViewById(R.id.saller_phone);
        emailInput = (EditText) findViewById(R.id.saller_email);
        passwordInput = (EditText) findViewById(R.id.saller_password);
        addressInput = (EditText) findViewById(R.id.saller_address);
        sallerLoginBeign = (Button) findViewById(R.id.saller_registeration_btn);
        sallerLoginBeign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.saller_already_have_account_btn:
                Intent intent = new Intent(SallerRegisterationActivity.this, SallerLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.saller_registeration_btn:

                registerSaller();
                break;
        }
    }

    private void registerSaller() {
        String name = nameInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String address = addressInput.getText().toString();

        if (name.isEmpty()) {
            nameInput.setError("Please enter Name");

        } else if (phone.isEmpty()) {
            nameInput.setError("please enter Phone Number");
        } else if (email.isEmpty()) {
            nameInput.setError("Please enter Email Address");

        } else if (password.isEmpty()) {
            nameInput.setError("Please enter Password");

        } else if (address.isEmpty()) {
            nameInput.setError("Please enter Address");

        } else {
            loadingBar.setTitle("Creating Saller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                                String sId=firebaseAuth.getCurrentUser().getUid();
                                HashMap<String,Object> sallerMap=new HashMap<>();
                                sallerMap.put("sId",sId);
                                sallerMap.put("phone",phone);
                                sallerMap.put("email",email);
                                sallerMap.put("address",address);
                                sallerMap.put("name",name);
                                databaseReference.child("Sallers").child(sId).updateChildren(sallerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){

                                               loadingBar.dismiss();
                                               Toast.makeText(SallerRegisterationActivity.this, "you are Registered Successfully.", Toast.LENGTH_SHORT).show();
                                               Intent intent=new Intent(SallerRegisterationActivity.this, SallerHomeActivity.class);
                                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                               startActivity(intent);
                                               finish();

                                           }
                                            }
                                        });



                            }
                        }
                    });

        }

    }
}