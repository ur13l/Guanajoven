package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.utilities.DateUtilities;
import code.guanajuato.gob.mx.activatecode.utilities.EditTextValidations;

/**
 * Created by Uriel on 11/03/2016.
 */
public class NuevoEventoDialogFragment extends DialogFragment implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    public final static String ID_EVENTOS = "nuevo_evento_id";

    private EditText tituloEt;
    private EditText descripcionEt;
    private EditText fechaInicioEt;
    private EditText horaInicioEt;
    private EditText fechaFinEt;
    private EditText horaFinEt;
    private Button aceptarBtn;

    private EditText viewFecha;
    private EditText viewHora;
    private String fechaInicioSQL;
    private String fechaFinSQL;

    private SharedPreferences prefs;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_nuevo_evento, container, false);
        getDialog().setTitle("Agregar nuevo evento");
        tituloEt = (EditText) v.findViewById(R.id.et_titulo);
        descripcionEt = (EditText) v.findViewById(R.id.et_descripcion);
        fechaInicioEt = (EditText) v.findViewById(R.id.et_fecha_inicio);
        horaInicioEt = (EditText) v.findViewById(R.id.et_hora_inicio);
        fechaFinEt = (EditText) v.findViewById(R.id.et_fecha_fin);
        horaFinEt = (EditText) v.findViewById(R.id.et_hora_fin);
        aceptarBtn = (Button) v.findViewById(R.id.btn_aceptar);

        fechaInicioEt.setKeyListener(null);
        fechaInicioEt.setOnFocusChangeListener(this);
        fechaInicioEt.setOnClickListener(this);

        fechaFinEt.setKeyListener(null);
        fechaFinEt.setOnFocusChangeListener(this);
        fechaFinEt.setOnClickListener(this);

        horaInicioEt.setKeyListener(null);
        horaInicioEt.setOnFocusChangeListener(this);
        horaInicioEt.setOnClickListener(this);

        horaFinEt.setKeyListener(null);
        horaFinEt.setOnFocusChangeListener(this);
        horaFinEt.setOnClickListener(this);

        aceptarBtn.setOnClickListener(this);

        EditTextValidations.removeErrorTyping(tituloEt);
        EditTextValidations.removeErrorTyping(descripcionEt);
        EditTextValidations.removeErrorTyping(fechaInicioEt);
        EditTextValidations.removeErrorTyping(fechaFinEt);
        EditTextValidations.removeErrorTyping(horaInicioEt);
        EditTextValidations.removeErrorTyping(horaFinEt);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_fecha_inicio:
            case R.id.et_fecha_fin:
                showCalendar((EditText) v);
                break;

            case R.id.et_hora_inicio:
            case R.id.et_hora_fin:
               showTimePicker((EditText) v);
               break;
            case R.id.btn_aceptar:
                continuar();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_fecha_inicio:
            case R.id.et_fecha_fin:
                if(hasFocus)
                    showCalendar((EditText) v);
                break;
            case R.id.et_hora_inicio:
            case R.id.et_hora_fin:
                if(hasFocus)
                    showTimePicker((EditText) v);
                break;
        }
    }

    public void showCalendar(EditText v){
        Calendar calendar = Calendar.getInstance();
        if(v.getText().length() != 0){
            String f = v.getText().toString();
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(f.split("/")[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(f.split("/")[1]) -1);
            calendar.set(Calendar.YEAR, Integer.parseInt(f.split("/")[2]));
        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                NuevoEventoDialogFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        viewFecha = v;
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    public void showTimePicker(EditText v){
        Calendar calendar = Calendar.getInstance();

        if(v.getText().length() != 0) {
            String f = v.getText().toString();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(f.split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(f.split(":")[1]));
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(NuevoEventoDialogFragment.this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
                );
        viewHora = v;
        tpd.show(getActivity().getFragmentManager(), "TimePickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        viewFecha.setText(dayOfMonth+ "/"+monthOfYear+"/"+year);

        if(viewFecha.getId() == R.id.et_fecha_inicio) {
            fechaInicioSQL = year + "-" +
                    (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear + "") + "-" +
                    (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "");
        }
        else{
            fechaFinSQL = year + "-" +
                    (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear + "") + "-" +
                    (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "");
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String strMinute = (minute < 10 ? "0" + minute : minute + "");
        viewHora.setText(hourOfDay + ":" + strMinute);
    }

    public void continuar(){
        boolean tituloVacio = EditTextValidations.esCampoVacio(tituloEt);
        boolean descVacio = EditTextValidations.esCampoVacio(descripcionEt);
        boolean f1Vacio = EditTextValidations.esCampoVacio(fechaInicioEt);
        boolean h1Vacio = EditTextValidations.esCampoVacio(horaInicioEt);
        boolean f2Vacio = EditTextValidations.esCampoVacio(fechaFinEt);
        boolean h2Vacio = EditTextValidations.esCampoVacio(horaFinEt);

        if(!tituloVacio && !descVacio && !f1Vacio && !h1Vacio && !f2Vacio && !h2Vacio ){
            String f1 = fechaInicioSQL + " " + horaInicioEt.getText().toString() + ":00";
            String f2 = fechaFinSQL + " " + horaFinEt.getText().toString() + ":00";
            if(EditTextValidations.compararFechaHora(fechaInicioEt, horaInicioEt, f1, f2)){
                int id = 10000 + prefs.getInt(ID_EVENTOS, 1);
                String titulo = tituloEt.getText().toString();
                String descripcion = descripcionEt.getText().toString();
                Date fechaInicio = DateUtilities.stringToDate(f1);
                Date fechaFin = DateUtilities.stringToDate(f1);
                int tipo = 0;

                prefs.edit().putInt(ID_EVENTOS, prefs.getInt(ID_EVENTOS,1) + 1).commit();
                DateUtilities.setFechas(getActivity(), id, fechaInicio, fechaFin,  titulo, descripcion, tipo);
                getTargetFragment().onActivityResult(1, 1, null);
                dismiss();
            }

        }
    }
}
