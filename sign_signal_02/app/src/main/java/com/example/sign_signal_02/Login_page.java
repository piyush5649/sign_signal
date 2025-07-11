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

public class Login_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        TextView uname = findViewById(R.id.uname);
        TextView pass = findViewById(R.id.pass);
        Button bforgot = findViewById(R.id.bforgot);
        Button bsignin = findViewById(R.id.bsignin);
        Button bsignup = findViewById(R.id.bsignup);

        bsignin.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           String suname = uname.getText().toString().trim();
                                           String spass = pass.getText().toString().trim();

                                           if(suname.isEmpty() || spass.isEmpty())
                                           {
                                               Toast.makeText(Login_page.this, "please enter credential", Toast.LENGTH_SHORT).show();
                                           }else {
                                               validateuser(suname, spass);
                                           }
                                       }
                                   });

        bforgot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                Toast.makeText(Login_page.this, "forgot password clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_page.this, Forgot_page.class);
                startActivity(intent);

            }
        });

        bsignup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(Login_page.this, "sign up for new user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login_page.this, Regestration_page.class);
                startActivity(intent);
            }
        });




                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
    }
    public void validateuser(String username,String password){

        if(username.equals("admin") && password.equals("1234"))
        {
            Intent intent = new Intent(Login_page.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "please enter valid credentiall", Toast.LENGTH_SHORT).show();
        }

    }
}