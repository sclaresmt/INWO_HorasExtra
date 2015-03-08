package com.example.inwo.inwo_horasextra;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class MainActivity extends ActionBarActivity{

int diaDeLaSemana;
String strDiaDeLaSemana;
ListView lista;
int totalDiasMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendario = Calendar.getInstance();

        //Mes siguiente.
//        calendario.add(Calendar.MONTH, 1);
        diaDeLaSemana=calendario.get(Calendar.DAY_OF_WEEK);


        totalDiasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);

        // proporciona el string segun el dia de la semana en que estemos
        strDiaDeLaSemana=null;


        ArrayList<Dia> arLiDia = new ArrayList<Dia>();

        arLiDia.add(new Dia("D", "S", "Horas Trab.", "art.13 Rec.", "art.54."));

        for(int i = 1; i<totalDiasMes+1; i++){

            // dispone los dias de la semana
            if(diaDeLaSemana==7)
            {
                diaDeLaSemana=1;
            }else
            {
                diaDeLaSemana++;
            }

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

            Random r = new Random();
            int i1 = r.nextInt(8);

            // llena el ArrayList arLiDia con los dias de el mes correspondiente.
            arLiDia.add(new Dia(Integer.toString(i), strDiaDeLaSemana, Integer.toString(i1), "", ""));
        }

//        TextView tvHora1 = (TextView) findViewById(R.id.tvTotalHora1);
        int sumaHoras=0;
        for(int h=1; h<totalDiasMes+1; h++){
            sumaHoras=sumaHoras+Integer.parseInt(arLiDia.get(h).getHoraNormal());
        }
//
//        tvHora1.setText(Integer.toString(sumaHoras));


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
                    Log.d("log1", ((Dia) entrada).getDiaNum());
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
                Toast.makeText(view.getContext(), "Pulsaste:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        lista.setBackgroundResource(R.drawable.cell_shape);

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

        return super.onOptionsItemSelected(item);
    }
}
