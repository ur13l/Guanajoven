package mx.gob.jovenes.guanajuato.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.io.IOException;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Alarma;
import mx.gob.jovenes.guanajuato.persistencia.AlarmasDBHelper;
import mx.gob.jovenes.guanajuato.receivers.AlarmasBroadcastReceiver;

/**
 * Created by Uriel on 22/03/2016.
 */
public class AlarmaDialogFragment extends DialogFragment implements View.OnClickListener {
    private Alarma aa;
    private AlarmasDBHelper dbHelper;

    private TimePicker timePicker;
    private Button aceptarBtn;
    private Button cancelarBtn;
    private int position;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        aa = new Alarma();
        aa.setId(args.getInt("id"));
        aa.setIdLoginApp(args.getInt("id_login_app"));
        aa.setHora(args.getString("hora"));
        aa.setLunes(args.getBoolean("lunes"));
        aa.setMartes(args.getBoolean("martes"));
        aa.setMiercoles(args.getBoolean("miercoles"));
        aa.setJueves(args.getBoolean("jueves"));
        aa.setViernes(args.getBoolean("viernes"));
        aa.setSabado(args.getBoolean("sabado"));
        aa.setDomingo(args.getBoolean("domingo"));
        aa.setActivo(args.getBoolean("activo"));
        this.position = args.getInt("position");

        dbHelper = new AlarmasDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("DB", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dialog_fragment_alarma, parent, false);
        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        aceptarBtn = (Button) v.findViewById(R.id.aceptar_btn);
        cancelarBtn = (Button) v.findViewById(R.id.cancelar_btn);

        //Ajusta la hora de acuerdo a la alarma seleccionada al abrir.
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(Integer.parseInt(aa.getHora().split(":")[0]));
        timePicker.setCurrentMinute(Integer.parseInt(aa.getHora().split(":")[1]));

        //Se llama al Listener en el botón de aceptar.
        aceptarBtn.setOnClickListener(this);
        cancelarBtn.setOnClickListener(this);
        return v;
    }

    public static AlarmaDialogFragment newInstance(Alarma aa, int position){
        AlarmaDialogFragment fragment = new AlarmaDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id",aa.getId());
        args.putInt("id_peke", aa.getIdLoginApp());
        args.putString("hora", aa.getHora());
        args.putBoolean("lunes", aa.isLunes());
        args.putBoolean("martes", aa.isMartes());
        args.putBoolean("miercoles", aa.isMiercoles());
        args.putBoolean("jueves", aa.isJueves());
        args.putBoolean("viernes", aa.isViernes());
        args.putBoolean("sabado", aa.isSabado());
        args.putBoolean("domingo", aa.isDomingo());
        args.putBoolean("activo", aa.isActivo());
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aceptar_btn:
                String nuevaHora = timePicker.getCurrentHour() + ":"
                        + (timePicker.getCurrentMinute() > 9? timePicker.getCurrentMinute()
                        : "0"+timePicker.getCurrentMinute());
                aa.setHora(nuevaHora);
                ContentValues values = new ContentValues();
                values.put("hora", nuevaHora);
                if(aa.isActivo()) {
                    AlarmasBroadcastReceiver.registerAlarm(getActivity().getApplicationContext(), aa);
                }
                dbHelper.updateAlarma(values, aa.getId());
                if(aa.isActivo() && position != -1) {
                    AlarmasBroadcastReceiver.registerAlarm(getActivity().getApplicationContext(), aa);
                }
                if(position == -1){
                    aa.setId((int) dbHelper.insertAlarma(aa));

                    ((AlarmasActivacionFragment)getTargetFragment()).getAlarmas().add(aa);
                    ((AlarmasActivacionFragment)getTargetFragment()).getRecyclerView().getAdapter().notifyItemInserted(((AlarmasActivacionFragment) getTargetFragment()).getAlarmas().size() - 1);
                    ((AlarmasActivacionFragment)getTargetFragment()).getRecyclerView().setVisibility(View.VISIBLE);
                    ((AlarmasActivacionFragment)getTargetFragment()).getEmptyView().setVisibility(View.GONE);

                    AlarmasBroadcastReceiver.registerAlarm(getActivity().getApplicationContext(), aa);
                }
                Intent i = new Intent();
                i.putExtra("hora", nuevaHora);
                i.putExtra("position", position);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                dismiss();
                break;
            case R.id.cancelar_btn:
                dismiss();
                break;
        }

    }
}
