package com.example.inwo.inwo_horasextra;

/**
 * Created by Sergio on 14/03/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ManejadorConexion {

    static String respuesta = null;
    public final static int GET = 1;
    public final static int POST = 2;
    private Context context;

    public ManejadorConexion(Context context) {
        this.context=context;
    }

    /**
     * Llama al servicio web y obtiene una respuesta.
     * @url - url al que hacer la petición
     * @method - método de petición http
     * @params - http request params
     * */
    public String llamadaServicioWeb(String url, int method, List<NameValuePair> params) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //Parámetros para comprobar el tiempo de respuesta
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            //Crea el objeto cliente
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpEntity httpEntity = null;
            HttpResponse respuestaHttp = null;
            Log.d("GCMDemo", "Va a hacer el post");

            //Comprueba el método de petición y cambia los parámetros
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                //Añade los parámetros POST
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    Log.d("GCMDemo", "Pone los datos en el post");
                }
                respuestaHttp = httpClient.execute(httpPost);
                Log.d("GCMDemo", "Hace el post. Respuesta="+respuestaHttp);

            } else if (method == GET) {
                //Añadimos los parámetros a la url, codificándolos a utf-8 por si acaso
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                respuestaHttp = httpClient.execute(httpGet);
            }
            httpEntity = respuestaHttp.getEntity();
            respuesta = EntityUtils.toString(httpEntity);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            respuesta = "null";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            respuesta = "null";
        } catch (IOException e) {
            e.printStackTrace();
            respuesta = "null";
        }

        return respuesta;
    }

    public int comprobarRespuesta(String resp){
        Log.d("Respuesta HTTP: ", resp);
        String descripcion = "null";
        int codRespuesta = 0;
        if(respuesta!="null"){
            try {
                JSONObject obj = new JSONObject(resp);
                descripcion = obj.getString("Descripcion");
                switch (descripcion){
                    case "Error: Usuario incorrecto.":
                        codRespuesta = 1;
                        break;

                    case "Error: Contraseña incorrecta.":
                        codRespuesta = 2;
                        break;

                    case "Acceso correcto.":
                        codRespuesta = 4;
                        break;

                    case "Calendario actualizado.":
                        actualizarDias(resp);
                        codRespuesta = 5;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return codRespuesta;
        }else{
            codRespuesta=6;
        }
        return codRespuesta;
    }

    public void actualizarDias(String datos){

        try {
            JSONObject obj = new JSONObject(datos);
            if(obj.getJSONArray("Dias")!=null) {
                JSONArray arrayDias = obj.getJSONArray("Dias");
                ContentValues cv = new ContentValues();
                GestorBD gestor = GestorBD.getInstancia(this.context);

                for (int i = 0; i < arrayDias.length(); i++) {

                    JSONObject a = arrayDias.getJSONObject(i);
                    String dia = a.getString("dia");

                    JSONObject b = arrayDias.getJSONObject(i);
                    int vacaciones = Integer.valueOf(b.getString("esVacaciones"));
                    cv.put("esVacaciones", vacaciones);

                    JSONObject c = arrayDias.getJSONObject(i);
                    int festivo = Integer.valueOf(c.getString("esFestivo"));
                    cv.put("esFestivo", festivo);

                    JSONObject d = arrayDias.getJSONObject(i);
                    int especial = Integer.valueOf(d.getString("esEspecial"));
                    cv.put("esArticulo54", especial);

                    gestor.actualizaDia(cv, dia);
                }
                gestor.close();

                //Actualiza la última version en las preferencias.
                SharedPreferences preferencias = context.getSharedPreferences("PreferenciasHorasExtra", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("version", obj.getString("Version"));
                editor.commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}