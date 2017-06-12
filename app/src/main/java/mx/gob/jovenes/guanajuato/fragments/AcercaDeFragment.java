package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class AcercaDeFragment extends CustomFragment {
    private CardView cardViewLineasAccion;
    private CardView cardViewDiagnosticoJuvenil;
    private CardView cardViewEncuestaJuventud;
    private CardView cardViewDirectorio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_acerca_de, parent, false);

        //Instancias
        //cardViewLineasAccion = (CardView) v.findViewById(R.id.card_view_lineas_accion);
        //cardViewDiagnosticoJuvenil = (CardView) v.findViewById(R.id.card_view_diagnostico_juvenil);
        //cardViewEncuestaJuventud = (CardView) v.findViewById(R.id.card_view_encuesta_juventud);
        //cardViewDirectorio = (CardView) v.findViewById(R.id.card_view_directorio);

        //Implementación
        /*cardViewLineasAccion.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/702-2/");
        });

        cardViewDiagnosticoJuvenil.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/diagnostico-juvenil/");
        });

        cardViewEncuestaJuventud.setOnClickListener((View) -> {
            enlace("http://jovenes.guanajuato.gob.mx/index.php/encuesta-de-juventud/");
        });

        cardViewDirectorio.setOnClickListener((View) -> {
            enlace("http://transparencia.guanajuato.gob.mx/transparencia/informacion_publica_directorio.php");
        });*/

        return v;
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }

}
