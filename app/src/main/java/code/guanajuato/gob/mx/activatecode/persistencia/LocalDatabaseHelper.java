package code.guanajuato.gob.mx.activatecode.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Autor: Uriel Infante
 * Archivo de code.guanajuato.gob.mx.activatecode.persistencia general.
 * Realiza la conexión con la base de datos en SQLite
 * Fecha: 27/05/2016
 */
public class LocalDatabaseHelper extends SQLiteOpenHelper{
    protected static final String DB_NAME = "code.sqlite";
    protected static final int VERSION = 11;
    protected Context context;
    protected String pathToSaveDBFile;

    public LocalDatabaseHelper(Context context, String filePath){
        super(context, DB_NAME, null, VERSION);
        this.context = context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DB_NAME).toString();

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Función para preparar la base de datos.
     * @throws IOException
     */
    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist) {
            Log.d("DB", "Database exists.");
            int currentDBVersion = getVersionId();
            //Verifica si la versión de la base de datos es mayor a la existente en el archivo.
            if (VERSION > currentDBVersion) {
                Log.d("DB", "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e("DB", e.getMessage());
                }
            }
        } else {
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("DB", e.getMessage());
            }
        }
    }

    /**
     * Función para verificar si el archivo de la base de datos existe.
     * @return boolean. true si existe, false en caso contrario.
     */
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d("DB", e.getMessage());
        }
        return checkDB;
    }

    /**
     * Función que copia la base de datos desde la carpeta de assets.
     * @throws IOException
     */
    private void copyDataBase() throws IOException {
        OutputStream os = new FileOutputStream(pathToSaveDBFile);
        InputStream is = context.getAssets().open("sqlite/"+DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
    }

    /**
     * Función que borra la base de datos existente para crear una nueva.
     */
    public void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if(file.exists()) {
            file.delete();
            Log.d("DB", "Database deleted.");
        }
    }

    /**
     * Función para obtener la versión de la base de datos del archivo de assets.
     * @return int: El número de la versión del archivo.
     */
    public int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v =  cursor.getInt(0);
        db.close();
        return v;
    }
}
