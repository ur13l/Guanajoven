package mx.gob.jovenes.guanajuato.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.models_tmp.RecuperarPass;
import mx.gob.jovenes.guanajuato.utils.OKDialog;


/**
 * Autor: Uriel Infante
 * Fragment de Recuperar password, se muestra cuando el usuario presiona "Olvidaste tu contraseña?.
 * La interfaz solicita el correo electrónico para enviar un código de recuperación.
 * Fecha: 02/05/2016
 */
public class RecuperarPasswordFragment extends Fragment implements View.OnClickListener{
    private EditText correoEt;
    private AppCompatButton recuperarPasswordBtn;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;


    /**
     * Inicialización de las preferencias.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    }

    /**
     * Método para hacer el inflate de la vista, se declaran también los elementos visuales.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recuperar_pass, container, false);


        // Personalización de la barra, se muestra el botón de navegación hacia atrás y cambia el título.
        AppCompatActivity appcom = (AppCompatActivity) getActivity();
        appcom.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appcom.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appcom.getSupportActionBar().setHomeButtonEnabled(true);
        appcom.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        appcom.getSupportActionBar().setTitle(R.string.recuperarpass2);

        //Declaración de los elementos visuales.
        //Declaración de los elementos visuales.
        recuperarPasswordBtn = (AppCompatButton) v.findViewById(R.id.btn_recuperar_password);
        correoEt = (EditText) v.findViewById(R.id.et_correo);
        recuperarPasswordBtn.setOnClickListener(this);

        return v;
    }


    /*
    Se ejecuta la tarea para enviar el código.
     */
    @Override
    public void onClick(View view) {
        String correo = correoEt.getText().toString();
        new RecuperarPasswordAsyncTask().execute(correo);
    }


    /**
     * Clase privada para enviar el correo con el código de usuario y solicitar el cambio de contraseña.
     */
    private class RecuperarPasswordAsyncTask extends AsyncTask<String, Void, RecuperarPass> {

        @Override
        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(getActivity(), "Enviando código", "Enviando código de recuperación de contraseña.", true);
        }


        /**
         * La clase RecuperarPass obtiene el código y el status de la operación.
         * El método doInBackground se ejecuta de manera asíncrona.
         * @param args
         * @return
         */
        @Override
        protected RecuperarPass doInBackground(String... args) {
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            String url = "http://"+ ClienteHttp.SERVER_IP+"//app_php/login/recuperarContrasena.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, params);
            Gson gson = new Gson();
            return gson.fromJson(result, RecuperarPass.class);
        }

        @Override
        public void onPostExecute(RecuperarPass result) {
            super.onPostExecute(result);
            if(result != null) {
                if(result.getStatus().equals("valid")){
                    //Se guardan las preferencias de usuario, para ingresar directamente a ReestablecerPasswordFragment.
                    prefs.edit().putString(LoginFragment.CORREO, correoEt.getText().toString()).commit();
                    prefs.edit().putString(LoginFragment.CODIGO, result.getCodigo()).commit();

                    //Se cambia el fragment en pantalla.
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    Fragment f = new ReestablecerPasswordFragment();
                    ft.replace(R.id.login_fragment_container, f, "fragment_reestablecer").commit();

                }
                else{
                    OKDialog.showOKDialog(getActivity(), "Correo inválido", "El correo del cual desea recuperar su contraseña es inválido o no se encuentra registrado");
                }
            }
            else{
                OKDialog.showOKDialog(getActivity(), "Error al recuperar contraseña", "Hubo un problema con la red. Revise su conexión a Internet");
            }

            progressDialog.dismiss();
        }
    }

}
