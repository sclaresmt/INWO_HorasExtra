package com.example.inwo.inwo_horasextra;

/**
 * Created by Sergio on 14/03/2015.
 */

import android.content.Context;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ManejadorConexion {

    static String respuesta = null;
    public final static int GET = 1;
    public final static int POST = 2;
    private Context context;

    public ManejadorConexion() {

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
            //DefaultHttpClient httpClient = new DefaultHttpClient();
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            //HttpClient httpClient = AndroidHttpClient.newInstance("Android");
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
}