package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVPromocionAdapter;
import mx.gob.jovenes.guanajuato.model.Empresa;

/**
 * Created by codigus on 17/07/2017.
 */

public class PromocionFragment extends Fragment {
    private RecyclerView rvPromociones;
    private TextView textViewEmptyPromociones;
    private Empresa empresa;
    private RVPromocionAdapter adapter;
    private CollapsingToolbarLayout cToolBar;
    private Toolbar toolbar;
    private AppCompatActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolBar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promociones, container, false);
        rvPromociones = (RecyclerView) view.findViewById(R.id.rv_promociones);
        textViewEmptyPromociones = (TextView) view.findViewById(R.id.textview_empty_promociones);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        adapter = new RVPromocionAdapter(getContext(), empresa.getPromociones(), empresa);

        rvPromociones.setLayoutManager(llm);
        rvPromociones.setAdapter(adapter);

        if (empresa.getPromociones().isEmpty()) {
            textViewEmptyPromociones.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        cToolBar.setVisibility(View.GONE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppBarLayout) cToolBar.getParent()).setExpanded(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView imagen = (ImageView) cToolBar.findViewById(R.id.image);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        cToolBar.setVisibility(View.VISIBLE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Eventos");
        cToolBar.setTitle(empresa.getNombreComercial());
        Picasso.with(getContext()).load(empresa.getLogo()).into(imagen);
    }

}
