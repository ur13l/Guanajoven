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
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.VerticalText;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;

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
            imgBackground,
            imgGuanajoven,
            imgGuanajuato;
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
        fechaNacimiento = getFechaCast(u.getDatosUsuario().getFechaNacimiento());
        codigoGuanajoven = String.valueOf(u.getCodigoGuanajoven().getIdCodigoGuanajoven());
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
        imgGuanajoven = (ImageView) vista.findViewById(R.id.logo_guanajoven);
        imgGuanajuato = (ImageView) vista.findViewById(R.id.logo_guanajuato);

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

        String dato = Curp;

        try {
            generarQR(dato, imagenQr);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return vista;
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
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pedirPermisos();
                } else {
                    generarPDF();
                }*/

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
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
            PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(nombreCompleto));

            documento.open();

            PdfContentByte canvas = writer.getDirectContentUnder();

            canvas.saveState();

            PdfGState state = new PdfGState();
            state.setFillOpacity(0.4f);
            canvas.setGState(state);
            canvas.addImage(fotoPerfil());
            canvas.restoreState();

            agregarMetaDatos(documento);
            documento.add(encabezado());
            documento.add(new Chunk(new LineSeparator()));
            documento.add(nombreUsuario());
            documento.add(Chunk.NEWLINE);
            documento.add(correoUsuario());
            documento.add(Chunk.NEWLINE);
            documento.add(camposConTitulo("Código Guanajoven", codigoGuanajoven));
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(codigoQR());
            documento.add(Chunk.NEWLINE);
            documento.add(camposConTitulo("CURP", Curp));
            documento.add(Chunk.NEWLINE);
            documento.add(Chunk.NEWLINE);
            documento.add(tabla());
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
        document.addTitle("Código Guanajoven - " + codigoGuanajoven);
        document.addSubject("Usa este código para identificarte");
        document.addKeywords("Código, Guanajoven, App");
        document.addAuthor("Guanajoven");
        document.addCreator("Guanajoven");
    }

    private Paragraph encabezado() {
        Paragraph p = new Paragraph();
        Phrase logoGuanajoven = new Phrase(new Phrase(new Chunk(logoGuanajoven(), 0, 0, true)));
        fuentePDF.setColor(new BaseColor(191, 51, 100));
        Phrase titulo = new Phrase("Código Guanajoven", fuentePDF);
        Phrase logoGuanajuato = new Phrase(new Chunk(logoGuanajuato(), 0, 0, true));
        String SPACES = "                                ";
        p.add(logoGuanajoven);
        p.add(new Phrase(SPACES));
        p.add(titulo);
        p.add(new Phrase(SPACES));
        p.add(logoGuanajuato);

        return p;
    }

    private Image logoGuanajoven() {
        Image logo = null;
        try {
            Drawable drawable = imgGuanajoven.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            logo = Image.getInstance(byteArrayOutputStream.toByteArray());
            logo.scaleAbsolute(65, 50);
            logo.setAlignment(Image.LEFT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo;
    }

    private Image logoGuanajuato() {
        Image logo = null;
        try {
            Drawable drawable = imgGuanajuato.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            logo = Image.getInstance(byteArrayOutputStream.toByteArray());
            logo.scaleAbsolute(65, 50);
            logo.setAlignment(Image.RIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logo;
    }

    private Paragraph nombreUsuario() {
        fuentePDF.setColor(new BaseColor(7, 70, 119));
        Paragraph nombreUsuario = new Paragraph(nombre, fuentePDF);
        nombreUsuario.setAlignment(Element.ALIGN_CENTER);

        return nombreUsuario;
    }

    private Paragraph correoUsuario() {
        fuentePDF.setColor(new BaseColor(7, 70, 119));
        String correoUsuario = Sesion.getUsuario().getEmail();
        Paragraph correo = new Paragraph(correoUsuario, fuentePDF);
        correo.setAlignment(Element.ALIGN_CENTER);

        return correo;
    }

    private Paragraph codigoGuanajoven() {
        Paragraph codigo = new Paragraph();
        Paragraph titulo = new Paragraph("Código Guanajoven");
        titulo.setAlignment(Element.ALIGN_CENTER);
        Paragraph numeroCodigo = new Paragraph(codigoGuanajoven);
        numeroCodigo.setAlignment(Element.ALIGN_CENTER);

        codigo.add(titulo);
        codigo.add(numeroCodigo);

        return codigo;
    }

    private Image fotoPerfil() {
        Image foto = null;
        try {
            Drawable drawable = imgBackground.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            foto = Image.getInstance(byteArrayOutputStream.toByteArray());
            foto.scaleAbsolute(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight() - 50);
            foto.setAbsolutePosition(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return foto;
    }

    private Paragraph saludo() {
        Paragraph saludo = new Paragraph();
        saludo.setAlignment(Element.ALIGN_CENTER);

        Paragraph parrafo1 = new Paragraph("Saludos!", new Font(Font.FontFamily.HELVETICA, 14));
        parrafo1.setAlignment(Element.ALIGN_CENTER);

        Paragraph parrafo2 = new Paragraph("Te hacemos llegar tu código guanajoven para que lo puedas usar en nuestros eventos y convocatorias, " +
                "además te ayudara a identificarte como parte de la comunidad Guanajoven!", new Font(Font.FontFamily.HELVETICA, 14));
        parrafo2.setAlignment(Element.ALIGN_CENTER);

        saludo.add(parrafo1);
        saludo.add(Chunk.NEWLINE);
        saludo.add(Chunk.NEWLINE);
        saludo.add(parrafo2);

        return saludo;
    }

    private Paragraph camposConTitulo(String titulo, String valor) {
        Paragraph paragraphCampo = new Paragraph();

        Paragraph paragraphTitulo = new Paragraph(titulo);
        paragraphTitulo.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragraphValor = new Paragraph(valor);
        paragraphValor.setAlignment(Element.ALIGN_CENTER);

        paragraphCampo.add(paragraphTitulo);
        paragraphCampo.add(paragraphValor);

        return paragraphCampo;
    }

    private PdfPTable tabla() {
        PdfPTable tabla = new PdfPTable(2);

        tabla.addCell(celda("Género"));
        tabla.addCell(celda("Fecha nacimiento"));
        tabla.addCell(celda(genero));
        tabla.addCell(celda(fechaNacimiento));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda(" "));
        tabla.addCell(celda("Municipio"));
        tabla.addCell(celda("Estado de nacimiento"));
        tabla.addCell(celda(municipio));
        tabla.addCell(celda(estado));
        return tabla;
    }

    private PdfPCell celda(String contenido) {
        Paragraph parrafo = new Paragraph(contenido);
        parrafo.setAlignment(Element.ALIGN_CENTER);
        PdfPCell celda = new PdfPCell();
        celda.addElement(parrafo);
        celda.setUseAscender(true);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorder(Rectangle.NO_BORDER);
        return celda;
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

    //Castear fecha para que muestre solo dd/MM/yyyy
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

}
