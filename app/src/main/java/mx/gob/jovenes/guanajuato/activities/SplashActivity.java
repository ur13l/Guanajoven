package mx.gob.jovenes.guanajuato.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;

import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;

/**
 * Created by Uriel on 17/01/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    /**
     * Método para iniciar la vista Splash.
     * Al terminar de mostrar el Splash podrá redirigir a Bienvenida, Usuario o Home, dependiendo de
     * la instancia del sistema.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent;

        //Instancia de las preferencias;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!prefs.getBoolean(BienvenidaActivity.BIENVENIDA_KEY, false)){
            intent = new Intent(this, BienvenidaActivity.class);
        }
        else {
            Sesion sesion = new Sesion(getApplicationContext());

            System.err.println("------------------------------");
            System.err.println(Sesion.getUsuario().getId());
            System.err.println("-------------------------------");

            if (Sesion.getUsuario().getId() == 0 ) {
                intent = new Intent(this, LoginActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
        }




        startActivity(intent);
        finish();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
}
