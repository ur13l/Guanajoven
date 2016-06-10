package code.guanajuato.gob.mx.activatecode.fragments;

import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.connection.ClienteHttp;
import code.guanajuato.gob.mx.activatecode.utilities.OKDialog;

/**
* Autor: Uriel Infante
 * Fragment de la interfaz de Reestablecer Contraseña, esta interfaz solicita un código de autenticación.
 * Una vez introducido el código correcto aparecen los campos para cambiar la contraseña.
 * Fecha: 02/05/2016
*/
public class ReestablecerPasswordFragment extends Fragment implements View.OnClickListener{

    private EditText codigoEt;
    private EditText password1Et;
    private EditText password2Et;
    private Button confirmarBtn;
    private Button aceptarBtn;
    private TextView reenviarCodigoTv;
    private LinearLayout enviarLl;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reestablecer_password, container, false);
        // Inflate the layout for this fragment
        AppCompatActivity appcom = (AppCompatActivity) getActivity();
        appcom.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appcom.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appcom.getSupportActionBar().setHomeButtonEnabled(true);
        appcom.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        appcom.getSupportActionBar().setTitle(R.string.recuperarpass2);

        codigoEt = (EditText) v.findViewById(R.id.et_codigo);
        password1Et = (EditText) v.findViewById(R.id.et_password1);
        password2Et = (EditText) v.findViewById(R.id.et_password2);
        aceptarBtn = (Button) v.findViewById(R.id.btn_enviar_codigo);
        confirmarBtn = (Button) v.findViewById(R.id.btn_reestablecer_pass);
        reenviarCodigoTv = (TextView) v.findViewById(R.id.tv_reenviar_codigo);
        enviarLl = (LinearLayout) v.findViewById(R.id.ll_enviar);

        aceptarBtn.setOnClickListener(this);
        confirmarBtn.setOnClickListener(this);
        reenviarCodigoTv.setOnClickListener(this);

        password1Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextInputLayout)password1Et.getParent()).setErrorEnabled(false);
                ((TextInputLayout)password1Et.getParent()).setError(null);
            }
        });


        return v;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_enviar_codigo:
                if(codigoEt.getText().toString().trim().length() > 0) {
                    new ReestablecerPasswordAsyncTask().execute(prefs.getString(LoginFragment.CORREO, null), codigoEt.getText().toString());
                }
                else{
                    //Implementar mensaje de código incompleto, darle tamaño de 5 y programar Reestablecer Contraseña
                }
                break;
            case R.id.tv_reenviar_codigo:
                prefs.edit().remove(LoginFragment.CORREO).commit();
                prefs.edit().remove(LoginFragment.CODIGO).commit();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment f = fm.findFragmentByTag("fragment_recuperar");
                if ( f == null ){
                    f = new RecuperarPasswordFragment();
                }
                ft.replace(R.id.login_fragment_container, f, "fragment_recuperar").commit();
                break;
            case R.id.btn_reestablecer_pass:
                if(password1Et.getText().toString().equals(password2Et.getText().toString())){
                    if(password1Et.getText().toString().length()  >6){
                       new CambiarPasswordAsyncTask().execute(prefs.getString(LoginFragment.CORREO,null), password1Et.getText().toString());
                    }
                    else{
                        ((TextInputLayout)password1Et.getParent()).setErrorEnabled(true);
                        ((TextInputLayout)password1Et.getParent()).setError("La contraseña debe ser mayor de 6 caracteres.");
                    }
                }
                else {
                    ((TextInputLayout)password1Et.getParent()).setErrorEnabled(true);
                    ((TextInputLayout)password1Et.getParent()).setError("Las contraseñas no coinciden.");
                }
                break;
        }
    }


    /**
     * Clase privada pra realizar la llamada asíncrona al servidor para reestablecer la contraseña de un usuario.
     */
    private class ReestablecerPasswordAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Enviando código", "Verificando código para reestablecer contraseña", true);
        }


        @Override
        protected Boolean doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            params.put("codigo", args[1].toString());
            String url = "http://"+ ClienteHttp.SERVER_IP+"/code_web/src/app_php/login/comprobarCodigo.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            return Boolean.valueOf(result);
        }

        @Override
        public void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result != null) {
                //Si el código y el correo coinciden
                if(result){
                    codigoEt.setEnabled(false);
                    password1Et.setVisibility(View.VISIBLE);
                    password2Et.setVisibility(View.VISIBLE);
                    confirmarBtn.setVisibility(View.VISIBLE);

                    aceptarBtn.setVisibility(View.GONE);
                    reenviarCodigoTv.setVisibility(View.GONE);
                    enviarLl.setVisibility(View.GONE);

                    password2Et.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password2Et.setTypeface(Typeface.DEFAULT);

                    password1Et.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password1Et.setTypeface(Typeface.DEFAULT);

                }
                //Si no coinciden
                else{
                    OKDialog.showOKDialog(getActivity(), "Codigo Incorrecto", "El código proporcionado no coincide con el del correo o ya ha caducado.");
                }
            }
            else{
                OKDialog.showOKDialog(getActivity(), "Error al recuperar contraseña", "Hubo un problema con la red. Revise su conexión a Internet");
            }

            progressDialog.dismiss();
        }
    }

    /**
     * Clase privada pra realizar la llamada asíncrona al servidor para el inicio de sesión con facebook
     */
    private class CambiarPasswordAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Cambiando Password", "Cambiando la contraseña", true);
        }


        @Override
        protected String doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            params.put("password", args[1].toString());
            String url = "http://"+ ClienteHttp.SERVER_IP+"/code_web/src/app_php/login/cambiarPassword.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            return result;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null) {
                //Si el cóadigo y el correo coinciden
                if(result.equals("success")){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Contraseña cambiada con éxito", Snackbar.LENGTH_LONG).show();
                    Fragment f = fm.findFragmentByTag("fragment_login");
                    ft.replace(R.id.login_fragment_container, f, "fragment_login").commit();

                    //Se eliminan las preferencias de usuario para no abrir este fragment directamente..
                    prefs.edit().remove(LoginFragment.CORREO).commit();
                    prefs.edit().remove(LoginFragment.CODIGO).commit();
                }
                else {
                    OKDialog.showOKDialog(getActivity(), "Error al cambiar contraseña", "No se pudo completar su petición para cambiar la contraseña.");
                }
            }
            else{
                OKDialog.showOKDialog(getActivity(), "Error al cambiar contraseña", "Hubo un problema con la red. Revise su conexión a Internet");
            }

            progressDialog.dismiss();
        }
    }

}
