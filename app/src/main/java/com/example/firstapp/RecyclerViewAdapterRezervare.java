package com.example.firstapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapterRezervare extends RecyclerView.Adapter<RecyclerViewAdapterRezervare.RecycleViewHolder> {
    private List<Rezervare> rezervari;
    private Context context;
    private LayoutInflater inflater;
    private String numeUtilizator;

    private DatabaseReference reff;

    public RecyclerViewAdapterRezervare(List<Rezervare> rezervari, Context context) {
        this.rezervari = rezervari;
        this.context = context;
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
        Rezervare rezervare = rezervari.get(position);

        reff = FirebaseDatabase.getInstance().getReference();

        holder.tv_nume_teren.setText(rezervare.getNumeTeren());
        holder.tv_adresa_teren.setText(rezervare.getAdresaTeren());
        holder.tv_data.setText(rezervare.getData());

        List<String> listaOre = rezervare.getOre();

        StringBuilder strbul=new StringBuilder();

        for(int i=0; i<listaOre.size(); i++){
            if(i == listaOre.size()-1){
                strbul.append(listaOre.get(i));
            } else {
                strbul.append(listaOre.get(i));
                strbul.append(", ");
            }
        }

        holder.tv_ora.setText(strbul);

        if (rezervare.isEsteAnulata()) {
            holder.btn_anuleaza.setText(R.string.anulat);
            holder.btn_anuleaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.red));
            holder.isCanceled = true;
        } else {
            holder.btn_anuleaza.setText(R.string.anuleaza_rezervare);
            holder.btn_anuleaza.setBackgroundColor(holder.itemView.getResources().getColor(R.color.teal_700));
            holder.isCanceled = false;
        }

        holder.btn_anuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rezervare.isEsteAnulata()){
                    holder.btn_anuleaza.setText(R.string.anulat);
                    holder.btn_anuleaza.setBackgroundColor(view.getResources().getColor(R.color.red));
                    rezervare.setEsteAnulata(false);
                    holder.isCanceled = false;
                } else {
                    holder.btn_anuleaza.setText(R.string.anuleaza_rezervare);
                    holder.btn_anuleaza.setBackgroundColor(view.getResources().getColor(R.color.teal_700));
//                    Map<String, Boolean> ore = reff.child("Users").child(numeUtilizator).child("rezervari").child(rezervare.ge)
                    rezervare.setEsteAnulata(true);
                    holder.isCanceled = true;
                }
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

        public static boolean isCanceled;


        public RecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nume_teren = itemView.findViewById(R.id.tv_rezervare_nume_teren);
            tv_adresa_teren = itemView.findViewById(R.id.tv_rezervare_adresa_teren);
            btn_anuleaza = itemView.findViewById(R.id.btn_anuleaza);
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





}
