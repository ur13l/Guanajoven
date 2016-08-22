package code.guanajuato.gob.mx.activatecode.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Time;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by code on 12/05/16.
 */
public class DateUtilities {

    public static Date stringToDate(String sqlDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = formatter.parse(sqlDate);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String dateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }


    /**
     * Función para guardar o actualizar un nuevo evento en la base de datos del calendario.
     * @param context: Contexto de la aplicación.
     * @param id: Id asignado al evento.
     * @param date1: Fecha de inicio
     * @param date2: Fecha de finalización
     * @param title: Título
     * @param description: Descripción del evento.
     * @param tipo: Tipo del evento (int)
     */
    public static void setFechas(Context context, int id, Date date1, Date date2, String title, String description, int tipo){
        Calendar cal1 = dateToCalendar(date1);
        Calendar cal2 = dateToCalendar(date2);
        ContentValues values = new ContentValues();
        values.put(CalendarProvider.ID, id);
        values.put(CalendarProvider.DESCRIPTION, description);
        values.put(CalendarProvider.LOCATION, "Some location");
        values.put(CalendarProvider.EVENT, title);

        switch (tipo){
            case 0:
                values.put(CalendarProvider.COLOR, Event.COLOR_BLUE);
                break;
            case 1:
                values.put(CalendarProvider.COLOR, Event.COLOR_GREEN);
                break;
            case 2:
                values.put(CalendarProvider.COLOR, Event.COLOR_PURPLE);
                break;
        }

        TimeZone tz = TimeZone.getDefault();
        int startDayJulian = Time.getJulianDay(cal1.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal1.getTimeInMillis())));
        values.put(CalendarProvider.START, cal1.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, startDayJulian);


        int endDayJulian = Time.getJulianDay(cal2.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal2.getTimeInMillis())));
        values.put(CalendarProvider.END, cal2.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, endDayJulian);

        String mSelectionClause = CalendarProvider.ID + " = ? ";
        String[] mSelectionArgs = {""+id};
        String[] projection = {CalendarProvider.ID};

        Cursor cursor = context.getContentResolver().query(CalendarProvider.CONTENT_URI, projection, mSelectionClause, mSelectionArgs, null );

        if(cursor == null || cursor.getCount() == 0){
            Uri uri = context.getContentResolver().insert(CalendarProvider.CONTENT_URI, values);
        }
        else {
            context.getContentResolver().update(CalendarProvider.CONTENT_URI, values, mSelectionClause, mSelectionArgs);
        }

    }

    /**
     * Función para eliminar un evento existente en la base de datos del calendario de activación
     * @param context: Contexto de la aplicación
     * @param id: Id del elemento a eliminar.
     */
    public static void deleteEvento(Context context, int id){
        // Defines selection criteria for the rows you want to delete
        String mSelectionClause = CalendarProvider.ID + " = ? ";
        String[] mSelectionArgs = {""+id};

        // Defines a variable to contain the number of rows deleted
        int mRowsDeleted = 0;


        // Deletes the words that match the selection criteria
        mRowsDeleted = context.getContentResolver().delete(
                CalendarProvider.CONTENT_URI,   // the user dictionary content URI
                mSelectionClause,                    // the column to select on
                mSelectionArgs                      // the value to compare to
        );
    }

    /**
     * Función para acortar los nombres de los meses en español. Ej. enero - ENE
     * @param mes
     * @return
     */
    public static String displayMonthShort(String mes){
        switch (mes){
            case "enero":
                return "ENE";
            case "febrero":
                return "FEB";
            case "marzo":
                return "MAR";
            case "abril":
                return "ABR";
            case "mayo":
                return "MAY";
            case "junio":
                return "JUN";
            case "julio":
                return "JUL";
            case "agosto":
                return "AGO";
            case "septiembre":
                return "SEP";
            case "octubre":
                return "OCT";
            case "noviembre":
                return "NOV";
            case "diciembre":
                return "DIC";
        }
        return null;
    }


}
