package com.example.firstapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.activitati.LoginActivity;
import com.example.firstapp.R;
import com.example.firstapp.clase.RecyclerViewAdapter;
import com.example.firstapp.clase.Sport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FotbalFragment extends Fragment {
    private DatabaseReference reff;
    public List<Sport> terenuri = new ArrayList<>();
    public String numeUtilizator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

        numeUtilizator = (getActivity().getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE));

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
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 1);

                } else if(sectorSpinner.equals("Sector 2")){
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 2);
                } else if(sectorSpinner.equals("Sector 3")){
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 3);
                } else if(sectorSpinner.equals("Sector 4")){
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 4);
                } else if(sectorSpinner.equals("Sector 5")){
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 5);
                } else if(sectorSpinner.equals("Sector 6")){
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 6);
                } else {
                    terenuri.clear();
                    getTerenuriFromFirebase(new TerenuriListCallback() {
                        @Override
                        public void onCallback(List<Sport> value) {
                            Log.v("Terenuri", value.toString());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(value, adapterView.getContext(), numeUtilizator);
                            rv_terenuri.setAdapter(adapter);
                        }
                    }, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.reguli:
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Reguli de joc");
                alertDialog.setMessage(getResources().getString(R.string.reguliFotbal));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //    public List<Sport> readSector(int sector){
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                terenuri.clear();
//                List<String> keys = new ArrayList<>();
//                for(DataSnapshot keyNode : snapshot.child("TerenuriFotbal").child("Sector " + sector).getChildren()){
//                    keys.add(keyNode.getKey());
//                    Sport sport = keyNode.getValue(Sport.class);
//                    terenuri.add(sport);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return terenuri;
//    }

    public interface TerenuriListCallback{
        void onCallback(List<Sport> value);
    }

    public void getTerenuriFromFirebase(final TerenuriListCallback myCallback, int sector){
        if(sector==0){
            for(int i = 1; i <= 6; i++){
                reff.child("TerenuriFotbal").child("Sector " + i).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Sport sport = dataSnapshot.getValue(Sport.class);
                            assert sport != null;
                            terenuri.add(sport);
                        }
                        myCallback.onCallback(terenuri);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        reff.child("TerenuriFotbal").child("Sector " + sector).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Sport sport = dataSnapshot.getValue(Sport.class);
                    assert sport != null;
                    terenuri.add(sport);
                }
                myCallback.onCallback(terenuri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
