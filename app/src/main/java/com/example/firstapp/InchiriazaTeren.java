package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InchiriazaTeren extends AppCompatActivity {
    private TextView nume_teren;
    private TextView adresa_teren;
    private TextView pret_teren;
    private DatePicker zi_inchiriere;
    private ListView lv_ore;
    private Button btn_inchiriaza;
    private DatabaseReference reff;
    public String nume_teren_extra;
    public String cifra_sector;
    private List<String> ore_ocupate;
    private Map<String, Object> ore;
    private List<Boolean> ore_ocupate_baza_de_date = new ArrayList<Boolean>();
    private RadioGroup rg_tip_teren;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_rent_layout);
        setTitle("Inchiriaza teren");

        reff = FirebaseDatabase.getInstance().getReference();
        nume_teren_extra = getIntent().getStringExtra("nume_teren");
        cifra_sector = getIntent().getStringExtra("sector_teren");

        Log.v("cifra_sector", cifra_sector);
        Log.v("nume_teren_extra", nume_teren_extra);

        ore_ocupate = new ArrayList<>();
        ore = new HashMap<>();

        initViews();
        ArrayAdapter<CharSequence> oreAdapter = ArrayAdapter.createFromResource(this, R.array.ore, android.R.layout.simple_list_item_multiple_choice);

        lv_ore.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_ore.setAdapter(oreAdapter);

        autocompletareDate();

        getOreFromFirebase(new OreListCallback() {
            @Override
            public void onCallback(List<Boolean> value) {
                int i = 0;
                Log.v("ore", value.toString());
                for(Boolean ora : value){
                    if(ora){
                        View child = lv_ore.getChildAt(i);
                        child.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                        child.setEnabled(false);
                        child.setOnClickListener(null);
                        i++;
                    } else {
                        i++;
                    }
                }
            }
        });

        reff.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).child("tipTeren").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tipTeren = (String) snapshot.getValue();
                if(tipTeren.equals("acoperit")){
                    rg_tip_teren.getChildAt(1).setEnabled(false);
                } else if(tipTeren.equals("aer liber")){
                    rg_tip_teren.getChildAt(0).setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_inchiriaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOreFromFirebase(new OreListCallback() {
                    @Override
                    public void onCallback(List<Boolean> value) {
                        Log.v("ore",ore_ocupate.toString());
                        SparseBooleanArray checked = lv_ore.getCheckedItemPositions();
                        Log.v("checkl", checked.toString());
                        boolean existaOraSelectata = false;

                        // NU TRB SA VERIFIC DACA ESTE NULL, TRB SA VERIFIC DACA EXISTA CEL PUTIN UN ITEM SELECTAT
                        for(int i = 0; i < oreAdapter.getCount(); i++){
                            if(checked.get(i) == true){
                                existaOraSelectata = true;
                                break;
                            }
                        }

                        if(existaOraSelectata == true && rg_tip_teren.getCheckedRadioButtonId() != -1){
                            int len = lv_ore.getCount();
                            for(int i = 0; i < len ; i++){
                                if(checked.get(i)){
                                    ore_ocupate.add((String) oreAdapter.getItem(i));
                                    ore.put((String) oreAdapter.getItem(i), true);
                                    reff.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).child("oreSelectate").updateChildren(ore);
                                }
                            }
                            lv_ore.clearChoices();
                            oreAdapter.notifyDataSetChanged();

                            Toast.makeText(view.getContext(), "Ati rezervat cu succes!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(InchiriazaTeren.this, MainActivity2.class);
                            startActivity(intent);

                        } else if (rg_tip_teren.getCheckedRadioButtonId() == -1){
                            Toast.makeText(view.getContext(), "Selectati tipul de teren!", Toast.LENGTH_LONG).show();
                        } else if(existaOraSelectata == false || checked.size() == 0) {
                            Toast.makeText(view.getContext(), "Selectati orele!", Toast.LENGTH_LONG).show();
                        }


                    }
                });
            }
        });

        Date dataCurenta = new Date();
        zi_inchiriere.setMaxDate(dataCurenta.getTime() + 604800000);
        zi_inchiriere.setMinDate(dataCurenta.getTime());

    }

    private void initViews(){
        nume_teren = findViewById(R.id.tv_rent_nume);
        adresa_teren = findViewById(R.id.tv_rent_adresa);
        pret_teren = findViewById(R.id.tv_rent_pret_week);
        zi_inchiriere = findViewById(R.id.dp_start);
        lv_ore = findViewById(R.id.lv_ore);
        btn_inchiriaza = findViewById(R.id.btn_rent);
        rg_tip_teren = findViewById(R.id.rg_tip_teren);
    }

    private void autocompletareDate(){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sport terenCurent = snapshot.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).getValue(Sport.class);
                nume_teren.setText(terenCurent.getNume());
                adresa_teren.setText(terenCurent.getAdresa());
                pret_teren.setText(terenCurent.getPret());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface OreListCallback{
        void onCallback(List<Boolean> value);
    }

    public void getOreFromFirebase(final OreListCallback myCallback){
        reff.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).child("oreSelectate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        boolean ora = (boolean) dataSnapshot.getValue();
                        ore_ocupate_baza_de_date.add(ora);
                    }
                    myCallback.onCallback(ore_ocupate_baza_de_date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
