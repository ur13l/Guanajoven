package mx.gob.jovenes.guanajuato.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVAlarmaAdapter;
import mx.gob.jovenes.guanajuato.model.Alarma;
import mx.gob.jovenes.guanajuato.model.Login;
import mx.gob.jovenes.guanajuato.persistencia.AlarmasDBHelper;
import mx.gob.jovenes.guanajuato.receivers.AlarmasBroadcastReceiver;

/**
 * Created by Uriel on 19/03/2016.
 */
public class AlarmasActivacionFragment extends CustomFragment implements TimePickerDialog.OnTimeSetListener{
    private static final String ALARMA_REGISTRADA = "alarma_registrada_pref";
    private RecyclerView rv;
    private RVAlarmaAdapter adapter;
    private ArrayList<Alarma> alarmas;
    private AlarmasDBHelper dbHelper;
    private Login sesion;
    private SharedPreferences prefs;
    private FloatingActionButton fab;
    private TextView emptyView;
    public static int positionSel = -1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        sesion = new Login(getActivity().getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        if(savedInstanceState == null){
            dbHelper = new AlarmasDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
            try {
                dbHelper.prepareDatabase();
            } catch (IOException e) {
                Log.e("DB", e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        fab.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_alarmas_activacion, parent, false);
        dbHelper = new AlarmasDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("DB", e.getMessage());
        }
        initializeData();
        rv = (RecyclerView) v.findViewById(R.id.rv_alarmas);
        emptyView = (TextView) v.findViewById(R.id.empty_view);
        //Se hace el inflate del FAB creado en los recursos y se añade al CoordinatorLayout de la activity.
        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        if(alarmas.size() == 0){
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        adapter = new RVAlarmaAdapter(alarmas);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        AlarmasActivacionFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );

                dpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if(data.getIntExtra("position", 0) != -1) {
                LinearLayout child = (LinearLayout) rv.getLayoutManager().getChildAt(data.getIntExtra("position", 0));
                ((TextView) child.findViewById(R.id.hora_tv)).setText(data.getStringExtra("hora"));
                ((RVAlarmaAdapter) rv.getAdapter()).getItem(data.getIntExtra("position", 0)).setHora(data.getStringExtra("hora"));
            }
        }

    }

    private void initializeData(){
        alarmas = new ArrayList<Alarma>();

        alarmas = dbHelper.getAlarmas(sesion.getId());

        if(!prefs.getBoolean(ALARMA_REGISTRADA + sesion.getId(), false)) {
            for (Alarma a : alarmas) {

                if (a.isActivo()) {
                    AlarmasBroadcastReceiver.registerAlarm(getActivity().getApplicationContext(),
                            a);
                }

            }
            prefs.edit().putBoolean(ALARMA_REGISTRADA + sesion.getId(), true).commit();
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                Alarma aa = ((RVAlarmaAdapter)rv.getAdapter()).getItem(positionSel);
                dbHelper.removeAlarma(aa);
                AlarmasBroadcastReceiver.cancelAlarmIfExists(getActivity().getApplicationContext(), aa.getId(), new Intent(getActivity().getApplicationContext(), AlarmasBroadcastReceiver.class));
                alarmas.remove(positionSel);
                rv.getAdapter().notifyItemRemoved(positionSel);

                if(alarmas.size()==0){
                    rv.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    public RecyclerView getRecyclerView() {
        return rv;
    }

    public TextView getEmptyView(){ return emptyView;}

    public ArrayList<Alarma> getAlarmas() {
        return alarmas;
    }

    public static Alarma setTime(Context a, int hourOfDay, int minute, Alarma aa, int position, AlarmasActivacionFragment f){
        AlarmasDBHelper dbHelper = new AlarmasDBHelper(a, a.getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("DB", e.getMessage());
        }

        String nuevaHora = hourOfDay + ":"
                + (minute > 9? minute
                : "0"+minute);
        aa.setHora(nuevaHora);
        ContentValues values = new ContentValues();
        values.put("hora", nuevaHora);
        if(aa.isActivo()) {
            AlarmasBroadcastReceiver.registerAlarm(a.getApplicationContext(), aa);
        }
        dbHelper.updateAlarma(values, aa.getId());
        if(aa.isActivo() && position != -1) {
            AlarmasBroadcastReceiver.registerAlarm(a.getApplicationContext(), aa);
        }
        if(position == -1){
            aa.setId((int) dbHelper.insertAlarma(aa));


            f.getRecyclerView().getAdapter().notifyItemInserted(((AlarmasActivacionFragment) f).getAlarmas().size() - 1);
            f.getRecyclerView().setVisibility(View.VISIBLE);
            f.getEmptyView().setVisibility(View.GONE);

            AlarmasBroadcastReceiver.registerAlarm(a.getApplicationContext(), aa);
        }
        Intent i = new Intent();
        i.putExtra("hora", nuevaHora);
        i.putExtra("position", position);

        aa.setHora(aa.getHora());
        return aa;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        Alarma aa = new Alarma();

        aa.setActivo(true);
        aa.setLunes(true);
        aa.setMartes(true);
        aa.setMiercoles(true);
        aa.setJueves(true);
        aa.setViernes(true);
        aa.setSabado(true);
        aa.setDomingo(true);
        aa.setHora("8:00");
        aa.setIdLoginApp(sesion.getId());
        alarmas.add(setTime(getActivity(),hourOfDay,minute, aa, -1, this));
        adapter.notifyItemInserted(alarmas.size()-1);
        adapter.notifyDataSetChanged();
    }
}
