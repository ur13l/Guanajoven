package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.Bitacora;
import code.guanajuato.gob.mx.activatecode.model.Ejercicio;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.persistencia.BitacoraDBHelper;
import code.guanajuato.gob.mx.activatecode.persistencia.EjercicioDBHelper;
import code.guanajuato.gob.mx.activatecode.utilities.MathFormat;

/**
 * Created by Uriel on 01/02/2016.
 */
public class RegistrarEjercicioFragment extends CustomFragment {

    public final static String MINUTOS_COLUMN = "minutos_ejercicio";
    public final static String CALORIAS_COLUMN = "calorias_ejercicio";

    private ArrayList<Ejercicio> ejercicios;
    private EjercicioDBHelper dbHelper;
    private BitacoraDBHelper bitacoraDBHelper;
    private LinearLayout ejerciciosView;
    private Button registrarBtn;
    private TextView ejercicioIndicator;
    private ProgressBar ejercicioPb;
    private float progressMin;
    private float currentProgressMin;
    private float progressCal;
    private float currentProgressCal;
    private Login session;
    private Bitacora bitacora;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        progressCal = 0;
        currentProgressCal = 0;
        progressMin = 0;
        currentProgressMin = 0;
        View v = inflater.inflate(R.layout.fragment_registrar_ejercicio, parent, false);
        ejerciciosView = (LinearLayout)v.findViewById(R.id.view_ejercicios);
        dbHelper = new EjercicioDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("DB", e.getMessage());
        }
        ejercicioPb = (ProgressBar) v.findViewById(R.id.progress_bar_ejercicio);
        registrarBtn = (Button) v.findViewById(R.id.button_registrar_agua);
        ejercicioIndicator = (TextView) v.findViewById(R.id.ejercicio_indicator);
        ejercicios = dbHelper.getEjerciciosActivos();
        for(final Ejercicio ejercicio: ejercicios){
            LinearLayout ejercicioView = (LinearLayout)inflater.inflate(R.layout.item_ejercicio, parent, false);
            ejerciciosView.addView(ejercicioView);
            TextView nombreEjercicioTv = (TextView)ejercicioView.getChildAt(0);
            TextView tiempoEjercicioTv = (TextView)ejercicioView.getChildAt(1);
            TextView caloriasEjercicioTv = (TextView) ejercicioView.getChildAt(2);
            CheckBox ejercicioChk = (CheckBox) ejercicioView.getChildAt(3);
            nombreEjercicioTv.setText(ejercicio.getNombre());
            tiempoEjercicioTv.setText(MathFormat.removeDots(ejercicio.getTiempo()) + " mins.");
            caloriasEjercicioTv.setText(MathFormat.removeDots(ejercicio.getCalorías()) + " cal.");
            ejercicioChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        currentProgressMin += ejercicio.getTiempo();
                        currentProgressCal += ejercicio.getCalorías();
                    } else {
                        currentProgressCal -= ejercicio.getCalorías();
                        currentProgressMin -= ejercicio.getTiempo();
                    }
                }
            });
        }

        session = new Login(getActivity().getApplicationContext());
        bitacoraDBHelper = new BitacoraDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        bitacora = new Bitacora(getActivity().getApplicationContext());
        progressMin = bitacora.getMinutosEjercicio();
        progressCal = bitacora.getCaloriasEjercicio();
        ejercicioPb.setProgress((int)progressMin);
        ejercicioIndicator.setText(MathFormat.removeDots(progressMin));

        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCal += currentProgressCal;
                progressMin += currentProgressMin;
                ejercicioPb.setProgress((int) progressMin);
                ejercicioIndicator.setText(MathFormat.removeDots(progressMin));

                for (int i = 0; i < ejerciciosView.getChildCount(); i++) {
                    ((CheckBox) ((LinearLayout) ejerciciosView.getChildAt(i)).getChildAt(3)).setChecked(false);
                }
                currentProgressMin = 0;
                currentProgressCal = 0;
                bitacora.setMinutosEjercicio(progressMin);
                bitacora.setCaloriasEjercicio(progressCal);

                if (bitacora.getId() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(MINUTOS_COLUMN, progressMin);
                    values.put(CALORIAS_COLUMN, progressCal);
                    bitacoraDBHelper.update(values, bitacora.getIdUser(), bitacora.getFecha().toString());
                } else {
                    bitacoraDBHelper.insert(bitacora);
                }
            }
        });
        return v;
    }

}