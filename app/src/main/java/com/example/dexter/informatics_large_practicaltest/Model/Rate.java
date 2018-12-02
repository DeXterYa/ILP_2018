package com.example.dexter.informatics_large_practicaltest.Model;

public class Rate {
    private Double DOLR;
    private Double PENY;
    private Double QUID;
    private Double SHIL;

    public Rate(Double DOLR, Double PENY, Double QUID, Double SHIL) {
        this.DOLR = DOLR;
        this.PENY = PENY;
        this.QUID = QUID;
        this.SHIL = SHIL;
    }

    public Rate() {
    }

    public Double getDOLR() {
        return DOLR;
    }

    public void setDOLR(Double DOLR) {
        this.DOLR = DOLR;
    }

    public Double getPENY() {
        return PENY;
    }

    public void setPENY(Double PENY) {
        this.PENY = PENY;
    }

    public Double getQUID() {
        return QUID;
    }

    public void setQUID(Double QUID) {
        this.QUID = QUID;
    }

    public Double getSHIL() {
        return SHIL;
    }

    public void setSHIL(Double SHIL) {
        this.SHIL = SHIL;
    }
}
