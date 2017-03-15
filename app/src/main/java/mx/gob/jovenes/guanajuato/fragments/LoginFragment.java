package mx.gob.jovenes.guanajuato.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.HomeActivity;
import mx.gob.jovenes.guanajuato.activities.LoginActivity;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.model.LoginPOJO;
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
public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {


    //Constantes
    public final static String CORREO = "recuperar_correo";
    public final static String CODIGO = "recuperar_codigo";
    private static final int RC_SIGN_IN = 0;

    private boolean email, pass;


    //Elementos para el login con Google
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private GoogleSignInApi googleSignInApi;
    private Button googleSignInButton;
    private ProgressDialog loginGooglePd;
    private ImageButton btnBack;

    //Elementos para el login con Facebook
    //private LoginButton fbButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //Otros elementos
    private ProgressDialog loginSimplePd;
    private EditText contrasenaEt;
    private EditText correoEt;
    private Button loginButton;
    private TextView recuperarPasswordTv;

    private Retrofit retrofit;
    private UsuarioAPI usuarioAPI;

    public SharedPreferences prefs;


    /**
     * Callback para iniciar sesión con Facebook.
     */
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            //Si el resultado ha sido exitoso, se ejecuta la AsyncTask LoginFacebookAsyncTask.
                            String email = object.optString("email");
                            //new LoginFacebookAsyncTask().execute(email);
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email");
            request.setParameters(parameters);
            request.executeAsync();

        }

        //Si el login no fue exitoso, se envía un mensaje con la clase OKDialog.
        @Override
        public void onCancel() {
            OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Hubo un problema con la red. Revise su conexión a Internet");

        }

        @Override
        public void onError(FacebookException e) {
            OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Hubo un problema con la red. Revise su conexión a Internet");

        }
    };


    /**
     * Método de inicialización. Se instancian todas las referencias del login con Google y Facebook.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se instancian las GoogleSignInOptions y el GoogleApiClient.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null)
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();



        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
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
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //Instancia y listener del botón de Google.
        googleSignInButton = (Button) v.findViewById(R.id.googlebtn);
        googleSignInButton.setOnClickListener(this);

        //Instancia del botón de Facebook.
        //fbButton = (LoginButton) v.findViewById(R.id.btn_facebook);

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
                } else {
                    correoEt.setHint("");
                }
            }
        });

        //Botón para presionar atrás y volver al menú inicial.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        loginGooglePd = new ProgressDialog(getActivity());
        loginGooglePd.setMessage("Signing in...");


        recuperarPasswordTv.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        logOutFacebook();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //fbButton = (LoginButton) view.findViewById(R.id.btn_facebook);

        //fbButton.setBackgroundResource(R.drawable.facebook_button);
        //fbButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_logo, 0, 0, 0);

        //fbButton.setPadding(30, 30, 30, 30);
        googleSignInButton.setPadding(28, 28, 28, 28);

        //loginButton.setReadPermissions("user_friends");
        //fbButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        //fbButton.setFragment(this);
        //fbButton.registerCallback(callbackManager, callback);


    }

    //Métodos utilizados por Facebook.
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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

    private void showDialog() {
        String dialog_title = getResources().getString(R.string.politica);
        String dialog_message = getResources().getString(R.string.mensaje_aviso_de_privacidad);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialog_title);
        builder.setMessage(Html.fromHtml(dialog_message));

        String positiveText = "Acepto";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    /**
     * Obtiene el resultado del logueo utilizando Google
     *
     * @param result: Resultado de la operación, de ser fallido, el inicio de sesión no
     *                puede continuar.
     */
    private void getSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //new LoginGoogleAsyncTask().execute(acct.getEmail());
            if(loginGooglePd != null)
                loginGooglePd.dismiss();
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        googleApiClient.connect();
        if(googleApiClient.isConnected()){
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

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
                                loginSimplePd.dismiss();
                                Response<Usuario> body = response.body();
                                if(body.success){
                                    Sesion.cargarSesion(body.data);
                                    ((LoginActivity)getActivity()).startHomeActivity();
                                }
                                else{
                                    Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente.", Snackbar.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                                loginSimplePd.dismiss();
                                Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente.", Snackbar.LENGTH_LONG).show();
                                Log.d("WOW", "Error");
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
                    email = false;
                }

                break;

            case R.id.googlebtn:
                i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i, RC_SIGN_IN);
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


    private void signOut() {
        if(googleApiClient.isConnected())
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }







    public void logOutFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
    }
}