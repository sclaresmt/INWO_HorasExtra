package com.example.inwo.inwo_horasextra;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Calendario extends ActionBarActivity {

    private GridView grid, grid2, grid3, grid4, grid5, grid6, grid7, grid8, grid9, grid10, grid11, grid12;
    private Calendar calendario3;
    private Date fechaDeHoy;
    Context contexto;
    ArrayList<Dia> arLiDiaList;
    int anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        grid = (GridView) findViewById(R.id.gridView);
        grid2 = (GridView) findViewById(R.id.gridView2);
        grid3 = (GridView) findViewById(R.id.gridView3);
        grid4 = (GridView) findViewById(R.id.gridView4);
        grid5 = (GridView) findViewById(R.id.gridView5);
        grid6 = (GridView) findViewById(R.id.gridView6);
        grid7 = (GridView) findViewById(R.id.gridView7);
        grid8 = (GridView) findViewById(R.id.gridView8);
        grid9 = (GridView) findViewById(R.id.gridView9);
        grid10 = (GridView) findViewById(R.id.gridView10);
        grid11 = (GridView) findViewById(R.id.gridView11);
        grid12 = (GridView) findViewById(R.id.gridView12);

        contexto=this;

        calendario3 = Calendar.getInstance();
        anio=calendario3.get(Calendar.YEAR);

        m1();m2();m3();m4();m5();m6();m7();m8();m9();m10();m11();m12();
    }

    public void m1(){
        TextView anioCaledario = (TextView) findViewById(R.id.txEne);
        anioCaledario.setText(getString(R.string.enero)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.set(anio, Calendar.JANUARY, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid.getLayoutParams();

            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m2(){
        TextView anioCaledario = (TextView) findViewById(R.id.txFeb);
        anioCaledario.setText(getString(R.string.febrero)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid2.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid2.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid2.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m3(){
        TextView anioCaledario = (TextView) findViewById(R.id.txMar);
        anioCaledario.setText(getString(R.string.marzo)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid3.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid3.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid3.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m4(){
        TextView anioCaledario = (TextView) findViewById(R.id.txAbr);
        anioCaledario.setText(getString(R.string.abril)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid4.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid4.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid4.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m5(){
        TextView anioCaledario = (TextView) findViewById(R.id.txMay);
        anioCaledario.setText(getString(R.string.mayo)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid5.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid5.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid5.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m6(){
        TextView anioCaledario = (TextView) findViewById(R.id.txJun);
        anioCaledario.setText(getString(R.string.junio)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid6.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid6.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid6.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m7(){
        TextView anioCaledario = (TextView) findViewById(R.id.txJul);
        anioCaledario.setText(getString(R.string.julio)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid7.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid7.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid7.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m8(){
        TextView anioCaledario = (TextView) findViewById(R.id.txAgo);
        anioCaledario.setText(getString(R.string.agosto)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid8.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid8.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid8.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m9(){
        TextView anioCaledario = (TextView) findViewById(R.id.txSep);
        anioCaledario.setText(getString(R.string.septiembre)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid9.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid9.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid9.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m10(){
        TextView anioCaledario = (TextView) findViewById(R.id.txOct);
        anioCaledario.setText(getString(R.string.octubre)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid10.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid10.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid10.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m11(){
        TextView anioCaledario = (TextView) findViewById(R.id.tNov);
        anioCaledario.setText(getString(R.string.noviembre)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid11.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid11.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid11.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public void m12(){
        TextView anioCaledario = (TextView) findViewById(R.id.txDic);
        anioCaledario.setText(getString(R.string.diciembre)+" "+anio);
        anioCaledario.setBackgroundResource(R.drawable.cell_shape_rojo);

        calendario3.add(Calendar.MONTH, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        Log.d("log1", "fecha: "+ mesCarga);

        arLiDiaList = new ArrayList<Dia>();

        GestorBD gestor = GestorBD.getInstancia(this.contexto);
        Cursor miCursor = gestor.obtenerDias(mesCarga);

        while (miCursor.moveToNext()){

            arLiDiaList.add(new Dia(
                    miCursor.getString(miCursor.getColumnIndexOrThrow("fechaDia")),
                    String.valueOf(miCursor.getInt(miCursor.getColumnIndexOrThrow("diaMes"))),
                    miCursor.getString(miCursor.getColumnIndexOrThrow("diaSemana")),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasNormales"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasExtra"))),
                    String.valueOf(miCursor.getFloat(miCursor.getColumnIndexOrThrow("horasArt54"))),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esVacaciones")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esFestivo")),
                    miCursor.getInt(miCursor.getColumnIndexOrThrow("esArticulo54"))
            ));
        }

        miCursor.close();
        gestor.close();

        String dia1 = arLiDiaList.get(0).getDiaSemana();
        int numDiaSemana = 0;

        switch (dia1){
            case "L": numDiaSemana=0;
                break;
            case "M": numDiaSemana=1;
                break;
            case "MI": numDiaSemana=2;
                break;
            case "J": numDiaSemana=3;
                break;
            case "V": numDiaSemana=4;
                break;
            case "S": numDiaSemana=5;
                break;
            case "D": numDiaSemana=6;
                break;
        }


        for(int i=0; i<numDiaSemana; i++){
            arLiDiaList.add(0, new Dia("", "", "", "", "", "", 0, 0, 0));

        }

        if(arLiDiaList.size()>19&&arLiDiaList.size()<35){
            int rellenoCasillasFinal = 35-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add(new Dia("", "", "", "", "", "", 0, 0, 0));
            }

        }
        else if(arLiDiaList.size()>35){
            ViewGroup.LayoutParams layoutParams = grid12.getLayoutParams();
            layoutParams.height = convertDpToPixels(240, this); //this is in pixels
            grid12.setLayoutParams(layoutParams);

            int rellenoCasillasFinal = 42-arLiDiaList.size();
            for(int i=0; i<rellenoCasillasFinal; i++){
                arLiDiaList.add( new Dia("", "", "", "", "", "", 0, 0, 0));
            }
        }

        grid12.setAdapter(new AdaptadorDias(this, R.layout.dia_calendario, arLiDiaList){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.txDiaCalendario);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaMes());

                    //color de las filas
                    if(((Dia) entrada).getDiaSemana().equals(""))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_gris);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("D"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);

                    }
                    else if(((Dia) entrada).getDiaSemana().equals("S"))
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);

                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);

                    }
                    if(((Dia) entrada).getEsVacaciones()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_verde);
                    }
                    if(((Dia) entrada).getEsArticulo54()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_cyan);
                    }
                    if(((Dia) entrada).getEsFestivo()==1)
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                }
            }
        });
    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
