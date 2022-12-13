package com.example.image_editor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.image_editor.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SliderAdapterExample adapter;
    SliderItem item1, item2, item3;
    List<SliderItem> list;
    private long Timeback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest=new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        slide();
        animate();


        binding.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings();
            }
        });
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.edit.setBackgroundResource(R.drawable.btn_design11);
                binding.camerabtn.setBackgroundResource(R.drawable.btn_design2);
                select_image();
            }
        });
        binding.camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.camerabtn.setBackgroundResource(R.drawable.btn_design12);
                binding.edit.setBackgroundResource(R.drawable.btn_design1);
                open_camera();
            }
        });

    }

    private void settings() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_design);
        bottomSheetDialog.setDismissWithAnimation(true);

        bottomSheetDialog.findViewById(R.id.cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_camera();
            }
        });
        bottomSheetDialog.findViewById(R.id.cancelbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.findViewById(R.id.gal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,GalleryActivity.class));
            }
        });
        bottomSheetDialog.findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Still working", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.findViewById(R.id.devloper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendevloper();
            }
        });

        bottomSheetDialog.show();
    }

    private void opendevloper() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.developer_design);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.show();
    }

    private void open_camera() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 5);
        } else {
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraintent, 10);
        }
    }

    private void select_image() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data.getData() != null) {
                Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(data.getData());
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Pixit");
                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, 200);
            }
        }
        if (requestCode == 200) {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        }
        if (requestCode == 10) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageurl(photo);
            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Pixit");
            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

            startActivityForResult(dsPhotoEditorIntent, 200);
        }
    }

    public Uri getImageurl(Bitmap bitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", "Description");
        return Uri.parse(path);
    }

    private void animate() {
        binding.Heading.setTranslationX(-800);
        binding.Heading.setAlpha(1);
        binding.Heading.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
        binding.subtitle.setTranslationX(-800);
        binding.subtitle.setAlpha(1);
        binding.subtitle.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(600).start();
        binding.edit.setTranslationX(-800);
        binding.edit.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(500).start();
        binding.camerabtn.setTranslationX(800);
        binding.camerabtn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(500).start();
        binding.settings.setTranslationX(800);
        binding.settings.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(500).start();
        binding.slider.setTranslationY(700);
        binding.slider.setAlpha(1);
        binding.slider.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(350).start();
    }

    private void slide() {
        list = new ArrayList<>();
        list.add(new SliderItem(MainActivity.this, R.drawable.picback));
        list.add(new SliderItem(MainActivity.this, R.drawable.pic2));
        list.add(new SliderItem(MainActivity.this, R.drawable.pic3));
        list.add(new SliderItem(MainActivity.this, R.drawable.picback4));
        list.add(new SliderItem(MainActivity.this, R.drawable.picback5));
        list.add(new SliderItem(MainActivity.this, R.drawable.pic6));
        adapter = new SliderAdapterExample(list);
        binding.slider.setSliderAdapter(adapter);
        binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        binding.slider.setScrollTimeInSec(2);
        binding.slider.setAutoCycle(true);
        binding.slider.startAutoCycle();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - Timeback > 1500) {
            Timeback = System.currentTimeMillis();
            Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        super.onBackPressed();
    }
}