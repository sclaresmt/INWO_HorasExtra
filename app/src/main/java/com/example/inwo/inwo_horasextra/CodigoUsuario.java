package com.example.inwo.inwo_horasextra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;


public class CodigoUsuario extends ActionBarActivity {

    private EditText textoUsuario, textoPass;
    private Button btnAcceder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_usuario);

        textoUsuario = (EditText)findViewById(R.id.textNomUsuario);
        textoPass = (EditText)findViewById(R.id.textPassUsuario);
        btnAcceder = (Button)findViewById(R.id.btnAcceder);

        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_codigo_usuario, menu);
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

    /**
     * Comprueba si la fecha de hoy es 8 días más antigua que la de la última conexión y guarda la actual.
     */
    public void comprobarUltimaConexion(){
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        //Cogemos la fecha actual en formato americano para que al restarla tenga sentido.
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String fechaActual = df.format(cal.getTime());
        editor.putString("ultimaConexion", fechaActual);
        editor.commit();
        int diasPasados = parseInt(fechaActual) - parseInt(preferencias.getString("ultimaConexion", "inexistente"));
        if(diasPasados>7){
            comprobarAcceso(preferencias.getString("usuario", "inexistente"), preferencias.getString("pass", "inexistente"));
        }
    }

    /**
     * Comprueba los datos de acceso. Si son válidos se podrá seguir usando la app y comprobará la fecha de la última actualización.
     */
    public void comprobarAcceso(String usuario, String pass){

        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        List<NameValuePair> parametros = new ArrayList<>();

        parametros.add(new BasicNameValuePair("accion", "acceso"));
        parametros.add(new BasicNameValuePair("usuario", usuario));
        parametros.add(new BasicNameValuePair("pass", pass));
        parametros.add(new BasicNameValuePair("fechaVersion", preferencias.getString("fechaVersion", "inexistente")));

        ManejadorConexion actualizador = new ManejadorConexion(this);
        String datos = actualizador.llamadaServicioWeb("http://inwo.esy.es/api.php", ManejadorConexion.GET, parametros);

    }
}
