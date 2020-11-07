package com.ehabahmed.ecommerce.buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView profileImageView;
    private EditText fullNameEdittext, userPhoneEdittext, addressEdittext, passwordEdittext;
    private TextView profileChangeText, closeTextview, saveTextView;
    private Uri imageUri;
    private String myUri;
    private StorageReference storageProfilePictures;
    private String checker = "";
    public static final int read_writePermission = 1;
    private UploadTask uploadTask;
    private Button securityQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageProfilePictures = FirebaseStorage.getInstance().getReference().child(Prevalent.PROFILE_IMAGES);
        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEdittext = (EditText) findViewById(R.id.settings_full_name);
        userPhoneEdittext = (EditText) findViewById(R.id.settings_phone_number);
        addressEdittext = (EditText) findViewById(R.id.settings_address);
        passwordEdittext = (EditText) findViewById(R.id.settings_password);
        securityQuestion = findViewById(R.id.security_qustion_btn);
        profileChangeText = (TextView) findViewById(R.id.profile_image_change);
        closeTextview = (TextView) findViewById(R.id.close_settings);
        saveTextView = (TextView) findViewById(R.id.update_settings);
     securityQuestion.setOnClickListener(this);
        userInfoDisplay(profileImageView, fullNameEdittext, userPhoneEdittext, addressEdittext, passwordEdittext);
        closeTextview.setOnClickListener(this);
        saveTextView.setOnClickListener(this);
        profileChangeText.setOnClickListener(this);

    }

    private void userInfoDisplay(CircleImageView profileImageView, EditText fullNameEdittext, EditText userPhoneEdittext, EditText addressEdittext, EditText passwordEdittext) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(Prevalent.USERS).child(Prevalent.CURRENT_ONLINE_USER.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (snapshot.child("image").exists()) {

                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();
                        Glide.with(SettingsActivity.this).load(image).into(profileImageView);
                        fullNameEdittext.setText(name);
                        userPhoneEdittext.setText(phone);
                        addressEdittext.setText(address);
                        passwordEdittext.setText(password);
                    } else if (snapshot.child("address").exists()) {
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();
                        fullNameEdittext.setText(name);
                        userPhoneEdittext.setText(phone);
                        addressEdittext.setText(address);
                        passwordEdittext.setText(password);


                    } else {
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("phone").getValue().toString();
                        String password = snapshot.child("password").getValue().toString();
                        fullNameEdittext.setText(name);
                        userPhoneEdittext.setText(phone);
                        passwordEdittext.setText(password);


                    }

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
            case R.id.close_settings:
                finish();
                break;

            case R.id.update_settings:

                if (checker.equals("clicked")) {

                    userInfoSaved(); // All Data(image+phone+password+address)
                } else {

                    upadteOnlyUsrInfo();   // Data(phone+password+address)
                }

                break;

            case R.id.profile_image_change:
                setPermissions();
                checker = "clicked";

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
                break;
            case R.id.security_qustion_btn:

                Intent intent=new Intent(SettingsActivity.this, RestPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);
                break;

        }
    }


    private void upadteOnlyUsrInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Prevalent.USERS);
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("name", fullNameEdittext.getText().toString());
        dataMap.put("phone", userPhoneEdittext.getText().toString());
        dataMap.put("address", addressEdittext.getText().toString());
        dataMap.put("password", passwordEdittext.getText().toString());
        reference.child(Prevalent.CURRENT_ONLINE_USER.getPhone()).updateChildren(dataMap);
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile Info Updating Scuccessfully..", Toast.LENGTH_SHORT).show();
        finish();


    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEdittext.getText().toString())) {

            Toast.makeText(this, "Name is mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEdittext.getText().toString())) {

            Toast.makeText(this, "Name is Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userPhoneEdittext.getText().toString())) {

            Toast.makeText(this, "Name is Phone", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")) {

            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePictures.child(Prevalent.CURRENT_ONLINE_USER.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);


            // get Uri of Upload file
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    myUri = uri.toString();

                }
            });


            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Prevalent.USERS);
                    HashMap<String, Object> dataMap = new HashMap<>();
                    dataMap.put("name", fullNameEdittext.getText().toString());
                    dataMap.put("phone", userPhoneEdittext.getText().toString());
                    dataMap.put("address", addressEdittext.getText().toString());
                    dataMap.put("password", passwordEdittext.getText().toString());
                    dataMap.put("image", myUri);

                    reference.child(Prevalent.CURRENT_ONLINE_USER.getPhone()).updateChildren(dataMap);

                    progressDialog.dismiss();

                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                    Toast.makeText(SettingsActivity.this, "Profile Info Updating Scuccessfully..", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {

            Toast.makeText(this, "image is not selected", Toast.LENGTH_SHORT).show();
        }


    }


    private void setPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, read_writePermission);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
        }
    }
}