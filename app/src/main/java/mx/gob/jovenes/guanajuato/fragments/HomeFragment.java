package mx.gob.jovenes.guanajuato.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.text.format.Time;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.zzb;
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

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.Funcion;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.HomeActivity;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;
import mx.gob.jovenes.guanajuato.api.PublicidadAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Bitacora;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Publicidad;
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
import mx.gob.jovenes.guanajuato.utils.ImageHandler;
import mx.gob.jovenes.guanajuato.utils.PublicidadSingleton;
import mx.gob.jovenes.guanajuato.utils.SlideHandler;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Autor: Uriel Infante
 * Fragment de Home.
 * La ventana principal del proyecto, es la que se abre cuando el usuario inicia sesión.
 * Fecha: 10/04/2017
 */
public class HomeFragment extends CustomFragment {
    public final static String FECHA_ACTUALIZACION = "fecha_actualizacion";
    private Sesion session;

    //Elementos gráficos
    private ImageButton btnSlide;
    private ViewGroup pnlPublicidad;
    private ImageButton btnClose;
    private View slidePublicidad;

    //Instancias de API
    private Retrofit retrofit;
    private PublicidadAPI publicidadAPI;
    private Realm realm;

    //Preferencias almacenadas del usuario
    private SharedPreferences prefs;




    //Al crearse el fragment se genera el singleton que contendrá la lista de anuncios disponibles
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instancias de la API
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        publicidadAPI = retrofit.create(PublicidadAPI.class);

        //Instancia de Realm
        realm = MyApplication.getRealmInstance();

        //Asignando al usuario activo
        session = new Sesion(getActivity().getApplicationContext());

        //Declarando las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        pnlPublicidad = (ViewGroup) v.findViewById(R.id.pnl_publicidad);
        btnSlide = (ImageButton) v.findViewById(R.id.btn_slide);
        btnClose = (ImageButton) v.findViewById(R.id.close);
        slidePublicidad = v.findViewById(R.id.slide_publicidad);


        btnSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnlPublicidad.animate()
                        .translationX(pnlPublicidad.getWidth());
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

        ImageHandler.start( pnlPublicidad, getActivity());
        //Se define la acción para cuando se descargan las imágenes publicitarias.
        retrofit2.Call<Response<ArrayList<Publicidad>>> call = publicidadAPI.get(prefs.getString(MyApplication.LAST_UPDATE_PUBLICIDAD, "0000-00-00 00:00:00"));
        call.enqueue(new Callback<Response<ArrayList<Publicidad>>>() {
            @Override
            public void onResponse(retrofit2.Call<Response<ArrayList<Publicidad>>> call, retrofit2.Response<Response<ArrayList<Publicidad>>> response) {
                if(response.body().success) {
                    //Se ejecuta el guardado de elementos en Realm a partir de lo obtenido en el servicio.

                    List<Publicidad> publicidades = response.body().data;

                    //Transacción de realm, se itera sobre las publicidades obtenidas desde el servidor.
                    realm.beginTransaction();
                    for(Publicidad p : publicidades) {
                        if(p.getDeletedAt() != null) {
                            Publicidad pr = realm.where(Publicidad.class)
                                    .equalTo("idPublicidad", p.getIdPublicidad())
                                    .findFirst();
                            if(pr != null) {
                                pr.deleteFromRealm();
                            }
                        }
                        else {
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
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //Haciendo que la barra se pueda ocultar
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
        ImageHandler.stopCambioPublicidadTask();
    }

    /**
     * Asigna los valores de la sesión y la bitácora.
     * @throws ParseException
     */
    public void setValoresSesion() throws ParseException {

        FirebaseMessaging.getInstance().subscribeToTopic("mx.gob.jovenes.guanajuato.CODEApp");
        FirebaseInstanceId.getInstance().getToken();
        //new EnviarTokenAsyncTask().execute(); //TODO: Servicio para el token

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