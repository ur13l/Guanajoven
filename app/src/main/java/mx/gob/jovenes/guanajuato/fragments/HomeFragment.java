package mx.gob.jovenes.guanajuato.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.Funcion;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.PublicidadAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Publicidad;
import mx.gob.jovenes.guanajuato.notifications.FirebaseInstanceIDService;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.ImageHandler;
import mx.gob.jovenes.guanajuato.utils.SlideHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Autor: Uriel Infante
 * Fragment de Home.
 * La ventana principal del proyecto, es la que se abre cuando el usuario inicia sesión.
 * Fecha: 10/04/2017
 */
public class HomeFragment extends CustomFragment {
    private ImageButton btnSlide;
    private ViewGroup pnlPublicidad;
    private ImageButton btnClose;
    private View slidePublicidad;

    public static String MENU_ID = "menu_id";

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    ImageButton botonNavigationDrawer;
    ImageButton botonCodigoGuanajoven;
    ImageButton botonRegiones;
    ImageButton botonNotificaciones;
    ImageButton botonEventos;
    ImageButton botonPromociones;
    ImageButton botonConvocatorias;
    ImageButton botonRedesSociales;
    ImageButton botonChat;
    ImageButton botonAyuda;
    TextView textViewIdGuanajoven;
    TextView textViewPromociones;
    TextView textViewRegiones;
    TextView textViewNotificaciones;

    private Retrofit retrofit;
    private PublicidadAPI publicidadAPI;
    private Realm realm;
    private NotificacionAPI notificacionAPI;

    private SharedPreferences prefs;

    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;

    private TextView textViewBolsaTrabajo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        publicidadAPI = retrofit.create(PublicidadAPI.class);
        notificacionAPI = retrofit.create(NotificacionAPI.class);

        realm = MyApplication.getRealmInstance();

        Sesion.sessionStart(getActivity().getApplication());

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        fragmentTransaction = getFragmentManager().beginTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        pnlPublicidad = (ViewGroup) v.findViewById(R.id.pnl_publicidad);
        btnSlide = (ImageButton) v.findViewById(R.id.btn_slide);
        btnClose = (ImageButton) v.findViewById(R.id.close);
        slidePublicidad = v.findViewById(R.id.slide_publicidad);

        botonNavigationDrawer = (ImageButton) v.findViewById(R.id.boton_navigation_drawer);
        botonCodigoGuanajoven = (ImageButton) v.findViewById(R.id.boton_codigo_guanajoven);
        botonEventos = (ImageButton) v.findViewById(R.id.boton_eventos);
        botonRegiones = (ImageButton) v.findViewById(R.id.boton_regiones);
        botonNotificaciones = (ImageButton) v.findViewById(R.id.boton_notificaciones);
        botonPromociones = (ImageButton) v.findViewById(R.id.boton_promociones);
        botonConvocatorias = (ImageButton) v.findViewById(R.id.boton_convocatorias);
        botonRedesSociales = (ImageButton) v.findViewById(R.id.boton_redes_sociales);
        botonChat = (ImageButton) v.findViewById(R.id.boton_chat);
        botonAyuda = (ImageButton) v.findViewById(R.id.boton_ayuda);
        textViewIdGuanajoven = (TextView) v.findViewById(R.id.textview_id_guanajoven);
        textViewPromociones = (TextView) v.findViewById(R.id.textview_promociones);
        textViewRegiones = (TextView) v.findViewById(R.id.textview_regiones);
        textViewNotificaciones = (TextView) v.findViewById(R.id.textview_notificaciones);

        textViewBolsaTrabajo = (TextView) v.findViewById(R.id.textview_bolsa_de_trabajo);

        textViewBolsaTrabajo.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/empresas-incluyentes/");
        });

        //Listeners de publicidad
        btnSlide.setOnClickListener((View) -> pnlPublicidad.animate().translationX(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        pnlPublicidad.setVisibility(View.VISIBLE);
                    }
                }));

        btnClose.setOnClickListener((View) -> pnlPublicidad.animate().translationX(pnlPublicidad.getWidth()));

        //Listeners botones menu

        botonAyuda.setOnClickListener((View) -> {
            try {
                Fragment fragment = new AyudaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonNavigationDrawer.setOnClickListener((View) -> {
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        });

        botonCodigoGuanajoven.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_codigo_guanajoven);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonRegiones.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_regiones);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonNotificaciones.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_historial_notificaciones);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonEventos.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_mis_eventos);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonConvocatorias.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_convocatorias);
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        botonRedesSociales.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_redes_sociales);
                fragment = RedesSocialesFragment.newInstance(R.id.nav_redes_sociales, R.string.redes_sociales, RedesSocialesFragment.class);

                startActivity(intent);
            } catch (Exception e) {

            }
        });

        botonChat.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_chat_ayuda);
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        botonPromociones.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_promociones);
                startActivity(intent);
            } catch (Exception e) {

            }
        });


        SlideHandler.initSlider(slidePublicidad, "left", new Funcion() {
            @Override
            public void exec() {
                pnlPublicidad.animate()
                        .translationX(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                pnlPublicidad.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });

        SlideHandler.initSlider(pnlPublicidad, "right", new Funcion() {
            @Override
            public void exec() {
                pnlPublicidad.animate()
                        .translationX(pnlPublicidad.getWidth());
            }
        });

        ImageHandler.start(pnlPublicidad, getActivity());
        //Se define la acción para cuando se descargan las imágenes publicitarias.
        retrofit2.Call<Response<ArrayList<Publicidad>>> call = publicidadAPI.get(prefs.getString(MyApplication.LAST_UPDATE_PUBLICIDAD, "0000-00-00 00:00:00"));
        call.enqueue(new Callback<Response<ArrayList<Publicidad>>>() {
            @Override
            public void onResponse(retrofit2.Call<Response<ArrayList<Publicidad>>> call, retrofit2.Response<Response<ArrayList<Publicidad>>> response) {
                if (response.body().success) {
                    //Se ejecuta el guardado de elementos en Realm a partir de lo obtenido en el servicio.

                    List<Publicidad> publicidades = response.body().data;

                    //Transacción de realm, se itera sobre las publicidades obtenidas desde el servidor.
                    realm.beginTransaction();
                    for (Publicidad p : publicidades) {
                        p.setUrl(getValidURL(p.getUrl()));
                        if (p.getDeletedAt() != null) {
                            Publicidad pr = realm.where(Publicidad.class)
                                    .equalTo("idPublicidad", p.getIdPublicidad())
                                    .findFirst();
                            if (pr != null) {
                                pr.deleteFromRealm();
                            }
                        } else {
                            realm.copyToRealmOrUpdate(p);
                        }
                    }
                    realm.commitTransaction();

                    //Actualizando el timestamp para no descargar el contenido ya existente.
                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_PUBLICIDAD, lastUpdate).apply();
                }


            }

            @Override
            public void onFailure(retrofit2.Call<Response<ArrayList<Publicidad>>> call, Throwable t) {
                Log.d("Error", "Error");
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pnlPublicidad.animate()
                .translationX(pnlPublicidad.getWidth());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Haciendo que la barra se pueda ocultar
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            setValoresSesion();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!lessThan30Years(getFechaCast(Sesion.getUsuario().getDatosUsuario().getFechaNacimiento()))) {
            //hide views
            botonCodigoGuanajoven.setVisibility(View.GONE);
            botonPromociones.setVisibility(View.GONE);
            textViewIdGuanajoven.setVisibility(View.GONE);
            textViewPromociones.setVisibility(View.GONE);
            //show views
            botonRegiones.setVisibility(View.VISIBLE);
            botonNotificaciones.setVisibility(View.VISIBLE);
            textViewRegiones.setVisibility(View.VISIBLE);
            textViewNotificaciones.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        ImageHandler.stopCambioPublicidadTask();
    }

    /**
     * Asigna los valores de la sesión y la bitácora.
     *
     * @throws ParseException
     */
    public void setValoresSesion() throws ParseException {
        FirebaseMessaging.getInstance().subscribeToTopic("mx.gob.jovenes.guanajuato.Guanajoven");
        FirebaseInstanceId.getInstance().getToken();
        String token = prefs.getString(FirebaseInstanceIDService.TOKEN, null);
        int idUsuario = Sesion.getUsuario().getId();
        Call<Response<Boolean>> call = notificacionAPI.enviarToken(
                token,
                idUsuario,
                "android"
        );

        call.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                Response<Boolean> body = response.body();
                if (body != null) {
                    if (body.success) {
                        if (body.data) {
                            //Código cuando fue exitoso
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {

            }
        });
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
            return gson.fromJson(result, new TypeToken<List<Evento>>() {
            }.getType());
        }

    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }


    private boolean lessThan30Years(String date) {
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

    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        return calendar;
    }

    private String getFechaCast(String fecha) {
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

    /**
     *
     * @param url
     * @return parse url
     * @description Parse a invalid url to a http request, example: www.google.com, parse to http://www.google.com
     */
    private String getValidURL(String url) {
        String returnURL;
        if (url.startsWith("www")) {
            returnURL = "http://" + url;
            return returnURL;
        } else {
            return url;
        }
    }


}