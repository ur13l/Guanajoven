package code.guanajuato.gob.mx.activatecode.fragments;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.adapters.RVReporteAdapter;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.model.StatusReporte;
import code.guanajuato.gob.mx.activatecode.persistencia.BitacoraDBHelper;

/**
 * Created by Uriel on 11/03/2016.
 */
public class ReporteFragment extends CustomFragment {
    private BitacoraDBHelper bitacoraDBHelper;
    private ArrayList<StatusReporte> lista;
    private View rootView;
    private RecyclerView rvReporte;
    private RVReporteAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_reportes, parent, false);
        rvReporte = (RecyclerView) v.findViewById(R.id.rv_reporte);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvReporte.setLayoutManager(llm);

        bitacoraDBHelper = new BitacoraDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());

        try {
            bitacoraDBHelper.prepareDatabase();
            Login session = new Login(getActivity().getApplicationContext());
            lista = bitacoraDBHelper.getStatusReporte(session.getId());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new RVReporteAdapter(lista);
        rvReporte.setAdapter(adapter);
        for(int i = 0 ; i != lista.size(); i++){
            Log.d("REPORTE", lista.get(i).getFecha().toString() + " | " + lista.get(i).isAgua() + " | " + lista.get(i).isEjercicio());
        }
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.collapsing_toolbar).setVisibility(View.GONE);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.collapsing_toolbar).setVisibility(View.VISIBLE);
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reporte");
        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitle("Reporte");
    }


}
