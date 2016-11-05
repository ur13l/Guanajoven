package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class ColaboradoresFragment extends CustomFragment {
    private CardView itlCard;
    private CardView codeCard;
    private CardView fbCard;
    private CardView twitterCard;
    private CardView youtubeCard;
    private CardView instagramCard;


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

        fbCard = (CardView) v.findViewById(R.id.card_fb);
        fbCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlace("https://www.facebook.com/GuanajuatoCODE/");

            }
        });

        twitterCard = (CardView) v.findViewById(R.id.card_twitter);
        twitterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlace("https://twitter.com/guanajuatocode");
            }
        });

        youtubeCard = (CardView) v.findViewById(R.id.card_youtube);
        youtubeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlace("https://www.youtube.com/channel/UC7CygsxGue63PCBMrSa3vew");
            }
        });

        instagramCard = (CardView) v.findViewById(R.id.card_instagram);
        instagramCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enlace("https://www.instagram.com/guanajuatocode/");

            }
        });

        return v;
    }

    public void abrirColaboradores(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DetalleColaboradoresCodeFragment f = new DetalleColaboradoresCodeFragment();
        ft.replace(R.id.segunda_fragment_container, f)
                .addToBackStack(null)
                .commit();
    }

    public void abrirITL(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DetalleColaboradoresFragment f = new DetalleColaboradoresFragment();
        ft.replace(R.id.segunda_fragment_container, f)
                .addToBackStack(null)
                .commit();
    }

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}
