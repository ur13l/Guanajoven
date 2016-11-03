package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.adapters.RVEventosAdapter;
import code.guanajuato.gob.mx.activatecode.utils.DateUtilities;

/**
 * Created by Uriel on 11/03/2016.
 */
public class CalendarioActividadesFragment extends CustomFragment{
    private ExtendedCalendarView calendarEcv;
    private TextView mesTv;
    private TextView diaTv;
    private TextView anioTv;
    private TextView emptyTv;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private RVEventosAdapter adapter;
    private Calendar fecha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){

        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_calendario_activacion, parent, false);
        calendarEcv = (ExtendedCalendarView) v.findViewById(R.id.calendario_activacion);
        mesTv = (TextView) v.findViewById(R.id.mes_tv);
        diaTv = (TextView) v.findViewById(R.id.dia_tv);
        anioTv = (TextView) v.findViewById(R.id.anio_tv);
        rv = (RecyclerView) v.findViewById(R.id.rv_eventos);
        emptyTv = (TextView) v.findViewById(R.id.empty_view);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        fecha = Calendar.getInstance();
        mesTv.setText(DateUtilities.displayMonthShort(fecha.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault())) );
        diaTv.setText(fecha.get(Calendar.DAY_OF_MONTH) + "");
        anioTv.setText(fecha.get(Calendar.YEAR) + "");

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        ArrayList<Event> events = getEvents();
        adapter = new RVEventosAdapter(events);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //setFechas();
        calendarEcv.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
           @Override
           public void onDayClicked(Day day) {
               ArrayList<Event> events = day.getEvents();
               fecha = Calendar.getInstance();
               fecha.set(Calendar.MONTH, day.getMonth());
               fecha.set(Calendar.DAY_OF_MONTH, day.getDay());
               fecha.set(Calendar.YEAR, day.getYear());
                mesTv.setText(DateUtilities.displayMonthShort(fecha.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault())) );
                diaTv.setText(day.getDay() + "");
                anioTv.setText(day.getYear() + "");

                if (events.size() == 0){
                    emptyTv.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
               else{
                    emptyTv.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                }

               adapter = new RVEventosAdapter(events);
               rv.setAdapter(adapter);
               adapter.notifyDataSetChanged();

               }

           }
        );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NuevoEventoDialogFragment nuevoEventoDialogFragment = new NuevoEventoDialogFragment();
                nuevoEventoDialogFragment.setTargetFragment(CalendarioActividadesFragment.this, 1);
                nuevoEventoDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        return v;
    }


    @Override
    public void onResume(){
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }


    @Override
    public void onStart(){
        super.onStop();
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i){
        updateEvents();
    }


    public ArrayList<Event> getEvents(){
        ArrayList<Event> events = new ArrayList<>();

        Calendar cTemp = fecha;
        TimeZone tz = TimeZone.getDefault();

        int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));

        Cursor c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI,new String[] {CalendarProvider.ID,CalendarProvider.EVENT,
                        CalendarProvider.DESCRIPTION,CalendarProvider.LOCATION,CalendarProvider.START,CalendarProvider.END,CalendarProvider.COLOR},"?>="+CalendarProvider.START_DAY+" AND "+ CalendarProvider.END_DAY+">=?",
                new String[] {String.valueOf(startDay),String.valueOf(startDay)}, null);
        if(c != null){
            if(c.moveToFirst()){
                do{
                    Event event = new Event(c.getLong(0),c.getLong(4),c.getLong(5));
                    event.setName(c.getString(1));
                    event.setDescription(c.getString(2));
                    event.setLocation(c.getString(3));
                    event.setColor(c.getInt(6));
                    events.add(event);
                }while(c.moveToNext());
            }
            c.close();
        }

        if (events.size() == 0){
            emptyTv.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        else{
            emptyTv.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
        return events;
    }


    public void updateEvents(){
        ArrayList<Event> events = getEvents();
        adapter = new RVEventosAdapter(events);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        calendarEcv.refreshCalendar();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DateUtilities.deleteEvento(getActivity(), (int)adapter.getSelectedEvent().getEventId());
        updateEvents();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_calendario, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String dialog_title = getResources().getString(R.string.calendario);
        String dialog_message = getResources().getString(R.string.leyenda_calendario);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialog_title);
        builder.setMessage(dialog_message);

        String positiveText = "Aceptar";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

        return false;
    }
}
