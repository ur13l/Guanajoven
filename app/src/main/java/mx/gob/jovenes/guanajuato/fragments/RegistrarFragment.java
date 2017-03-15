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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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

import static android.app.Activity.RESULT_OK;


/**
 * Autor: Chema Cruz Parada, Uriel Infante
 * Fragment de la interfaz inicial de registro, solicita el correo, el nombre de usuario y la contraseña.
 * Fecha: 04/05/2016
 */
public class RegistrarFragment extends Fragment implements  View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {
    private static final int REQUEST_CAMERA = 101;
    private static final int SELECT_FROM_GALLERY = 102;
    private Button continuarBtn;
    private EditText etEmail;
    private EditText etPassword1;
    private EditText etPassword2;
    private EditText etNombre;
    private EditText etApPaterno;
    private EditText etApMaterno;
    private EditText etFechaNacimiento;
    private CircleImageView imgPerfil;
    private MaterialSpinner spnGenero;
    private MaterialSpinner spnEstado;
    private EditText etCodigoPostal;
    private ProgressDialog progressDialog;
    private ImageButton btnBack;
    private Uri picUri;


    private String[] estadosValueArray;
    private String fecha;
    private Calendar calendar;

    private UsuarioAPI usuarioAPI;

    private String[] generos = {"Masculino", "Femenino"};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
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
        etApPaterno = (EditText) v.findViewById(R.id.et_ap_paterno);
        etApMaterno = (EditText) v.findViewById(R.id.et_ap_materno);
        etFechaNacimiento = (EditText) v.findViewById(R.id.et_fecha_nacimiento);
        etCodigoPostal = (EditText) v.findViewById(R.id.et_codigo_postal);
        spnGenero = (MaterialSpinner) v.findViewById(R.id.spn_genero);
        spnEstado = (MaterialSpinner) v.findViewById(R.id.spn_estado);
        imgPerfil = (CircleImageView) v.findViewById(R.id.img_profile);
        btnBack = (ImageButton) v.findViewById(R.id.btn_back);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, generos);

        String[] estadosArray = getActivity().getResources().getStringArray(R.array.estados);
        estadosValueArray = getActivity().getResources().getStringArray(R.array.estados_values);

        ArrayAdapter<String> estadosAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, estadosArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenero.setAdapter(adapter);
        spnEstado.setAdapter(estadosAdapter);

        //Configurando el input type de contraseña en los dos campos.
        etPassword1.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword1.setTypeface(Typeface.DEFAULT);

        etPassword2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword2.setTypeface(Typeface.DEFAULT);

        etFechaNacimiento.setKeyListener(null);
        etFechaNacimiento.setOnFocusChangeListener(this);
        etFechaNacimiento.setOnClickListener(this);

        imgPerfil.setOnClickListener(this);

        EditTextValidations.removeErrorTyping(etEmail);
        EditTextValidations.removeErrorTyping(etNombre);
        EditTextValidations.removeErrorTyping(etApPaterno);
        EditTextValidations.removeErrorTyping(etCodigoPostal);
        EditTextValidations.removeErrorTyping(etPassword1);
        EditTextValidations.removeErrorTyping(etPassword2);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        continuarBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continuar:
                continuar();
                break;
            case R.id.et_fecha_nacimiento:
                showCalendar();
                break;
            case R.id.img_profile:
                selectImage();
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
        boolean apPaternoEmpty = EditTextValidations.esCampoVacio(etApPaterno);
        boolean cpEmpty = EditTextValidations.esCampoVacio(etCodigoPostal);
        boolean emailV = false;
        boolean pass1V = false;
        boolean pass2V = false;
        boolean passEq = false;

        //Si ninguno de los campos es vacío
        if(!emailEmpty && !pass1Empty && !pass2Empty && !nombreEmpty && !generoEmpty && !apPaternoEmpty && !cpEmpty){
            emailV = EditTextValidations.esEmailValido(etEmail);
            pass1V = EditTextValidations.esContrasenaValida(etPassword1);
            pass2V = EditTextValidations.esContrasenaValida(etPassword2);
            passEq = EditTextValidations.contrasenasCoinciden(etPassword1, etPassword2);
        }

        //Si todas las validaciones se cumplen, se genera el nuevo fragment.
        if(emailV && pass1V && pass2V && passEq) {

            Call<Response<String>> callCurp = usuarioAPI.verificarCurp(
                    etNombre.getText().toString(),
                    etApPaterno.getText().toString(),
                    etApMaterno.getText().toString(),
                    etFechaNacimiento.getText().toString(),
                    estadosValueArray[spnEstado.getSelectedItemPosition()],
                    spnGenero.getSelectedItemPosition() == 0? "H" : "M"
            );



            progressDialog = ProgressDialog.show(getActivity(), "Registrando", "Espere un momento mientras se completa el registro", true);

            callCurp.enqueue(new Callback<Response<String>>() {

                //TODO: Adecuar entrada para los cambios en el servicio.
                @Override
                public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                    if(response.body().success){
                        Call<Response<Usuario>> callRegistrar = usuarioAPI.registrar(
                                etEmail.getText().toString(),
                                etPassword1.getText().toString(),
                                etPassword2.getText().toString(),
                                etNombre.getText().toString(),
                                spnGenero.getSelectedItemPosition() + "",//.getText().toString(),
                                null,
                                null,
                                null,
                                null,
                                null,
                                etCodigoPostal.getText().toString(),
                                null,
                                null,
                                null,
                                null
                        );

                        callRegistrar.enqueue(new Callback<Response<Usuario>>() {
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
                                progressDialog.dismiss();
                                Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Hubo un error al registrar su solicitud, intente más tarde.", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Snackbar.make(getActivity().findViewById(R.id.login_fragment_container), "Hubo un error al solicitar tu CURP, revisar que los datos sean correctos.", Snackbar.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<Response<String>> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            // desplegar el calendario para introducir fecha
            case R.id.et_fecha_nacimiento:
                if(b) {
                    showCalendar();
                }
                break;
        }
    }

    public void showCalendar(){

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                RegistrarFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
        etFechaNacimiento.setText(date);
    }


    /**
     * Método utilizado para seleccionar una imagen al dar click en img_profile
     */
    private void selectImage() {
        final CharSequence[] items = { "Tomar una foto", "Escoger de tu galería",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Añadir imagen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Tomar una foto")) {
                    checkCameraPermission();
                } else if (items[item].equals("Escoger de tu galería")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Selecciona una imagen"),
                            SELECT_FROM_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
            else{
                startCamera();
            }
        }
        else{
            startCamera();
        }
    }

    /** Create a File for saving an image */
    private  File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Guanajoven");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startCamera();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(getView(), "Permiso denegado, no se puede acceder a la cámara", Snackbar.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) if (requestCode == REQUEST_CAMERA) {
            // Display image received on the view
            Bundle b = data.getExtras(); // Kept as a Bundle to check for other things in my actual code
            Bitmap pic = (Bitmap) b.get("data");

            if (pic != null) { // Display your image in an ImageView in your layout (if you want to test it)
                imgPerfil.setImageBitmap(pic);
                imgPerfil.invalidate();
            }
        } else if (requestCode == SELECT_FROM_GALLERY) {
            Uri selectedImageUri = data.getData();

            String tempPath = getPath(selectedImageUri, getActivity());
            Bitmap bm;
            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
            bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
            imgPerfil.setImageBitmap(bm);
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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