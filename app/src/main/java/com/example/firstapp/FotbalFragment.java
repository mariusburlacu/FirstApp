package com.example.firstapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.*;

public class FotbalFragment extends Fragment {
    private DatabaseReference reff;
    public List<Sport> terenuri = new ArrayList<>();

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

        reff = FirebaseDatabase.getInstance().getReference();

        RecyclerView rv_terenuri = view.findViewById(R.id.rv_terenuri);

        Spinner spinner = view.findViewById(R.id.spinner_sectoare);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sectoare,
                android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sectorSpinner = spinner.getSelectedItem().toString();
                Log.v("select", sectorSpinner);

                rv_terenuri.setLayoutManager(new LinearLayoutManager(adapterView.getContext()));


                if(sectorSpinner.equals("Sector 1")){

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(1), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                    Log.v("terenuri", readSector(1).toString());
                } else if(sectorSpinner.equals("Sector 2")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(2), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                    Log.v("terenuri", readSector(2).toString());
                } else if(sectorSpinner.equals("Sector 3")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(3), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 4")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(4), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 5")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(5), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                } else if(sectorSpinner.equals("Sector 6")){
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(readSector(6), adapterView.getContext());
                    rv_terenuri.getRecycledViewPool().clear();
                    adapter.notifyDataSetChanged();
                    rv_terenuri.setAdapter(adapter);
                }
//                else {
//                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri_fotbal, adapterView.getContext());
//                    rv_terenuri.setAdapter(adapter);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        rv_terenuri.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(terenuri, this.getContext());
//        rv_terenuri.setAdapter(adapter);
    }

    public List<Sport> readSector(int sector){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                terenuri.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : snapshot.child("TerenuriFotbal").child("Sector " + sector).getChildren()){
                    keys.add(keyNode.getKey());
                    Sport sport = keyNode.getValue(Sport.class);
                    terenuri.add(sport);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return terenuri;
    }

}
