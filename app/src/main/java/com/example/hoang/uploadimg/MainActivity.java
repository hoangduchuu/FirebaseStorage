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
    private static final String BASE_URL = "gs://newconsoleduchuu.appspot.com" ;
    private static final String TAG = "TAGER";
    private Button btnPhoto;
    private ImageView ivPhoto;
    FirebaseStorage storage = FirebaseStorage.getInstance();
     StorageReference storageRef = storage.getReferenceFromUrl(BASE_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageRef = storage.getReferenceFromUrl(BASE_URL);



        btnPhoto = (Button) findViewById(R.id.btnPh);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        setUpIv();
        clickSave();
    }

    private void clickSave() {
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference mountainsRef = storageRef.child("mountains.jpg" + Calendar.getInstance().getTimeInMillis());

                ivPhoto.setDrawingCacheEnabled(true);
                ivPhoto.buildDrawingCache();
                Bitmap bitmap = ivPhoto.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                });
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
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
