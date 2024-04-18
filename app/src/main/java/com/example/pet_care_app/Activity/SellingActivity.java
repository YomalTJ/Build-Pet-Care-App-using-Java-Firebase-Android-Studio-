package com.example.pet_care_app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pet_care_app.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SellingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-care-app-a9ed0-default-rtdb.firebaseio.com/");
        storageReference = FirebaseStorage.getInstance().getReference().child("sellingpets");

        EditText editTextName = findViewById(R.id.userEdt);
        EditText editTextAge = findViewById(R.id.editTextTextSPAge);
        EditText editTextBreed = findViewById(R.id.editTextTextSPBreed);
        EditText editTextColor = findViewById(R.id.editTextTextSPColor);
        EditText editTextGender = findViewById(R.id.editTextTextSPGender);
        EditText editTextLocation = findViewById(R.id.editTextTextSPLocation);

        Button buttonSelectImage = findViewById(R.id.buttonSPSelectImage);
        Button buttonAdd = findViewById(R.id.buttonSPAdd);
        imageView = findViewById(R.id.imageView19);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String breed = editTextBreed.getText().toString();
                String color = editTextColor.getText().toString();
                String gender = editTextGender.getText().toString();
                String location = editTextLocation.getText().toString();

                if (name.isEmpty() || age.isEmpty() || breed.isEmpty() || color.isEmpty() || gender.isEmpty() || location.isEmpty()) {
                    Toast.makeText(SellingActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> petInfo = new HashMap<>();
                petInfo.put("name", name);
                petInfo.put("age", age);
                petInfo.put("breed", breed);
                petInfo.put("color", color);
                petInfo.put("gender", gender);
                petInfo.put("location", location);

                String pushKey = databaseReference.child("selling_list").push().getKey();
                databaseReference.child("selling_list").child(pushKey).setValue(petInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SellingActivity.this, "successfully", Toast.LENGTH_SHORT).show();

                                    if (selectedImageUri != null) {
                                        uploadImage(pushKey, selectedImageUri);
                                    } else {
                                        showSuccessAndNavigateToHome();
                                    }
                                } else {
                                    Toast.makeText(SellingActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private Uri selectedImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void uploadImage(String pushKey, Uri imageUri) {
        StorageReference imageRef = storageReference.child(pushKey + ".jpg");

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageData = baos.toByteArray();

            imageRef.putBytes(imageData)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    databaseReference.child("selling_list").child(pushKey).child("imageUrl").setValue(downloadUrl.toString());

                                    Toast.makeText(SellingActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                                    showSuccessAndNavigateToHome();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellingActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSuccessAndNavigateToHome() {
        Toast.makeText(SellingActivity.this, "Pet added successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SellingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}