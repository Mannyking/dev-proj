package com.example.mobilebanking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.mobilebanking.interfaces.UserLevelInit;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity_DEPRECATED extends AppCompatActivity implements UserLevelInit {
    ImageButton imageButton;
    ImageButton imageButton2, documentationIv;
    View rootLayout, toastView;
    BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton qrCodeFAB;
    TextView nameTv;
    String levelType = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        count.start();
        imageButton = findViewById(R.id.imageButton);
        imageButton2 = findViewById(R.id.pdf_iv);
        toastView = getLayoutInflater().inflate(R.layout.toast_customization, findViewById(R.id.toast_customization_parent));
        rootLayout = findViewById(R.id.root_layout);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);
        View includedLayout = findViewById(R.id.includedLayout);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        qrCodeFAB = findViewById(R.id.qrCodeFAB);
        documentationIv = findViewById(R.id.documentation_iv);
        nameTv = findViewById(R.id.name_tv);


        TextView text = toastView.findViewById(R.id.text);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_box, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_DEPRECATED.this);
                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ImageView imageView = view.findViewById(R.id.animated_checkmark);
                imageView.setVisibility(View.VISIBLE);

                Button btn_ok = view.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(toastView);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
//                        Toast.makeText(MainActivity_DEPRECATED.this, "Toast", Toast.LENGTH_SHORT).show();
                    }
                });

                Button btn_cancel = view.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_DEPRECATED.this, PdfGenerator.class);
                startActivity(intent);
            }
        });

        documentationIv.setOnClickListener(v ->  {
            startActivity(new Intent(MainActivity_DEPRECATED.this, DocumentUpload.class));
            CoroutineMomentKt.main();
            CoroutineMomentKt.secondRequest();
            nameTv.setText(CoroutineMomentKt.getJsonValue());
        });


        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick(); // Simulate a click event
                count.cancel();
                count.start();
            }
            return false;
        });

//        nestedScrollView.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.performClick(); // Simulate a click event
//                count.cancel();
//                count.start();
//            }
//            return false;
//        });
//
//        includedLayout.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.performClick(); // Simulate a click event
//                count.cancel();
//                count.start();
//            }
//            return false;
//        });

        bottomAppBar.setOnTouchListener((v, event) -> {
            count.cancel();
            count.start();
            return false;
        });

        qrCodeFAB.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick(); // Simulate a click event
                count.cancel();
                count.start();
            }
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CoroutineMomentKt.main();
        CoroutineMomentKt.secondRequest();
    }

    CountDownTimer count = new CountDownTimer(10 * 10 * 10 * 10, 100000) {
        @Override
        public void onTick(long millisUntilFinished) {
            System.out.println("ticking");
        }

        @Override
        public void onFinish() {
            Toast.makeText(MainActivity_DEPRECATED.this, "The idle works", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public int hasAccessLevel(@NonNull String levelType, int levelCode) {
        this.levelType = levelType;
        return levelCode;
    }

    @NonNull
    @Override
    public String getUserLevel() {
        return levelType;
    }
}
