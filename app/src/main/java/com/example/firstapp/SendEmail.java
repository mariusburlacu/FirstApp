package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SendEmail extends Activity {

    private EditText et_Mail;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        init();

        btnSend.setOnClickListener(view -> {
            if(!et_Mail.getText().toString().isEmpty()){
                sendEmail();
            }
        });
    }

    private void init(){
        et_Mail = findViewById(R.id.problema_text);
        btnSend = findViewById(R.id.btn_send);
    }

    private void sendEmail() {
        String[] emailTo = {"marius.bm29@gmail.com"};
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setData(Uri.parse("mailto:"));
        intentEmail.setType("text/plain");

        String text = et_Mail.getText().toString();

        intentEmail.putExtra(Intent.EXTRA_EMAIL, emailTo);
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Problema aplicatia Sportdeck");
        intentEmail.putExtra(Intent.EXTRA_TEXT, text);

        try {
            startActivity(Intent.createChooser(intentEmail, "Send mail..."));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendEmail.this, "Niciun email", Toast.LENGTH_LONG).show();
        }
    }
}