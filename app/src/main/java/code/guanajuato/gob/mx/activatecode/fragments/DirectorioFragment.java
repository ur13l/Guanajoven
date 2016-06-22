package code.guanajuato.gob.mx.activatecode.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.adapters.RVLugaresAdapter;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.model.Lugar;
import code.guanajuato.gob.mx.activatecode.persistencia.BitacoraDBHelper;
import code.guanajuato.gob.mx.activatecode.persistencia.LugarDBHelper;

/**
 * Created by Uriel on 11/03/2016.
 */
public class DirectorioFragment extends CustomFragment {
    private ArrayList<Lugar> lugares;
    private RecyclerView rv;
    private RVLugaresAdapter adapter;
    private LugarDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_directorio, parent, false);

        rv = (RecyclerView) v.findViewById(R.id.rv_lugares);

        setDatos();

        adapter = new RVLugaresAdapter(lugares);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        return v;
    }

    public void setDatos() {
        dbHelper = new LugarDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());

        try {
            dbHelper.prepareDatabase();
            lugares = dbHelper.getLugares();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
