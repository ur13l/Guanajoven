package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by code on 31/01/17.
 */
public class MisEventosFragment extends CustomFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_eventos, container, false);
        v.findViewById(R.id.ev1).setOnClickListener(this);
        v.findViewById(R.id.ev2).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DetalleEventoFragment f = DetalleEventoFragment.newInstance();
        ft.replace(R.id.segunda_fragment_container, f).commit();

    }
}
