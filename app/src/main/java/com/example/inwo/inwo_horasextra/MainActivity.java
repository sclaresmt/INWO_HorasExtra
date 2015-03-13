package com.example.inwo.inwo_horasextra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    int diaDeLaSemana;
    String strDiaDeLaSemana;
    ListView lista;
    int totalDiasMes;
    Context c;
    //Preferencias
    SharedPreferences prefs;
    TextView tvHorasAcumuladas;
    TextView tvMes;
    private ArrayList<Dia> arLiDia;
    private int mes;
    private String strMes;
    Button btnMesAnterior, btnMesSiguiente;
    Calendar calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMesAnterior= (Button) findViewById(R.id.btnMesAnterior);
        btnMesSiguiente= (Button) findViewById(R.id.btnMesSiguiente);
        btnMesAnterior.setOnClickListener(this);
        btnMesSiguiente.setOnClickListener(this);

        //Contexto.
        c=this;

       calendario = Calendar.getInstance();

        //Mes siguiente.
//        calendario.add(Calendar.MONTH, 1);
        diaDeLaSemana=calendario.get(Calendar.DAY_OF_WEEK);

        //Recoge el mes y lo gestiona.
        mes=calendario.get(Calendar.MONTH);


        // proporciona el string segun el dia de la semana en que estemos
        strDiaDeLaSemana=null;


       arLiDia = new ArrayList<Dia>();

       cargaMes();

//        Setea la horas a 0. Por si peta.
//        prefs = getSharedPreferences("PreferenciasHorasExtra",Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("horasAcumuladas", "0");
//        editor.commit();

        horasAcumuladas();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(calendario.getTime());
        Log.d("log1", "fecha: "+formattedDate);
        calendario.add(Calendar.DAY_OF_YEAR, -36);
        formattedDate = df.format(calendario.getTime());
        Log.d("log1", "fecha: "+formattedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if(id==R.id.calendario_action_bar){
            Intent intentCalendario = new Intent(this, Calendario.class);
            startActivity(intentCalendario);
        }
        if(id==R.id.hAcumuladas_action_bar){
            dialogoHorasAcumuladas();
        }

        return super.onOptionsItemSelected(item);
    }

    //Se ocupa de gestionar la carga de los meses.
    public void cargaMes(){

        switch (mes){
            case 0: strMes="Enero";
                break;
            case 1: strMes="Febrero";
                break;
            case 2: strMes="Marzo";
                break;
            case 3: strMes="Abril";
                break;
            case 4: strMes="Mayo";
                break;
            case 5: strMes="Junio";
                break;
            case 6: strMes="Julio";
                break;
            case 7: strMes="Agosto";
                break;
            case 8: strMes="Septiembre";
                break;
            case 9: strMes="Octubre";
                break;
            case 10: strMes="Noviembre";
                break;
            case 11: strMes="Diciembre";
                break;
        }
        tvMes= (TextView) findViewById(R.id.tvMes);
        tvMes.setText(strMes);

        totalDiasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int i = 1; i<totalDiasMes+1; i++){

            switch (diaDeLaSemana){
                case 1: strDiaDeLaSemana="D";
                    break;
                case 2: strDiaDeLaSemana="L";
                    break;
                case 3: strDiaDeLaSemana="M";
                    break;
                case 4: strDiaDeLaSemana="MI";
                    break;
                case 5: strDiaDeLaSemana="J";
                    break;
                case 6: strDiaDeLaSemana="V";
                    break;
                case 7: strDiaDeLaSemana="S";
                    break;
            }

            // dispone los dias de la semana
            if(diaDeLaSemana==7)
            {
                diaDeLaSemana=1;
            }else
            {
                diaDeLaSemana++;
            }

            // llena el ArrayList arLiDia con los dias de el mes correspondiente.
            arLiDia.add(new Dia(Integer.toString(i), strDiaDeLaSemana, Integer.toString(1), Integer.toString(2), "0"));
        }


        lista = (ListView) findViewById(R.id.listaDias);

        lista.setAdapter(new AdaptadorDias(this, R.layout.dia, arLiDia){

            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    TextView tvDiaNum = (TextView) view.findViewById(R.id.colDiaNum);
                    if (tvDiaNum != null)
                        tvDiaNum.setText(((Dia) entrada).getDiaNum());

                    TextView tvDiaStr = (TextView) view.findViewById(R.id.colDiaText);
                    if (tvDiaStr != null)
                        tvDiaStr.setText(((Dia) entrada).getDiaText());

                    TextView tvHora1 = (TextView) view.findViewById(R.id.colHora1);
                    if (tvHora1 != null)
                        tvHora1.setText(((Dia) entrada).getHoraNormal());

                    TextView tvHora2 = (TextView) view.findViewById(R.id.colHora2);
                    if (tvHora2 != null)
                        tvHora2.setText(((Dia) entrada).getHoraExtra());

                    TextView tvHora3 = (TextView) view.findViewById(R.id.colHora3);
                    if (tvHora3 != null)
                        tvHora3.setText(((Dia) entrada).getHoraArt54());

//                    tvDiaNum.setTextSize(tamanioFuente);

                    // color de las filas
                    if(((Dia) entrada).getDiaText()=="D")
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_rojo);
                        tvDiaStr.setBackgroundResource(R.drawable.cell_shape_rojo);
                        tvHora1.setBackgroundResource(R.drawable.cell_shape_rojo);
                        tvHora2.setBackgroundResource(R.drawable.cell_shape_rojo);
                        tvHora3.setBackgroundResource(R.drawable.cell_shape_rojo);
                    }
                    else if(((Dia) entrada).getDiaText()=="S")
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape_azul);
                        tvDiaStr.setBackgroundResource(R.drawable.cell_shape_azul);
                        tvHora1.setBackgroundResource(R.drawable.cell_shape_azul);
                        tvHora2.setBackgroundResource(R.drawable.cell_shape_azul);
                        tvHora3.setBackgroundResource(R.drawable.cell_shape_azul);
                    }
                    else
                    {
                        tvDiaNum.setBackgroundResource(R.drawable.cell_shape);
                        tvDiaStr.setBackgroundResource(R.drawable.cell_shape);
                        tvHora1.setBackgroundResource(R.drawable.cell_shape);
                        tvHora2.setBackgroundResource(R.drawable.cell_shape);
                        tvHora3.setBackgroundResource(R.drawable.cell_shape);
                    }
                }
            }
        });

        // listener para cuando pulsamos sobre una fila
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(view.getContext(), "Pulsaste:" + position, Toast.LENGTH_SHORT).show();
                Intent intentHoras = new Intent(c, ActualizarHoras.class);
                startActivityForResult(intentHoras, 1);


            }
        });

        lista.setBackgroundResource(R.drawable.cell_shape);
    }

    //Nos da el total de horas acumuladas.
    public void horasAcumuladas(){
        //Instancia las preferencias.
        prefs = getSharedPreferences("PreferenciasHorasExtra",Context.MODE_PRIVATE);

        tvHorasAcumuladas = (TextView) findViewById(R.id.horasAcumuladasResultado);
        Double totalHoras = 0.0;
        Double hNorm, hExt, d=0.0;


        for(int i =0; i<arLiDia.size();i++){
            hNorm= Double.parseDouble(arLiDia.get(i).getHoraNormal());
            hExt= Double.parseDouble(arLiDia.get(i).getHoraExtra());
            totalHoras=totalHoras+hNorm-hExt;
        }

        //Recoge las horas guardadas en las preferencias.
        d=Double.parseDouble(prefs.getString("horasAcumuladas", "0"));
        //Operacion que devuelve el total de horas.
        totalHoras=totalHoras+d;

        tvHorasAcumuladas.setText(Double.toString(totalHoras));

    }


    //Dialog que sirve para introducir las horas acumuladas
    public void dialogoHorasAcumuladas(){
        //Se infla con el xml referente al dialog
        LayoutInflater li = LayoutInflater.from(this);
        final View prompt = li.inflate(R.layout.dialog_horas_acumuladas, null);

        //Instanciamos el dialog.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Rescatamos el nombre del EditText y lo mostramos por pantalla
                        EditText etAcumuladas = (EditText) prompt.findViewById(R.id.etRecogeHorasAcumuladas);
                        String sAcumuladas = etAcumuladas.getText().toString();

                        SharedPreferences.Editor editor = prefs.edit();
                        if (sAcumuladas.equals("")) {
                            sAcumuladas = "0";
                        } else {
                            editor.putString("horasAcumuladas", sAcumuladas);
                        }

                        editor.commit();
                        horasAcumuladas();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancelamos el cuadro de dialogo
                        dialog.cancel();
                    }
                });

        // Creamos un AlertDialog y lo mostramos
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==btnMesAnterior){
            if(mes==0)
            {
                mes=11;
            }else
            {
                mes=mes-1;
            }
            arLiDia.clear();
            cargaMes();
        }
        if(v==btnMesSiguiente){
            if(mes==11)
            {
                mes=0;
            }else{
                mes=mes+1;
            }
            arLiDia.clear();
            cargaMes();
        }
    }
}
