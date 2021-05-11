package com.example.firstapp;

import java.util.ArrayList;
import java.util.List;

public class Sport {
    private String nume;
    private String adresa;
    private String tipTeren;
    private String pret;
//    private String oreOcupate;

    public Sport() {
    }

    public Sport(String nume, String adresa, String tipTeren, String pret) {
        this.nume = nume;
        this.adresa = adresa;
        this.tipTeren = tipTeren;
        this.pret = pret;
//        this.oreOcupate = oreOcupate;
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

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

//    public String getOreOcupate() {
//        return oreOcupate;
//    }
//
//    public void setOreOcupate(String oreOcupate) {
//        this.oreOcupate = oreOcupate;
//    }

    @Override
    public String toString() {
        return "Sport{" +
                "nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", tipTeren='" + tipTeren + '\'' +
                ", pret='" + pret + '\'' +
//                ", oreOcupate=" + oreOcupate +
                '}';
    }
}
