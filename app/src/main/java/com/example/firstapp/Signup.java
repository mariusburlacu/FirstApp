package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    private EditText et_username;
    private EditText et_email;
    private EditText et_password;
    private EditText et_retype_password;
    private Button et_btn_signup;

    private DatabaseReference reff;
    private User user; // e mandatory - o folosim pentru FIREBASE


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sign-up page");
        //myDb = new DatabaseHelper(this);

        user = new User();
        reff = FirebaseDatabase.getInstance().getReference();

        init();

        et_btn_signup.setOnClickListener((view -> {
            if(validateContent()) {
                String username = et_username.getText().toString();
                String email = et_email.getText().toString();
                String pass1 = et_password.getText().toString();

                reff.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            user.setUsername(username);
                            user.setPassword(pass1);
                            user.setEmail(email);


                            //reff.child("User "+String.valueOf(maxId+1)).setValue(user);
                            reff.child(et_username.getText().toString()).setValue(user);
                            Toast.makeText(Signup.this, "Signup successful!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Signup.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Signup.this, "Username already exists", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //myDb.insertData(et_username.getText().toString(), et_email.getText().toString(),
                // et_password.getText().toString()); // insert values in database
            }
        }));
    }

    private void init(){
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_retype_password = findViewById(R.id.et_retype_password);
        et_btn_signup = findViewById(R.id.sign_up_btn);
    }


    private boolean validateContent(){
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String pass1 = et_password.getText().toString();
        String pass2 = et_retype_password.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username must not be empty", Toast.LENGTH_LONG).show();
            //et_username.setError("Username must not be empty");
            return false;
        }

        if(email.isEmpty()){
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email format invalid", Toast.LENGTH_LONG).show();
            return false;
        }

        if(pass1.isEmpty() || pass1.length() < 8){
            Toast.makeText(this, "Password must not be empty and must be 8+ characters", Toast.LENGTH_LONG).show();
            return false;
        }

        if(pass2.isEmpty()){
            Toast.makeText(this, "Password must not be empty", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!pass1.equals(pass2)){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}