package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class ColaboradoresFragment extends CustomFragment {
    private CardView itlCard;
    private CardView codeCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_colaboradores, parent, false);
        codeCard = (CardView) v.findViewById(R.id.card_code);
        codeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirColaboradores();
            }
        });

        itlCard = (CardView) v.findViewById(R.id.card_itl);
        itlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirITL();
            }
        });

        return v;
    }

    public void abrirColaboradores(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.codegto.gob.mx/index.php/directorio/"));
        startActivity(i);
    }

    public void abrirITL(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DetalleColaboradoresFragment f = new DetalleColaboradoresFragment();
        ft.replace(R.id.segunda_fragment_container, f)
                .addToBackStack(null)
                .commit();
    }
}
