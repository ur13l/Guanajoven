package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.HomeActivity;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Bitacora;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.model.Perfil;
import mx.gob.jovenes.guanajuato.model.PerfilPOJO;
import mx.gob.jovenes.guanajuato.model.models_tmp.Imagen;
import mx.gob.jovenes.guanajuato.notifications.FirebaseInstanceIDService;
import mx.gob.jovenes.guanajuato.persistencia.AlarmasDBHelper;
import mx.gob.jovenes.guanajuato.persistencia.BitacoraDBHelper;
import mx.gob.jovenes.guanajuato.receivers.AlarmaBootReceiver;
import mx.gob.jovenes.guanajuato.receivers.RetrieveVideosBroadcastReceiver;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.FileUtils;
import mx.gob.jovenes.guanajuato.utils.PublicidadSingleton;

/**
 * Autor: Uriel Infante
 * Fragment de Home.
 * La ventana principal del proyecto, es la que se abre cuando el usuario inicia sesión.
 * Muestra una imagen de CODE, las barras de estado de agua y ejercicio, una vista del calendario de activación
 * y una sección de publicidad donde se van a promover eventos.
 * Fecha: 27/05/2016
 */
public class HomeFragment extends CustomFragment {
    public final static String FECHA_ACTUALIZACION = "fecha_actualizacion";
    private Sesion session;




    //Preferencias almacenadas del usuario
    private SharedPreferences prefs;




    //Al crearse el fragment se genera el singleton que contendrá la lista de anuncios disponibles
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Asignando al usuario activo
        session = new Sesion(getActivity().getApplicationContext());

        //Declarando las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        String fecha = prefs.getString(FECHA_ACTUALIZACION,  DateUtilities.dateToString(DateUtilities.stringToDate("2002-02-02 00:00:00")));
        new NuevosEventosAsyncTask().execute(fecha); //TODO: Cambiar por nuevo servicio
        new RecibirImagenesAsyncTask().execute(); //TODO: Cambiar por otro servicio

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Actívate CODE");
        View v = inflater.inflate(R.layout.fragment_home, parent, false);


        //Función para activar las alarmas de Descargar videos.
        if(!prefs.getBoolean(RetrieveVideosBroadcastReceiver.REGISTERED_ALARM, false)){

            RetrieveVideosBroadcastReceiver.registerAlarm(getActivity());
            prefs.edit().putBoolean(RetrieveVideosBroadcastReceiver.REGISTERED_ALARM, true).commit();
        }

        initEvents();


       return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Haciendo que la barra se pueda ocultar
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        //ScrollFlag para el comportamiento deseado "scroll|snap|enterAlways"
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS + AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        toolbar.setLayoutParams(params);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinatorLayout);

        //Añadir toolbar en el fondo.
        //Toolbar bottomToolbar = (Toolbar) getActivity().getLayoutInflater().inflate(R.layout.toolbar_bottom_home,coordinatorLayout,false);
        //coordinatorLayout.addView(bottomToolbar);




    }

    @Override
    public void onStart(){
        super.onStart();

        try {
            setValoresSesion();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(){

        super.onStop();
    }


    /**
     * Asigna los valores de la sesión y la bitácora.
     * @throws ParseException
     */
    public void setValoresSesion() throws ParseException {
        session = new Sesion(getActivity().getApplicationContext());

        FirebaseMessaging.getInstance().subscribeToTopic("mx.gob.jovenes.guanajuato.CODEApp");
        FirebaseInstanceId.getInstance().getToken();
        new EnviarTokenAsyncTask().execute(); //TODO: Servicio para el token

    }

    /**
     * Función para cambiar el fragment activo utilizando un botón
     * @param id_menu: Es el id del recurso del menú que se está utilizando.
     */
    public void cambiarFragment(int id_menu){
        //a mi me guta mas AQUI porque ve,
        Intent i = new Intent(getActivity(), SegundaActivity.class);
        i.putExtra(HomeActivity.MENU_ID, id_menu);
        startActivity(i);
    }



    public void initEvents() {
        ArrayList<Event> events = new ArrayList<>();

        Calendar cTemp = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();

        int startDay = Time.getJulianDay(cTemp.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cTemp.getTimeInMillis())));

        Cursor c = getActivity().getContentResolver().query(CalendarProvider.CONTENT_URI, new String[]{CalendarProvider.ID, CalendarProvider.EVENT,
                        CalendarProvider.DESCRIPTION, CalendarProvider.LOCATION, CalendarProvider.START, CalendarProvider.END, CalendarProvider.COLOR}, "?>=" + CalendarProvider.START_DAY + " AND " + CalendarProvider.END_DAY + ">=?",
                new String[]{String.valueOf(startDay), String.valueOf(startDay)}, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Event event = new Event(c.getLong(0), c.getLong(4), c.getLong(5));
                    event.setName(c.getString(1));
                    event.setDescription(c.getString(2));
                    event.setLocation(c.getString(3));
                    event.setColor(c.getInt(6));
                    events.add(event);
                } while (c.moveToNext());
            }
            c.close();
        }


    }




    /**
     * Clase privada para la llamada asíncrona que descarga los eventos más recientes
     */
    private class NuevosEventosAsyncTask extends AsyncTask<String, Void, ArrayList<Evento>> {


        @Override
        protected ArrayList<Evento> doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("fecha_actualizacion", args[0].toString());
            String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/eventos/nuevosEventos.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            Log.d("RESULTAD", result);
            return gson.fromJson(result, new TypeToken<List<Evento>>(){
            }.getType());
        }

        @Override
        public void onPostExecute(ArrayList<Evento> result) {
            super.onPostExecute(result);
            if (result != null) {
                prefs.edit().putString(FECHA_ACTUALIZACION, DateUtilities.dateToString(new Date())).commit();

                if(result.size() > 0) {
                    for (Evento e : result) {
                        if(e.getEstado() == 0){
                            DateUtilities.deleteEvento(getActivity(), e.getId_evento());
                        }
                        else {
                            DateUtilities.setFechas(getActivity(),
                                    e.getId_evento(),
                                    DateUtilities.stringToDate(e.getFecha_inicio()),
                                    DateUtilities.stringToDate(e.getFecha_fin()),
                                    e.getTitulo(),
                                    e.getDescripcion(),
                                    e.getTipo()
                            );
                        }
                    }

                }
            }
        }
    }

    private class ObternerAsyncTask extends AsyncTask<Integer, Void, String>{
        @Override
        protected String doInBackground(Integer... args) {
            String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/registro/obtenerPerfil.php";
            ClienteHttp cliente = new ClienteHttp();
            HashMap<String,String> param = new HashMap<>();
            param.put("id_login_app",args[0]+"");
            String result = cliente.hacerRequestHttp(url, param);
            Log.d("RESULT", result);
            Gson gson = new Gson();
            PerfilPOJO perfilpo = gson.fromJson(result, PerfilPOJO.class);
            if(perfilpo != null) {
                Perfil perfil = new Perfil(getActivity().getApplicationContext());
                perfil.setNombreCompleto(perfilpo.getNombre());
                perfil.setGenero(perfilpo.getId_genero());
                String fechaBaseDatos = perfilpo.getFec_nacimiento();
                perfil.setFecha(fechaBaseDatos);
                perfil.setOcupacion(perfilpo.getId_ocupacion());
                perfil.setCodigo_postal(perfilpo.getCodigo_postal());
                perfil.setTelefono(perfilpo.getTelefono());
                perfil.setSuccess(perfilpo.getSuccess());
                perfil.setPeso(perfilpo.getPeso());
                perfil.setEstatura(perfilpo.getEstatura());
                perfil.setPresion(perfilpo.getPresion());
                perfil.setGlucosa(perfilpo.getGlucosa());
                perfil.setActividad(perfilpo.getActividad());
                perfil.setLesion(perfilpo.getLesion());
            }
            Log.d("result",result);
            return result;
        }
    }


    private class EnviarTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {

            String token = prefs.getString(FirebaseInstanceIDService.TOKEN, null);
            ClienteHttp clienteHttp = new ClienteHttp();
            HashMap<String, String> params = new HashMap<>();
            params.put("Token", token);
            params.put("id_login_app", session.getIdUsuario()+"");
            params.put("os", "1"); //1 Representa Android
            clienteHttp.hacerRequestHttp("http://" + ClienteHttp.SERVER_IP + "//app_php/notificaciones/registrar.php",
                    params);

            return null;
        }

    }


    private class RecibirImagenesAsyncTask extends AsyncTask<Void, Void, ArrayList<Imagen>> {

        @Override
        protected ArrayList<Imagen> doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            String url = "http://" + ClienteHttp.SERVER_IP + "/app_php/imagenes/imagenes.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            Log.d("RESULTAD", result);
            return gson.fromJson(result, new TypeToken<List<Imagen>>(){
            }.getType());
        }

        @Override
        public void onPostExecute(ArrayList<Imagen> result) {
            super.onPostExecute(result);
            Gson gson = new Gson();
            if(result != null) {
                JsonArray jsonArray = gson.toJsonTree(result).getAsJsonArray();
                FileUtils.writeToFile(jsonArray.toString(), getActivity());
            }
        }
    }


}
