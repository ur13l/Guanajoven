package mx.gob.jovenes.guanajuato.utils;

/**
 * Created by Uriel on 07/03/2016.
 */
public class MathFormat {

    public static String removeDots(float value){
        int valueInt = (int)value;
        float res = value - valueInt;
        if(res == 0){
            return (int)value + "";
        }
        else{
            return round(value,2) + "";
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}
