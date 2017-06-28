package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class AcercaDeFragment extends CustomFragment {
    private TextView textViewLineasAccion;
    private TextView textViewDiagnosticoJuvenil;
    private TextView textViewEncuestaJuventud;
    private TextView textViewDirectorio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_acerca_de, parent, false);

        //Instancias
        textViewLineasAccion = (TextView) v.findViewById(R.id.enlace_lineas_de_accion);
        textViewDiagnosticoJuvenil = (TextView) v.findViewById(R.id.enlace_diagnostico_juvenil);
        textViewEncuestaJuventud = (TextView) v.findViewById(R.id.enlace_encuesta_de_juventud);
        textViewDirectorio = (TextView) v.findViewById(R.id.enlace_directorio);

        //Implementación
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
