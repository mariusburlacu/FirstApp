package com.example.firstapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.*;

public class FotbalFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fotbal, container, false);
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_terenuri = view.findViewById(R.id.rv_terenuri);

        Spinner spinner = view.findViewById(R.id.spinner_sectoare);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sectoare,
                android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


//        List<Sport> terenuri = new ArrayList<>();
//        terenuri.add(new Sport("Complex Sport Team Agronomia", "Bd. Marasti, nr. 59, sector 1", "acoperit / aer liber"));
//        terenuri.add(new Sport("Pro Rapid", "Bd. Bucurestii Noi, nr. 170, sector 1", "aer liber"));
//        terenuri.add(new Sport("Champion Club", "Str. Primo Nebiolo, nr. 2, sector 1", "aer liber"));
//        terenuri.add(new Sport("CTF Mihai I", "Str. Butuceni, nr. 10, sector 1", "aer liber"));
//
//        terenuri.add(new Sport("Teren Fotbal Apolo", "Bd. Pierre de Coubertin, Aleea Belvedere, sector 2", "acoperit"));

        List<Sport> terenuri_sector1 = new ArrayList<>();
        terenuri_sector1.add(new Sport("Complex Sport Team Agronomia", "Bd. Marasti, nr. 59, sector 1", "acoperit / aer liber"));
        terenuri_sector1.add(new Sport("Pro Rapid", "Bd. Bucurestii Noi, nr. 170, sector 1", "aer liber"));
        terenuri_sector1.add(new Sport("Champion Club", "Str. Primo Nebiolo, nr. 2, sector 1", "aer liber"));
        terenuri_sector1.add(new Sport("CTF Mihai I", "Str. Butuceni, nr. 10, sector 1", "aer liber"));

        List<Sport> terenuri_sector2 = new ArrayList<>();
        terenuri_sector2.add(new Sport("Teren Fotbal Apolo", "Bd. Pierre de Coubertin, Aleea Belvedere, sector 2", "acoperit"));
        terenuri_sector2.add(new Sport("Scoala nr. 77", "Sos. Pantelimon, nr. 289, sector 2", "aer liber"));
        terenuri_sector2.add(new Sport("Delfin Arena", "Str. Torentului, nr. 17-19, sector 2", "acoperit"));

        List<Sport> terenuri_sector3 = new ArrayList<>();
        terenuri_sector3.add(new Sport("Pro Luceafarul", "Str. Sergent Serbanica Vasile, sector 3", "aer liber"));
        terenuri_sector3.add(new Sport("Asii Sport", "Bd. Theodor Pallady, nr. 287, sector 3", "aer liber"));
        terenuri_sector3.add(new Sport("Bro Crio Sport", "Parcul Tineretului Deb 1, sector 3", "aer liber"));
        terenuri_sector3.add(new Sport("Club Cara", "Str. Rotunda, nr. 4, sector 3", "aer liber"));
        terenuri_sector3.add(new Sport("Complex Sportiv Extreme", "Str. Ghita Serban, nr. 15-19, sector 3", "aer liber"));
        terenuri_sector3.add(new Sport("Club Voinicelu", "Str. Gheorghe Petrascu, nr. 43-45, sector 3", "aer liber"));

        List<Sport> terenuri_sector4 = new ArrayList<>();
        terenuri_sector4.add(new Sport("Aris Fotbal Club", "Str. Izvorul Crisului, nr. 4, sector 4", "aer liber"));
        terenuri_sector4.add(new Sport("Arena Temerarii", "Str. Luica, nr. 70, sector 4", "aer liber"));
        terenuri_sector4.add(new Sport("Offside Arena", "Bd. Metalurgiei, nr. 23, sector 4", "acoperit"));
        terenuri_sector4.add(new Sport("Baza sportiva Romprim Bucuresti", "Sos. Oltenitei. nr. 193, sector 4", "aer liber"));
        terenuri_sector4.add(new Sport("Football Point", "Sos. Berceni, nr. 285-287, sector 4", "acoperit"));
        terenuri_sector4.add(new Sport("Daimon Sport Club", "Calea Piscului, nr. 10, sector 4", "aer liber"));
        terenuri_sector4.add(new Sport("Dangelo Sport", "Sos. Vitan Barzesti, nr. 11, sector 4", "acoperit"));
        terenuri_sector4.add(new Sport("Sud Arena", "Sos. Vitan Barzesti, nr. 5-7, sector 4", "aer liber"));

        List<Sport> terenuri_sector5 = new ArrayList<>();
        terenuri_sector5.add(new Sport("Progresul Spartac '44", "Intrarea Vrabiei, nr. 30, sector 5", "acoperit / aer liber"));
        terenuri_sector5.add(new Sport("Complex Mirano", "Str. Progresului, nr. 90-100, sector 5", "acoperit"));
        terenuri_sector5.add(new Sport("Primavera Arena", "Sos. Bucuresti-Magurele, nr. 32A, sector 5", "acoperit"));

        List<Sport> terenuri_sector6 = new ArrayList<>();
        terenuri_sector6.add(new Sport("Robert Arena", "Str. Dealul Tugulea, nr. 35, sector 6", "aer liber"));
        terenuri_sector6.add(new Sport("BSP Arena", "Aleea Moinesti, sector 6", "acoperit"));
        terenuri_sector6.add(new Sport("Complex Sportiv Mazicon", "Strada Bascov 14-18, sector 6", "aer liber"));
        terenuri_sector6.add(new Sport("Complex Sportiv Diamond", "Bd. Iuliu Maniu, nr. 209, sector 6", "aer liber"));
        terenuri_sector6.add(new Sport("Smart Arena Plaza", "Str. Lujerului, nr, 17, sector 6", "acoperit"));
        terenuri_sector6.add(new Sport("Poli Arena", "Splaiul Independentei, nr. 313, sector 6", "aer liber"));
        terenuri_sector6.add(new Sport("Tiki Taka Minifotbal", "Str. Pestera Scarisoara, nr. 1, sector 6", "aer liber"));

        List<Sport> terenuri_fotbal = Stream.of(terenuri_sector1, terenuri_sector2, terenuri_sector3, terenuri_sector4,
                terenuri_sector5, terenuri_sector6).flatMap(Collection::stream).collect(Collectors.toList());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sectorSpinner = spinner.getSelectedItem().toString();
                Log.v("select", sectorSpinner);

                rv_terenuri.setLayoutManager(new LinearLayoutManager(adapterView.getContext()));

                if(sectorSpinner.equals("Sector 1")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector1, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 2")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector2, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 3")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector3, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 4")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector4, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 5")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector5, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 6")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_sector6, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                } else {
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_fotbal, adapterView.getContext());
                    rv_terenuri.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        rv_terenuri.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri, this.getContext());
//        rv_terenuri.setAdapter(adapter);
    }
}
