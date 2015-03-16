package com.example.inwo.inwo_horasextra;

public class Dia {

    private String idDia, diaMes, diaSemana, horaNormal, horaExtra, horaArt54;
    private int esVacaciones, esFestivo, esArticulo54;

    public Dia(String idDia, String diaMes, String diaSemana, String horaNormal, String horaExtra, String horaArt54, int esVacaciones, int esFestivo, int esArticulo54){
        this.idDia=idDia;
        this.diaMes=diaMes;
        this.diaSemana=diaSemana;
        this.horaNormal=horaNormal;
        this.horaExtra=horaExtra;
        this.horaArt54=horaArt54;
        this.esVacaciones=esVacaciones;
        this.esFestivo=esFestivo;
        this.esArticulo54=esArticulo54;
    }

    public String getIdDia() {return idDia; }

    public String getDiaMes() {
        return diaMes;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public String getHoraNormal() {
        return horaNormal;
    }

    public String getHoraExtra() {
        return horaExtra;
    }

    public String getHoraArt54() {
        return horaArt54;
    }

    public int getEsVacaciones() {
        return esVacaciones;
    }

    public int getEsFestivo() {
        return esFestivo;
    }

    public int getEsArticulo54() {
        return esArticulo54;
    }

}
