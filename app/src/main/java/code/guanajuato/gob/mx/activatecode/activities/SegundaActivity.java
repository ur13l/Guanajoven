package code.guanajuato.gob.mx.activatecode.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.fragments.AlarmasActivacionFragment;
import code.guanajuato.gob.mx.activatecode.fragments.CalendarioActividadesFragment;
import code.guanajuato.gob.mx.activatecode.fragments.ColaboradoresFragment;
import code.guanajuato.gob.mx.activatecode.fragments.DirectorioFragment;
import code.guanajuato.gob.mx.activatecode.fragments.NuevoEventoDialogFragment;
import code.guanajuato.gob.mx.activatecode.fragments.PerfilFragment;
import code.guanajuato.gob.mx.activatecode.fragments.RegistrarAguaFragment;
import code.guanajuato.gob.mx.activatecode.fragments.RegistrarEjercicioFragment;
import code.guanajuato.gob.mx.activatecode.fragments.ReporteFragment;


/**
 * Autor: Uriel Infante
 * ACtivity contenedora de múltiples interfaces utilizando CustomFragment, aquí se inflan los
 * code.guanajuato.gob.mx.activatecode.fragments al seleccionar un elemento del Navigation Drawer.
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

        int conditional =0;
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
                case R.id.nav_datos_tutor:
                    fragment = PerfilFragment.newInstance(R.id.nav_datos_tutor, R.string.datos_usuario, PerfilFragment.class);
                    break;

                case R.id.nav_registrar_agua:
                    fragment = RegistrarAguaFragment.newInstance(R.id.nav_registrar_agua, R.string.registrar_agua, RegistrarAguaFragment.class);
                    break;
                case R.id.nav_registrar_ejercicio:
                    fragment = RegistrarEjercicioFragment.newInstance(R.id.nav_registrar_ejercicio, R.string.registrar_ejercicio,RegistrarEjercicioFragment.class);
                    break;

                case R.id.nav_colaboradores:
                    fragment = ColaboradoresFragment.newInstance(R.id.nav_colaboradores, R.string.colaboradores, ColaboradoresFragment.class);
                    break;

                case R.id.nav_calendario:
                    fragment = CalendarioActividadesFragment.newInstance(R.id.nav_calendario, R.string.calendario, CalendarioActividadesFragment.class);
                    break;

                case R.id.nav_alarmas:
                    fragment = AlarmasActivacionFragment.newInstance(R.id.nav_alarmas, R.string.alarmas, AlarmasActivacionFragment.class);
                    break;

                case R.id.nav_directorio:
                    fragment = AlarmasActivacionFragment.newInstance(R.id.nav_directorio, R.string.directorio_code, DirectorioFragment.class);
                    break;
                case R.id.nav_ver_reportes:
                    fragment = ReporteFragment.newInstance(R.id.nav_ver_reportes, R.string.reportes,ReporteFragment.class);
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
