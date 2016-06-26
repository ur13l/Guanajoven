package code.guanajuato.gob.mx.activatecode.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.fragments.LoginFragment;


/**
 * Autor: Uriel Infante
 * Activity contenedora de la interfaz Login.
 * Fecha: 02/05/2016
 */
public class LogueoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logueo);
        toolbar = (Toolbar)findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        try {
            fragment = new LoginFragment();
            ft.replace(R.id.login_fragment_container, fragment, "fragment_login");
            ft.commit();
        }catch(Exception e){
            e.getMessage();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }




}
