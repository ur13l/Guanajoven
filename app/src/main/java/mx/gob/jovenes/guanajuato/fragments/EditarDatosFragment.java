package mx.gob.jovenes.guanajuato.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.LoginActivity;
import mx.gob.jovenes.guanajuato.api.RegistroRequest;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.EditTextValidations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Autor: Uriel Infante
 * Fragment de datos complementarios del usuario, esta interfaz solicita información obligatoria
 * al usuario, y es llamada cuando se va a crear un nuevo usuario, sin importar su forma de logueo.
 * Fecha: 02/05/2016
 */
public class EditarDatosFragment extends CustomFragment implements View.OnClickListener{
    private static final String EMAIL = "email";
    private static final String NOMBRE = "nombre";
    private static final String AP_PATERNO = "ap_paterno";
    private static final String RUTA_IMAGEN = "ruta_imagen";

    private Usuario usuario; //instancia que se usará para cargar datos que vengan de la interfaz.
    private Button btnContinuar;
    private ImageView imgPerfil;
    private MaterialSpinner spnOcupacion;
    private MaterialSpinner spnEstadoCivil;
    private MaterialSpinner spnNivelAcademico;
    private EditText etPasatiempo;
    private ProgressDialog progressDialog;
    private ImageButton btnBack;

    private UsuarioAPI usuarioAPI;

    private String[] ocupaciones = {"Abogado", "Médico", "Estudiante", "Ingeniero"};
    private String[] estadosCiviles = {"Soltero/a", "Comprometido/a", "Casado/a", "Divorciado/a", "Casado/a"};
    private String[] niveles = {"Sin estudios", "Preescolar", "Primaria", "Secundaria", "Bachillerato", "Licenciatura", "Maestría", "Doctorado"};


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Retrofit retrofit = ((MyApplication)getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);

        Bundle args = getArguments();
        usuario = new Usuario();
        usuario.setCorreo(args.getString(EMAIL));
        usuario.setNombre(args.getString(NOMBRE));
        usuario.setApellidoPaterno(args.getString(AP_PATERNO));
        usuario.setRutaImagen(args.getString(RUTA_IMAGEN));
    }

    /**
     * Método del ciclo de vida que se ejecuta después de que la vista ha sido generada
     * @param inflater
     * @param parent
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_editar_datos, parent, false);

        btnContinuar = (Button) v.findViewById(R.id.btn_continuar);
        etPasatiempo = (EditText) v.findViewById(R.id.et_pasatiempo_favorito);
        spnOcupacion = (MaterialSpinner) v.findViewById(R.id.spn_ocupacion);
        spnEstadoCivil = (MaterialSpinner) v.findViewById(R.id.spn_estado_civil);
        spnNivelAcademico = (MaterialSpinner) v.findViewById(R.id.spn_nivel_academico);
        imgPerfil = (ImageView) v.findViewById(R.id.img_profile);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ocupaciones);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, estadosCiviles);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, niveles);


        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnOcupacion.setAdapter(adapter1);
        spnEstadoCivil.setAdapter(adapter2);
        spnNivelAcademico.setAdapter(adapter3);


        btnContinuar.setOnClickListener(this);

        //Picasso.with(getActivity()).load(usuario.getRutaImagen()).into(imgPerfil);
        return v;
    }



    /**
     * Funcionalidad de la interfaz aplicada al Fragment para el clic
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continuar:
                continuar();
                break;
        }
    }

    /**
     * onStop: Ciclo de vida de fragment, se ejecuta cuando se detiene el fragment.
     */
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
            progressDialog = ProgressDialog.show(getActivity(), "Registrando", "Espere un momento mientras se completa el registro", true);


        /*
            Call<Response<Usuario>> callRegistrar = usuarioAPI.registrar(
                    new RegistroRequest(
                            usuario.getEmail(),
                            "_",
                            "_",
                            etApPaterno.getText().toString(),
                            etApMaterno.getText().toString(),
                            etNombre.getText().toString(),
                            spnGenero.getSelectedItemPosition() == 1? "H" : "M",
                            etFechaNacimiento.getText().toString(),
                            etCodigoPostal.getText().toString(),
                            estadosValueArray[spnEstado.getSelectedItemPosition() - 1],
                            "data:image/jpeg;base64," + getBase64(imgPerfil),
                            usuario.getIdGoogle(),
                            usuario.getIdFacebook()
                    )
            );


            callRegistrar.enqueue(new Callback<Response<Usuario>>() {
                @Override
                public void onResponse(Call<Response<Usuario>> call, retrofit2.Response<Response<Usuario>> response) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    Response<Usuario> body = response.body();
                    if (body.success) {
                        Sesion.cargarSesion(body.data);
                        ((LoginActivity) getActivity()).startHomeActivity();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(body.errors[0])
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();                    }
                }

                @Override
                public void onFailure(Call<Response<Usuario>> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Error al conectar con el servidor, intente más adelante")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });


        }
        */
    }





}
