package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by code on 9/02/17.
 */

public class HistorialNotificacionesFragment extends CustomFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historial_notificaciones, container, false);
        return v;
    }
}
