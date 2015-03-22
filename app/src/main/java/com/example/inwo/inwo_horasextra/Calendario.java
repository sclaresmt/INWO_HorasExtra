package com.example.inwo.inwo_horasextra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Calendario extends ActionBarActivity {

    private GridView grid;
    private Calendar calendario3;
    private Date fechaDeHoy;
    Context contexto;
    ArrayList<Dia> arLiDiaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        contexto=this;

        calendario3 = Calendar.getInstance();
        int anio=calendario3.get(Calendar.YEAR);
        calendario3.set(anio, Calendar.JANUARY, 1);
        fechaDeHoy=calendario3.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String mesCarga = formattedDate2.substring(3);

        grid = (GridView) findViewById(R.id.grid);

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
                }
            }
        });
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
