package com.example.firstapp;

public class Sport {
    private String nume;
    private String adresa;
    private String tipTeren;

    public Sport() {
    }

    public Sport(String nume, String adresa, String tipTeren) {
        this.nume = nume;
        this.adresa = adresa;
        this.tipTeren = tipTeren;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTipTeren() {
        return tipTeren;
    }

    public void setTipTeren(String tipTeren) {
        this.tipTeren = tipTeren;
    }

    @Override
    public String toString() {
        return "Sport{" +
                "nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", tipTeren='" + tipTeren + '\'' +
                '}';
    }
}
