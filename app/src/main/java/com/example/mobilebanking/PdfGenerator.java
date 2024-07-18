package com.example.mobilebanking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


public class PdfGenerator extends AppCompatActivity {
    Button pdfButton;
    private static final int REQUEST_CODE_SAVE_PDF = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_generator);

        pdfButton = findViewById(R.id.pdfButton);

        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PdfGenerator", "pdfButton clicked!");
                createPdfAndPrint();
            }
        });
    }

    private void createPdfAndPrint() {
        // Inflate the XML layout
        View contentView = LayoutInflater.from(this).inflate(R.layout.receipt_output, null);

        // Create the PrintDocumentAdapter
        ReceiptPrintAdapter printAdapter = new ReceiptPrintAdapter(this, contentView);

        // Create a print job with the PrintManager
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        String jobName = getString(R.string.app_name) + " Receipt";
        printManager.print(jobName, printAdapter, null);
    }

    private void requestExternalStoragePermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_SAVE_PDF);
            } else {
                openFilePicker();
            }
        } else {
            openFilePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_SAVE_PDF) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the action that required the permission
                openFilePicker();
            } else {
                // Permission denied, handle this case (e.g., show a message or take appropriate action)
                Toast.makeText(this, "Storage permission required to save PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFilePicker(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "MyPdfFile.pdf");
        startActivityForResult(intent, REQUEST_CODE_SAVE_PDF);
        Log.d("OpenFilePicker", "openFilePicker works");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_PDF && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
            }
        }
    }
}