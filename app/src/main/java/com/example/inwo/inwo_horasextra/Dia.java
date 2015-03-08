package com.example.inwo.inwo_horasextra;

public class Dia {

    private String diaNum, diaText, horaNormal, horaExtra, horaArt54;

    public Dia(String diaNum, String diaText, String horaNormal, String horaExtra, String horaArt54){
        this.diaNum=diaNum;
        this.diaText=diaText;
        this.horaNormal=horaNormal;
        this.horaExtra=horaExtra;
        this.horaArt54=horaArt54;
    }

    public String getDiaNum() {
        return diaNum;
    }

    public String getDiaText() {
        return diaText;
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

}
