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

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecycleViewHolder> {
    private List<Sport> terenuri;
    private Context context;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(List<Sport> terenuri, Context context) {
        this.terenuri = terenuri;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sport_layout, parent, false);
        return new RecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        Sport teren = terenuri.get(position);

        holder.tv_nume_teren.setText(teren.getNume());
        holder.tv_adresa_teren.setText(teren.getAdresa());

        holder.btn_inchiriaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adresa = holder.tv_adresa_teren.getText().toString();
                Log.v("adresa", adresa);
                String cifraSector = adresa.substring(adresa.length() - 1);
                Intent intent = new Intent(holder.itemView.getContext(), InchiriazaTeren.class);
                intent.putExtra("nume_teren", holder.tv_nume_teren.getText().toString());
                intent.putExtra("sector_teren", cifraSector);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() { return terenuri.size();}

    public static class RecycleViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_nume_teren;
        public TextView tv_adresa_teren;
        public Button btn_inchiriaza;


        public RecycleViewHolder(@NonNull View itemView){
            super(itemView);
            tv_nume_teren = itemView.findViewById(R.id.tv_nume_teren);
            tv_adresa_teren = itemView.findViewById(R.id.tv_adresa_teren);
            btn_inchiriaza = itemView.findViewById(R.id.btn_inchiriaza);

        }
    }
}
