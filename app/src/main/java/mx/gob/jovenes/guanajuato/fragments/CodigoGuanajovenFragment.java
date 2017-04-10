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
import android.widget.EditText;
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
                   curp,
                   cp,
                   estado;

    Date fecha_nacimiento = null;

    private int id_genero,
                id_estado;

    private Resources res;
    private String[] estados;

    //Campos
    private EditText input_nombre,
                     input_correo,
                     input_genero,
                     input_fecha_nacimiento,
                     input_curp, input_cp,
                     input_estado;

    //Imagen
    private ImageView imagen_qr;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdg = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_codigo_guanajoven, container, false);

        //Obtener datos de usuario
        nombre = Sesion.getNombre() + " " + Sesion.getApellidoPaterno() + " " + Sesion.getApellidoMaterno();
        correo = Sesion.getEmail();
        id_genero = Sesion.getIdGenero();

        if (id_genero == 1) genero = "Masculino";
        else genero = "Femenino";

        try {
            fecha_nacimiento = sdg.parse(Sesion.getFechaNacimiento());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        curp = Sesion.getCurp();
        cp = Sesion.getCurp();
        id_estado = Sesion.getIdEstado();

        res = getResources();
        estados = res.getStringArray(R.array.estados);
        estado = estados[id_estado];

        //Cargar datos de usuario
        input_nombre = (EditText) vista.findViewById(R.id.nombre_usuario);
        input_correo = (EditText) vista.findViewById(R.id.email);
        input_genero = (EditText) vista.findViewById(R.id.genero);
        input_fecha_nacimiento = (EditText) vista.findViewById(R.id.fecha);
        input_curp = (EditText) vista.findViewById(R.id.curp);
        input_cp = (EditText) vista.findViewById(R.id.cp);
        input_estado = (EditText) vista.findViewById(R.id.estado);
        imagen_qr = (ImageView) vista.findViewById(R.id.codigo_guanajoven_qr);

        input_nombre.setText(nombre);
        input_correo.setText(correo);
        input_genero.setText(genero);
        input_fecha_nacimiento.setText(sdf.format(fecha_nacimiento));
        input_curp.setText(curp);
        input_cp.setText(cp);
        input_estado.setText(estado);

        String dato = nombre + curp;
        try {
            generarQR(dato, imagen_qr);
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
