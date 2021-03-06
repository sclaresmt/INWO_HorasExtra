package com.example.inwo.inwo_horasextra;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class ActualizarHoras extends ActionBarActivity implements View.OnClickListener{

    private EditText horasRecuperadas, horasDisfrutadas, horasArt54;
    private Button btnGuardarHoras;
    private Context contexto;
    private String fechaRecibida, horaRecuperadaRecibida, horaDisfrutadaRecibida, horaArt54Recibida;
    private String diaRecibido, mesRecibido, anioRecibido;
    private String  guardarRecuperada, guardarDisfrutada;
    private boolean sumaRec, sumaDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_horas);

        sumaRec=true;
        sumaDis=true;

        Bundle recibeFecha = getIntent().getExtras();
        if (recibeFecha != null) {
            fechaRecibida = recibeFecha.getString("enviarFecha");
            diaRecibido = recibeFecha.getString("enviarDia");
            mesRecibido = recibeFecha.getString("enviarMes");
            anioRecibido = recibeFecha.getString("enviarAnio");
            horaRecuperadaRecibida = recibeFecha.getString("enviarHorasRecuperadas");
            horaDisfrutadaRecibida = recibeFecha.getString("enviarHorasDisfrutadas");
            horaArt54Recibida = recibeFecha.getString("enviarHorasArt54");
        }

        TextView fecha = (TextView) findViewById(R.id.tvFechaActuarHoras);

        fecha.setText(diaRecibido+" de "+mesRecibido+" de "+anioRecibido);



        guardarRecuperada=horaRecuperadaRecibida;
        guardarDisfrutada=horaDisfrutadaRecibida;

        horasRecuperadas = (EditText) findViewById(R.id.etHorasNormalesGuardar);
        horasDisfrutadas = (EditText) findViewById(R.id.etHorasExtraGuardar);
        horasArt54 = (EditText) findViewById(R.id.etHorasArt54Guardar);

        horasRecuperadas.setText(horaRecuperadaRecibida);
        horasDisfrutadas.setText(horaDisfrutadaRecibida);
        horasArt54.setText(horaArt54Recibida);

        btnGuardarHoras = (Button) findViewById(R.id.btnGuardarHoras);
        btnGuardarHoras.setOnClickListener(this);

        contexto=this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actualizar_horas, menu);
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

    @Override
    public void onClick(View v) {
        if(v==btnGuardarHoras)
        {
            String hRecuperadas = horasRecuperadas.getText().toString();
            String hDisfrutadas = horasDisfrutadas.getText().toString();
            String hArt54 = horasArt54.getText().toString();
            GestorBD gestor = GestorBD.getInstancia(this.contexto);
            ContentValues cv = new ContentValues();
            cv.put("horasNormales", hRecuperadas);
            cv.put("horasExtra", hDisfrutadas);
            cv.put("horasArt54", hArt54);
            gestor.actualizaDia(cv, fechaRecibida);
            gestor.close();

            if (hRecuperadas.equals("") | hRecuperadas.equals(".")) {
                hRecuperadas = "0";
            }
            if (hDisfrutadas.equals("") | hDisfrutadas.equals(".")) {
                hDisfrutadas = "0";
            }
            if (hArt54.equals("") | hArt54.equals(".")) {
                hArt54 = "0";
            }

            Double dHRecuperadas = Double.parseDouble(hRecuperadas);
            Double dHGuardarRec = Double.parseDouble(guardarRecuperada);


            if(dHRecuperadas>dHGuardarRec)
            {
                dHRecuperadas=dHRecuperadas-dHGuardarRec;
                sumaRec=true;
            }
            else if(dHRecuperadas<dHGuardarRec
                    ){
                dHRecuperadas=dHGuardarRec-dHRecuperadas;
                sumaRec=false;
            }
            else
            {
                dHRecuperadas=0.0;
            }

            Double dHDisfrutada = Double.parseDouble(hDisfrutadas);
            Double dHGuardarDisf = Double.parseDouble(guardarDisfrutada);

            if(dHDisfrutada>dHGuardarDisf)
            {
                dHDisfrutada=dHDisfrutada-dHGuardarDisf;
                sumaDis=true;
            }
            else if(dHDisfrutada<dHGuardarDisf)
            {
                dHDisfrutada=dHGuardarDisf-dHDisfrutada;
                sumaDis=false;
            }
            else
            {
                dHDisfrutada=0.0;
            }


            SharedPreferences prefs = getSharedPreferences("PreferenciasHorasExtra",Context.MODE_PRIVATE);

            Double doubleAcumuladas=Double.parseDouble(prefs.getString("horasRecuperadasDisfrutadas", "0"));

            Double operacionHoras;

            if(sumaRec){
                operacionHoras = doubleAcumuladas+dHRecuperadas;
            }else{
                operacionHoras = doubleAcumuladas-dHRecuperadas;
            }

            if(sumaDis){
                operacionHoras = operacionHoras-dHDisfrutada;
            }else{
                operacionHoras = operacionHoras+dHDisfrutada;
            }

            //operacionHoras = doubleAcumuladas+(dHRecuperadas-dHDisfrutada);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("horasRecuperadasDisfrutadas", Double.toString(operacionHoras));
            editor.commit();

            finish();
        }
    }
}
