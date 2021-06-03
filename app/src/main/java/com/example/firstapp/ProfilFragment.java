package com.example.firstapp;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ProfilFragment extends Fragment {
    private DatabaseReference reff;
    public List<Rezervare> rezervariActive = new ArrayList<>();
    public List<Rezervare> rezervariAnulate = new ArrayList<>();
    public List<Rezervare> rezervariTrecute = new ArrayList<>();
    public String numeUtilizator;

    public List<String> dateRezervari = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reff = FirebaseDatabase.getInstance().getReference();

        numeUtilizator = (getActivity().getIntent().getStringExtra(LoginActivity.EXTRA_MESSAGE));

        RecyclerView rv_rezervari_active = view.findViewById(R.id.rv_rezervari_terenuri_active);
        RecyclerView rv_rezervari_trecute = view.findViewById(R.id.rv_rezervari_terenuri_trecute);
        rv_rezervari_active.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv_rezervari_trecute.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getRezervari(numeUtilizator, new RezervariListCallback() {
            @Override
            public void onCallback(List<Rezervare> rezervariActuale, List<Rezervare> rezervariTrecute) {
                Log.v("rezAc", rezervariActuale.toString());
                Log.v("rezTr", rezervariTrecute.toString());
                RecyclerViewAdapterRezervare adapter = new RecyclerViewAdapterRezervare(rezervariActuale, view.getContext());
                RecyclerViewAdapterRezervare adapter2 = new RecyclerViewAdapterRezervare(rezervariTrecute, view.getContext());
                rv_rezervari_active.setAdapter(adapter);
                rv_rezervari_trecute.setAdapter(adapter2);
            }
        });

        Log.v("data-azi", getDataAzi());
    }

    public String getDataAzi(){
        Calendar calendar = Calendar.getInstance();
        String zi = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String luna = String.valueOf(calendar.get(Calendar.MONTH)+1);
        String an = String.valueOf(calendar.get(Calendar.YEAR));

        return zi+"-"+luna+"-"+an;
    }

    public boolean comparaData(String dataAzi, String dataFirebase){
        String[] dataCurenta = dataAzi.split("\\-");
        String[] dataBazaDeDate = dataFirebase.split("\\-");

        Log.v("luna", dataCurenta[0] + " " + dataBazaDeDate[0]);
        Log.v("luna", dataCurenta[1] + " " + dataBazaDeDate[1]);
        Log.v("luna", dataCurenta[2] + " " + dataBazaDeDate[2]);

        if(Integer.parseInt(dataBazaDeDate[2]) > Integer.parseInt(dataCurenta[2])) {
            return true;
        } else {
            if (Integer.parseInt(dataBazaDeDate[2]) == Integer.parseInt(dataCurenta[2])){
                if (Integer.parseInt(dataBazaDeDate[1]) > Integer.parseInt(dataCurenta[1])) {
                    return true;
                } else {
                    if (Integer.parseInt(dataBazaDeDate[1]) == Integer.parseInt(dataCurenta[1])) {
                        if(Integer.parseInt(dataBazaDeDate[0]) >= Integer.parseInt(dataCurenta[0])){
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
    }

    public interface RezervariListCallback {
        void onCallback(List<Rezervare> rezervariActuale, List<Rezervare> rezervariTrecute);
    }

    public void getRezervari(String nume, RezervariListCallback myCallback){
        reff.child("Users").child(nume).child("rezervari").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    String dataRezervare = data.getKey();
                    dateRezervari.add(dataRezervare);

                    reff.child("Users").child(nume).child("rezervari").child(dataRezervare).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot data : snapshot.getChildren()){
                                String numeTeren = data.getKey();

                                reff.child("Users").child(nume).child("rezervari").child(dataRezervare).child(numeTeren).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        List<String> ore = new ArrayList<>();
                                        for(DataSnapshot data : snapshot.getChildren()){
                                            String ora = data.getKey();
                                            ore.add(ora);
                                        }

                                        Rezervare rezervare = new Rezervare(numeTeren, ore, dataRezervare);
                                        String dataAzi = getDataAzi();

                                        if(comparaData(dataAzi, rezervare.getData())){
                                            rezervariActive.add(rezervare);
                                        } else {
                                            rezervariTrecute.add(rezervare);
                                        }


                                        myCallback.onCallback(rezervariActive, rezervariTrecute);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                       }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
