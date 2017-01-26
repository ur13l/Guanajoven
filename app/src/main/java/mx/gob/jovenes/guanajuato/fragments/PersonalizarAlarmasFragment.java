package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.gob.jovenes.guanajuato.R;


/**
 * Created by Uriel on 01/02/2016.
 */
public class PersonalizarAlarmasFragment extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_personalizar_alarmas, parent, false);

        return v;
    }
}