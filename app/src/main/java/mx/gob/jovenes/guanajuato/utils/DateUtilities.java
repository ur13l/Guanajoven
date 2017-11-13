package mx.gob.jovenes.guanajuato.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtilities {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

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

    public static boolean lessThan30Years(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String todayString  = new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance().getTime());
        try {
            Date bornDateParse = formatter.parse(date);
            Date todayDateParse = formatter.parse(todayString);
            Calendar bornDate = getCalendar(bornDateParse);
            Calendar today = getCalendar(todayDateParse);

            int years = today.get(Calendar.YEAR) - bornDate.get(Calendar.YEAR);

            if (bornDate.get(Calendar.MONTH) > today.get(Calendar.MONTH) || (bornDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) && bornDate.get(Calendar.DATE) > today.get(Calendar.DATE))) {
                years--;
            }

            return years < 30;
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            return false;
        }
    }

    public static String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat(SYSTEM_DATE_FORMAT);
        SimpleDateFormat miFormato = new SimpleDateFormat(DATE_FORMAT);

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        return calendar;
    }

}
