package com.example.mobilebanking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

public class DocumentUpload extends AppCompatActivity {
    private static final int PICK_ID_IMAGE_CODE = 1;
    private static final int PICK_UTILITY_BILL_IMAGE_CODE = 2;
    private static final int REQUEST_IMAGE_CAPTURE_CODE = 3;
    private static final int REQUEST_CAMERA_PERMISSION_CODE = 4;

    ImageView selfieIv, idIv, utilityBillIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);

        selfieIv = findViewById(R.id.selfie_iv);
        idIv = findViewById(R.id.ID_iv);
        utilityBillIv = findViewById(R.id.utility_bill_iv);

        selfieIv.setOnClickListener(v -> {
            if (hasCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        idIv.setOnClickListener(v -> {
            openGallery(PICK_ID_IMAGE_CODE);
        });

        utilityBillIv.setOnClickListener(v -> {
            openGallery(PICK_UTILITY_BILL_IMAGE_CODE);
        });
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Result not OK", Toast.LENGTH_SHORT).show();
        }
        else {
            Uri imageUri = data.getData();
            if (requestCode == PICK_ID_IMAGE_CODE) {
                idIv.setColorFilter(null);
                idIv.setImageURI(imageUri);
            }
            else if (requestCode == PICK_UTILITY_BILL_IMAGE_CODE) {
                utilityBillIv.setColorFilter(null);
                utilityBillIv.setImageURI(imageUri);
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE_CODE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    selfieIv.setColorFilter(null);
                    selfieIv.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Camera Permission Needed")
                    .setMessage("This app needs the Camera permission to take pictures.")
                    .setPositiveButton("OK", (dialogInterface, i) ->
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION_CODE))
                    .create()
                    .show();
        } 
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Camera Permission Needed")
                            .setMessage("Please enable camera permission in app settings.")
                            .setPositiveButton("Go to Settings", (dialogInterface, i) -> {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            })
                            .create()
                            .show();
                } else {
                    Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}