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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;

import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.model.models_tmp.RecuperarPass;
import mx.gob.jovenes.guanajuato.utils.OKDialog;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Autor: Uriel Infante
 * Fragment de Recuperar password, se muestra cuando el usuario presiona "Olvidaste tu contraseña?.
 * La interfaz solicita el correo electrónico para enviar un código de recuperación.
 * Fecha: 02/05/2016
 */
public class RecuperarPasswordFragment extends Fragment implements View.OnClickListener{
    private EditText correoEt;
    private Button recuperarPasswordBtn;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private UsuarioAPI usuarioAPI;
    private ImageButton btnBack;


    /**
     * Inicialización de las preferencias.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        usuarioAPI = ((MyApplication) getActivity().getApplication()).getRetrofitInstance().create(UsuarioAPI.class);
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

        //Declaración de los elementos visuales.
        //Declaración de los elementos visuales.
        recuperarPasswordBtn = (Button) v.findViewById(R.id.btn_recuperar_password);
        correoEt = (EditText) v.findViewById(R.id.et_correo);
        btnBack = (ImageButton) v.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        recuperarPasswordBtn.setOnClickListener(this);

        return v;
    }


    /*
    Se ejecuta la tarea para enviar el código.
     */
    @Override
    public void onClick(View view) {
        String correo = correoEt.getText().toString();
        progressDialog = ProgressDialog.show(getActivity(), getString(R.string.espera), getString(R.string.verificando), true);
        Call<Response<Boolean>> call = usuarioAPI.recuperarPassword(correo);
        call.enqueue(new Callback<Response<Boolean>>() {
            @Override
            public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                Response<Boolean> resp = response.body();
                if(resp.success){
                    OKDialog.showOKDialog(getActivity(),  getString(R.string.correo_enviado), getString(R.string.enviado));
                }
                else{
                    OKDialog.showOKDialog(getActivity(),  getString(R.string.error), resp.errors[0]);
                }
            }

            @Override
            public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                OKDialog.showOKDialog(getActivity(), getString(R.string.error), getString(R.string.hubo_error));
            }
        });

    }

}
