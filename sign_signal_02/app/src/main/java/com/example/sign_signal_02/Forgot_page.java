package com.example.sign_signal_02;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Forgot_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_page);

        TextView inputemail = findViewById(R.id.email);
        Button bforgot = findViewById(R.id.bforgot);
        Button bsignin = findViewById(R.id.bsignin);

        bforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputemail.getText().toString().trim();


                if (!email.isBlank()) {
                    Toast.makeText(Forgot_page.this, "otp sent to yor email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Forgot_page.this, "please enter email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgot_page.this, Login_page.class);
                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}