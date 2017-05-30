package mx.gob.jovenes.guanajuato.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by leonardolirabecerra on 28/03/17.
 */

public class CodigoGuanajovenFragment extends CustomFragment {
    private String nombre,
                   correo,
                   genero,
                   Curp,
                   estado,
                   rutaImagen,
                   municipio,
                   fechaNacimiento;

    private int idGenero;

    private Resources res;
    private String[] estados;

    //Campos
    private TextView inputNombre,
                     inputCorreo,
                     inputGenero,
                     inputFechaNacimiento,
                     inputCurp,
                     inputMunicipio,
                     inputEstado,
                     inputCodigoGuanajoven;

    //Imagen
    private ImageView imagenQr,
                      imgBackground;
    private CircleImageView imagenUsuario;

    private UsuarioAPI usuarioAPI;
    private Retrofit retrofit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sesion.sessionStart(getActivity());
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);


        /**
         * Llamada para actualizar el nuevo token guanajoven con nueva validez.
         */
        Call<Response<String>> call = usuarioAPI.actualizarTokenGuanajoven(Sesion.getUsuario().getApiToken());
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                if(response.body().success) {
                    String tokenGuanajoven = response.body().data;
                    try {
                        generarQR(tokenGuanajoven, imagenQr);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                else {

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

        fechaNacimiento = u.getDatosUsuario().getFechaNacimiento();
        Curp = u.getDatosUsuario().getCurp();
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
        inputCodigoGuanajoven.setText(u.getCodigoGuanajoven().getIdCodigoGuanajoven() + "");
        inputFechaNacimiento.setText(fechaNacimiento);
        inputCurp.setText(Curp);
        inputMunicipio.setText(municipio);
        inputEstado.setText(estado);
        Picasso.with(getActivity()).load(rutaImagen).into(imagenUsuario);
        Picasso.with(getActivity()).load(rutaImagen).into(imgBackground);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(nombre);

        String dato = u.getCodigoGuanajoven().getToken();
        try {
            generarQR(dato, imagenQr);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return vista;
    }

    //Generar código QR
    private void generarQR(String dato, ImageView objeto) throws WriterException {
        Writer generador = new QRCodeWriter();
        String datoFinal = Uri.encode(dato, "utf-8");

        BitMatrix bm = generador.encode(datoFinal, BarcodeFormat.QR_CODE, 150, 150);
        Bitmap imagenBitMap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 150; i++) { //width
            for (int j = 0; j  < 150; j++) { //height
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Código Guanajoven");
    }
}
