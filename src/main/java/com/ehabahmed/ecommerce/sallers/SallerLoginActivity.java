package com.ehabahmed.ecommerce.sallers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ehabahmed.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SallerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button registerSallerButton;
    private EditText emailEditText, passwordEditText;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saller_login);
        registerSallerButton = findViewById(R.id.saller_login_btn);
        emailEditText = findViewById(R.id.saller_login_email);
        passwordEditText = findViewById(R.id.saller_login_password);
        firebaseAuth = FirebaseAuth.getInstance();
        registerSallerButton.setOnClickListener(this);
        loadingBar = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.saller_login_btn:
                loginSaller();
                break;
        }
    }

    private void loginSaller() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty()) {
            emailEditText.setError("Please enter Email Address.");
        } else if (password.isEmpty()) {
            passwordEditText.setError("Please enter Password.");

        } else {
            loadingBar.setTitle("Saller Account Login");
            loadingBar.setMessage("Please wait,  we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SallerLoginActivity.this, SallerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }
}