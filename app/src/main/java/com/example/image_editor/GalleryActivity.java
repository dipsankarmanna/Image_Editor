package com.example.image_editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.image_editor.databinding.ActivityGalleryBinding;

import java.net.URI;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    ActivityGalleryBinding binding;
    GalleryAdapter galleryAdapter;
    List<String> images;

    private static final int PERMISSION_CODE=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        photo();
        getSupportActionBar().hide();
    }

    private void photo() {
        if (ContextCompat.checkSelfPermission(GalleryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GalleryActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_CODE);
        }else {
            try {
                loadImage();
            } catch (Exception e) {
                Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadImage() {
        binding.recyler.setHasFixedSize(true);
        binding.recyler.setLayoutManager(new GridLayoutManager(this,4));
        images=ImagesGallery.imagelist(this);
        galleryAdapter=new GalleryAdapter(this,images,new GalleryAdapter.PhotoListener(){
            @Override
            public void onPhotoClick(String path) {
                /*Intent intent = new Intent(GalleryActivity.this, ResultActivity.class);
                intent.setData(Uri.parse(path));
                startActivity(intent);*/
                edit(Uri.parse(path));
            }
        });
        binding.recyler.setAdapter(galleryAdapter);
        binding.galleryNumber.setText("Photos ("+images.size()+")");
    }

    private void edit(Uri uri) {
        Intent intent = new Intent(GalleryActivity.this, ResultActivity.class);
        intent.setData(uri);
        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_CODE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                loadImage();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}