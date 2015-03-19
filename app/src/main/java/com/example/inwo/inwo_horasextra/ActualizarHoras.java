package com.example.inwo.inwo_horasextra;

import android.content.ContentValues;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ActualizarHoras extends ActionBarActivity implements View.OnClickListener{

    private EditText horasRecuperadas, horasDisfrutadas, horasArt54;
    private Button btnGuardarHoras;
    private Context contexto;
    private String fechaRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_horas);

        Bundle recibeFecha = getIntent().getExtras();
        if (recibeFecha != null) {
            fechaRecibida = recibeFecha.getString("enviarFecha");
        }

        horasRecuperadas = (EditText) findViewById(R.id.etHorasNormalesGuardar);
        horasDisfrutadas = (EditText) findViewById(R.id.etHorasExtraGuardar);
        horasArt54 = (EditText) findViewById(R.id.etHorasArt54Guardar);

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
            GestorBD gestor = GestorBD.getInstancia(this.contexto);
            ContentValues cv = new ContentValues();
            cv.put("horasNormales", horasRecuperadas.getText().toString());
            cv.put("horasExtra", horasDisfrutadas.getText().toString());
            cv.put("horasArt54", horasArt54.getText().toString());
            gestor.actualizaDia(cv, fechaRecibida);
            gestor.close();
            finish();
        }
    }
}
