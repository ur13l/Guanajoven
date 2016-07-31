 package code.guanajuato.gob.mx.activatecode.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.model.Alarma;

 /**
 * Created by Uriel on 22/03/2016.
 */
public class AlarmasDBHelper extends LocalDatabaseHelper {
    private static String TABLA_ALARMAS = "alarma";


    public AlarmasDBHelper(Context context, String filePath) {
        super(context, filePath);
    }


    /**
     * Método para registrar las alarmas por defecto que debe tener un peke.
     * @param idUsuario: Id del usuario registrando la alarma.
     */
    public void registrarAlarmasDefault(int idUsuario) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues values = new ContentValues();
        values.put("id_login_app", idUsuario);
        values.put("hora", "8:00");
        values.put("lunes", 1);
        values.put("martes", 1);
        values.put("miercoles", 1);
        values.put("jueves", 1);
        values.put("viernes", 1);
        values.put("sabado", 1);
        values.put("domingo", 1);
        values.put("estado", 0);
        db.insert(TABLA_ALARMAS, null, values);

        db.close();

    }


    /**
     * Método para actualizar una alarma, sin importar el campo que se modificó.
     * @param values: Valores que se actualizarán;
     * @param id: Id de la alarma que se modificará.
     */
    public void updateAlarma(ContentValues values, int id) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String[] whereArgs = {id + ""};
        db.update(TABLA_ALARMAS, values, "id = ?", whereArgs);
        db.close();
    }


    /**
     * Método que devuelve la lista de alarmas de agua de un Peke.
     * @param id_login_app
     * @return
     */
    public ArrayList<Alarma> getAlarmas(int id_login_app){
       SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        ArrayList<Alarma> lista = new ArrayList<>();
        Cursor cursor = db.query(TABLA_ALARMAS, null, "id_login_app = '" + id_login_app +"'", null, null, null, "id");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Alarma aa = new Alarma();
            aa.setId(cursor.getInt(0));
            aa.setIdLoginApp(cursor.getInt(1));
            aa.setHora(cursor.getString(2));
            aa.setLunes(cursor.getInt(3) > 0);
            aa.setMartes(cursor.getInt(4) > 0);
            aa.setMiercoles(cursor.getInt(5) > 0);
            aa.setJueves(cursor.getInt(6) > 0);
            aa.setViernes(cursor.getInt(7) > 0);
            aa.setSabado(cursor.getInt(8) > 0);
            aa.setDomingo(cursor.getInt(9) > 0);
            aa.setActivo(cursor.getInt(10)>0);
            lista.add(aa);
            cursor.moveToNext();
        }
        db.close();
        return lista;
    }


    /**
     * Método para eliminar una alarma de agua.
     * @param aa: Instancia de la alarma
     */
    public void removeAlarma(Alarma aa){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String[] whereArgs = {aa.getId() + ""};
        db.delete(TABLA_ALARMAS, "id = ?", whereArgs);
        db.close();
    }


    /**
     * Método para insertar una nueva Alarma.
     * @param aa: La instancia de la alarma generada (sin ID)
     * @return LA función devuelve el ID generado en la BD para poder identificar al elemento.
     */
    public long insertAlarma(Alarma aa) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);

        //Content Values que contiene los elementos de la nueva alarma
        ContentValues values = new ContentValues();
        values.put("id_login_app", aa.getIdLoginApp());
        values.put("hora", aa.getHora());
        values.put("lunes", aa.isLunes());
        values.put("martes", aa.isMartes());
        values.put("miercoles", aa.isMiercoles());
        values.put("jueves", aa.isJueves());
        values.put("viernes", aa.isViernes());
        values.put("sabado", aa.isSabado());
        values.put("domingo", aa.isDomingo());
        values.put("estado", aa.isActivo());
        long ret = db.insert(TABLA_ALARMAS, null, values);
        db.close();
        return ret;
    }

}