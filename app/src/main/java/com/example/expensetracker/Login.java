package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Button register, login;
    private TextView Email, Password;
    private FirebaseAuth mAuth;
    private ProgressBar pg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        register = findViewById(R.id.button2);
        login = findViewById(R.id.button1);
        mAuth = FirebaseAuth.getInstance();
        pg1 = findViewById(R.id.progressbar);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code goes here
                loginuser();
            }
        });
    }

    private void loginuser() {

        //User inputs
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        //Checking whether empty or not
        if(email.isEmpty())
        {
            Email.setError("Enter an email address");
            Email.requestFocus();
            return;
        }

        pg1.setVisibility(View.VISIBLE);

        if(password.isEmpty())
        {
            Password.setError("Enter a password");
            Password.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid email address");
            Email.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pg1.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    //In the event of a successfull login
                    Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_SHORT).show();
                    Email.setText("");
                    Password.setText("");
                } else {
                    //If login fails
                    pg1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Wrong password or email!", Toast.LENGTH_SHORT).show();
                    Email.setText("");
                    Password.setText("");
                    Email.requestFocus();

                }
            }
        });
    }
}