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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    //Objects
    private Button register, login;
    private TextView Name, Email, Password;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        //If User is there or not to check!
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser == null) {
//
//        } else {
//            Intent home = new Intent(Register.this, HomePage.class);
//            startActivity(home);
//        }

        //referencing the variables
        register = findViewById(R.id.button1);
        login = findViewById(R.id.button2);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        pg = findViewById(R.id.progressbar1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code goes letter
                userRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If user wants to go to the login page
                Intent intent1 = new Intent(Register.this, Login.class);
                startActivity(intent1);
            }
        });
    }

    private void userRegister() {
        //Loading state
        pg.setVisibility(View.VISIBLE);
        //Inputs from the user
        String name = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String key = databaseReference.push().getKey();

        //Checking whether empty or not
        if(email.isEmpty())
        {
            Email.setError("Enter an email address");
            Email.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            Password.setError("Enter a password");
            Password.requestFocus();
            return;
        }

        if(name.isEmpty())
        {
            Name.setError("Enter a name");
            Name.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid email address");
            Email.requestFocus();
            return;
        }
        /////

        ///Firebase Authentication and Real-time Database function starts here

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pg.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                        //Also stores name and email in the Firebase Database as name cannot be stored in Authentication
                        User user = new User(name, email, password);
                        databaseReference.child(key).setValue(user);
                        Toast.makeText(getApplicationContext(), "User signed up successfully", Toast.LENGTH_SHORT).show();
                        updateUI(1);
                } else {
                    //if user already exists
                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                        updateUI(2);
                    }
                }
            }
        });
    }

    private void updateUI(int i) {

        if(i == 1) {
            Name.setText("");
            Email.setText("");
            Password.setText("");
        } else {
            Name.setText("");
            Email.setText("");
            Password.setText("");
            Name.requestFocus();
        }
    }
}