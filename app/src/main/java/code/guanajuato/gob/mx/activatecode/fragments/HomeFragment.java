package code.guanajuato.gob.mx.activatecode.fragments;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.json.JSONArray;

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

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.activities.HomeActivity;
import code.guanajuato.gob.mx.activatecode.activities.SegundaActivity;
import code.guanajuato.gob.mx.activatecode.connection.ClienteHttp;
import code.guanajuato.gob.mx.activatecode.model.Bitacora;
import code.guanajuato.gob.mx.activatecode.model.Evento;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.model.Perfil;
import code.guanajuato.gob.mx.activatecode.model.PerfilPOJO;
import code.guanajuato.gob.mx.activatecode.model.Publicidad;
import code.guanajuato.gob.mx.activatecode.model.models_tmp.Imagen;
import code.guanajuato.gob.mx.activatecode.notifications.FirebaseInstanceIDService;
import code.guanajuato.gob.mx.activatecode.persistencia.AlarmasDBHelper;
import code.guanajuato.gob.mx.activatecode.persistencia.BitacoraDBHelper;
import code.guanajuato.gob.mx.activatecode.receivers.AlarmaBootReceiver;
import code.guanajuato.gob.mx.activatecode.receivers.RetrieveVideosBroadcastReceiver;
import code.guanajuato.gob.mx.activatecode.utils.DateUtilities;
import code.guanajuato.gob.mx.activatecode.utils.FileUtils;
import code.guanajuato.gob.mx.activatecode.utils.MathFormat;
import code.guanajuato.gob.mx.activatecode.utils.PublicidadSingleton;

/**
 * Autor: Uriel Infante
 * Fragment de Home.
 * La ventana principal del proyecto, es la que se abre cuando el usuario inicia sesión.
 * Muestra una imagen de CODE, las barras de estado de agua y ejercicio, una vista del calendario de activación
 * y una sección de publicidad donde se van a promover eventos.
 * Fecha: 27/05/2016
 */
public class HomeFragment extends CustomFragment {
    private final static int INTERVALO_PUBLICIDAD = 1000*10; // 10 segundos
    public final static String FECHA_ACTUALIZACION = "fecha_actualizacion";
    public static final String ALARMA_REGISTRADA = "alarma_registro_default";
    public static final String DATOS_PERFIL = "perfil_datos_usuario";
    public static final String RES_URL = "http://app.codegto.gob.mx/code_web/src/res/";
    private PublicidadSingleton publicidad;
    private ImageLoader imageLoader;
    private Login session;
    private BitacoraDBHelper bitacoraDBHelper;
    private Bitacora bitacora;

    //Elementos visuales
    private TextView hidratacionIndicatorTv;
    private TextView ejercicioIndicatorTv;
    private ProgressBar hidratacionPb;
    private ProgressBar ejercicioPb;
    private ExtendedCalendarView calendar;
    private TextView dateTv;
    private TextView titleEvent;
    private LinearLayout llCalendario;
    private View aguaView;
    private View ejercicioView;
    private static ArrayList<Imagen> listaImagenes;

    //Handler para manipular el cambio de la publicidad
    Handler handlerPublicidad; //Handler para manipular las imágenes en el diagnóstico

    //Elementos de la vista
    private Button btnAgua;
    private Button btnEjercicio;

    //Preferencias almacenadas del usuario
    private SharedPreferences prefs;

    //Código que ejecuta el handler para cambiar la publicidad cada 10 segundos
    Runnable handlerPublicidadTask =  new Runnable(){
        @Override
        public void run() {

            Random rand = new Random();
            if(publicidad.length() != 0) {
                int randInt = rand.nextInt(publicidad.length());
                final Imagen p = publicidad.getAt(randInt);
                ImageView img = (ImageView) getActivity().findViewById(R.id.image_view_publicidad);
                if (img != null) {

                    Picasso.with(getActivity())
                            .load(RES_URL + "imagenes/" + p.getImg())
                            .into(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(p.getLink()));
                            startActivity(intent);
                        }
                    });

                    Log.d("HandlerPublicidad", "Mostrando:" + p.getLink());
                }
            }
            handlerPublicidad.postDelayed(handlerPublicidadTask, INTERVALO_PUBLICIDAD);
        }
    };


    //Al crearse el fragment se genera el singleton que contendrá la lista de anuncios disponibles
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        publicidad = PublicidadSingleton.getInstance(getActivity());

        //Configuración de UniversalImageLoader
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity().getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        //Asignación del ImageLoader como elemento global.
        imageLoader = ImageLoader.getInstance();

        //Asignando al usuario activo
        session = new Login(getActivity().getApplicationContext());

        //Declarando las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        String fecha = prefs.getString(FECHA_ACTUALIZACION,  DateUtilities.dateToString(DateUtilities.stringToDate("2002-02-02 00:00:00")));
        new NuevosEventosAsyncTask().execute(fecha);
        new RecibirImagenesAsyncTask().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Actívate CODE");
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        //Se asocian las vistas a los elementos en Java.
        hidratacionIndicatorTv = (TextView) v.findViewById(R.id.hidratacion_indicator_tv);
        hidratacionPb = (ProgressBar) v.findViewById(R.id.progress_bar_hidratacion);
        ejercicioIndicatorTv = (TextView) v.findViewById(R.id.ejercicio_indicator_tv);
        ejercicioPb = (ProgressBar) v.findViewById(R.id.progress_bar_ejercicio);
        calendar = (ExtendedCalendarView) v.findViewById(R.id.calendar_home);
        dateTv = (TextView) v.findViewById(R.id.date_home);
        llCalendario = (LinearLayout) v.findViewById( R.id.ll_calendario);
        titleEvent = (TextView) v.findViewById(R.id.title_event);
        aguaView = v.findViewById(R.id.selectAgua);
        ejercicioView = v.findViewById(R.id.selectEjercicio);

        //Función para activar las alarmas de Descargar videos.
        if(!prefs.getBoolean(RetrieveVideosBroadcastReceiver.REGISTERED_ALARM, false)){

            RetrieveVideosBroadcastReceiver.registerAlarm(getActivity());
            prefs.edit().putBoolean(RetrieveVideosBroadcastReceiver.REGISTERED_ALARM, true).commit();
        }

        calendar.setGesture(1);
        //Evento para que se asigne algo al calendario.
        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
           @Override
           public void onDayClicked(Day day) {
               ArrayList<Event> events = day.getEvents();
               Calendar fecha = Calendar.getInstance();
               fecha.set(Calendar.MONTH, day.getMonth());
               fecha.set(Calendar.DAY_OF_MONTH, day.getDay());
               fecha.set(Calendar.YEAR, day.getYear());
               dateTv.setText(day.getDay() + " de " +
                       fecha.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault()) + " de "
                       + day.getYear());
               String strEvents = "";
               for (Event e : events){
                  strEvents += "• " + e.getTitle() + "\n";
               }
               titleEvent.setText(strEvents);
               if(events.isEmpty()){
                   titleEvent.setText("No hay eventos programados durante este día");
               }
                   }
                                       }
        );

        llCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(R.id.nav_calendario);
            }
        });
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
        Toolbar bottomToolbar = (Toolbar) getActivity().getLayoutInflater().inflate(R.layout.toolbar_bottom_home,coordinatorLayout,false);
        coordinatorLayout.addView(bottomToolbar);

        btnAgua = (Button)bottomToolbar.findViewById(R.id.btn_agua);
        btnEjercicio = (Button)bottomToolbar.findViewById(R.id.btn_ejercicio);

        aguaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(R.id.nav_registrar_agua);
            }
        });

        ejercicioView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(R.id.nav_registrar_ejercicio);
            }
        });
        btnAgua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(R.id.nav_registrar_agua);
            }
        });
        btnEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(R.id.nav_registrar_ejercicio);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        handlerPublicidad = new Handler();
        startCambioPublicidadTask();
        try {
            setValoresSesion();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(){

        super.onStop();
        stopCambioPublicidadTask();
    }


    /**
     * Asigna los valores de la sesión y la bitácora.
     * @throws ParseException
     */
    public void setValoresSesion() throws ParseException {
        session = new Login(getActivity().getApplicationContext());
        bitacoraDBHelper = new BitacoraDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        bitacora = new Bitacora(getActivity().getApplicationContext());

        Login session = new Login(getActivity().getApplicationContext());
        try {
            bitacoraDBHelper.prepareDatabase();
            Log.d("BITACORA", session.getId() + "");
            bitacora = bitacoraDBHelper.bitacoraDelDia(session.getId(), new Date());
            if(bitacora.getId() == 0){
                Log.d("id_hoho", session.getId()+"");
                bitacora.setIdUser(session.getId());
            }
        } catch (Exception e) {
            Log.e("DB", e.getMessage());
        }


        ejercicioIndicatorTv.setText(MathFormat.removeDots(bitacora.getMinutosEjercicio()) + "");
        hidratacionIndicatorTv.setText(MathFormat.removeDots(bitacora.getRegistrAgua()/1000)+"");

        ejercicioPb.setProgress((int) bitacora.getMinutosEjercicio());
        hidratacionPb.setProgress((int)bitacora.getRegistrAgua());

        registrarAlarmasDefault();

        if(!prefs.getBoolean(DATOS_PERFIL, false)){
            new ObternerAsyncTask().execute(session.getId());
            prefs.edit().putBoolean(DATOS_PERFIL,true).commit();
        }

        Log.d("WIFI", "Enviando token");
        FirebaseMessaging.getInstance().subscribeToTopic("code.guanajuato.gob.mx.activatecode.CODEApp");
        FirebaseInstanceId.getInstance().getToken();
        new EnviarTokenAsyncTask().execute();

        AlarmaBootReceiver.configurarTodasAlarmas(this.getActivity());

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


    public void registrarAlarmasDefault(){
        AlarmasDBHelper alarmaDBHelper = new AlarmasDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());


        if(!prefs.getBoolean(ALARMA_REGISTRADA + session.getId(), false)){
            try {
                alarmaDBHelper.prepareDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            alarmaDBHelper.registrarAlarmasDefault(session.getId());
            prefs.edit().putBoolean(ALARMA_REGISTRADA+session.getId(), true).commit();
        }
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

        dateTv.setText(cTemp.get(Calendar.DAY_OF_MONTH) + " de " +
                cTemp.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " de "
                + cTemp.get(Calendar.YEAR));
        String strEvents = "";

        for (Event e : events) {
            Log.d("EVENTOS", strEvents);
            strEvents = "• " + e.getTitle() + "\n";
        }
        titleEvent.setText(strEvents);
        if (events.isEmpty()) {
            titleEvent.setText("No hay eventos programados durante este día");
        }

    }



    /**
     * Arranque para el cambio de publicidad.
     */
    void startCambioPublicidadTask()
    {
        handlerPublicidadTask.run();
    }

    /**
     * Se detiene el cambio de publicidad.
     */
    void stopCambioPublicidadTask()
    {
        handlerPublicidad.removeCallbacks(handlerPublicidadTask);
    }


    /**
     * Clase privada para la llamada asíncrona que descarga los eventos más recientes
     */
    private class NuevosEventosAsyncTask extends AsyncTask<String, Void, ArrayList<Evento>> {


        @Override
        protected ArrayList<Evento> doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("fecha_actualizacion", args[0].toString());
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/eventos/nuevosEventos.php";
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
                    calendar.refreshCalendar();
                }
            }
        }
    }

    private class ObternerAsyncTask extends AsyncTask<Integer, Void, String>{
        @Override
        protected String doInBackground(Integer... args) {
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/registro/obtenerPerfil.php";
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
            params.put("id_login_app", session.getId()+"");
            params.put("os", "1"); //1 Representa Android
            clienteHttp.hacerRequestHttp("http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/notificaciones/registrar.php",
                    params);

            return null;
        }

    }


    private class RecibirImagenesAsyncTask extends AsyncTask<Void, Void, ArrayList<Imagen>> {

        @Override
        protected ArrayList<Imagen> doInBackground(Void... voids) {
            HashMap<String, String> params = new HashMap<>();
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/imagenes/imagenes.php";
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
            JsonArray jsonArray = gson.toJsonTree(result).getAsJsonArray();
            FileUtils.writeToFile(jsonArray.toString(), getActivity());
        }
    }


}
