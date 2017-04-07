package mx.gob.jovenes.guanajuato.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import mx.gob.jovenes.guanajuato.model.Lugar;

/**
 * Created by ichema on 6/21/16.
 */
public class LugarDBHelper extends LocalDatabaseHelper {

    private static String TABLA_LUGAR = "lugar";


    public LugarDBHelper(Context context, String filePath) {
        super(context, filePath);
    }

    /**
     * @return
     */
    public ArrayList<Lugar> getLugares(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        ArrayList<Lugar> lista = new ArrayList<>();
        Cursor cursor = db.query(TABLA_LUGAR, null, null, null, null, null, "nombre");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Lugar aa = new Lugar();
            aa.setId_lugar(cursor.getInt(0));
            aa.setNombre(cursor.getString(1));
            aa.setTipo_instalacion(cursor.getString(2));
            aa.setDireccion(cursor.getString(3));
            aa.setColonia(cursor.getString(4));
            aa.setMunicipio(cursor.getString(5));
            aa.setCp(cursor.getInt(6));
            aa.setAdministrador(cursor.getString(7));
            aa.setTelefono(cursor.getString(8));
            aa.setEmail(cursor.getString(9));
            aa.setLatitud(cursor.getFloat(10));
            aa.setLongitud(cursor.getFloat(11));
            lista.add(aa);
            cursor.moveToNext();
        }
        db.close();
        return lista;
    }
}
