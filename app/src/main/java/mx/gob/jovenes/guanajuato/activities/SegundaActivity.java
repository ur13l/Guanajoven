package mx.gob.jovenes.guanajuato.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.AlarmasActivacionFragment;
import mx.gob.jovenes.guanajuato.fragments.CalendarioActividadesFragment;
import mx.gob.jovenes.guanajuato.fragments.ChatFragment;
import mx.gob.jovenes.guanajuato.fragments.CodigoGuanajovenFragment;
import mx.gob.jovenes.guanajuato.fragments.ColaboradoresFragment;
import mx.gob.jovenes.guanajuato.fragments.ConvocatoriaFragment;
import mx.gob.jovenes.guanajuato.fragments.DirectorioFragment;
import mx.gob.jovenes.guanajuato.fragments.EditarDatosFragment;
import mx.gob.jovenes.guanajuato.fragments.HistorialNotificacionesFragment;
import mx.gob.jovenes.guanajuato.fragments.MisEventosFragment;
import mx.gob.jovenes.guanajuato.fragments.NuevoEventoDialogFragment;
import mx.gob.jovenes.guanajuato.fragments.RegionFragment;
import mx.gob.jovenes.guanajuato.fragments.RegistrarAguaFragment;
import mx.gob.jovenes.guanajuato.fragments.RegistrarEjercicioFragment;


/**
 * Autor: Uriel Infante
 * ACtivity contenedora de múltiples interfaces utilizando CustomFragment, aquí se inflan los
 * mx.gob.jovenes.guanajuato.fragments al seleccionar un elemento del Navigation Drawer.
 * Fecha: 02/05/2016
 */
public class SegundaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);
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
                    fragment = MisEventosFragment.newInstance(R.id.nav_mis_eventos, R.string.mis_eventos ,MisEventosFragment.class);
                    break;

                case R.id.nav_colaboradores:
                    fragment = ColaboradoresFragment.newInstance(R.id.nav_colaboradores, R.string.colaboradores, ColaboradoresFragment.class);
                    break;

                case R.id.nav_calendario:
                    fragment = CalendarioActividadesFragment.newInstance(R.id.nav_calendario, R.string.calendario, CalendarioActividadesFragment.class);
                    break;

                case R.id.nav_historial_notificaciones:
                    fragment = HistorialNotificacionesFragment.newInstance(R.id.nav_historial_notificaciones, R.string.historial_notificaciones, HistorialNotificacionesFragment.class);
                    break;

                case R.id.nav_regiones:
                    fragment = RegionFragment.newInstance(R.id.nav_regiones, R.string.regiones, RegionFragment.class);
                    break;
                case R.id.nav_chat_ayuda:
                    fragment = ChatFragment.newInstance(R.id.nav_chat_ayuda, R.string.chat, ChatFragment.class);
                    break;
                case R.id.nav_codigo_guanajoven:
                    fragment = CodigoGuanajovenFragment.newInstance(R.id.nav_codigo_guanajoven, R.string.codigo_guanajoven, CodigoGuanajovenFragment.class);
                    //fragment.getView().setBackgroundColor(R.color.colorPrimary);
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

}
