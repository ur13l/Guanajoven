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
            enlace("http://jovenes.guanajuato.gob.mx/index.php/702-2/");
        });

        textViewDiagnosticoJuvenil.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/diagnostico-juvenil/");
        });

        textViewEncuestaJuventud.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/encuesta-de-juventud/");
        });

        textViewDirectorio.setOnClickListener((View) -> {
            enlace("http://transparencia.guanajuato.gob.mx/transparencia/informacion_publica_directorio.php");
        });

        return v;
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

}
