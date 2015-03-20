package com.example.inwo.inwo_horasextra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CodigoUsuario extends ActionBarActivity {

    private EditText textoUsuario, textoPass;
    private Button btnAcceder;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_usuario);

        textoUsuario = (EditText)findViewById(R.id.textNomUsuario);
        textoPass = (EditText)findViewById(R.id.textPassUsuario);
        btnAcceder = (Button)findViewById(R.id.btnAcceder);
        preferencias = getSharedPreferences("PreferenciasHorasExtra", this.MODE_PRIVATE);
        editor = preferencias.edit();
        contexto = this;

        //Muestra el mensaje con la razón de abrir esta activity, pasada en el Intent.
        Intent intent = getIntent();
        String mensaje = intent.getStringExtra("mensaje");
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

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
                editor.putString("usuario", textoUsuario.getText().toString());
                editor.commit();
                editor.putString("pass", textoPass.getText().toString());
                editor.commit();
                new ComprobarLogin().execute();
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
     * AsyncTask que ejecuta las comprobaciones y muestra el resultado al usuario.
     */
    public class ComprobarLogin extends AsyncTask<Void ,String, Integer> {

        ProgressDialog pDialog;
        String mensaje;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(contexto);
            pDialog.setMessage("Comprobando datos de acceso. Por favor espere...");
            pDialog.show();

            //Impide que la pantalla se apague mientras el diálogo está mostrándose
            pDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setMessage(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String [] progreso = new String[1];

            ComprobadorAcceso comprobador = new ComprobadorAcceso(contexto);
            progreso[0] = "Conectando...";
            publishProgress(progreso);

            int codResultado = comprobador.comprobarUltimaConexion();
            progreso[0] = "Recibiendo resultado...";
            publishProgress(progreso);

            switch(codResultado) {
                case 0:
                    mensaje = "Por favor, introduzca sus datos de acceso.";
                    break;
                case 1:
                    mensaje = "Lo sentimos. Su usuario ha sido dado de baja.";
                    break;
                case 2:
                    mensaje = "La contraseña no es correcta. Quizá la haya cambiado.";
                    break;
                case 3:
                    mensaje = "Datos correctos.";
                    break;
                case 4:
                    mensaje = "Datos correctos.";
                    break;
                case 5:
                    mensaje = "Calendario actualizado.";
                    break;
            }

            return codResultado;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if(result==3||result==4||result==5) {
                Intent resultado = new Intent();
                resultado.putExtra("mensaje", mensaje);
                setResult(MainActivity.RESULT_OK, resultado);
                finish();
            }else{
                Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
            }
        }
    }
}
