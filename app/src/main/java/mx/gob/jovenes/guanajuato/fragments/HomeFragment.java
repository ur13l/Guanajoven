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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.Funcion;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.HomeActivity;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.PublicidadAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Publicidad;
import mx.gob.jovenes.guanajuato.model.Perfil;
import mx.gob.jovenes.guanajuato.model.PerfilPOJO;
import mx.gob.jovenes.guanajuato.model.models_tmp.Imagen;
import mx.gob.jovenes.guanajuato.notifications.FirebaseInstanceIDService;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.FileUtils;
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
    public final static String FECHA_ACTUALIZACION = "fecha_actualizacion";
    //Elementos gráficos
    private ImageButton btnSlide;
    private ViewGroup pnlPublicidad;
    private ImageButton btnClose;
    private View slidePublicidad;

    public static String MENU_ID = "menu_id";

    //Botones
    ImageButton botonNavigationDrawer;
    ImageButton botonHelp;
    Button botonCodigoGuanajoven;
    Button botonEventos;
    Button botonNotificaciones;
    Button botonConvocatorias;
    Button botonRedesSociales;
    Button botonChat;

    //Instancias de API
    private Retrofit retrofit;
    private PublicidadAPI publicidadAPI;
    private Realm realm;
    private NotificacionAPI notificacionAPI;

    //Preferencias almacenadas del usuario
    private SharedPreferences prefs;

    //Cambio de fragments
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;

    private TextView textViewBolsaTrabajo;


    //Al crearse el fragment se genera el singleton que contendrá la lista de anuncios disponibles
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instancias de la API
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        publicidadAPI = retrofit.create(PublicidadAPI.class);
        notificacionAPI = retrofit.create(NotificacionAPI.class);

        //Instancia de Realm
        realm = MyApplication.getRealmInstance();

        Sesion.sessionStart(getActivity().getApplication());

        //Declarando las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        fragmentTransaction = getFragmentManager().beginTransaction();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, parent, false);

        //Elementos publicidad
        pnlPublicidad = (ViewGroup) v.findViewById(R.id.pnl_publicidad);
        btnSlide = (ImageButton) v.findViewById(R.id.btn_slide);
        btnClose = (ImageButton) v.findViewById(R.id.close);
        slidePublicidad = v.findViewById(R.id.slide_publicidad);

        //Elementos menu
        botonNavigationDrawer = (ImageButton) v.findViewById(R.id.boton_navigation_drawer);
        botonHelp = (ImageButton) v.findViewById(R.id.boton_help);
        botonCodigoGuanajoven = (Button) v.findViewById(R.id.boton_codigo_guanajoven);
        botonEventos = (Button) v.findViewById(R.id.boton_eventos);
        botonNotificaciones = (Button) v.findViewById(R.id.boton_notificaciones);
        botonConvocatorias = (Button) v.findViewById(R.id.boton_convocatorias);
        botonRedesSociales = (Button) v.findViewById(R.id.boton_redes_sociales);
        botonChat = (Button) v.findViewById(R.id.boton_chat);

        textViewBolsaTrabajo = (TextView) v.findViewById(R.id.textview_bolsa_de_trabajo);

        textViewBolsaTrabajo.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/empresas-incluyentes/");
        });

        //Listeners de publicidad
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



        //Listeners botones menu

        botonNavigationDrawer.setOnClickListener((View) -> {
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        });

        botonHelp.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.boton_help);
                startActivity(intent);
            } catch (Exception e) {

            }
        });

        botonCodigoGuanajoven.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_codigo_guanajoven);
                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
            }
        });

        botonEventos.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_mis_eventos);
                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
            }
        });

        botonConvocatorias.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_convocatorias);
                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
            }
        });

        botonRedesSociales.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_redes_sociales);
                fragment = RedesSocialesFragment.newInstance(R.id.nav_redes_sociales, R.string.redes_sociales, RedesSocialesFragment.class);

                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
            }
        });

        botonChat.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_chat_ayuda);
                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
            }
        });

        botonNotificaciones.setOnClickListener((View) -> {
            try {
                Intent intent = new Intent(this.getContext(), SegundaActivity.class);
                intent.putExtra(MENU_ID, R.id.nav_historial_notificaciones);
                startActivity(intent);
            } catch (Exception e) {
                System.err.println("que pendejo...");
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
                if (body.success) {
                    if (body.data) {
                        //Código cuando fue exitoso
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

        /*
        @Override
        public void onPostExecute(ArrayList<Evento> result) {
            super.onPostExecute(result);
            if (result != null) {
                prefs.edit().putString(FECHA_ACTUALIZACION, DateUtilities.dateToString(new Date())).commit();

                if(result.size() > 0) {
                    for (Evento e : result) {
                        if(e.ge == 0){
                            DateUtilities.deleteEvento(getActivity(), e.getIdEvento());
                        }
                        else {
                            DateUtilities.setFechas(getActivity(),
                                    e.getIdEvento(),
                                    DateUtilities.stringToDate(e.getFechaInicio()),
                                    DateUtilities.stringToDate(e.getFechaFin()),
                                    e.getTitulo(),
                                    e.getDescripcion(),
                                    e.getTipo()
                            );
                        }
                    }

                }
            }
        }
    }*/


        private class ObternerAsyncTask extends AsyncTask<Integer, Void, String> {
            @Override
            protected String doInBackground(Integer... args) {
                String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/registro/obtenerPerfil.php";
                ClienteHttp cliente = new ClienteHttp();
                HashMap<String, String> param = new HashMap<>();
                param.put("id_login_app", args[0] + "");
                String result = cliente.hacerRequestHttp(url, param);
                Log.d("RESULT", result);
                Gson gson = new Gson();
                PerfilPOJO perfilpo = gson.fromJson(result, PerfilPOJO.class);
                if (perfilpo != null) {
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
                Log.d("result", result);
                return result;
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
                return gson.fromJson(result, new TypeToken<List<Imagen>>() {
                }.getType());
            }

            @Override
            public void onPostExecute(ArrayList<Imagen> result) {
                super.onPostExecute(result);
                Gson gson = new Gson();
                if (result != null) {
                    JsonArray jsonArray = gson.toJsonTree(result).getAsJsonArray();
                    FileUtils.writeToFile(jsonArray.toString(), getActivity());
                }
            }
        }
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}