package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class InchiriazaTeren extends AppCompatActivity {
    private TextView nume_teren;
    private TextView adresa_teren;
    private TextView pret_teren_zi_saptamana;
    private TextView pret_teren_weekend;
    private DatePicker zi_inchiriere;
    private ListView lv_ore;
    private Button btn_inchiriaza;
    private DatabaseReference reff;
    public String nume_teren_extra;
    public String cifra_sector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_rent_layout);

        reff = FirebaseDatabase.getInstance().getReference();
        nume_teren_extra = getIntent().getStringExtra("nume_teren");
        cifra_sector = getIntent().getStringExtra("sector_teren");

        Log.v("cifra_sector", cifra_sector);
        Log.v("nume_teren_extra", nume_teren_extra);

        initViews();

        autocompletareDate();

        ArrayAdapter<CharSequence> oreAdapter = ArrayAdapter.createFromResource(this, R.array.ore, android.R.layout.simple_expandable_list_item_1);
        lv_ore.setAdapter(oreAdapter);

        Date dataCurenta = new Date();
        zi_inchiriere.setMaxDate(dataCurenta.getTime() + 604800000);
        zi_inchiriere.setMinDate(dataCurenta.getTime());

    }

    private void initViews(){
        nume_teren = findViewById(R.id.tv_rent_nume);
        adresa_teren = findViewById(R.id.tv_rent_adresa);
        pret_teren_zi_saptamana = findViewById(R.id.tv_rent_pret_week);
        pret_teren_weekend = findViewById(R.id.tv_rent_pret_weekend);
        zi_inchiriere = findViewById(R.id.dp_start);
        lv_ore = findViewById(R.id.lv_ore);
        btn_inchiriaza = findViewById(R.id.btn_rent);
    }

    private void autocompletareDate(){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sport terenCurent = snapshot.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).getValue(Sport.class);
                nume_teren.setText(terenCurent.getNume());
                adresa_teren.setText(terenCurent.getAdresa());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
