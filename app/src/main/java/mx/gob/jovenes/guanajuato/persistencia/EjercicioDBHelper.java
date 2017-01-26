package mx.gob.jovenes.guanajuato.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Ejercicio;

/**
 * Created by Uriel on 08/03/2016.
 */
public class EjercicioDBHelper extends LocalDatabaseHelper{
    private static String TABLA_EJERCICIO = "ejercicio";

    public EjercicioDBHelper(Context context, String filePath) {
        super(context, filePath);
    }

    public ArrayList<Ejercicio> getEjerciciosActivos() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        ArrayList<Ejercicio> lista = new ArrayList<>();
        Cursor cursor = db.query(TABLA_EJERCICIO, null, null, null, null, null, "nombre");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ejercicio ejercicio = new Ejercicio();
            ejercicio.setId(cursor.getInt(0));
            ejercicio.setNombre(cursor.getString(1));
            ejercicio.setTiempo(cursor.getFloat(2));
            ejercicio.setCalorías(cursor.getFloat(3));
            ejercicio.setEstado(cursor.getString(4));
            lista.add(ejercicio);
            cursor.moveToNext();
        }
        return lista;
    }
}
