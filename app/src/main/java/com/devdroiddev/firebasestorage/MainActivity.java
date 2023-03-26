package com.devdroiddev.firebasestorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.devdroiddev.firebasestorage.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    private static final int IMG_REQUEST = 101;
    ActivityMainBinding binding;
    ImageView image;
    private Uri imgUri;
    private String fileName;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        image = binding.imageToLoad;
        Button uploadButton = binding.uploadButton;
        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading");
        dialog.setIcon(R.drawable.upload_file);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Method to use Image
                chooseImage();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri != null)
                {
                    fileName = binding.etFileName.getText().toString();
                    if (fileName.trim().length() == 0) {
                        fileName = "NoFileName";
                    }
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName + ".jpg");
        dialog.show();
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"Image Uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        long transferred = snapshot.getBytesTransferred();
                        long total = snapshot.getTotalByteCount();
                        long percentage = 100*transferred/total;
                        dialog.setMessage("Please wait while uploading:\n" + percentage + "%");
                    }
                });
    }

    /* This piece of code choose the image from the gallery */
    private void chooseImage() {
        Intent iImage = new Intent(Intent.ACTION_GET_CONTENT);
        iImage.setType("image/*");
        startActivityForResult(iImage,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK)
        {
            if (data!= null)
            {
                imgUri = data.getData();
                image.setImageURI(imgUri);
            }
        }
    }
}