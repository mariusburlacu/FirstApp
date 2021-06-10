package com.example.firstapp;

import java.util.ArrayList;
import java.util.List;

public class Sport {
    private String nume;
    private String adresa;
    private String tipTeren;
    private String pret;
    private String imagine;
    private String tipSport; // poate sa fie fotbal, handbal, volei, baschet, tenis
//    private String oreOcupate;

    public Sport() {
    }

    public Sport(String nume, String adresa, String tipTeren, String pret, String tipSport) {
        this.nume = nume;
        this.adresa = adresa;
        this.tipTeren = tipTeren;
        this.pret = pret;
        this.tipSport = tipSport;
//        this.oreOcupate = oreOcupate;
    }

    public String getTipSport() {
        return tipSport;
    }

    public void setTipSport(String tipSport) {
        this.tipSport = tipSport;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
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
