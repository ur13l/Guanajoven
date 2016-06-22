package code.guanajuato.gob.mx.activatecode.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.Lugar;

/**
 * Created by Uriel on 11/03/2016.
 */
public class DirectorioFragment extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_directorio, parent, false);
        View item = v.findViewById(R.id.item);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        Lugar l = new Lugar();
        l.setId_lugar(0);
        l.setNombre("Auditorio Municipal");
        l.setLatitud(21.1251939f);
        l.setLongitud(-101.697773f);
        final LugarFragment lf = LugarFragment.newInstance(l);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft.replace(R.id.segunda_fragment_container, lf).commit();
            }
        });
        return v;
    }
}
