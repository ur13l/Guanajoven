package mx.gob.jovenes.guanajuato.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by Uriel on 25/05/2017.
 */

public class IdiomasAdicionalesDialogFragment  extends DialogFragment{
    private ElegirIdiomasFragment elegirIdiomasFragment;
    private List<DetalleIdiomaFragment> detalleIdiomaFragmentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idiomas_adicionales, container, false);

        return v;
    }
}
