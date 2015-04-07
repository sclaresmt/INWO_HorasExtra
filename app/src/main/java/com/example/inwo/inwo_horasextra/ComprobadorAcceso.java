package com.example.inwo.inwo_horasextra;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Sergio on 19/03/2015.
 */
public class ComprobadorAcceso {

    private Context context;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    Calendar cal;
    String fechaActual, diaAnyioActual;

    public ComprobadorAcceso(Context contexto){
        this.context = contexto;
        preferencias = this.context.getSharedPreferences("PreferenciasHorasExtra", this.context.MODE_PRIVATE);
        editor = preferencias.edit();
    }

    /**
     * Comprueba si la fecha de hoy es 8 días más antigua que la de la última conexión y guarda la actual.
     */
    public int comprobarUltimaConexion(){

        //Cogemos la fecha actual en formato día del año para que al restarla tenga sentido.
        DateFormat dfDias = new SimpleDateFormat("D");
        cal = Calendar.getInstance();
        diaAnyioActual = dfDias.format(cal.getTime());

        int diasPasados = -1;
        int resultado = 0;
        //Si ya se había conectado antes, comprueba el número de días desde la última conexión.
        if(this.preferencias.getString("ultimaConexion", "inexistente").equals("inexistente")==false) {
            diasPasados = parseInt(diaAnyioActual) - parseInt(this.preferencias.getString("ultimaConexion", "inexistente"));
        }
//        Log.d("Fechas", "Ultima conexión: " + this.preferencias.getString("ultimaConexion", "inexistente"));
//        Log.d("Fechas", "Fecha día del año: "+ diaAnyioActual);
//        Log.d("Fechas", "Días pasados: "+ diasPasados);

        //Si los días pasados desde la última son más de 7 comprueba el acceso.
        if(diasPasados>7||diasPasados<0){
            if(this.preferencias.getString("usuario", "inexistente").equals("inexistente")&&
                    this.preferencias.getString("pass", "inexistente").equals("inexistente")){
                resultado = 0;
            }else{
                resultado = comprobarAcceso(this.preferencias.getString("usuario", "inexistente"),this.preferencias.getString("pass", "inexistente"));
            }
        }else{
            resultado = 3;
        }
        return resultado;
    }

    /**
     * Comprueba los datos de acceso. Si son válidos se podrá seguir usando la app y comprobará la fecha de la última actualización.
     */
    public int comprobarAcceso(String usuario, String pass){

        //Formateamos la fecha de hoy para sacar el año.
        DateFormat dfNormal = new SimpleDateFormat("yyyyMMdd");
        fechaActual = dfNormal.format(cal.getTime());
        Log.d("Fechas", "Fecha actual: "+ fechaActual);

        //Guardamos la id del dispositivo
        String android_id = Settings.Secure.getString(this.context.getContentResolver(),Settings.Secure.ANDROID_ID);

        List<NameValuePair> parametros = new ArrayList<>();

        parametros.add(new BasicNameValuePair("accion", "loginUsuario"));
        parametros.add(new BasicNameValuePair("usuario", usuario));
        parametros.add(new BasicNameValuePair("pass", pass));
        parametros.add(new BasicNameValuePair("año", fechaActual.substring(0, 4)));
        parametros.add(new BasicNameValuePair("version", this.preferencias.getString("version", "inexistente")));
        parametros.add(new BasicNameValuePair("idDispositivo", android_id));

        ManejadorConexion actualizador = new ManejadorConexion(this.context);
        String datos = actualizador.llamadaServicioWeb("http://inwo.esy.es/api.php", ManejadorConexion.GET, parametros);
        int codRespuesta = actualizador.comprobarRespuesta(datos);

        if(codRespuesta==4||codRespuesta==5){
            //Guarda la fecha de hoy como la fecha de última conexión en las preferencias.
            this.editor.putString("ultimaConexion", diaAnyioActual);
            this.editor.commit();
        }

        return codRespuesta;
    }
}
