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

import org.w3c.dom.Text;

public class Regestration_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regestration_page);


        TextView uname = findViewById(R.id.uname);
        TextView pass = findViewById(R.id.pass);
        TextView cpass = findViewById(R.id.cpass);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);
        Button bsignup = findViewById(R.id.bsignup);

        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String suname = uname.getText().toString().trim();
                String spass = pass.getText().toString().trim();
                String scpass = cpass.getText().toString().trim();
                String sphone = phone.getText().toString().trim();
                String semail = email.getText().toString().trim();

                if(suname.isEmpty()||spass.isEmpty()||scpass.isEmpty()||sphone.isEmpty()||semail.isEmpty())
                {
                    Toast.makeText(Regestration_page.this, "please enter full detail for registration", Toast.LENGTH_SHORT).show();
                } else if (!spass.equals(scpass)) {
                    Toast.makeText(Regestration_page.this, "password and confirm password does not matched", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Regestration_page.this, "Registration succusesfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Regestration_page.this, Login_page.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}