package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mx.gob.jovenes.guanajuato.R;

/**
 * Created by leonardolirabecerra on 28/03/17.
 */

public class CodigoGuanajovenFragment extends CustomFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_codigo_guanajoven, container, false);
        return vista;
    }
}
