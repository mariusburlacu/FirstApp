package com.example.firstapp;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecyclerViewAdapterRezervare extends RecyclerView.Adapter<RecyclerViewAdapterRezervare.RecycleViewHolder> {
    private List<Rezervare> rezervari;
    private Context context;
    private LayoutInflater inflater;
    private String numeUtilizator;
    private String status;

    private Rezervare rezervare = new Rezervare();

    private DatabaseReference reff;

    public RecyclerViewAdapterRezervare(List<Rezervare> rezervari, Context context, String numeUtilizator) {
        this.rezervari = rezervari;
        this.context = context;
        this.numeUtilizator = numeUtilizator;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rezervari_layout, parent, false);
        return new RecycleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        rezervare = rezervari.get(position);

        reff = FirebaseDatabase.getInstance().getReference();

        holder.tv_nume_teren.setText(rezervare.getNumeTeren());
        holder.tv_adresa_teren.setText(rezervare.getAdresaTeren());
        holder.tv_data.setText(rezervare.getData());
        holder.tv_adresa_teren.setText(rezervare.getAdresaTeren());

        holder.btn_navigheaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.teal_700));

        List<String> listaOre = rezervare.getOre();
        Map<String, Object> mapOre = new HashMap<>();
        for(String ora : listaOre){
            mapOre.put(ora, false);
        }

        StringBuilder strbul = new StringBuilder();

        for(int i=0; i<listaOre.size(); i++) {
            if (i == listaOre.size() - 1) {
                strbul.append(listaOre.get(i));
            } else {
                strbul.append(listaOre.get(i));
                strbul.append(", ");
            }
        }

        holder.tv_ora.setText(strbul);

        String adresaTerenPentruLocatie = rezervare.getAdresaTeren() + ", Bucuresti";

        LatLng locatie = getLocationFromAddress(holder.itemView.getContext(), adresaTerenPentruLocatie);

        getTipSport(rezervare, new TipSportCallback() {
            @Override
            public void onCallback(String tipSport) {
                rezervare.setTipSport(tipSport);
            }
        });

        getStatus(rezervare, new StatusCallback() {
            @Override
            public void onCallback(String value) {
                status = value;

                ProfilFragment profilFragment = new ProfilFragment();
                String dataAzi = profilFragment.getDataAzi();

                if (!(status.equals("activa"))) {
                    holder.btn_anuleaza.setText(R.string.anulat);
                    holder.btn_anuleaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.red));
                    holder.btn_anuleaza.setEnabled(false);
                    holder.btn_navigheaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.red));
                    holder.btn_navigheaza.setEnabled(false);
                } else {
                    holder.btn_anuleaza.setText(R.string.anuleaza_rezervare);
                    holder.btn_anuleaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.teal_700));
                }

                if(!profilFragment.comparaData(dataAzi, rezervare.getData())){
                    holder.btn_anuleaza.setText(R.string.rezervare_expirata);
                    holder.btn_anuleaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.grey));
                    holder.btn_anuleaza.setEnabled(false);
                    holder.btn_navigheaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.grey));
                    holder.btn_navigheaza.setEnabled(false);
                }

                holder.btn_anuleaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("status", status);
                        Log.v("sportAdapter", rezervare.getTipSport());
                        new AlertDialog.Builder(view.getContext()).setTitle("Anulare rezervare").setMessage("Sunteti sigur ca doriti anularea rezervarii?")
                            .setPositiveButton(R.string.da, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                holder.btn_anuleaza.setText(R.string.anulat);
                                holder.btn_anuleaza.setBackgroundColor(view.getResources().getColor(R.color.red));
                                holder.btn_anuleaza.setEnabled(false);

                                reff.child("Users").child(numeUtilizator).child("rezervari").child(rezervare.getData()).child(rezervare.getNumeTeren()).child("status").setValue("anulata");
                                getTipSport(rezervare, new TipSportCallback() {
                                    @Override
                                    public void onCallback(String tipSport) {
                                        reff.child("Rezervari").child(rezervare.getData()).child(tipSport).child(rezervare.getNumeTeren()).updateChildren(mapOre);
                                        rezervare.setEsteAnulata(false);
                                        stergeRezervare(rezervare);
                                        Toast.makeText(view.getContext(), "Anulare cu succes!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setNegativeButton(R.string.nu, null).show();

                    }
                });

                holder.btn_navigheaza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", locatie.latitude, locatie.longitude);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        context.startActivity(intent);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return rezervari.size();
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_nume_teren;
        public TextView tv_adresa_teren;
        public TextView tv_ora;
        public TextView tv_data;
        public Button btn_anuleaza;
        public Button btn_navigheaza;

        public static boolean isCanceled;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nume_teren = itemView.findViewById(R.id.tv_rezervare_nume_teren);
            tv_adresa_teren = itemView.findViewById(R.id.tv_rezervare_adresa_teren);
            btn_anuleaza = itemView.findViewById(R.id.btn_anuleaza);
            btn_navigheaza = itemView.findViewById(R.id.btn_maps);
            tv_ora = itemView.findViewById(R.id.tv_rezervare_ore);
            tv_data = itemView.findViewById(R.id.tv_rezervare_data);

            isCanceled = false;

            if(isCanceled){
                btn_anuleaza.setText(R.string.anulat);
                btn_anuleaza.setBackgroundColor(itemView.getResources().getColor(R.color.red));
            } else {
                btn_anuleaza.setText(R.string.anuleaza_rezervare);
                btn_anuleaza.setBackgroundColor(itemView.getResources().getColor(R.color.teal_700));
            }
        }
    }


    public interface StatusCallback {
        void onCallback(String value);
    }

    public void getStatus(Rezervare rezervare, StatusCallback myCallback){
        reff.child("Users").child(numeUtilizator).child("rezervari").child(rezervare.getData()).child(rezervare.getNumeTeren()).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = (String) snapshot.getValue();
                myCallback.onCallback(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface TipSportCallback {
        void onCallback(String tipSport);
    }

    public void getTipSport(Rezervare rezervare, TipSportCallback myCallback){
        reff.child("Users").child(numeUtilizator).child("rezervari").child(rezervare.getData()).child(rezervare.getNumeTeren()).child("tipSport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String tipSport = (String) snapshot.getValue();
                myCallback.onCallback(tipSport);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void stergeRezervare(Rezervare rezervare){
        rezervari.remove(rezervare);
        notifyDataSetChanged();
    }

    public void adaugaRezervare(Rezervare rezervare){
        rezervari.add(rezervare);
        notifyDataSetChanged();
    }
}
