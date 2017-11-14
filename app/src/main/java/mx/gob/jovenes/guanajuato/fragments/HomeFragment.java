package mx.gob.jovenes.guanajuato.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.Funcion;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.PublicidadAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Publicidad;
import mx.gob.jovenes.guanajuato.notifications.FirebaseInstanceIDService;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.ImageHandler;
import mx.gob.jovenes.guanajuato.utils.SlideHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HomeFragment extends CustomFragment {
    private ImageButton btnSlide;
    private ViewGroup pnlPublicidad;
    private ImageButton btnClose;
    private View slidePublicidad;

    public static String MENU_ID = "menu_id";

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
            enlace(getString(R.string.fragment_home_link_bolsa_de_trabajo));
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


        SlideHandler.initSlider(slidePublicidad, getString(R.string.fragment_home_left), new Funcion() {
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

        SlideHandler.initSlider(pnlPublicidad, getString(R.string.fragment_home_right), new Funcion() {
            @Override
            public void exec() {
                pnlPublicidad.animate()
                        .translationX(pnlPublicidad.getWidth());
            }
        });

        ImageHandler.start(pnlPublicidad, getActivity());
        //Se define la acción para cuando se descargan las imágenes publicitarias.
        retrofit2.Call<Response<ArrayList<Publicidad>>> call = publicidadAPI.get(prefs.getString(MyApplication.LAST_UPDATE_PUBLICIDAD, getString(R.string.fragment_home_timestamp)));
        call.enqueue(new Callback<Response<ArrayList<Publicidad>>>() {
            @Override
            public void onResponse(retrofit2.Call<Response<ArrayList<Publicidad>>> call, retrofit2.Response<Response<ArrayList<Publicidad>>> response) {
                if (response.body().success) {

                    List<Publicidad> publicidades = response.body().data;

                    realm.beginTransaction();
                    for (Publicidad p : publicidades) {
                        p.setUrl(getValidURL(p.getUrl()));
                        if (p.getDeletedAt() != null) {
                            Publicidad pr = realm.where(Publicidad.class)
                                    .equalTo(getString(R.string.fragment_home_idpublicidad), p.getIdPublicidad())
                                    .findFirst();
                            if (pr != null) {
                                pr.deleteFromRealm();
                            }
                        } else {
                            realm.copyToRealmOrUpdate(p);
                        }
                    }
                    realm.commitTransaction();

                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_PUBLICIDAD, lastUpdate).apply();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Response<ArrayList<Publicidad>>> call, Throwable t) {

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
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            setValoresSesion();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!DateUtilities.lessThan30Years(DateUtilities.getFechaCast(Sesion.getUsuario().getDatosUsuario().getFechaNacimiento()))) {
            botonCodigoGuanajoven.setVisibility(View.GONE);
            botonPromociones.setVisibility(View.GONE);
            textViewIdGuanajoven.setVisibility(View.GONE);
            textViewPromociones.setVisibility(View.GONE);
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


    public void setValoresSesion() throws ParseException {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.fragment_home_suscription));
        FirebaseInstanceId.getInstance().getToken();
        String token = prefs.getString(FirebaseInstanceIDService.TOKEN, null);
        int idUsuario = Sesion.getUsuario().getId();
        Call<Response<Boolean>> call = notificacionAPI.enviarToken(
                token,
                idUsuario,
                getString(R.string.fragment_home_os)
        );

        call.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                Response<Boolean> body = response.body();
                if (body != null) {
                    if (body.success) {
                        if (body.data) {
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {

            }
        });
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private String getValidURL(String url) {
        String returnURL;
        if (url.startsWith(getString(R.string.fragment_home_starts_with))) {
            returnURL = getString(R.string.fragment_home_default_http) + url;
            return returnURL;
        } else {
            return url;
        }
    }


}