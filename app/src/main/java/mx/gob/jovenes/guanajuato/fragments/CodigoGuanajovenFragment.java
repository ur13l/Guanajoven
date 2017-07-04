package mx.gob.jovenes.guanajuato.fragments;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.panorama.PanoramaApi;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

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
            codigoGuanajoven,
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
    private Bitmap imagenBitMap;

    private static final int PERMISSION_REQUEST_CODE = 123;
    private Font fuentePDF;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sesion.sessionStart(getActivity());
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        usuarioAPI = retrofit.create(UsuarioAPI.class);

        fuentePDF = new Font(Font.FontFamily.HELVETICA, 22);

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
        fechaNacimiento = u.getDatosUsuario().getFechaNacimiento();
        codigoGuanajoven = String.valueOf(u.getCodigoGuanajoven().getIdCodigoGuanajoven());
        Curp = u.getDatosUsuario().getCurp();
        municipio = u.getDatosUsuario().getMunicipio().getNombre();
        estado = u.getDatosUsuario().getEstadoNacimiento().getNombre();
        rutaImagen = u.getDatosUsuario().getRutaImagen();

        System.err.println("------------------------------------------");
        System.err.println(codigoGuanajoven);
        System.err.println("-----------------------------------------");

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
        inputCurp.setText(Curp);
        inputMunicipio.setText(municipio);
        inputEstado.setText(estado);
        Picasso.with(getActivity()).load(rutaImagen).into(imagenUsuario);
        Picasso.with(getActivity()).load(rutaImagen).into(imgBackground);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(nombre);

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Código Guanajoven");
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
                pedirPermisos();
                break;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permitir = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults) {
                    permitir = permitir && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                permitir = false;
                break;
        }

        if (permitir) {
            generarPDF();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(getView(), "Permiso denegado", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    //Validar si la app tiene el permiso de escribir
    private boolean tienePermisos() {
        int res;
        String[] permisos = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        for (String permiso : permisos) {
            res = getActivity().checkCallingOrSelfPermission(permiso);
            if (!(res == PackageManager.PERMISSION_DENIED)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void pedirPermisos() {
        String[] permisos = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permisos, PERMISSION_REQUEST_CODE);
        }
    }

    public void generarPDF() {
        //Se crea el documento
        Document documento = new Document(PageSize.LETTER);

        //nombres de carpeta, subcarpeta y archivo
        String nombreCapeta = "Guanajoven";
        String generados = "MisArchivos";
        String nombreArchivo = "CodigoGuanajoven.pdf";

        //nombre de la raíz de la SD
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();

        //directorio sd/Guanajoven
        File pdfDir = new File(tarjetaSD + File.separator + nombreCapeta);

        //Si no existe lo crea
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        File pdfSubDir = new File(pdfDir.getPath() + File.separator + generados);

        if (!pdfSubDir.exists()) {
            pdfSubDir.mkdir();
        }

        String nombreCompleto = Environment.getExternalStorageDirectory() + File.separator + nombreCapeta + File.separator + generados + File.separator + nombreArchivo;

        File outputFile = new File(nombreCompleto);

        if (outputFile.exists()) {
            outputFile.delete();
        }

        try {
            PdfWriter.getInstance(documento, new FileOutputStream(nombreCompleto));
            documento.open();

            agregarMetaDatos(documento);
            documento.add(logo());
            documento.add(new Paragraph(""));
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(saludo());
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(codigoQR());
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(footer());

            try {
                documento.close();
                Snackbar.make(getView(), "PDF generado", Snackbar.LENGTH_LONG).show();
            } catch (Exception e) {
                Snackbar.make(getView(), "Error en generar PDF", Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }

        } catch (Exception e) {
            Snackbar.make(getView(), "Error en generar PDF", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void agregarMetaDatos(Document document) {
        document.addTitle("Código Guanajoven - " + Sesion.getUsuario().getCodigoGuanajoven());
        document.addSubject("Usa este código para identificarte");
        document.addKeywords("Código, Guanajoven, App");
        document.addAuthor("Guanajoven");
        document.addCreator("Guanajoven");
    }

    private Paragraph titulo() {
        Paragraph titulo = new Paragraph("Código Guanajoven", new Font(Font.FontFamily.HELVETICA, 22));
        titulo.setAlignment(Element.ALIGN_CENTER);
        return titulo;
    }

    private Image logo() {
        Image logo = null;
        try {
            Drawable drawable = getResources().getDrawable(R.drawable.logo);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            logo = Image.getInstance(byteArrayOutputStream.toByteArray());
            logo.scaleAbsolute(200, 170);
            logo.setAlignment(Image.MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo;
    }

    private Paragraph saludo() {
        Paragraph saludo = new Paragraph();
        saludo.setAlignment(Element.ALIGN_CENTER);

        Paragraph parrafo1 = new Paragraph("Saludos " + Sesion.getUsuario().getDatosUsuario().getNombre() + "!", fuentePDF);
        parrafo1.setAlignment(Element.ALIGN_CENTER);

        Paragraph parrafo2 = new Paragraph("Te hacemos llegar tu código guanajoven con clave: " +
                Sesion.getUsuario().getCodigoGuanajoven().getIdCodigoGuanajoven() +
                " para que lo puedas usar en nuestros eventos y convocatorias, " +
                "además te ayudara a identificarte como parte de la red Guanajoven!", new Font(Font.FontFamily.HELVETICA, 14));
        parrafo2.setAlignment(Element.ALIGN_CENTER);

        saludo.add(parrafo1);
        saludo.add(Chunk.NEWLINE);
        saludo.add(Chunk.NEWLINE);
        saludo.add(parrafo2);

        return saludo;
    }

    private Image codigoQR() {
        Image codigoQR = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imagenBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            codigoQR = Image.getInstance(byteArrayOutputStream.toByteArray());
            codigoQR.setAlignment(Image.MIDDLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codigoQR;
    }

    private Paragraph footer() {
        Paragraph footer = new Paragraph("Guanajoven® 2017", new Font(Font.FontFamily.HELVETICA, 10));
        footer.setAlignment(Element.ALIGN_CENTER);

        return footer;
    }

}
