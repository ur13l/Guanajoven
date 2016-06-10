package code.guanajuato.gob.mx.activatecode.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import code.guanajuato.gob.mx.activatecode.model.Login;

/**
 * Created by Uriel on 17/01/2016.
 */
public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    /**
     * Método para iniciar la vista Splash.
     * Al terminar de mostrar el Splash podrá redirigir a Bienvenida, Login o Home, dependiendo de
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
            Login sesion = new Login(getApplicationContext());

            if (sesion.getId() == 0) {
                intent = new Intent(this, LogueoActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
        }




        startActivity(intent);
        finish();
    }
}
