package com.example.firstapp;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InchiriazaTeren extends AppCompatActivity {
    private TextView nume_teren;
    private TextView adresa_teren;
    private TextView pret_teren;
//    private DatePicker zi_inchiriere;
    private ListView lv_ore;
    private Button btn_inchiriaza;
    private DatabaseReference reff;
    public String nume_teren_extra;
    public String cifra_sector;
    public String numeUtilizator;
    public String tipSport;
    private String tipSportRezervare;
    private List<String> ore_ocupate;
    private Map<String, Object> ore;
    private List<String> ore_ocupate_baza_de_date = new ArrayList<String>();
    private RadioGroup rg_tip_teren;

    private TextView et_zi;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_rent_layout);
        setTitle("Inchiriaza teren");

        reff = FirebaseDatabase.getInstance().getReference();
        nume_teren_extra = getIntent().getStringExtra("nume_teren");
        cifra_sector = getIntent().getStringExtra("sector_teren");
        numeUtilizator = getIntent().getStringExtra("numeUtilizator");
        tipSport = getIntent().getStringExtra("tipSport");

        switch (tipSport){
            case "fotbal": {
                tipSport = "TerenuriFotbal";
                break;
            }
            case "baschet": {
                tipSport = "TerenuriBaschet";
                break;
            }
            case "volei": {
                tipSport = "TerenuriVolei";
                break;
            }
            case "tenis": {
                tipSport = "TerenuriTenis";
                break;
            }
            case "handbal": {
                tipSport = "TerenuriHandbal";
                break;
            }
        }

        tipSportRezervare = tipSport.substring(8);

        Log.v("tipSport", tipSport);
        Log.v("cifra_sector", cifra_sector);
        Log.v("nume_teren_extra", nume_teren_extra);

        ore_ocupate = new ArrayList<>();
        ore = new HashMap<>();

        initViews();
        ArrayAdapter<CharSequence> oreAdapter = ArrayAdapter.createFromResource(this, R.array.ore, android.R.layout.simple_list_item_multiple_choice);

        lv_ore.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_ore.setAdapter(oreAdapter);

        autocompletareDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        oreDisponibileAzi();


        reff.child(tipSport).child("Sector " + cifra_sector).child(nume_teren_extra).child("tipTeren").addValueEventListener(new ValueEventListener() {
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


        // TEST PENTRU DATEPICKER DIALOG - MERGE!

        DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(); // metoda care scrie data in textview

                ArrayAdapter<CharSequence> oreRecreate = ArrayAdapter.createFromResource(view.getContext(), R.array.ore, android.R.layout.simple_list_item_multiple_choice);
                lv_ore.setAdapter(oreRecreate);
                List<String> oreLista = Arrays.asList(getResources().getStringArray(R.array.ore));
//                String dataSelectata = dayOfMonth+"-"+(monthOfYear)+"-"+year;
                String dataSelectata = et_zi.getText().toString();
                getOreFromFirebase(dataSelectata,new OreListCallback() {
                    @Override
                    public void onCallback(List<String> value) {
                        int i = 0;
                        Log.v("ore", value.toString());
                        List<Integer> listaIndex = new ArrayList<>();
                        listaIndex = getSameIndexes(value, oreLista);
                        for(int index : listaIndex){
                            View child = lv_ore.getChildAt(index);
                            child.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                            child.setEnabled(false);
                            child.setOnClickListener(null);
                        }
                        listaIndex.clear();
                        value.clear();
                    }

                });
            }

        };


        et_zi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dialog = new DatePickerDialog(InchiriazaTeren.this, dateSet, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(new Date().getTime());
                dialog.getDatePicker().setMaxDate(new Date().getTime() + 604800000);
                dialog.show();
            }
        });
        // END TEST PENTRU DATEPICKER DIALOG - MERGE!


        btn_inchiriaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String data = String.valueOf(zi_inchiriere.getDayOfMonth())+"-"+String.valueOf(zi_inchiriere.getMonth()+1)+"-"+String.valueOf(zi_inchiriere.getYear());
                String data = et_zi.getText().toString(); // AM INLOCUIT CU DATA DE LA DATEPICKER DIALOG
                getOreFromFirebase(data,new OreListCallback() {
                    @Override
                    public void onCallback(List<String> value) {
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

                        if(existaOraSelectata == true && rg_tip_teren.getCheckedRadioButtonId() != -1 && !data.equals("Apasati pentru a selecta data pentru rezervare!")){
                            int len = lv_ore.getCount();
                            for(int i = 0; i < len ; i++){
                                if(checked.get(i)){
                                    ore_ocupate.add((String) oreAdapter.getItem(i));
                                    ore.put((String) oreAdapter.getItem(i), true);
//                                    reff.child("TerenuriFotbal").child("Sector " + cifra_sector).child(nume_teren_extra).child("oreSelectate").updateChildren(ore);
                                    reff.child("Rezervari").child(data).child(tipSportRezervare).child(nume_teren_extra).updateChildren(ore);
                                    reff.child("Users").child(numeUtilizator).child("rezervari").child(data).child(nume_teren_extra).child("ore").updateChildren(ore);
                                    reff.child("Users").child(numeUtilizator).child("rezervari").child(data).child(nume_teren_extra).child("status").setValue("activa");
                                    reff.child("Users").child(numeUtilizator).child("rezervari").child(data).child(nume_teren_extra).child("adresa").setValue(adresa_teren.getText().toString());
                                }
                            }
                            lv_ore.clearChoices();
                            oreAdapter.notifyDataSetChanged();

                            Log.v("zi", et_zi.getText().toString());
                            Toast.makeText(view.getContext(), "Ati rezervat cu succes!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(InchiriazaTeren.this, MainActivity2.class);
                            startActivity(intent);

                        } else if(data.equals("Apasati pentru a selecta data pentru rezervare!")){
                            Toast.makeText(view.getContext(), "Selectati data!", Toast.LENGTH_LONG).show();
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
//        zi_inchiriere.setMaxDate(dataCurenta.getTime() + 604800000);
//        zi_inchiriere.setMinDate(dataCurenta.getTime());
    }

    private void initViews(){
        nume_teren = findViewById(R.id.tv_rent_nume);
        adresa_teren = findViewById(R.id.tv_rent_adresa);
        pret_teren = findViewById(R.id.tv_rent_pret_week);
//        zi_inchiriere = findViewById(R.id.dp_start);
        lv_ore = findViewById(R.id.lv_ore);
        btn_inchiriaza = findViewById(R.id.btn_rent);
        rg_tip_teren = findViewById(R.id.rg_tip_teren);
        et_zi = findViewById(R.id.et_zi);
    }

    private void autocompletareDate(){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sport terenCurent = snapshot.child(tipSport).child("Sector " + cifra_sector).child(nume_teren_extra).getValue(Sport.class);
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
        void onCallback(List<String> value);
    }

    public void getOreFromFirebase(String data, final OreListCallback myCallback){
        reff.child("Rezervari").child(data).child(tipSportRezervare).child(nume_teren_extra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Boolean value = (Boolean) dataSnapshot.getValue();
                    if(value){
                        String ora = (String) dataSnapshot.getKey();
                        ore_ocupate_baza_de_date.add(ora);
                    }

                }
                myCallback.onCallback(ore_ocupate_baza_de_date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public List<Integer> getSameIndexes(List<String> arr1, List<String> arr2) {
        List<Integer> indexes = new ArrayList<Integer>();
        for(int x = 0; x < arr1.size(); x++) {
            for(int y = 0; y < arr2.size(); y++) {
                if(arr1.get(x).equals(arr2.get(y)))
                    indexes.add(y);
            }
        }
        return indexes;
    }

    public void oreDisponibileAzi(){
        List<String> oreLista = Arrays.asList(getResources().getStringArray(R.array.ore));
        Calendar calendar = Calendar.getInstance();
        String zi = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String luna = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String an = String.valueOf(calendar.get(Calendar.YEAR));

        String dataSelectata = zi+"-"+luna+"-"+an;

        getOreFromFirebase(dataSelectata,new OreListCallback() {
            @Override
            public void onCallback(List<String> value) {
                int i = 0;
                Log.v("ore", value.toString());
                List<Integer> listaIndex = new ArrayList<>();
                listaIndex = getSameIndexes(value, oreLista);
                for(int index : listaIndex){
                    View child = lv_ore.getChildAt(index);
                    child.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                    child.setEnabled(false);
                    child.setOnClickListener(null);
                }
                listaIndex.clear();
                value.clear();
            }

        });
    }

    // TEST DATE PICKER DIALOG
    private void updateLabel() {
        String myFormat = "d-M-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_zi.setText(sdf.format(myCalendar.getTime()));
    }
    //END TEST DATE PICKER DIALOG
}
