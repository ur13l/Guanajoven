package code.guanajuato.gob.mx.activatecode.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.activities.HomeActivity;
import code.guanajuato.gob.mx.activatecode.connection.ClienteHttp;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.model.LoginPOJO;
import code.guanajuato.gob.mx.activatecode.utilities.EditTextValidations;
import code.guanajuato.gob.mx.activatecode.utilities.OKDialog;
import code.guanajuato.gob.mx.activatecode.utilities.ValidEmail;

/**
 * Autor: Uriel Infante
 * Fragment de la interfaz Login, el usuario puede introducir su correo y contraseña para iniciar sesión.
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


    private Toolbar toolbar;
    //Elementos para el login con Google
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;
    private GoogleSignInApi googleSignInApi;
    private AppCompatButton googleSignInButton;
    private ProgressDialog loginGooglePd;

    //Elementos para el login con Facebook
    private LoginButton fbButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //Otros elementos
    private ProgressDialog loginSimplePd;
    private EditText contrasenaEt;
    private EditText correoEt;
    private AppCompatButton loginButton;
    private TextView recuperarPasswordTv;
    private Button btnRegistrar;

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
                            new LoginFacebookAsyncTask().execute(email);
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


        //Inicialización del SDK de Facebook, llamada al callback y definición del TokenTrackerb y ProfileTracker.
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
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

        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbarlogin);

        //Instancia y listener del botón de Google.
        googleSignInButton = (AppCompatButton) v.findViewById(R.id.googlebtn);
        googleSignInButton.setOnClickListener(this);

        //Instancia del botón de Facebook.
        fbButton = (LoginButton) v.findViewById(R.id.btn_facebook);

        //Instancia de los demás elementos de la vista.
        contrasenaEt = (EditText) v.findViewById(R.id.pass_et);
        correoEt = (EditText) v.findViewById(R.id.email_et);
        loginButton = (AppCompatButton) v.findViewById(R.id.btn_iniciar_sesion);
        recuperarPasswordTv = (TextView) v.findViewById(R.id.tv_recuperar_pass);
        btnRegistrar = (Button) v.findViewById(R.id.btnRegistrar);

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
        btnRegistrar.setOnClickListener(this);

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
        fbButton = (LoginButton) view.findViewById(R.id.btn_facebook);

        fbButton.setBackgroundResource(R.drawable.facebook_button);
        fbButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_logo, 0, 0, 0);

        fbButton.setPadding(30, 30, 30, 30);
        googleSignInButton.setPadding(28, 28, 28, 28);

        //loginButton.setReadPermissions("user_friends");
        fbButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        fbButton.setFragment(this);
        fbButton.registerCallback(callbackManager, callback);


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
            new LoginGoogleAsyncTask().execute(acct.getEmail());
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
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar.setVisibility(View.VISIBLE);
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
                    ((TextInputLayout) correoEt.getParent()).setError(null);
                    if (password.length() > 0) {
                        new LoginSimpleAsyncTask().execute(correo, password);
                    } else {
                        ((TextInputLayout) contrasenaEt.getParent()).setErrorEnabled(true);
                        ((TextInputLayout) contrasenaEt.getParent()).setError("El campo se encuentra vacío");
                    }
                } else {
                    ((TextInputLayout) correoEt.getParent()).setErrorEnabled(true);
                    ((TextInputLayout) correoEt.getParent()).setError("Correo no valido");
                    correoEt.setHintTextColor(Color.BLACK);
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

            case R.id.btnRegistrar:
                FragmentManager fm1 = getActivity().getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                RegistrarFragment f1 = new RegistrarFragment();
                ft1.replace(R.id.login_fragment_container, f1).addToBackStack(null).commit();
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

    /**
     * Clase privada pra realizar la llamada asíncrona al servidor para el inicio de sesión simple
     */
    private class LoginSimpleAsyncTask extends AsyncTask<String, Void, LoginPOJO> {

        @Override
        protected void onPreExecute() {
            loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);

        }

        @Override
        protected LoginPOJO doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            params.put("password", args[1].toString());
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/login/loginSimple.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            return gson.fromJson(result, LoginPOJO.class);
        }

        @Override
        public void onPostExecute(LoginPOJO result) {

            if (result != null) {

                    Login sesion = new Login(getActivity().getApplicationContext());
                    sesion.setId(result.getId());
                    sesion.setCorreo(result.getCorreo());
                    sesion.setFacebook(result.isFacebook() == 1);
                    sesion.setGoogle(result.isGoogle() == 1);

                    if (sesion.getId() == 0) {
                        Snackbar snack = Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente", Snackbar.LENGTH_LONG);
                        View sView = snack.getView();
                        sView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.error));
                        snack.show();
                    } else {
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        //new TutorAsyncTask().execute(sesion.getId(), 4);

                        //Entra al sistema SERVLET
                    }

            } else {
                if(loginSimplePd.isShowing()){
                    loginSimplePd.dismiss();
                }
                OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Hubo un problema con la red. Revise su conexión a Internet");
            }
        }
    }

    /**
     * Clase privada pra realizar la llamada asíncrona al servidor para el inicio de sesión con facebook
     */
    private class LoginFacebookAsyncTask extends AsyncTask<String, Void, LoginPOJO> {

        @Override
        protected void onPreExecute() {
            loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);

        }


        @Override
        protected LoginPOJO doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/login/loginFacebook.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            return gson.fromJson(result, LoginPOJO.class);
        }

        @Override
        public void onPostExecute(LoginPOJO result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result.getInserted() == 0) {
                    Login sesion = new Login(getActivity().getApplicationContext());
                    sesion.setId(result.getId());
                    sesion.setCorreo(result.getCorreo());
                    sesion.setFacebook(result.isFacebook() == 1);
                    sesion.setGoogle(result.isGoogle() == 1);

                    //Error para cuando los datos de sesión son incorrectos o no se ha traído bien la información.
                    if (sesion.getId() == 0) {
                        Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente", Snackbar.LENGTH_LONG).show();
                    } else
                        //Error para cuando ya existe el registro como de Google o Facebook
                        if (sesion.getId() == -1) {
                            OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Su cuenta de correo ya se encuentra ligada a otro tipo de cuenta.");
                            LoginManager.getInstance().logOut();

                        } else {
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            //new TutorAsyncTask().execute(sesion.getId(), 4);

                            //Entra al sistema SERVLET
                        }
                }
                //Cuando se debe realizar la inserción, enviar a Datos complementarios con correo, google, facebook, password = null;
                else {
                    DatosComplementariosFragment fragment = DatosComplementariosFragment.newInstance(result.getCorreo(), "", result.isFacebook(), result.isGoogle());
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.login_fragment_container, fragment).addToBackStack(null).commit();
                }
            } else {
                if(loginSimplePd.isShowing()){
                    loginSimplePd.dismiss();

                }
                OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Hubo un problema con la red. Revise su conexión a Internet");
            }

        }
    }

    /**
     * Clase privada pra realizar la llamada asíncrona al servidor para el inicio de sesión con facebook
     */
    private class LoginGoogleAsyncTask extends AsyncTask<String, Void, LoginPOJO> {

        @Override
        protected void onPreExecute() {
            loginSimplePd = ProgressDialog.show(getActivity(), "Iniciando sesión", "Espere un momento mientras se inicia la sesión", true);

        }


        @Override
        protected LoginPOJO doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            String url = "http://" + ClienteHttp.SERVER_IP + "/code_web/src/app_php/login/loginGoogle.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            return gson.fromJson(result, LoginPOJO.class);
        }

        @Override
        public void onPostExecute(LoginPOJO result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result.getInserted() == 0) {
                    Login sesion = new Login(getActivity().getApplicationContext());
                    sesion.setId(result.getId());
                    sesion.setCorreo(result.getCorreo());
                    sesion.setFacebook(result.isFacebook() == 1);
                    sesion.setGoogle(result.isGoogle() == 1);

                    if (sesion.getId() == 0) {
                        Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Email o Contraseña Incorrectos, intenta nuevamente", Snackbar.LENGTH_LONG);
                    } else
                        //Error para cuando ya existe el registro como de Google o Facebook
                        if (sesion.getId() == -1) {
                            OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Su cuenta de correo ya se encuentra ligada a otro tipo de cuenta.");

                        } else {
                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            //new TutorAsyncTask().execute(sesion.getId(), 4);

                            //Entra al sistema SERVLET
                        }
                }
                //Cuando se debe realizar la inserción, enviar a Datos complementarios con correo, google, facebook, password = null;
                else {
                    DatosComplementariosFragment fragment = DatosComplementariosFragment.newInstance(result.getCorreo(), "", result.isFacebook(), result.isGoogle());
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.login_fragment_container, fragment).addToBackStack(null).commit();
                }
            } else {
                if(loginSimplePd.isShowing()){
                    loginSimplePd.dismiss();
                }

                if(googleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                }
                OKDialog.showOKDialog(getActivity(), "Error al iniciar sesión", "Hubo un problema con la red. Revise su conexión a Internet");
            }
        }
    }

    public void logOutFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            LoginManager.getInstance().logOut();
        }
    }
}