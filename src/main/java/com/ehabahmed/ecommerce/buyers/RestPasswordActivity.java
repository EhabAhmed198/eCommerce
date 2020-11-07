package com.ehabahmed.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RestPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestion;
    private EditText phoneNumber, question1, question2;
    private Button verfiy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);

        check = getIntent().getExtras().getString("check");
        titleQuestion = findViewById(R.id.title_question);
        pageTitle = findViewById(R.id.page_title);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verfiy = findViewById(R.id.verify_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        if (check.equals("settings")) {
            pageTitle.setText("Set Questions");
            titleQuestion.setText("Please set Answers for the Following Security Questions?");
            displayPreviousAnswer();
            phoneNumber.setVisibility(View.GONE);
            verfiy.setText("Set");
            verfiy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();

                }
            });

        } else if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);
            verfiy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verfiyUser();
                }
            });


        }
    }

    private void verfiyUser() {
        String phone = phoneNumber.getText().toString();
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.hasChild("Security Questions")) {

                        String ans1 = snapshot.child("Security Questions").child("answer1").getValue(String.class);
                        String ans2 = snapshot.child("Security Questions").child("answer2").getValue(String.class);
                        if (!ans1.equals(answer1)) {

                            Toast.makeText(RestPasswordActivity.this, "your 1st answer is wrong.", Toast.LENGTH_SHORT).show();
                        } else if (!ans2.equals(answer2)) {
                            Toast.makeText(RestPasswordActivity.this, "your 2st answer is wrong.", Toast.LENGTH_SHORT).show();

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(RestPasswordActivity.this);
                            builder.setTitle("New Password");
                            final EditText newPassword = new EditText(RestPasswordActivity.this);
                            newPassword.setHint("Write New Password here");
                            builder.setView(newPassword);
                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!newPassword.getText().toString().equals("")) {

                                        databaseReference.child("password")
                                                .setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RestPasswordActivity.this, "Password change Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(RestPasswordActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });
                            builder.show();


                        }

                    }
                } else {
                    Toast.makeText(RestPasswordActivity.this, "you have not set the security questions.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setAnswers() {
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();
        if (answer1.equals("") && answer2.equals("")) {
            Toast.makeText(RestPasswordActivity.this, "Please answer both questions.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.CURRENT_ONLINE_USER.getPhone());
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("answer1", answer1);
            dataMap.put("answer2", answer2);
            databaseReference.child("Security Questions").updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RestPasswordActivity.this, "you have set security Questions successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RestPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displayPreviousAnswer() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.CURRENT_ONLINE_USER.getPhone());
        databaseReference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String answer1 = snapshot.child("answer1").getValue(String.class);
                    String answer2 = snapshot.child("answer2").getValue(String.class);
                    question1.setText(answer1);
                    question2.setText(answer2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}