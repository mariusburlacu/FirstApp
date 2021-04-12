package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_ACTIVITY = "loginActivity";
    public static final String NAME = "name";
    public static final String PASS = "pass";
    public static final String CHECK = "CHECK";
    private Button btn;
    private EditText etUsername;
    private EditText etPassword;
    public static final String EXTRA_MESSAGE = "Nume_User";
    private TextView textView;
    private CheckBox checkBox;
    //private DatabaseHelper myDb; nu e mandatory pentru ca se foloseste de SQL si e stocata

    private DatabaseReference reff;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); //sterge titlul aplicatiei din toolbar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //sterge dark mode ul
        reff = FirebaseDatabase.getInstance().getReference();

        preferences = getSharedPreferences(LOGIN_ACTIVITY, MODE_PRIVATE);

        init();

        incarcaPreferinte();

        // CLICK ON TEXT VIEW - SIGN UP!!!
        String text = "Don't have an account? Sign up!";
        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //Toast.makeText(MainActivity.this, "Clicked on sign up!", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(LoginActivity.this, Signup.class);
                startActivity(intent2);
            }
        };

        ss.setSpan(clickableSpan1, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        btn.setOnClickListener((view -> {
            if(validateContent()){
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(checkBox.isChecked()) {
                    Boolean isChecked = checkBox.isChecked();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(NAME, username);
                    editor.putString(PASS, password);
                    editor.putBoolean(CHECK, isChecked);
                    editor.apply();
                } else {
                    preferences.edit().clear().apply();
                }

                reff.child(username).addValueEventListener(new ValueEventListener() {
                    // reff.child(username) practic iti verifica daca exista username-ul, pentru ca
                    // am pus ca fiecare child sa aiba numele username-ului
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = String.valueOf(snapshot.child("password").getValue());
                        if(value.equals(password)){
                            // value a luat valoarea copilului "password" si
                            // verificam daca valoarea lui value este egala cu password-ul nostru
                            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                            intent.putExtra(EXTRA_MESSAGE, etUsername.getText().toString());
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect username or password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }));
    }

    private void init(){
        btn = findViewById(R.id.btn_submit);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        textView = findViewById(R.id.sign_up);
        checkBox = findViewById(R.id.cb_remember);
    }

    private boolean validateContent(){
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(this, "Username must not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if(myDb.isUsernameExist(username) == false){
//            Toast.makeText(this, "Incorrect username", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if(password.isEmpty()){
            Toast.makeText(this, "Password must not be empty", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if(myDb.isPasswordExist(password) == false){
//            Toast.makeText(this, "Incorrect password", Toast.LENGTH_LONG).show();
//            return false;
//        }

        return true;
    }

    private void incarcaPreferinte() {
        SharedPreferences preferences = getSharedPreferences(LOGIN_ACTIVITY, MODE_PRIVATE);
        if(preferences.contains(NAME)) {
            String nume_pref = preferences.getString(NAME, "");
            etUsername.setText(nume_pref);
        }
        if(preferences.contains(PASS)) {
            String pass_pref = preferences.getString(PASS, "");
            etPassword.setText(pass_pref);
        }
        if(preferences.contains(CHECK)) {
            Boolean checked = preferences.getBoolean(CHECK, false);
            checkBox.setChecked(checked);
        }
    }
}