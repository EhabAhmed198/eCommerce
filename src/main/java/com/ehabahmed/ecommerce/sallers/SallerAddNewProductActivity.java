package com.ehabahmed.ecommerce.sallers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ehabahmed.ecommerce.R;
import com.ehabahmed.ecommerce.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SallerAddNewProductActivity extends AppCompatActivity implements View.OnClickListener {
    private String productName, productDescription, productPrice, categoryName, saveCurrentData, saveCurrentTime;
    private Button addNewProduct;
    private EditText inputProductName, inputProductDescription, inputProductPrice;
    private ImageView inputProductImage;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUri;
    private StorageReference productImageRef;
    private DatabaseReference rootRef,sallerReference;
    private ProgressDialog loadingBar;
  private String sName,sAddress,sPhone,sEmail,sId;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saller_add_new_product);
        categoryName = getIntent().getExtras().getString(Prevalent.CATEGORY);
        productImageRef = FirebaseStorage.getInstance().getReference().child(Prevalent.ProductImages).child(Prevalent.PRODUCTS);
        rootRef = FirebaseDatabase.getInstance().getReference();
        sallerReference = FirebaseDatabase.getInstance().getReference().child("Sallers");

        loadingBar = new ProgressDialog(this);
        addNewProduct = (Button) findViewById(R.id.add_new_product);
        inputProductName = (EditText) findViewById(R.id.product_name);
        inputProductDescription = (EditText) findViewById(R.id.product_description);
        inputProductPrice = (EditText) findViewById(R.id.product_price);
        inputProductImage = (ImageView) findViewById(R.id.select_product_image);

        inputProductImage.setOnClickListener(this);
        addNewProduct.setOnClickListener(this);

    sallerReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            if(snapshot.exists()){
                sName=snapshot.child("name").getValue(String.class);
                sEmail=snapshot.child("email").getValue(String.class);
                sPhone=snapshot.child("phone").getValue(String.class);
                sId=snapshot.child("sId").getValue(String.class);
                sAddress=snapshot.child("address").getValue(String.class);
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
            case R.id.select_product_image:

                openGallery();
                break;
            case R.id.add_new_product:
                loadingBar.setTitle("Add New Product");
                loadingBar.setMessage("Dear Saller, Please wait, while we are adding new product..");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                vaildDataProductData();
                break;

        }
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputProductImage.setImageURI(imageUri);

        }
    }

    private void vaildDataProductData() {

        productName = inputProductName.getText().toString();
        productDescription = inputProductDescription.getText().toString();
        productPrice = inputProductPrice.getText().toString();

        if (imageUri == null) {
            loadingBar.dismiss();
            Toast.makeText(this, "Product Image is madatory", Toast.LENGTH_SHORT).show();
        } else if (productDescription.isEmpty()) {
            loadingBar.dismiss();
            inputProductDescription.setError("Please write product description");

        } else if (productPrice.isEmpty()) {
            loadingBar.dismiss();
            inputProductPrice.setError("Please write product price");

        } else if (productName.isEmpty()) {
            loadingBar.dismiss();
            inputProductName.setError("Please write product name");

        } else {

            storeProductInformation();
        }

    }

    private void storeProductInformation() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentData = currentData.format(calendar.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentData + saveCurrentTime;

        StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        // upload file
        final UploadTask uploadTask = filePath.putFile(imageUri);
        // upload failure
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(SallerAddNewProductActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
            // upload scucess
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SallerAddNewProductActivity.this, "Product Image upload Successfully", Toast.LENGTH_SHORT).show();

                // get Uri of file
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            loadingBar.dismiss();
                            throw task.getException();
                        }
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadImageUri = uri.toString();
                                Toast.makeText(SallerAddNewProductActivity.this, "get Product Image url successfully", Toast.LENGTH_SHORT).show();
                                saveProductInfoToDatabase();
                            }
                        });


                        return filePath.getDownloadUrl();


                    }

                });
            }
        });


    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentData);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", productDescription);
        productMap.put("image", downloadImageUri);
        productMap.put("category", categoryName);
        productMap.put("price", productPrice);
        productMap.put("pname", productName);

        productMap.put("sallerName", sName);
        productMap.put("sallerAddress", sAddress);
        productMap.put("sallerPhone", sPhone);
        productMap.put("sallerEmail", sEmail);
        productMap.put("sId", sId);
        productMap.put("productState","Not Approved");

        rootRef.child("Products").child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(SallerAddNewProductActivity.this, "Error : " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(SallerAddNewProductActivity.this, SallerHomeActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(SallerAddNewProductActivity.this, "Product Added Scucessfully..", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sallerInformation(){

    }
}