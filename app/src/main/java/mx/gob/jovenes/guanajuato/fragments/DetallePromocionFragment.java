package mx.gob.jovenes.guanajuato.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.IdGuanajovenAPI;
import mx.gob.jovenes.guanajuato.api.PromocionesAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.api.UsuarioAPI;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Empresa;
import mx.gob.jovenes.guanajuato.model.Promocion;
import mx.gob.jovenes.guanajuato.model.Usuario;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by codigus on 18/07/2017.
 */

public class DetallePromocionFragment extends Fragment {
    private ImageView imageViewLogoEmpresa;
    private ImageView imageViewCodigoQR;
    private TextView textViewTituloPromocion;
    private TextView textViewCodigoPromocion;
    private TextView textViewDescripcionPromocion;
    private TextView textViewBasesPromocion;
    private TextView textViewFechaInicio;
    private TextView textViewFechaFin;
    private TextView textViewSitioPromocion;
    private Button buttonAplicarPromocion;
    private LinearLayout linearLayoutCodigoPromocion;

    private Bitmap imagenBitMap;

    private Promocion promocion;
    private Empresa empresa;
    private UsuarioAPI usuarioAPI;
    private PromocionesAPI promocionAPI;
    private Retrofit retrofit;

    private static final String ERROR_ID_GUANAJOVEN = "No existe id guanajoven";
    private static final String ERROR_PROMOCION = "No existe promocion";
    private static final String ERROR_EMPRESA = "No existe la empresa";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);
        promocionAPI = retrofit.create(PromocionesAPI.class);


        Call<Response<String>> call = usuarioAPI.actualizarTokenGuanajoven(Sesion.getUsuario().getApiToken());
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, retrofit2.Response<Response<String>> response) {
                if (response.body().success) {
                    String tokenGuanajoven = response.body().data;
                    Sesion.getUsuario().getCodigoGuanajoven().setToken(tokenGuanajoven);
                    try {
                        generarQR(tokenGuanajoven, imageViewCodigoQR);
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
        View view = inflater.inflate(R.layout.fragment_detalle_promocion, container, false);
        imageViewLogoEmpresa = (ImageView) view.findViewById(R.id.imageview_logo_empresa);
        imageViewCodigoQR = (ImageView) view.findViewById(R.id.imageview_codigo_qr_promocion);
        textViewTituloPromocion = (TextView) view.findViewById(R.id.textview_titulo_promocion);
        textViewCodigoPromocion = (TextView) view.findViewById(R.id.textview_codigo_promocion);
        textViewDescripcionPromocion = (TextView) view.findViewById(R.id.textview_descripcion_promocion);
        textViewBasesPromocion = (TextView) view.findViewById(R.id.textview_bases_promocion);
        textViewFechaInicio = (TextView) view.findViewById(R.id.textview_fechainicio_promocion);
        textViewFechaFin = (TextView) view.findViewById(R.id.textview_fechafin_promocion);
        textViewSitioPromocion = (TextView) view.findViewById(R.id.textview_visitar_sitio_promocion);
        linearLayoutCodigoPromocion = (LinearLayout) view.findViewById(R.id.linearlayout_codigo_promocion);
        buttonAplicarPromocion = (Button) view.findViewById(R.id.button_aplicar_promocion);

        Picasso.with(getContext()).load(empresa.getLogo()).into(imageViewLogoEmpresa);
        textViewTituloPromocion.setText(promocion.getTitulo());
        textViewCodigoPromocion.setText(promocion.getCodigoPromocion());
        textViewDescripcionPromocion.setText(promocion.getDescripcion());
        textViewBasesPromocion.setText(promocion.getBases());
        textViewFechaInicio.setText(getFechaCast(promocion.getFechaInicio()));
        textViewFechaFin.setText(getFechaCast(promocion.getFechaFin()));

        textViewSitioPromocion.setOnClickListener((View) -> { enlace(promocion.getUrlPromocion()); });
        buttonAplicarPromocion.setOnClickListener((View) -> { meInteresa(); });

        return view;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
    }

    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generarQR(String dato, ImageView objeto) throws WriterException {
        Writer generador = new QRCodeWriter();
        String datoFinal = Uri.encode(dato, "utf-8");

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

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

    private void meInteresa() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());

        alerta.setTitle("Advertencia");
        alerta.setMessage("Al aplicar esta promoción quedará registrado en la base de datos que has sido beneficiado por la empresa, ¿Estas seguro de Aplicar esta promoción?");

        alerta.setPositiveButton("Aceptar", (dialog, which) -> {
            Call<Response<Boolean>> call = promocionAPI.registrar(Sesion.getUsuario().getCodigoGuanajoven().getToken(), promocion.getIdPromocion());

            call.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    if (response.body().errors.length == 0) {
                        linearLayoutCodigoPromocion.setVisibility(android.view.View.VISIBLE);
                        imageViewCodigoQR.setVisibility(android.view.View.VISIBLE);

                        Snackbar.make(getView(), "Promoción registrada", Snackbar.LENGTH_LONG).show();
                    } else if (response.body().errors[0].equals(ERROR_ID_GUANAJOVEN)) {
                        Snackbar.make(getView(), ERROR_ID_GUANAJOVEN, Snackbar.LENGTH_LONG).show();
                    } else if (response.body().errors[0].equals(ERROR_PROMOCION)) {
                        Snackbar.make(getView(), ERROR_PROMOCION, Snackbar.LENGTH_LONG).show();
                    } else if (response.body().errors[0].equals(ERROR_EMPRESA)) {
                        Snackbar.make(getView(), ERROR_EMPRESA, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), "Error al conectar", Snackbar.LENGTH_LONG).show();
                }
            });

        });

        alerta.setNegativeButton("Cancelar", ((dialog, which) -> { dialog.dismiss(); }));

        alerta.show();

    }

}
