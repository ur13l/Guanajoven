package code.guanajuato.gob.mx.activatecode.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class ColaboradoresFragment extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_colaboradores, parent, false);

        return v;
    }
}
