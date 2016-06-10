package code.guanajuato.gob.mx.activatecode.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.model.Agua;

/**
 * Autor: Uriel Infante
 * Archivo de code.guanajuato.gob.mx.activatecode.persistencia para la tabla de agua en la base de datos.
 * Fecha: 27/05/2016
 */
public class AguaDBHelper extends LocalDatabaseHelper{
    private static String TABLA_AGUA = "agua";

    public AguaDBHelper(Context context, String filePath) {
        super(context, filePath);
    }

    /**
     * Función para devolver la lista de todos los tipos de agua registrados.
     * @return ArrayList<Agua>: Una lista de objetos de tipo Agua.
     */
    public ArrayList<Agua> getAguaList() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        ArrayList<Agua> lista = new ArrayList<>();
        Cursor cursor = db.query(TABLA_AGUA, null, null, null, null, null, "cantidad");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Agua agua = new Agua();
            agua.setId(cursor.getInt(0));
            agua.setNombre(cursor.getString(1));
            agua.setCantidad(cursor.getFloat(2));
            agua.setUnidad(cursor.getString(3));
            agua.setEstado(cursor.getString(4));
            Gson gson = new Gson();
            Log.d("AGUA", gson.toJson(agua));
            lista.add(agua);
            cursor.moveToNext();
        }
        return lista;
    }
}
