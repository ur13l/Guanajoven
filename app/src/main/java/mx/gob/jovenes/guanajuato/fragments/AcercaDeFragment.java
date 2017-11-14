package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.gob.jovenes.guanajuato.R;

public class AcercaDeFragment extends CustomFragment {
    private TextView textViewLineasAccion;
    private TextView textViewDiagnosticoJuvenil;
    private TextView textViewEncuestaJuventud;
    private TextView textViewDirectorio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_acerca_de, parent, false);

        textViewLineasAccion = (TextView) v.findViewById(R.id.enlace_lineas_de_accion);
        textViewDiagnosticoJuvenil = (TextView) v.findViewById(R.id.enlace_diagnostico_juvenil);
        textViewEncuestaJuventud = (TextView) v.findViewById(R.id.enlace_encuesta_de_juventud);
        textViewDirectorio = (TextView) v.findViewById(R.id.enlace_directorio);

        textViewLineasAccion.setOnClickListener((View) -> {
            enlace(getString(R.string.fragment_acercade_link_lineas_accion));
        });

        textViewDiagnosticoJuvenil.setOnClickListener((View) -> {
            enlace(getString(R.string.fragment_acercade_link_diagnostico));
        });

        textViewEncuestaJuventud.setOnClickListener((View) -> {
            enlace(getString(R.string.fragment_acerdade_link_encuesta));
        });

        textViewDirectorio.setOnClickListener((View) -> {
            enlace(getString(R.string.fragment_acercade_link_directorio));
        });

        return v;
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

}
