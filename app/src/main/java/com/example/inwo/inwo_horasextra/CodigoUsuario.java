package com.example.inwo.inwo_horasextra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CodigoUsuario extends ActionBarActivity {

    private EditText textoUsuario, textoPass;
    private Button btnAcceder;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_usuario);

        textoUsuario = (EditText)findViewById(R.id.textNomUsuario);
        textoPass = (EditText)findViewById(R.id.textPassUsuario);
        btnAcceder = (Button)findViewById(R.id.btnAcceder);
        preferencias = getSharedPreferences("PreferenciasHorasExtra", this.MODE_PRIVATE);
        editor = preferencias.edit();

        //Llena los editText con los datos introducidos en conexiones anteriores, si los hubiera.
        if(this.preferencias.getString("usuario", "inexistente").equals("inexistente")&&
                this.preferencias.getString("pass", "inexistente").equals("inexistente")){
            textoUsuario.setHint("Introduzca usuario");
            textoPass.setHint("Introduzca contraseña");
        }else{
            textoUsuario.setText(this.preferencias.getString("usuario", "inexistente"));
            textoPass.setText(this.preferencias.getString("pass", "inexistente"));
        }

        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                comprobarUltimaConexion();
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
}
