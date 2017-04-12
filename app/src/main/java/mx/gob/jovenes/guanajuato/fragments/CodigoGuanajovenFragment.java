package mx.gob.jovenes.guanajuato.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.sesion.Sesion;

/**
 * Created by leonardolirabecerra on 28/03/17.
 */

public class CodigoGuanajovenFragment extends CustomFragment {
    private String nombre,
                   correo,
                   genero,
                   Curp,
                   estado,
                   fechaNacimiento;

    private int idGenero,
                cp;

    private Resources res;
    private String[] estados;

    //Campos
    private TextView inputNombre,
                     inputCorreo,
                     inputGenero,
                     inputFechaNacimiento,
                     inputCurp, inputCp,
                     inputEstado;

    //Imagen
    private ImageView imagenQr, imagenUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_codigo_guanajoven, container, false);

        //Obtener datos de usuario
        nombre = Sesion.getNombre() + " " + Sesion.getApellidoPaterno() + " " + Sesion.getApellidoMaterno();
        correo = Sesion.getCorreo();
        idGenero = Sesion.getIdGenero();

        if (idGenero == 1) genero = "Masculino";
        else genero = "Femenino";

        fechaNacimiento = Sesion.getFechaNacimiento();
        Curp = Sesion.getCurp();
        cp = Sesion.getCodigoPostal();
        estado = Sesion.getEstado();

        //Cargar datos de usuario
        inputNombre = (TextView) vista.findViewById(R.id.tv_nombreCG);
        inputCorreo = (TextView) vista.findViewById(R.id.tv_correoCG);
        inputGenero = (TextView) vista.findViewById(R.id.tv_generoCG);
        inputFechaNacimiento = (TextView) vista.findViewById(R.id.tv_fechaCG);
        inputCurp = (TextView) vista.findViewById(R.id.tv_curpCG);
        inputCp = (TextView) vista.findViewById(R.id.tv_cpCG);
        inputEstado = (TextView) vista.findViewById(R.id.tv_estadoCG);
        imagenQr = (ImageView) vista.findViewById(R.id.iv_codigoCG);

        inputNombre.setText(nombre);
        inputCorreo.setText(correo);
        inputGenero.setText(genero);
        inputFechaNacimiento.setText(fechaNacimiento);
        inputCurp.setText(Curp);
        inputCp.setText(String.valueOf(cp));
        inputEstado.setText(estado);

        String dato = nombre + Curp;
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
}
