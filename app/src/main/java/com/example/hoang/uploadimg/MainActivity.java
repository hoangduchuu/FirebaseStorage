package com.example.hoang.uploadimg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final int REQUES_CODE = 999 ;
    private StorageReference mStorageRef;
    private Button btnPhoto;
    private ImageView ivPhoto;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mStorageRef = storage.getReferenceFromUrl("gs://newconsoleduchuu.appspot.com");

        btnPhoto = (Button) findViewById(R.id.btnPh);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        setUpIv();
        setUpclick();
    }

    private void setUpclick() {
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = mStorageRef.child("Image"+ calendar.getTimeInMillis());

// Get the data from an ImageView as bytes
                ivPhoto.setDrawingCacheEnabled(true);
                ivPhoto.buildDrawingCache();
                Bitmap bitmap = ivPhoto.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this, "abc" + downloadUrl, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setUpIv() {
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUES_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUES_CODE  &&  resultCode == RESULT_OK && data != null){
            Bitmap  bitmap = (Bitmap) data.getExtras().get("data");
            ivPhoto.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
