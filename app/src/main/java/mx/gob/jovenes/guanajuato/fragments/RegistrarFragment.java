package mx.gob.jovenes.guanajuato.fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import mx.gob.jovenes.guanajuato.utils.OKDialog;


/**
 * Autor: Chema Cruz Parada, Uriel Infante
 * Fragment de la interfaz inicial de registro, solicita el correo, el nombre de usuario y la contraseña.
 * Fecha: 04/05/2016
 */
public class RegistrarFragment extends Fragment implements  View.OnClickListener {
    private Button continuarBtn;
    private EditText emailEt;
    private EditText password1Et;
    private EditText password2Et;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatActivity a = (AppCompatActivity)getActivity();
        a.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        a.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        a.getSupportActionBar().setTitle(R.string.datos_complementarios);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_registrar, parent, false);

        //Declaración de vistas
        emailEt = (EditText) v.findViewById(R.id.et_emailreg);
        password1Et = (EditText) v.findViewById(R.id.et_passreg);
        password2Et = (EditText) v.findViewById(R.id.et_confpass);
        continuarBtn = (Button) v.findViewById(R.id.btn_continuar);


        //Configurando el input type de contraseña en los dos campos.
        password1Et.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password1Et.setTypeface(Typeface.DEFAULT);

        password2Et.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password2Et.setTypeface(Typeface.DEFAULT);

        EditTextValidations.removeErrorTyping(emailEt);
        EditTextValidations.removeErrorTyping(password1Et);
        EditTextValidations.removeErrorTyping(password2Et);

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
    public void continuar(){
        //Verifica que los campos no estén vacíos
        boolean emailEmpty = EditTextValidations.esCampoVacio(emailEt);
        boolean pass1Empty = EditTextValidations.esCampoVacio(password1Et);
        boolean pass2Empty = EditTextValidations.esCampoVacio(password2Et);
        boolean emailV = false;
        boolean pass1V = false;
        boolean pass2V = false;
        boolean passEq = false;

        //Si ninguno de los campos es vacío
        if(!emailEmpty && !pass1Empty && !pass2Empty){
            emailV = EditTextValidations.esEmailValido(emailEt);
            pass1V = EditTextValidations.esContrasenaValida(password1Et);
            pass2V = EditTextValidations.esContrasenaValida(password2Et);
            passEq = EditTextValidations.contrasenasCoinciden(password1Et, password2Et);
        }

        //Si todas las validaciones se cumplen, se genera el nuevo fragment.
        if(emailV && pass1V && pass2V && passEq) {
            new VerificarCorreoAsyncTask().execute(emailEt.getText().toString());
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
                    Fragment f = DatosComplementariosFragment.newInstance(emailEt.getText().toString(),
                            password1Et.getText().toString(), 0, 0);
                    ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
                }
                else{
                    ((TextInputLayout)emailEt.getParent()).setErrorEnabled(true);
                    ((TextInputLayout)emailEt.getParent()).setError("Este correo ya se encuentra registrado");
                }

            } else {
                OKDialog.showOKDialog(getActivity(), "Error al registrar usuario", "Hubo un problema con la red. Revise su conexión a Internet");
            }

            progressDialog.dismiss();
        }
    }

}