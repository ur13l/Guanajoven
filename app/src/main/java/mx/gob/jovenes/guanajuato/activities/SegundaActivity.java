package mx.gob.jovenes.guanajuato.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.fragments.AyudaFragment;
import mx.gob.jovenes.guanajuato.fragments.CalendarioActividadesFragment;
import mx.gob.jovenes.guanajuato.fragments.ChatFragment;
import mx.gob.jovenes.guanajuato.fragments.CodigoGuanajovenFragment;
import mx.gob.jovenes.guanajuato.fragments.AcercaDeFragment;
import mx.gob.jovenes.guanajuato.fragments.ConvocatoriaFragment;
import mx.gob.jovenes.guanajuato.fragments.EditarDatosFragment;
import mx.gob.jovenes.guanajuato.fragments.EmpresaFragment;
import mx.gob.jovenes.guanajuato.fragments.NotificacionesFragment;
import mx.gob.jovenes.guanajuato.fragments.EventoFragment;
import mx.gob.jovenes.guanajuato.fragments.NuevoEventoDialogFragment;
import mx.gob.jovenes.guanajuato.fragments.RedesSocialesFragment;
import mx.gob.jovenes.guanajuato.fragments.RegionFragment;
import mx.gob.jovenes.guanajuato.fragments.RegistrarAguaFragment;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


/**
 * Autor: Uriel Infante
 * ACtivity contenedora de múltiples interfaces utilizando CustomFragment, aquí se inflan los
 * mx.gob.jovenes.guanajuato.fragments al seleccionar un elemento del Navigation Drawer.
 * Fecha: 02/05/2016
 */
public class SegundaActivity extends BaseActivity {
    public static SegundaActivity segundaActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        segundaActivity = this;
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_segunda, contentFrameLayout);

        //setContentView(R.layout.activity_segunda);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int conditional = 0;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        int id = getIntent().getExtras().getInt(HomeActivity.MENU_ID);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    FragmentManager fm = getSupportFragmentManager();
                    NuevoEventoDialogFragment f = new NuevoEventoDialogFragment();
                    f.show(fm, "Nuevo evento");
                }
            });
        }

        try {
            switch (id) {
                /*
                case R.id.nav_home:
                    fragment = HomeFragment.newInstance(R.id.nav_home,HomeFragment.class);
                    break;
                 */
                case R.id.nav_perfil:
                    fragment = EditarDatosFragment.newInstance(R.id.nav_perfil, R.string.datos_usuario, EditarDatosFragment.class);
                    break;
                case R.id.nav_convocatorias:
                    fragment = ConvocatoriaFragment.newInstance(R.id.nav_convocatorias, R.string.convocatorias, ConvocatoriaFragment.class);
                    break;
                case R.id.nav_mis_eventos:
                    fragment = EventoFragment.newInstance(R.id.nav_mis_eventos, R.string.mis_eventos ,EventoFragment.class);
                    break;

                case R.id.nav_acerca_de:
                    fragment = AcercaDeFragment.newInstance(R.id.nav_acerca_de, R.string.acerca_de, AcercaDeFragment.class);
                    break;

                case R.id.nav_historial_notificaciones:
                    fragment = NotificacionesFragment.newInstance(R.id.nav_historial_notificaciones, R.string.historial_notificaciones, NotificacionesFragment.class);
                    break;

                case R.id.nav_regiones:
                    fragment = RegionFragment.newInstance(R.id.nav_regiones, R.string.regiones, RegionFragment.class);
                    break;
                case R.id.nav_chat_ayuda:
                    fragment = ChatFragment.newInstance(R.id.nav_chat_ayuda, R.string.chat, ChatFragment.class);
                    break;
                case R.id.nav_codigo_guanajoven:
                    fragment = CodigoGuanajovenFragment.newInstance(R.id.nav_codigo_guanajoven, R.string.codigo_guanajoven, CodigoGuanajovenFragment.class);
                    break;
                case R.id.nav_redes_sociales:
                    fragment = RedesSocialesFragment.newInstance(R.id.nav_redes_sociales, R.string.redes_sociales, RedesSocialesFragment.class);
                    break;
                case R.id.boton_ayuda:
                    //fragment = AyudaFragment.newInstance(R.id.boton_help, R.string.acerca_de, AyudaFragment.class);
                    break;
                case R.id.nav_promociones:
                    fragment = EmpresaFragment.newInstance(R.id.nav_promociones, R.string.nav_promociones, EmpresaFragment.class);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if(conditional != 1){
            ft.replace(R.id.segunda_fragment_container, fragment).commit();

        }



    }

    @Override
    public void onBackPressed(){
        this.getSupportActionBar().setTitle(R.string.app_name);
        super.onBackPressed();
    }

    public static void cerrarSesion() {
        segundaActivity.finish();
    }

}
