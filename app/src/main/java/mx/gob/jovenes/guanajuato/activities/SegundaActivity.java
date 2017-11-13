package mx.gob.jovenes.guanajuato.activities;

import android.os.Bundle;
<<<<<<< HEAD
import android.support.design.widget.FloatingActionButton;
=======
>>>>>>> 77b7636f1ff2334c50714bb1542b65d0a5d58a3d
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
<<<<<<< HEAD
import android.view.View;
=======
>>>>>>> 77b7636f1ff2334c50714bb1542b65d0a5d58a3d

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.ChatFragment;
import mx.gob.jovenes.guanajuato.fragments.CodigoGuanajovenFragment;
import mx.gob.jovenes.guanajuato.fragments.AcercaDeFragment;
import mx.gob.jovenes.guanajuato.fragments.ConvocatoriaFragment;
import mx.gob.jovenes.guanajuato.fragments.EditarDatosFragment;
import mx.gob.jovenes.guanajuato.fragments.EmpresaFragment;
import mx.gob.jovenes.guanajuato.fragments.NotificacionesFragment;
import mx.gob.jovenes.guanajuato.fragments.EventoFragment;
import mx.gob.jovenes.guanajuato.fragments.RedesSocialesFragment;
import mx.gob.jovenes.guanajuato.fragments.RegionFragment;


/**
 * Autor: Uriel Infante
 * ACtivity contenedora de múltiples interfaces utilizando CustomFragment, aquí se inflan los
 * mx.gob.jovenes.guanajuato.fragments al seleccionar un elemento del Navigation Drawer.
 * Fecha: 02/05/2016
 */
public class SegundaActivity extends AppCompatActivity {
    public static SegundaActivity segundaActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        segundaActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        int conditional = 0;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        int id = getIntent().getExtras().getInt(HomeActivity.MENU_ID);

        try {
            switch (id) {
                case R.id.nav_perfil:
                    fragment = EditarDatosFragment.newInstance(R.id.nav_perfil, R.string.datos_usuario, EditarDatosFragment.class);
                    break;
                case R.id.nav_convocatorias:
                    fragment = ConvocatoriaFragment.newInstance(R.id.nav_convocatorias, R.string.convocatorias, ConvocatoriaFragment.class);
                    break;
                case R.id.nav_mis_eventos:
                    fragment = EventoFragment.newInstance(R.id.nav_mis_eventos, R.string.mis_eventos, EventoFragment.class);
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
                case R.id.nav_promociones:
                    fragment = EmpresaFragment.newInstance(R.id.nav_promociones, R.string.nav_promociones, EmpresaFragment.class);
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (conditional != 1) {
            ft.replace(R.id.segunda_fragment_container, fragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        this.getSupportActionBar().setTitle(R.string.app_name);
        super.onBackPressed();
    }

}
