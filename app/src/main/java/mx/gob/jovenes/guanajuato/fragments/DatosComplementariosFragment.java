package mx.gob.jovenes.guanajuato.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.HomeActivity;
import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.model.Login;
import mx.gob.jovenes.guanajuato.model.LoginPOJO;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import mx.gob.jovenes.guanajuato.utils.OKDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Autor: Uriel Infante
 * Fragment de datos complementarios del usuario, esta interfaz solicita información obligatoria
 * al usuario, y es llamada cuando se va a crear un nuevo usuario, sin importar su forma de logueo.
 * Fecha: 02/05/2016
 */
public class DatosComplementariosFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener{
    private String[] arraySiNo = {"Sí", "No"};


    private MaterialSpinner generoS;
    private Button registrarBtn;
    private ProgressDialog progressDialog;
    private String[] arrayGenero = {"Hombre", "Mujer"};
    private EditText fechET;
    private String fecha;
    private Calendar calendar;
    private TextView politicaTv;

    private LoginPOJO loginPrevio;
    private HashMap<String, String> params;




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        AppCompatActivity a = (AppCompatActivity)getActivity();
        a.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        a.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        a.getSupportActionBar().setTitle(R.string.datos_complementarios);

        Bundle args = getArguments();
        loginPrevio = new LoginPOJO();
        loginPrevio.setCorreo(args.getString("correo"));
        loginPrevio.setContrasena(args.getString("password"));
        loginPrevio.setFacebook(args.getInt("facebook"));
        loginPrevio.setGoogle(args.getInt("google"));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int aux;
        calendar.set(Calendar.YEAR, year);
        fecha = "" + year;
        calendar.set(Calendar.MONTH, monthOfYear);
        aux = monthOfYear + 1;
        if(aux > 10){
            fecha += "-" + aux ;
        } else fecha += "-0" + aux;
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(dayOfMonth < 10) {
            fecha += "-0" + dayOfMonth;
        } else fecha += "-" + dayOfMonth;
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        fechET.setText(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_datos_complementarios, parent, false);

        calendar = Calendar.getInstance();
        generoS = (MaterialSpinner) v.findViewById(R.id.spinner_genero);
        fechET = (EditText) v.findViewById(R.id.et_fech);
        registrarBtn = (Button) v.findViewById(R.id.btn_registrar);
        politicaTv = (TextView) v.findViewById(R.id.aviso_privacidad);

        politicaTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySiNo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fechET.setKeyListener(null);
        fechET.setOnFocusChangeListener(this);
        fechET.setOnClickListener(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayGenero);
        //Cambiar item
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoS.setAdapter(adapter2);

        EditTextValidations.removeErrorTyping(fechET);

        registrarBtn.setOnClickListener(this);
        return v;
    }


    /**
     * Método para inicializar el fragment con los nuevos datos para ser dados de alta. Este formulario debe ser llenado para completar el registro.
     * @param correo
     * @param password
     * @param facebook
     * @param google
     * @return
     */
    public static DatosComplementariosFragment newInstance(String correo, String password, int facebook, int google){
        DatosComplementariosFragment f = new DatosComplementariosFragment();
        Bundle args = new Bundle();
        args.putString("correo", correo);
        args.putString("password", password);
        args.putInt("facebook", facebook);
        args.putInt("google", google);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_registrar:
                registrar();
                break;
            case R.id.et_fech:
                showCalendar();
        }
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            // desplegar el calendario para introducir fecha
            case R.id.et_fech:
                if(b) {
                    showCalendar();
                }
                break;
        }
    }

    public void showCalendar(){

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                DatosComplementariosFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    //Método para proceder al registro.
    public void registrar(){
        boolean fechaEmpty = EditTextValidations.esCampoVacio(fechET);
        boolean generoEmpty = EditTextValidations.spinnerSinSeleccion(generoS);

        if(!generoEmpty && !fechaEmpty) {

            String generoNum = generoS.getSelectedItemPosition() + "";

            params = new HashMap<>();
            params.put("correo", loginPrevio.getCorreo());
            params.put("password", loginPrevio.getContrasena());
            params.put("facebook", ""+loginPrevio.isFacebook());
            params.put("google", ""+loginPrevio.isGoogle());
            params.put("fec_nacimiento", fecha);
            new RegistrarAsyncTask().execute(params);
        }
        // boolean presionEmpty = EditTextValidations.
    }

    @Override
    public void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
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
     * Clase privada pra realizar la llamada asíncrona y registrar los datos complementarios de un usuario.
     */
    private class RegistrarAsyncTask extends AsyncTask<HashMap<String, String>, Void, Integer> {

        /**
         * Método previo a la solicitud en el servidor, muestra un ProgressDialog.
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Conectando con el servidor", "Enviando datos de registro.", true);

        }

        /**
         * Se realiza la llamada al servidor con los argumentos.
         * @param args
         * @return Integer.
         */
        @Override
        protected Integer doInBackground(HashMap<String, String>... args) {
            String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/registro/crearUsuario.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, args[0]);
            return Integer.parseInt(result);
        }


        /**
         * Llamada ejecutada al finalizar la petición, completa el inicio de sesión con el usuario.
         * @param result
         */
        @Override
        public void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result != null) {
                if(result != 0){
                    Login login = new Login(getActivity().getApplicationContext());
                    login.setCorreo(loginPrevio.getCorreo());
                    login.setFacebook(loginPrevio.isFacebook() == 1);
                    login.setGoogle(loginPrevio.isGoogle() == 1);
                    login.setId(result);
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                }
                else{
                    OKDialog.showOKDialog(getActivity(), "Error al registrar usuario", "Su solicitud no pudo ser completada");
                }

            } else {
                OKDialog.showOKDialog(getActivity(), "Error al registrar usuario", "Hubo un problema con la red. Revise su conexión a Internet");
            }

        }
    }
}
