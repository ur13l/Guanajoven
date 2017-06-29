package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by codigus on 29/06/2017.
 */

public class IdiomasAdicionalesPorcentajesFragment extends DialogFragment {
    SeekBar seekBarEscritura;
    TextView tvPorcentajeEscritura;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idiomas_adicionales_porcentajes, container, false);

        seekBarEscritura = (SeekBar) v.findViewById(R.id.seek_bar_escritura);
        tvPorcentajeEscritura = (TextView) v.findViewById(R.id.tv_porcentaje_escritura);

        seekBarEscritura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPorcentajeEscritura.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
