package mx.gob.jovenes.guanajuato.utils;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;


/**
 * Created by Arellano on 10/03/2016.
 */
public class DateSetting implements DatePickerDialog.OnDateSetListener{


        Context context;
        public DateSetting (Context context){
            this.context=context;

        }
        @Override
        public void onDateSet(DatePicker view, int year, int dateSetting, int dayOfMonth) {
            Toast.makeText(context, "selected date:" + dateSetting + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();

        }

}
