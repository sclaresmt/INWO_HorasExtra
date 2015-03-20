package com.example.inwo.inwo_horasextra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                "horasNormales FLOAT,"+
                "horasExtra FLOAT,"+
                "horasArt54 FLOAT,"+
                "diaMes INT,"+
                "esVacaciones TINYINT,"+
                "esFestivo TINYINT,"+
                //esEspecial? es necesario?
                "esArticulo54 TINYINT,"+
	            "diaSemana VARCHAR(5)"+
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
     * Inserta los días de un ContentValues en la BD interna.
     * @param cv Los valores (días de un mes) a introducir en la BD.
     */
    public void insertarDias(ContentValues cv){
        SQLiteDatabase bdInterna = this.getWritableDatabase();
        bdInterna.insertWithOnConflict("dias", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Obtiene todos los días de un mes de la BD interna.
     * @param mes de un día cualquiera de ese mes.
     * @return Cursor con los campos de todos los días de ese mes.
     */
    public Cursor obtenerDias(String mes){
        String [] columnas = {"fechaDia", "horasNormales", "horasExtra", "horasArt54", "esVacaciones", "esFestivo", "esArticulo54", "diaMes", "diaSemana"};
        SQLiteDatabase bdInterna = this.getReadableDatabase();
        Cursor cursor = bdInterna.rawQuery("SELECT * FROM dias WHERE fechaDia LIKE '__-"+mes+"'", null);
        return cursor;
    }

    public Cursor obtenerDia(){
        String [] columnas = {"fechaDia", "horasNormales", "horasExtra", "esVacaciones", "esFestivo", "esArticulo54"};
        SQLiteDatabase bdInterna = this.getReadableDatabase();
        Cursor cursor = bdInterna.query("dias", columnas, null, null, null, null, "fechaDia ASC", null);
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
        Log.d("Update SQLite: ", "Dia "+dia+" "+cv.getAsInteger("esVacaciones"));
    }
}
