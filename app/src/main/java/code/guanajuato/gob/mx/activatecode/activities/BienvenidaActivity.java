package code.guanajuato.gob.mx.activatecode.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.fragments.BienvenidaFragment;

/**
 * Autor: Uriel Infante
 * Activity que contiene al fragment de bienvenida.
 * Fecha: 02/05/2016
 */
public class BienvenidaActivity extends AppCompatActivity {
    public static String BIENVENIDA_KEY = "bienvenida";
    public static String LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new BienvenidaFragment();
        fm.beginTransaction().add( R.id.bienvenida_fragment_container, fragment).commit();
    }
}
