package mx.gob.jovenes.guanajuato.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.NotificacionAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.fragments.HomeFragment;
import mx.gob.jovenes.guanajuato.model.Usuario;

import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static final String MENU_ID = "menu_id";

    private GoogleApiClient mGoogleApiClient;
    private Retrofit retrofit;
    private NotificacionAPI notificacionAPI;

    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private CircleImageView imagenUsuarioDrawer;
    private TextView nombreUsuarioDrawer;
    private TextView correoUsuarioDrawer;
    private TextView puntajeDrawer;
    private TextView posicionDrawer;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View headerLayout = navigationView.getHeaderView(0);

        imagenUsuarioDrawer = (CircleImageView) headerLayout.findViewById(R.id.circleimageview_imagen_usuario_drawer);
        nombreUsuarioDrawer = (TextView) headerLayout.findViewById(R.id.textview_nombre_usuario_drawer);
        correoUsuarioDrawer = (TextView) headerLayout.findViewById(R.id.textview_correo_usuario_drawer);
        puntajeDrawer = (TextView) headerLayout.findViewById(R.id.textview_puntaje_drawer);
        posicionDrawer = (TextView) headerLayout.findViewById(R.id.textview_posicion_drawer);

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

        retrofit = ((MyApplication) getApplication()).getRetrofitInstance();
        notificacionAPI = retrofit.create(NotificacionAPI.class);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
                    public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {}

                    @Override
                    public void onFailure(Call<Response<Boolean>> call, Throwable t) {}
                });

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

        Usuario usuario = Sesion.getUsuario();
        String nombre = usuario.getDatosUsuario().getNombre() + getString(R.string.home_activity_space) +
                usuario.getDatosUsuario().getApellidoPaterno() + getString(R.string.home_activity_space) +
                usuario.getDatosUsuario().getApellidoMaterno();
        String correo = usuario.getEmail();
        String puntaje = getString(R.string.home_activity_points) + usuario.getPuntaje();

        if(Sesion.getUsuario().getDatosUsuario().getRutaImagen() != null ) {
            Picasso.with(getApplicationContext()).load(usuario.getDatosUsuario().getRutaImagen()).into(imagenUsuarioDrawer);
        }

        String posicion = Sesion.getUsuario().getPosicion() == null ?
                getString(R.string.home_activity_position) + getString(R.string.home_activity_zero) :
                getString(R.string.home_activity_position) + Sesion.getUsuario().getPosicion();

        nombreUsuarioDrawer.setText(nombre);
        correoUsuarioDrawer.setText(correo);
        puntajeDrawer.setText(puntaje);
        posicionDrawer.setText(posicion);

        if (!DateUtilities.lessThan30Years(DateUtilities.getFechaCast(Sesion.getUsuario().getDatosUsuario().getFechaNacimiento()))) {
            navigationView.getMenu().findItem(R.id.nav_codigo_guanajoven).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_promociones).setVisible(false);
            puntajeDrawer.setVisibility(View.GONE);
            posicionDrawer.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

}