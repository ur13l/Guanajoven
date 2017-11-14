package mx.gob.jovenes.guanajuato.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.IdGuanajovenAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class IDGuanajovenFragment extends CustomFragment {
    private String nombre;
    private String correo;
    private String genero;
    private String codigoGuanajoven;
    private String curp;
    private String estado;
    private String rutaImagen;
    private String municipio;
    private String fechaNacimiento;

    private TextView inputNombre;
    private TextView inputCorreo;
    private TextView inputGenero;
    private TextView inputFechaNacimiento;
    private TextView inputCurp;
    private TextView inputMunicipio;
    private TextView inputEstado;
    private TextView inputCodigoGuanajoven;

    private ImageView imagenQr;
    private ImageView imgBackground;

    private CircleImageView imagenUsuario;

    private UsuarioAPI usuarioAPI;
    private Retrofit retrofit;
    private Bitmap imagenBitMap;

    private IdGuanajovenAPI idGuanajovenAPI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sesion.sessionStart(getActivity());
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();

        idGuanajovenAPI = retrofit.create(IdGuanajovenAPI.class);
        usuarioAPI = retrofit.create(UsuarioAPI.class);

        setHasOptionsMenu(true);


        /**
         * Llamada para actualizar el nuevo token guanajoven con nueva validez.
         */
        Call<Response<String>> call = usuarioAPI.actualizarTokenGuanajoven(Sesion.getUsuario().getApiToken());
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                if (response.body().success) {
                    String tokenGuanajoven = response.body().data;
                    try {
                        generarQR(tokenGuanajoven, imagenQr);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_codigo_guanajoven, container, false);
        Usuario u = Sesion.getUsuario();

        //Obtener datos de usuario
        nombre = u.getDatosUsuario().getNombre() + " " + u.getDatosUsuario().getApellidoPaterno() +
                " " + u.getDatosUsuario().getApellidoMaterno();
        correo = u.getEmail();
        genero = u.getDatosUsuario().getGenero().getNombre();
        fechaNacimiento = DateUtilities.getFechaCast(u.getDatosUsuario().getFechaNacimiento());
        codigoGuanajoven = String.valueOf(u.getCodigoGuanajoven().getIdCodigoGuanajoven());
        curp = u.getDatosUsuario().getCurp();
        municipio = u.getDatosUsuario().getMunicipio().getNombre();
        estado = u.getDatosUsuario().getEstadoNacimiento().getNombre();
        rutaImagen = u.getDatosUsuario().getRutaImagen();

        //Cargar datos de usuario
        inputNombre = (TextView) vista.findViewById(R.id.tv_nombreCG);
        inputCorreo = (TextView) vista.findViewById(R.id.tv_correoCG);
        inputCodigoGuanajoven = (TextView) vista.findViewById(R.id.tv_codigoCG);
        inputGenero = (TextView) vista.findViewById(R.id.tv_generoCG);
        inputFechaNacimiento = (TextView) vista.findViewById(R.id.tv_fechaCG);
        inputCurp = (TextView) vista.findViewById(R.id.tv_curpCG);
        inputMunicipio = (TextView) vista.findViewById(R.id.tv_municipioCG);
        inputEstado = (TextView) vista.findViewById(R.id.tv_estadoCG);
        imagenQr = (ImageView) vista.findViewById(R.id.iv_codigoCG);
        imagenUsuario = (CircleImageView) vista.findViewById(R.id.iv_imagenCG);
        imgBackground = (ImageView) vista.findViewById(R.id.img_background);

        inputNombre.setText(nombre);
        inputCorreo.setText(correo);
        inputGenero.setText(genero);
        inputCodigoGuanajoven.setText(codigoGuanajoven);
        inputFechaNacimiento.setText(fechaNacimiento);
        inputCurp.setText(curp);
        inputMunicipio.setText(municipio);
        inputEstado.setText(estado);
        Picasso.with(getActivity()).load(rutaImagen).into(imagenUsuario);
        Picasso.with(getActivity()).load(rutaImagen).into(imgBackground);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(nombre);

        try {
            generarQR(curp, imagenQr);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return vista;
    }

    private void generarQR(String dato, ImageView objeto) throws WriterException {
        Writer generador = new QRCodeWriter();
        String datoFinal = Uri.encode(dato, getString(R.string.fragment_idguanajoven_utf8));

        BitMatrix bm = generador.encode(datoFinal, BarcodeFormat.QR_CODE, 150, 150);
        imagenBitMap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 150; i++) { //width
            for (int j = 0; j < 150; j++) { //height
                imagenBitMap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }

        if (imagenBitMap != null) {
            objeto.setImageBitmap(imagenBitMap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.fragment_idguanajoven_actionbar_title));
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_codigo_guanajoven, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_generar_pdf:
                ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.fragment_idguanajoven_enviando_correo),
                        getString(R.string.fragment_idguanajoven_porfavor_espere), true, false);

                Call<Response<Boolean>> call = idGuanajovenAPI.enviarCorreoIDGuanajoven(Sesion.getUsuario().getApiToken());

                call.enqueue(new Callback<Response<Boolean>>() {
                    @Override
                    public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                        progressDialog.dismiss();
                        Snackbar.make(getView(), getString(R.string.fragment_idguanajoven_correo_enviado), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                        progressDialog.dismiss();
                        Snackbar.make(getView(), getString(R.string.fragment_idguanajoven_error_al_enviar), Snackbar.LENGTH_LONG).show();
                    }
                });

                break;
        }

        return false;
    }

}
