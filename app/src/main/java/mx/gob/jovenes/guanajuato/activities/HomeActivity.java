package mx.gob.jovenes.guanajuato.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.fragments.HomeFragment;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.model.Perfil;
import mx.gob.jovenes.guanajuato.notifications.FirebaseInstanceIDService;
import mx.gob.jovenes.guanajuato.receivers.AlarmasBroadcastReceiver;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


/**
 * Autor: Uriel Infante
 * Activity contenedora de la interfaz Home.
 * Fecha: 02/05/2016
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static String MENU_ID = "menu_id";
    public static String INSTRUCCIONES_CHECK = "instrucciones_check";

    public int last_menu_id;

    private GoogleApiClient mGoogleApiClient;

    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private Retrofit retrofit;
    private NotificacionAPI notificacionAPI;

    private SharedPreferences prefs;

    private ImageView imagenUsuarioDrawer;
    private TextView nombreUsuarioDrawer;
    private TextView correoUsuarioDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();


       /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        toggle.syncState();

        */

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);



        imagenUsuarioDrawer = (ImageView) navigationView.findViewById(R.id.imagen_usuario_drawer);
        nombreUsuarioDrawer = (TextView) navigationView.findViewById(R.id.nombre_usuario_drawer);
        correoUsuarioDrawer = (TextView) navigationView.findViewById(R.id.correo_usuario_drawer);

        //Picasso.with(getApplicationContext()).load(Sesion.getRutaImagen()).into(imagenUsuarioDrawer);
        //nombreUsuarioDrawer.setText(Sesion.getNombre() + " " + Sesion.getApellidoPaterno() + " " + Sesion.getApellidoMaterno());
        //correoUsuarioDrawer.setText(Sesion.getCorreo());
        System.err.println("---------------------------------------------");

        System.err.println(Sesion.getRutaImagen());
        System.err.println(Sesion.getNombre());
        System.err.println(Sesion.getCorreo());

        System.err.println("---------------------------------------------");
        //Si no se ha iniciado la Activity genera una nueva (Evita generar nuevas al rotar la pantalla).
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = null;
            try {
                fragment = HomeFragment.newInstance(R.id.nav_home,R.string.app_name, HomeFragment.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Intent para abrir el tutorial de la aplicación
        if(!prefs.getBoolean(INSTRUCCIONES_CHECK, false)){
            Intent i = new Intent(this, InstruccionesActivity.class);
            startActivity(i);
            prefs.edit().putBoolean(INSTRUCCIONES_CHECK, true).commit();
        }

        //Configuración de retrofit
        retrofit = ((MyApplication) getApplication()).getRetrofitInstance();
        notificacionAPI = retrofit.create(NotificacionAPI.class);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.ayuda_home){
            Intent i = new Intent(this, HelpActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent intent;

        switch(id){
            case R.id.nav_home:
                break;
            case R.id.nav_logout:
                Call<Response<Boolean>> call = notificacionAPI.cancelarToken(Sesion.getUsuario().getId());
                call.enqueue(new Callback<Response<Boolean>>() {
                    @Override
                    public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                        Log.d("WOW", "WW");
                    }

                    @Override
                    public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                        Log.d("STOPO", "we");
                    }
                });
                //Verifica si la sesión de google existe
                if(mGoogleApiClient.isConnected()){
                   Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }

                Sesion.logout();
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                break;
            case R.id.nav_codigo_guanajoven:
                intent = new Intent(this, SegundaActivity.class);
                intent.putExtra(MENU_ID, id);
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, SegundaActivity.class);
                intent.putExtra(MENU_ID, id);
                startActivity(intent);
                break;

        }

        return true;
    }



    public NavigationView getNavigationView(){
        return navigationView;
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() { return toggle; }

    @Override
    public void onConnected(@org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected  void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

}