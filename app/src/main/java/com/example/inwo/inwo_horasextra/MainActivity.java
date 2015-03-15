package com.example.inwo.inwo_horasextra;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import java.util.Date;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ArrayList<Dia> arLiDia; //Almacena todos los dias del año
    private ListView lista; //Lista para mostrar
    private int totalDiasMes; //Numero de dias que tiene el mes
    private Context contexto;
    private SharedPreferences prefs; //Preferencias
    private TextView tvHorasAcumuladas;
    private TextView tvMes;
    private int mes;
    private int acumuladoMes;
    private int anio;
    private String strMes; //String del mes
    private Button btnMesAnterior, btnMesSiguiente;
    Calendar calendario, calendario2, calendario3; //Calendario
    GestorBD gestor; //Gestor de la SQLite
    Date fechaDeHoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instancia el ArrayList arliDia
        arLiDia = new ArrayList<Dia>();

        //Instancia los botones de cambio de mes
        btnMesAnterior= (Button) findViewById(R.id.btnMesAnterior);
        btnMesSiguiente= (Button) findViewById(R.id.btnMesSiguiente);
        btnMesAnterior.setOnClickListener(this);
        btnMesSiguiente.setOnClickListener(this);

        //Contexto.
        contexto=this;

        //Instancia el gestor
        gestor = gestor.getInstancia(contexto);

       //Instancia el calendario
       calendario = Calendar.getInstance();
       calendario2 = Calendar.getInstance();
       calendario3 = Calendar.getInstance();

        fechaDeHoy=calendario3.getTime();

        //Instancia del mes
        mes=calendario3.get(Calendar.MONTH);
        acumuladoMes=0;
        anio=calendario3.get(Calendar.YEAR);

       guardarAnio();

       cargaMes();

       horasAcumuladas();
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
        if(id==R.id.cambiar_codigo_action_bar){
            Intent i = new Intent(this, CodigoUsuario.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    //Gestiona el cambio de mes
    public void cambiarMes(){
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
        tvMes.setText(strMes+" "+anio);
    }

    //Se ocupa de gestionar la carga de los meses.
    public void cargaMes(){

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate2 = df2.format(fechaDeHoy);
        String ft2 = formattedDate2.substring(3);

//        Cursor miCursor = gestor.obtenerDias(formattedDate2);
//        miCursor.moveToFirst();
//        Log.d("log1", "Cursor: "+miCursor.getColumnIndexOrThrow("fechaDia"));
//        gestor.close();

        //Carga los dias de el mes seleccionado en el ArrayList arLiDiaList y los pasa al asaptador para mostrarlos.
        ArrayList<Dia> arLiDiaList = new ArrayList<Dia>();
        arLiDiaList.clear();
        String fechaMesAnio;
        for(int i = 0; i<arLiDia.size(); i++){
            fechaMesAnio= arLiDia.get(i).getIdDia().substring(3);

            if(fechaMesAnio.equals(ft2)){

                arLiDiaList.add(new Dia(arLiDia.get(i).getIdDia(), arLiDia.get(i).getDiaNum(), arLiDia.get(i).getDiaText(),
                        arLiDia.get(i).getHoraNormal(), arLiDia.get(i).getHoraExtra(), arLiDia.get(i).getHoraArt54()));
            }
        }

        lista = (ListView) findViewById(R.id.listaDias);

        lista.setAdapter(new AdaptadorDias(this, R.layout.dia, arLiDiaList){

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

                Intent intentHoras = new Intent(contexto, ActualizarHoras.class);
                startActivityForResult(intentHoras, 1);

            }
        });

        lista.setBackgroundResource(R.drawable.cell_shape);

        cambiarMes();
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
            calendario3.add(Calendar.MONTH, -1);
            fechaDeHoy=calendario3.getTime();
            Log.d("log1", "fecha next: "+fechaDeHoy);
            mes=calendario3.get(Calendar.MONTH);
            anio=calendario3.get(Calendar.YEAR);
            cargaMes();
        }
        if(v==btnMesSiguiente){
            calendario3.add(Calendar.MONTH, 1);
            fechaDeHoy=calendario3.getTime();
            Log.d("log1", "fecha next: "+fechaDeHoy);
            mes=calendario3.get(Calendar.MONTH);
            anio=calendario3.get(Calendar.YEAR);
            cargaMes();
        }
    }

    //Guarda el año la BD.
    public void guardarAnio(){

        calendario.set(anio, Calendar.JANUARY, 1);

        //Obtiene el total de dias de ese año.
        int totalDiasAnio=0;
        calendario2.set(anio, Calendar.JANUARY, 1);
        for(int d = 0; d<12; d++){
            calendario2.add(Calendar.MONTH, d);
            totalDiasAnio=totalDiasAnio+calendario2.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        Log.d("log1", "Dias totales año: "+Integer.toString(totalDiasAnio));

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate;

        for(int i = 1; i<totalDiasAnio; i++){

            // add one day to the date/calendar
            calendario.set(Calendar.DAY_OF_YEAR, i);
            formattedDate = df.format(calendario.getTime());


            int diaDeLaSemana=calendario.get(Calendar.DAY_OF_WEEK); //Dia de la semana en el que estamos
            String strDiaDeLaSemana=null; //String del dia de la semana

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

            int diaDelMes = calendario.get(Calendar.DAY_OF_MONTH);

            // llena el ArrayList arLiDia con los dias de el mes correspondiente.
            arLiDia.add(new Dia(formattedDate, Integer.toString(diaDelMes), strDiaDeLaSemana, Integer.toString(0), Integer.toString(0), "0"));


//            ContentValues cv = new ContentValues();
//            cv.put("fechaDia", formattedDate);
//            cv.put("horasNormales", 0);
//            cv.put("horasExtra", 0);
//            cv.put("horasArt54", 0);
//            cv.put("esVacaciones", 0);
//            cv.put("esFestivo", 0);
//            cv.put("esArticulo54", 0);
////          cv.put("tipoDia", 1);
//            gestor.insertarDias(cv);
//
//            Log.d("log1", formattedDate);


        }

    }
}
