package mx.gob.jovenes.guanajuato.fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.LoginActivity;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import mx.gob.jovenes.guanajuato.utils.OKDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


/**
 * Autor: Chema Cruz Parada, Uriel Infante
 * Fragment de la interfaz inicial de registro, solicita el correo, el nombre de usuario y la contraseña.
 * Fecha: 04/05/2016
 */
public class RegistrarFragment extends Fragment implements  View.OnClickListener {
    private Button continuarBtn;
    private EditText etEmail;
    private EditText etPassword1;
    private EditText etPassword2;
    private EditText etNombre;
    private MaterialSpinner spnGenero;
    private EditText etCodigoPostal;
    private EditText etCurp;
    private ProgressDialog progressDialog;
    private ImageButton btnBack;

    private UsuarioAPI usuarioAPI;

    private String[] generos = {"Masculino", "Femenino"};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Retrofit retrofit = ((MyApplication)getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_registrar, parent, false);

        //Declaración de vistas
        etEmail = (EditText) v.findViewById(R.id.et_emailreg);
        etPassword1 = (EditText) v.findViewById(R.id.et_passreg);
        etPassword2 = (EditText) v.findViewById(R.id.et_confpass);
        continuarBtn = (Button) v.findViewById(R.id.btn_continuar);
        etNombre = (EditText) v.findViewById(R.id.et_nombre);
        etCodigoPostal = (EditText) v.findViewById(R.id.et_codigo_postal);
        etCurp = (EditText) v.findViewById(R.id.et_curp);
        spnGenero = (MaterialSpinner) v.findViewById(R.id.spn_genero);
        btnBack = (ImageButton) v.findViewById(R.id.btn_back);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, generos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenero.setAdapter(adapter);


        //Configurando el input type de contraseña en los dos campos.
        etPassword1.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword1.setTypeface(Typeface.DEFAULT);

        etPassword2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword2.setTypeface(Typeface.DEFAULT);

        EditTextValidations.removeErrorTyping(etEmail);
        EditTextValidations.removeErrorTyping(etPassword1);
        EditTextValidations.removeErrorTyping(etPassword2);

        btnBack.setOnClickListener(view -> getActivity().onBackPressed());

        continuarBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continuar:
                continuar();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * Ejecución de lo que se hará al presionar el botón de contninuar, se realizan las validaciones
     * para pasar a la interfaz de Datos complementarios.
     */
    //TODO: Validación para código postal, curp y género.
    public void continuar(){
        //Verifica que los campos no estén vacíos
        boolean emailEmpty = EditTextValidations.esCampoVacio(etEmail);
        boolean pass1Empty = EditTextValidations.esCampoVacio(etPassword1);
        boolean pass2Empty = EditTextValidations.esCampoVacio(etPassword2);
        boolean nombreEmpty = EditTextValidations.esCampoVacio(etNombre);
        boolean generoEmpty = EditTextValidations.spinnerSinSeleccion(spnGenero);
        boolean curpEmpty = EditTextValidations.esCampoVacio(etCurp);
        boolean cpEmpty = EditTextValidations.esCampoVacio(etCodigoPostal);
        boolean emailV = false;
        boolean pass1V = false;
        boolean pass2V = false;
        boolean passEq = false;

        //Si ninguno de los campos es vacío
        if(!emailEmpty && !pass1Empty && !pass2Empty && !nombreEmpty && !generoEmpty && !curpEmpty && !cpEmpty){
            emailV = EditTextValidations.esEmailValido(etEmail);
            pass1V = EditTextValidations.esContrasenaValida(etPassword1);
            pass2V = EditTextValidations.esContrasenaValida(etPassword2);
            passEq = EditTextValidations.contrasenasCoinciden(etPassword1, etPassword2);
        }

        //Si todas las validaciones se cumplen, se genera el nuevo fragment.
        if(emailV && pass1V && pass2V && passEq) {
            Call<Response<Usuario>> call = usuarioAPI.registrar(
                    etEmail.getText().toString(),
                    etPassword1.getText().toString(),
                    etPassword2.getText().toString(),
                    etNombre.getText().toString(),
                    spnGenero.getSelectedItemPosition() + "",//.getText().toString(),
                    null,
                    null,
                    null,
                    null,
                    etCodigoPostal.getText().toString(),
                    null,
                    etCurp.getText().toString()
            );

            progressDialog = ProgressDialog.show(getActivity(), "Registrando", "Espere un momento mientras se completa el registro", true);
            call.enqueue(new Callback<Response<Usuario>>() {
                @Override
                public void onResponse(Call<Response<Usuario>> call, retrofit2.Response<Response<Usuario>> response) {
                    progressDialog.dismiss();
                    Response<Usuario> body = response.body();
                    if(body.success){
                        Sesion.cargarSesion(body.data);
                        ((LoginActivity)getActivity()).startHomeActivity();

                    }
                    else{
                        Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Hubo un error al registrar su solicitud, intente más tarde.", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                    Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Hubo un error al registrar su solicitud, intente más tarde.", Snackbar.LENGTH_LONG).show();

                }
            });
        }
    }

    /**
     * Clase privada para comprobar la existencia de un correo en la BD.
     */
    private class VerificarCorreoAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Conectando con el servidor", "Comprobando existencia de correo.", true);

        }


        @Override
        protected Boolean doInBackground(String... args) {
            String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/registro/comprobarCorreo.php";
            ClienteHttp cliente = new ClienteHttp();
            HashMap<String, String> params = new HashMap<>();
            params.put("correo", args[0].toString());
            String result = cliente.hacerRequestHttp(url, params);
            Boolean b = null;
            if(result.equals("true")){
                b = true;
            }
            else if(result.equals("false")){
                b = false;
            }
            return b;
        }

        @Override
        public void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result != null) {
                if(!result){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    //La nueva instancia lleva correo y contraseña,los campos de facebook y google van en 0.
                    Fragment f = DatosComplementariosFragment.newInstance(etEmail.getText().toString(),
                            etPassword1.getText().toString(), 0, 0);
                    ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
                }
                else{
                    ((TextInputLayout)etEmail.getParent()).setErrorEnabled(true);
                    ((TextInputLayout)etEmail.getParent()).setError("Este correo ya se encuentra registrado");
                }

            } else {
                OKDialog.showOKDialog(getActivity(), "Error al registrar usuario", "Hubo un problema con la red. Revise su conexión a Internet");
            }

            progressDialog.dismiss();
        }
    }

}