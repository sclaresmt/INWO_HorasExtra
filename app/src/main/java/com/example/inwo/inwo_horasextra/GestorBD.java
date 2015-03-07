package com.example.inwo.inwo_horasextra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sergio on 06/03/2015.
 */
public class GestorBD extends SQLiteOpenHelper {

    private static GestorBD instanciaBD;
    private static final int VERSION_BD = 14;
    private static final String NOMBRE_BD = "horas";

    /**
     * Constructor de la clase que gestionará la BD interna. Podrá ser utilizado por cualquier clase de la app
     * obteniendo su instancia con getInstancia. De esta forma nos aseguramos que sea un singleton. Es decir, un
     * solo objeto que represente a la BD para todo el ciclo de vida de la aplicación.
     * @param context
     */
    private GestorBD(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    /**
     * Devolverá la instancia de la BD interna, que podría ser usada por todas las clases.
     * @param context
     * @return
     */
    public static GestorBD getInstancia(Context context){
        if (instanciaBD == null) {
            instanciaBD = new GestorBD (context.getApplicationContext());
        }
        return instanciaBD;
    }

    /**
     * Crea la BD si es la primera vez que se ejecuta la aplicación en el dispositivo, o la abre si ya está creada.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE dias("+
                "fechaDia DATE PRIMARY KEY,"+
                "horasNormales DECIMAL,"+
                "horasExtra DECIMAL,"+
                "esVacaciones TINYINT,"+
                "esFestivo TINYINT,"+
                "esArticulo54 TINYINT"+
	            //tipoDia INTEGER
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        super.onOpen(db);
    }

    /**
     * Inserta los días de un año en la BD interna.
     * @param cv Los valores (días de un mes) a introducir en la BD.
     */
    public void insertarDias(ContentValues cv){
        SQLiteDatabase bdInterna = this.getWritableDatabase();
        bdInterna.insert("dias", null, cv);
    }

    /**
     * Obtiene todos los días de un mes de la BD interna.
     * @param fecha de un día cualquiera de ese mes.
     * @return Cursor con los campos de todos los días de ese mes.
     */
    public Cursor obtenerDias(String fecha){
        String [] columnas = {"fechaDia", "horasNormales", "horasExtra", "esVacaciones", "esFestivo", "esArticulo54"};
        String [] mesYAño = fecha.split("-");
        String [] parametro = {mesYAño[0]+mesYAño[1]+"%"};
        SQLiteDatabase bdInterna = this.getReadableDatabase();
        Cursor cursor = bdInterna.query("dias", columnas, null, null, null, null, "fechaDia LIKE ", null);
        return cursor;
    }

    /**
     * Actualiza los campos de un registro de la tabla dias.
     * @param cv Content values con pares índice-valor a actualizar.
     * @param dia el día a actualizar.
     */
    public void actualizaDia(ContentValues cv, String dia){
        SQLiteDatabase bdInterna = this.getWritableDatabase();
        String [] argumentosWhere = {dia};
        bdInterna.update("dias", cv, "fechaDia=?", argumentosWhere);
    }
}
