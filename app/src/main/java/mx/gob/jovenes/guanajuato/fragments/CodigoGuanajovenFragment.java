package mx.gob.jovenes.guanajuato.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

        input_nombre.setText(nombre);
        input_correo.setText(correo);
        input_genero.setText(genero);
        input_fecha_nacimiento.setText(sdf.format(fecha_nacimiento));
        input_curp.setText(curp);
        input_cp.setText(cp);
        input_estado.setText(estado);

        //TODO: Generar código QR

        return vista;
    }
}
