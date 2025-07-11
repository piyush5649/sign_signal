package com.example.sign_signal_02;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button bsld, bslg, bsa;
    ImageView bmenu, bnotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bmenu = findViewById(R.id.bmenu);
        bnotify = findViewById(R.id.bnotify);
        bsld = findViewById(R.id.bsld);
        bsa = findViewById(R.id.bsa);
        bslg = findViewById(R.id.bslg);

        bmenu.setOnClickListener(view -> {
            Toast.makeText(this, "menu will be coming soon...", Toast.LENGTH_SHORT).show();
        });


        bnotify.setOnClickListener(view -> {
            Toast.makeText(this, "No notifications....", Toast.LENGTH_SHORT).show();
        });

        bsld.setOnClickListener(view -> {
            Toast.makeText(this, "sign language detection is opened.....", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SLG_PAGE.class);
            startActivity(intent);
        });

        bsa.setOnClickListener(view -> {
            Toast.makeText(this, "work in progres...", Toast.LENGTH_SHORT).show();
        });

        bslg.setOnClickListener(view -> {

            Toast.makeText(this, "sign language genration is in progress...", Toast.LENGTH_SHORT).show();


        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}