package mx.gob.jovenes.guanajuato.fragments;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import mx.gob.jovenes.guanajuato.connection.ClienteHttp;
import mx.gob.jovenes.guanajuato.utils.MathFormat;
import mx.gob.jovenes.guanajuato.model.Login;
import mx.gob.jovenes.guanajuato.model.Perfil;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.utils.OKDialog;

public class PerfilFragment extends CustomFragment implements  View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener, TextWatcher {
    private EditText nomcomET;
    private MaterialSpinner generoS;
    private String[] arrayGenero = {"Hombre", "Mujer"};
    private EditText fechET;
    private String fecha;
    private TextView polipriTV;
    private CheckBox polipriCB;
    private Button actdatB;
    private Calendar calendar;
    private String fechapojo;
    private boolean calen = false;
    private Perfil perfil;

    private ProgressDialog progressDialog;
    private HashMap<String, String> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, parent, false);

        calendar = Calendar.getInstance();
        //Declaración de vistas
        nomcomET = (EditText) v.findViewById(R.id.et_nomcom);
        generoS = (MaterialSpinner) v.findViewById(R.id.spinner_genero);
        fechET = (EditText) v.findViewById(R.id.et_fech);

        polipriTV = (TextView) v.findViewById(R.id.tv_polipri);
        polipriCB = (CheckBox) v.findViewById(R.id.cb_polipri);
        actdatB = (Button) v.findViewById(R.id.btn_actudat);

        fechET.setKeyListener(null);
        fechET.setOnFocusChangeListener(this);
        fechET.setOnClickListener(this);
        actdatB.setOnClickListener(this);
        polipriCB.setOnClickListener(this);
        polipriTV.setOnClickListener(this);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arrayGenero);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoS.setAdapter(adapter);


        perfil = new Perfil(getActivity().getApplicationContext());
        nomcomET.setText(perfil.getNombreCompleto());
        generoS.setSelection(perfil.getGenero());


        fechapojo = perfil.getFecha();

        Date nac = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdg = new SimpleDateFormat("yyyy-MM-dd");

        try {
            nac = sdg.parse(perfil.getFecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fechET.setText(sdf.format(nac));

        return v;
    }

    public void continuar(){
        //Verifica que los campos no estén vacíos
        boolean fechaEmpty = EditTextValidations.esCampoVacio(fechET);
        boolean generoEmpty = EditTextValidations.spinnerSinSeleccion(generoS);

        boolean fechafalse = false;
        boolean codigofalse = false;
        boolean celfalse = false;


               //Si ninguno de los campos es vacío
        if(!fechaEmpty && codigofalse && celfalse && !generoEmpty){
            fechafalse = true;
        }

        //Si todas las validaciones se cumplen, se genera el nuevo fragment.
        if(fechafalse && codigofalse) {
            registrar();
        }
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
        calen=true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_fech:
                showCalendar();
                break;

            case R.id.tv_polipri:
                showLocationDialog();
                break;

            case R.id.cb_polipri:
                try{
                    if(polipriCB.isChecked() != false){
                        actdatB.setEnabled(true);
                    } else actdatB.setEnabled(false);
                }catch (Exception e) {
                    System.out.println("Error al hacer check en politica de privacidad");
                }

                break;

            case R.id.btn_actudat:
                try {
                    continuar();
                } catch (Exception  e) {OKDialog.showOKDialog(getActivity(), "Error al actualizar información de usuario", e.getMessage());
                e.printStackTrace();}
                break;
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
                PerfilFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    //Método para proceder al registro.
    public void registrar(){
        String generoStr = (generoS.getSelectedItemPosition()) +"";
        if(generoS.getSelectedItemPosition() == 0) generoStr = null;

        String aux= "";

        Login log = new Login(getActivity().getApplicationContext());

        params = new HashMap<>();
        params.put("id_login_app", ""+ log.getId());
        if(nomcomET.getText().toString().length() == 0) {
            params.put("nombre", "" + aux);
            perfil.setNombreCompleto(null);
        }else{
            params.put("nombre", "" + nomcomET.getText().toString());
            perfil.setNombreCompleto(nomcomET.getText().toString());
        }

        params.put("genero", generoStr);
        perfil.setGenero(Integer.parseInt(generoStr));


        params.put("actividad", generoStr);

        if (calen == true) {
            params.put("fec_nacimiento", "" + fecha);
            perfil.setFecha(fecha);
        } else{
            params.put("fec_nacimiento", "" + fechapojo);
            perfil.setFecha(fechapojo);
        }




        new RegistrarAsyncTask().execute(params);

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

    private void showLocationDialog() {
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
                        polipriCB.setChecked(true);
                        actdatB.setEnabled(true);
                    }
                });

        String negativeText = "NO acepto";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        polipriCB.setChecked(false);
                        actdatB.setEnabled(false);
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private class RegistrarAsyncTask extends AsyncTask<HashMap<String, String>, Void, String> {

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
        protected String doInBackground(HashMap<String, String>... args) {

            String url = "http://" + ClienteHttp.SERVER_IP + "//app_php/registro/actualizarPerfil.php";
            ClienteHttp cliente = new ClienteHttp();
            String result = cliente.hacerRequestHttp(url, args[0]);
            Log.d("result",result);
            return result;
        }


        /**
         * Llamada ejecutada al finalizar la petición, completa el inicio de sesión con el usuario.
         * @param result
         */
        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                Log.d("RESULT", result);
                if(result.equals("success")) {
                    Snackbar.make(getActivity().findViewById(R.id.segunda_fragment_container), "Datos actualizados con exito", Snackbar.LENGTH_LONG).show();
                }
            } else {
                OKDialog.showOKDialog(getActivity(), "Error al actualizar información de usuario", "Hubo un problema con la red. Revise su conexión a Internet");
            }
        }
    }
}
