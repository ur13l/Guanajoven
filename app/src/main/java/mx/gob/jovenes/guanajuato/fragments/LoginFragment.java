package mx.gob.jovenes.guanajuato.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.LoginActivity;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.DatosUsuario;
import mx.gob.jovenes.guanajuato.model.Genero;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import mx.gob.jovenes.guanajuato.utils.OKDialog;
import mx.gob.jovenes.guanajuato.utils.ValidEmail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Autor: Uriel Infante
 * Fragment de la interfaz Usuario, el usuario puede introducir su correo y contraseña para iniciar sesión.
 * Se cuenta con opción de recuperar contraseña e iniciar sesión con Google o Facebook, además de
 * tener la opción de registro.
 * Fecha: 02/05/2016
 */
public class LoginFragment extends Fragment implements  View.OnClickListener {


    //Constantes
    public final static String CORREO = "recuperar_correo";
    public final static String CODIGO = "recuperar_codigo";
    private static final int RC_SIGN_IN = 0;


    //Elementos para el login con Google
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private GoogleSignInApi googleSignInApi;
    private Button googleSignInButton;
    private ProgressDialog loginGooglePd;
    private ImageButton btnBack;

    //Elementos para el login con Facebook
    private Button fbButton;
    private LoginButton fb;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //Elementos de la interfaz
    private ProgressDialog loginSimplePd;
    private EditText contrasenaEt;
    private EditText correoEt;
    private Button loginButton;
    private TextView recuperarPasswordTv;

    //Utilidades Retrofit y SharedPreferences
    private Retrofit retrofit;
    private UsuarioAPI usuarioAPI;
    public SharedPreferences prefs;


    /**
     * CICLO DE VIDA DE APP
     */

    /**
     * Parte del ciclo de vida ejecutado al inicio, evita que exista una sesión de FB en cuanto
     * se abre la vista.
     */
    @Override
    public void onStart() {
        super.onStart();
        logOutFacebook();
    }

    /**
     * Método ejecutado al resumir la actividad, se ejecuta a pesar de que la Activity no se haya
     * muerto completamente.
     */
    @Override
    public void onResume(){
        super.onResume();
        googleApiClient.connect();
        if(googleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

    /**
     * Método ejecutado cuando se detiene la ejecución del Fragment en curso.
     */
    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();

        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
        if (loginSimplePd != null) {
            loginSimplePd.dismiss();
            loginSimplePd = null;
        }
        if (loginGooglePd != null) {
            loginGooglePd.dismiss();
            loginGooglePd = null;
        }
    }

    /**
     * Método de arranque. Se instancian todas las referencias del login con Google y Facebook.
     * @param savedInstanceState {Bundle}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se instancian las GoogleSignInOptions y el GoogleApiClient.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            OKDialog.showOKDialog(getActivity(), "Error", "Hubo un error en la conexión, reintentar más adelante");
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }


        //Configuración de Facebook.
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) { }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        //Instancia de las preferencias.
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());


        //Instancia de retrofit para servicio de API
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);

    }

    /**
     * Se instancian los elementos de la vista para el funcionamiento del Fragment.
     * @param inflater {LayoutInflater}
     * @param container {ViewGroup}
     * @param savedInstanceState {Bundle}
     * @return {View}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //Instancia y listener del botón de Google.
        googleSignInButton = (Button) v.findViewById(R.id.btn_google);
        googleSignInButton.setOnClickListener(this);

        //Instancia del botón de Facebook.
        fbButton = (Button) v.findViewById(R.id.btn_facebook);
        fb = (LoginButton) v.findViewById(R.id.lgn_btn_facebook);

        //Instancia de los demás elementos de la vista.
        contrasenaEt = (EditText) v.findViewById(R.id.pass_et);
        correoEt = (EditText) v.findViewById(R.id.email_et);
        loginButton = (Button) v.findViewById(R.id.btn_iniciar_sesion);
        recuperarPasswordTv = (TextView) v.findViewById(R.id.tv_recuperar_pass);
        btnBack = (ImageButton) v.findViewById(R.id.btn_back);


        //Se muestra mensaje cuando se gana el foco
        correoEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    correoEt.setHint("user@example.com");
                    correoEt.setTypeface(Typeface.DEFAULT);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(correoEt, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    correoEt.setHint("");
                }
            }
        });

        //Botón para presionar atrás y volver al menú inicial.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getActivity().onBackPressed();
            }
        });

        //Eliminar mensaje de error mientras tiene foco de correo
        EditTextValidations.removeErrorTyping(correoEt);
        EditTextValidations.removeErrorTyping(contrasenaEt);

        //Configurando el input type
        contrasenaEt.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        contrasenaEt.setTypeface(Typeface.DEFAULT);


        loginButton.setOnClickListener(this);

        recuperarPasswordTv.setOnClickListener(this);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return v;
    }

    /**
     * Se ejecuta una vez que la vista ha sido creada, permite asignar eventos y configuraciones
     * a los botones de Google y Facebook..
     * @param view {View}
     * @param savedInstanceState {Bundle}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbButton = (Button) view.findViewById(R.id.btn_facebook);

        fbButton.setPadding(30, 30, 30, 30);
        googleSignInButton.setPadding(28, 28, 28, 28);

        fb.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        fb.setFragment(this);
        fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            /**
             * Método ejecutado cuando el inicio de sesión es exitoso, el parámetro recibido permite
             * obtener la información del usuario.
             * @param loginResult {LoginResult}
             */
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            /**
                             * Método de la interfaz que se ejecuta una vez que se obtiene la conexión
                             * con GraphRequest. Se obtiene un JSON con los dataos del usuario.
                             * @param object
                             * @param response
                             */
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //Si el resultado ha sido exitoso, se ejecuta la AsyncTask LoginFacebookAsyncTask.
                                final String email = object.optString("email"),
                                        id = object.optString("id"),
                                        nombre = object.optString("first_name"),
                                        apellido = object.optString("last_name"),
                                        fecha = object.optString("birthday");
                                final int gender = object.optString("gender").equals("male") ? 1 : 2;

                                Call<Response<Boolean>> call = usuarioAPI.verificarCorreo(
                                        email
                                );
                                loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);
                                call.enqueue(new Callback<Response<Boolean>>() {
                                    @Override
                                    public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                                        Response<Boolean> body = response.body();
                                        if(body.success){
                                            //Se ejecuta cuando el correo ya existe en la base de datos
                                            if(body.data){
                                                Call<Response<Usuario>> callFacebook = usuarioAPI.loginFacebook(
                                                        email,
                                                        id
                                                );
                                                callFacebook.enqueue(new Callback<Response<Usuario>>() {
                                                    @Override
                                                    public void onResponse(Call<Response<Usuario>> call, retrofit2.Response<Response<Usuario>> response) {
                                                        login(response);
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                                                        if(loginSimplePd != null){
                                                            loginSimplePd.dismiss();
                                                        }
                                                        logOutFacebook();
                                                        OKDialog.showOKDialog(getActivity(), "Error", "Error de conexión, intente más adelante");
                                                    }
                                                });
                                            }
                                            else{
                                                final Usuario u = new Usuario();
                                                final DatosUsuario du = new DatosUsuario();
                                                final Genero g = new Genero();
                                                u.setEmail(email);
                                                u.setIdFacebook(id);
                                                du.setNombre(nombre);
                                                du.setApellidoPaterno(apellido);
                                                du.setFechaNacimiento(fecha);
                                                g.setIdGenero(gender);
                                                du.setRutaImagen("http://graph.facebook.com/"+id+"/picture?type=large");
                                                du.setGenero(g);
                                                u.setDatosUsuario(du);

                                            /* make the API call */
                                                new GraphRequest(
                                                        AccessToken.getCurrentAccessToken(),
                                                        "/"+u.getIdFacebook()+"/picture?redirect=false&type=large",
                                                        null,
                                                        HttpMethod.GET,
                                                        new GraphRequest.Callback() {
                                                            public void onCompleted(GraphResponse response) {
                                                                JSONObject object = response.getJSONObject();
                                                                String url = object.optJSONObject("data").optString("url");
                                                                du.setRutaImagen(url);
                                                                RegistrarFragment f = RegistrarFragment.newInstance(u);
                                                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
                                                            }
                                                        }
                                                ).executeAsync();


                                            }
                                        }
                                        else{
                                            OKDialog.showOKDialog(getActivity(), "Error", body.errors[0]);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                                        if(loginSimplePd != null){
                                            loginSimplePd.dismiss();
                                        }
                                        logOutFacebook();
                                        OKDialog.showOKDialog(getActivity(), "Error", "Hubo un error al conectarse con el servidor.");
                                    }
                                });



                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,first_name,last_name,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            /**
             * Método que se ejecuta cuando el usuario cancela el logueo.
             */
            @Override
            public void onCancel() {
                OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión",
                        "Hubo un problema con la red. Revise su conexión a Internet");

            }

            /**
             * Método ejecutado al detectar un fallo en la conexión con el servidor
             * @param e {FacebookException}
             */
            @Override
            public void onError(FacebookException e) {
                OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión",
                        "Hubo un problema con la red. Revise su conexión a Internet");

            }
        });
        fbButton.setOnClickListener(this);

    }

       /**
     * Metodo utilizado para traer la información ya sea del login con google o facebook.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != Activity.RESULT_OK) {
                if(loginGooglePd != null)
                    loginGooglePd.dismiss();

            }
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }



    /**
    LOGIN DE GOOGLE
    **/

    /*
     * Obtiene el resultado del logueo utilizando Google
     * @param result: Resultado de la operación, de ser fallido, el inicio de sesión no
     *                puede continuar.
     */
    private void getSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            //Llamada para comprobar si el correo ya existe en la base de datos.
            Call<Response<Boolean>> call = usuarioAPI.verificarCorreo(
                    acct.getEmail()
            );
            loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);
            call.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    //Si el correo ya se encuentra registrado se intenta hacer un login con los datos de la sesión.
                    if(loginSimplePd != null) {
                        loginSimplePd.dismiss();
                    }

                    if(response.body().data){
                        Call<Response<Usuario>> callGoogle = usuarioAPI.loginGoogle(
                                acct.getEmail(),
                                acct.getId()
                        );
                        callGoogle.enqueue(new Callback<Response<Usuario>>() {
                            @Override
                            public void onResponse(Call<Response<Usuario>> call, retrofit2.Response<Response<Usuario>> response) {
                                login(response);
                            }

                            @Override
                            public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                                if(loginSimplePd != null){
                                    loginSimplePd.dismiss();
                                }
                                OKDialog.showOKDialog(getActivity(), "Error", "Error en la conexión, por favor, intente más adelante.");
                            }
                        });
                    }
                    else{
                        //Caso contrario, se genera una instancia de usuario que se pasará a
                        //Datos complementarios para llenar el perfil.
                        Usuario u = new Usuario();
                        DatosUsuario du = new DatosUsuario();

                        du.setNombre(acct.getGivenName());
                        du.setApellidoPaterno(acct.getFamilyName());
                        u.setEmail(acct.getEmail());
                        u.setIdGoogle(acct.getId());
                        if(acct.getPhotoUrl() != null) {
                            du.setRutaImagen(acct.getPhotoUrl().toString());
                        }
                        u.setDatosUsuario(du);
                        RegistrarFragment f = RegistrarFragment.newInstance(u);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
                    }
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    if(loginSimplePd != null){
                        loginSimplePd.dismiss();
                    }
                    OKDialog.showOKDialog(getActivity(), "Error", "Error en la conexión, favor de revisar más adelante.");
                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Error al intentar obtener tus datos, intenta más adelante.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            // Signed out, show unauthenticated UI.
        }
    }


    /**
     * Método que realiza el cierre de sesión de Google.
     */
    private void signOut() {
        if(googleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
    }



    @Override
    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.btn_iniciar_sesion:
                String correo = correoEt.getText().toString();
                String password = contrasenaEt.getText().toString();
                if (ValidEmail.isValidEmail(correo) != false) {
                    //((TextInputLayout) correoEt.getParent()).setError(null);
                    if (password.length() > 0) {
                        loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);
                        Call<Response<Usuario>> call = usuarioAPI.login(correo, password);
                        call.enqueue(new Callback<Response<Usuario>>() {
                            @Override
                            public void onResponse(Call<Response<Usuario>> call, retrofit2.Response<Response<Usuario>> response) {
                                login(response);
                            }

                            @Override
                            public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                                if(loginSimplePd != null){
                                    loginSimplePd.dismiss();
                                }
                                Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente.", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        ((TextInputLayout) contrasenaEt.getParent().getParent()).setErrorEnabled(true);
                        ((TextInputLayout) contrasenaEt.getParent().getParent()).setError("El campo se encuentra vacío");
                    }
                } else {
                    ((TextInputLayout) correoEt.getParent().getParent()).setErrorEnabled(true);
                    ((TextInputLayout) correoEt.getParent().getParent()).setError("Correo no valido");
                    correoEt.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    correoEt.setTypeface(Typeface.DEFAULT);
                }

                break;

            case R.id.btn_google:

                i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i, RC_SIGN_IN);
                break;

            case R.id.btn_facebook:
                fb.performClick();
                break;
            case R.id.tv_recuperar_pass:
                String correo_prefs = prefs.getString(CORREO, null);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f = null;
                String tag = null;
                if (correo_prefs == null) {
                    f = new RecuperarPasswordFragment();
                } else {
                    f = new ReestablecerPasswordFragment();
                }

                ft.replace(R.id.login_fragment_container, f);
                ft.addToBackStack(null);
                ft.commit();
                break;

        }
    }


    /**
     * Método que realiza el cierre de sesión de Facebook.
     *
     */
    public void logOutFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
    }


    /**
     * Método para el login, esta función se ejecuta con cualquier tipo de sesión (Normal, Google Facebook).
     * @param response
     */
    public void login(retrofit2.Response<Response<Usuario>> response) {
        if (loginSimplePd != null) {
            loginSimplePd.dismiss();
        }
        Response<Usuario> body = response.body();
        if (body != null) {
            Sesion.cargarSesion(body.data);
            ((LoginActivity) getActivity()).startHomeActivity();
        } else {
            Snackbar.make(getView(), "Email o Contraseña Incorrectos, intenta nuevamente.", Snackbar.LENGTH_LONG).show();
        }
    }

}