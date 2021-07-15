package com.example.firstapp.clase;

import java.util.List;

public class Rezervare {
    private String numeTeren;
    private String adresaTeren;
    private List<String> ore;
    private String data;
    private boolean esteAnulata;
    private String tipSport;

    public Rezervare() {
    }

    public Rezervare(String numeTeren, List<String> ore, String data, String adresaTeren) {
        this.numeTeren = numeTeren;
        this.ore = ore;
        this.data = data;
        this.esteAnulata = false;
        this.adresaTeren = adresaTeren;
    }

    public String getTipSport() {
        return tipSport;
    }

    public void setTipSport(String tipSport) {
        this.tipSport = tipSport;
    }

    public boolean isEsteAnulata() {
        return esteAnulata;
    }

    public void setEsteAnulata(boolean esteAnulata) {
        this.esteAnulata = esteAnulata;
    }

    public String getNumeTeren() {
        return numeTeren;
    }

    public void setNumeTeren(String numeTeren) {
        this.numeTeren = numeTeren;
    }

    public String getAdresaTeren() {
        return adresaTeren;
    }

    public void setAdresaTeren(String adresaTeren) {
        this.adresaTeren = adresaTeren;
    }

    public List<String> getOre() {
        return ore;
    }

    public void setOre(List<String> ore) {
        this.ore = ore;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Rezervare{" +
                "numeTeren='" + numeTeren + '\'' +
                ", adresaTeren='" + adresaTeren + '\'' +
                ", ore=" + ore +
                ", data='" + data + '\'' +
                '}';
    }
}
