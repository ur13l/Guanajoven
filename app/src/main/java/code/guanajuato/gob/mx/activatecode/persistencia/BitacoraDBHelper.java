package code.guanajuato.gob.mx.activatecode.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import code.guanajuato.gob.mx.activatecode.model.Alarma;
import code.guanajuato.gob.mx.activatecode.model.Bitacora;
import code.guanajuato.gob.mx.activatecode.model.StatusReporte;

/**
 * Autor: Uriel Infante
 * Archivo de code.guanajuato.gob.mx.activatecode.persistencia para la tabla bitácora de la base de datos.
 * Permite devolver la bitácora del día, agregar un nuevo registro y actualizar.
 * Fecha: 27/05/2016
 */
public class BitacoraDBHelper extends LocalDatabaseHelper{
    private static String TABLA_BITACORA = "bitacora";

    public BitacoraDBHelper(Context context, String filePath) {
        super(context, filePath);
    }

    /**
     * Método para obtener la bitácora del día de un usuario en específico.
     * @param id_usuario: Id del usuario.
     * @param fecha: Fecha de la bitácora(Generalmente es la fecha actual de ejecución).
     * @return Bitacora: Objeto de tipo Bitacora.
     * @throws ParseException
     */
    public Bitacora bitacoraDelDia(int id_usuario, Date fecha) throws ParseException {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        Bitacora bitacora = new Bitacora(context.getApplicationContext());
        String datestr = (new java.sql.Date(fecha.getTime())).toString();
        Cursor cursor = db.query(TABLA_BITACORA,null,"id_user = '" + id_usuario + "' AND fecha = '" + datestr + "'",null,null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            bitacora.setId(cursor.getInt(0));
            bitacora.setIdUser(cursor.getInt(1));
            String startDate = cursor.getString(2);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf1.parse(startDate);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            bitacora.setFecha(sqlDate);
            bitacora.setMinutosEjercicio(cursor.getFloat(3));
            bitacora.setCaloriasEjercicio(cursor.getFloat(4));
            bitacora.setRegistrAgua(cursor.getFloat(5));
        }
        else{
            Date startDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(startDate.getTime());
            bitacora.setId(0);
            bitacora.setFecha(sqlDate);
            bitacora.setMinutosEjercicio(0);
            bitacora.setCaloriasEjercicio(0);
            bitacora.setRegistrAgua(0);
        }
        db.close();
        return bitacora;
    }

    /**
     * Función para actualizar la bitácora del día.
     * @param values: ContentValues con los valores a modificar.
     * @param id_user: Id del usuario para identificar el registro.
     * @param fecha; Sirve para traer solo la bitácora de ese día.
     */
    public void update(ContentValues values, int id_user, String fecha){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        String[] whereArgs = {id_user +"", fecha};
        int i = db.update(TABLA_BITACORA, values, "id_user = ? AND fecha = ?", whereArgs);

        db.close();
    }

    /**
     * Función para insertar una nueva bitácora en la BD.
     * @param bitacora: Objeto de tipo bitácora para salvar en la Base de Datos.
     */
    public void insert(Bitacora bitacora){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("id_user", bitacora.getIdUser());
        values.put("fecha", bitacora.getFecha().toString());
        values.put("minutos_ejercicio", bitacora.getMinutosEjercicio());
        values.put("registro_agua", bitacora.getRegistrAgua());
        db.insert(TABLA_BITACORA, null, values);
        db.close();
    }

    /**
     * Devuelve la lista con el estatus de datos de la bitácora del usuario.
     * @param id_login_app
     * @return
     */
    public ArrayList<StatusReporte> getStatusReporte(int id_login_app) throws ParseException {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
            ArrayList<StatusReporte> lista = new ArrayList<>();
            Cursor cursor = db.query(TABLA_BITACORA, null, "id_user = '" + id_login_app +"'", null, null, null, "fecha DESC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                StatusReporte sR = new StatusReporte();
                String startDate = cursor.getString(2);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf1.parse(startDate);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                sR.setFecha(sqlDate);
                sR.setEjercicio(cursor.getFloat(3) >= 30);
                sR.setEjercicioMin(cursor.getFloat(3));
                sR.setAgua(cursor.getFloat(5) >= 2000);
                sR.setAguaLt(cursor.getFloat(5));
                lista.add(sR);
                cursor.moveToNext();
            }
            db.close();
            return lista;
        }

}
