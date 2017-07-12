package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Map;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.DatosUsuarioIdioma;
import mx.gob.jovenes.guanajuato.model.IdiomaAdicional;
import mx.gob.jovenes.guanajuato.sesion.Sesion;

/**
 * Created by codigus on 29/06/2017.
 */

public class IdiomasAdicionalesPorcentajesFragment extends DialogFragment {
    private TextView tvNombreIdioma;

    private SeekBar seekBarConversacion;
    private SeekBar seekBarLectura;
    private SeekBar seekBarEscritura;

    private TextView tvPorcentajeConversacion;
    private TextView tvPorcentajeLectura;
    private TextView tvPorcentajeEscritura;

    private Button btnAceptar;
    private Button btnCancelar;

    private IdiomaAdicional idiomaAdicional;

    private DatosUsuarioIdioma datos;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idiomas_adicionales_porcentajes, container, false);

        tvNombreIdioma = (TextView) v.findViewById(R.id.tv_nombre_idioma);

        tvNombreIdioma.setText(idiomaAdicional.getNombre());

        seekBarConversacion = (SeekBar) v.findViewById(R.id.seek_bar_conversacion);
        seekBarLectura = (SeekBar) v.findViewById(R.id.seek_bar_lectura);
        seekBarEscritura = (SeekBar) v.findViewById(R.id.seek_bar_escritura);

        tvPorcentajeConversacion = (TextView) v.findViewById(R.id.tv_porcentaje_conversacion);
        tvPorcentajeLectura = (TextView) v.findViewById(R.id.tv_porcentaje_lectura);
        tvPorcentajeEscritura = (TextView) v.findViewById(R.id.tv_porcentaje_escritura);

        btnAceptar = (Button) v.findViewById(R.id.btn_aceptar_idiomas_porcentajes);
        btnCancelar = (Button) v.findViewById(R.id.btn_cancelar_idiomas_porcentajes);

        seekBarConversacion.setOnSeekBarChangeListener(new ActionSeekBar());
        seekBarLectura.setOnSeekBarChangeListener(new ActionSeekBar());
        seekBarEscritura.setOnSeekBarChangeListener(new ActionSeekBar());

        btnAceptar.setOnClickListener((View) -> {
            int idDatosUsuario = Sesion.getUsuario().getDatosUsuario().getIdDatosUsuario();
            int idIdioma = idiomaAdicional.getIdIdiomaAdicional();
            int porcentajeConversacion = seekBarConversacion.getProgress();
            int porcentajeLectura = seekBarLectura.getProgress();
            int porcentajeEscritura = seekBarEscritura.getProgress();
            datos = new DatosUsuarioIdioma(idDatosUsuario, idIdioma, porcentajeConversacion, porcentajeLectura, porcentajeEscritura);
            //System.out.println("------------------------------");
            //System.out.println(datos.getIdDatosUsuario() + "-" + datos.getIdIdiomaAdicional() + "-" + datos.getConversacion() + "-" + datos.getLectura() + "-" + datos.getEscritura());
            IdiomasAdicionalesDialogFragment.insertarIdiomas(datos);
            EditarDatosFragment.validarIdiomas();

            this.dismiss();
        });

        btnCancelar.setOnClickListener((View) -> {
            this.dismiss();
        });

        return v;
    }

    //Seteamos cada idioma que nos envia desde el dialog anterior
    public void setIdiomaAdicional(IdiomaAdicional idiomaAdicional) {
        this.idiomaAdicional = idiomaAdicional;
    }

    public DatosUsuarioIdioma getDatos() {
        return datos;
    }

    public void setDatos(DatosUsuarioIdioma datos) {
        this.datos = datos;
    }

    //Clase privada para poner porcentaje visual con los seek bar
    private class ActionSeekBar implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int id = seekBar.getId();
            if (id == seekBarConversacion.getId()) {
                tvPorcentajeConversacion.setText(String.valueOf(progress) + "%");
            } else if (id == seekBarLectura.getId()) {
                tvPorcentajeLectura.setText(String.valueOf(progress) + "%");
            } else if (id == seekBarEscritura.getId()) {
                tvPorcentajeEscritura.setText(String.valueOf(progress) + "%");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}
