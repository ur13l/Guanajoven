package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVRegionAdapter;
import mx.gob.jovenes.guanajuato.api.RegionAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Region;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Juan José Estrada Valtierra on 25/04/17.
 */

public class RegionFragment extends CustomFragment {
    private RegionAPI regionAPI;
    private RecyclerView rvRegion;
    private TextView tvEmptyRegion;
    private RVRegionAdapter adapter;
    private Retrofit retrofit;
    private Realm realm;
    private List<Region> regiones;
    private SharedPreferences prefs;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolbar;
    private SwipeRefreshLayout swipeRefreshLayoutRegiones;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        regionAPI = retrofit.create(RegionAPI.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        realm = MyApplication.getRealmInstance();

        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolbar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_regiones, container, false);
        swipeRefreshLayoutRegiones = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout_regiones);
        rvRegion = (RecyclerView) v.findViewById(R.id.rv_regiones);
        tvEmptyRegion = (TextView) v.findViewById(R.id.tv_empty_regiones);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvRegion.setLayoutManager(llm);
        actualizarLista();

        primeraLlamada();

        if (noHayDatosEnRealm()) {
            tvEmptyRegion.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayoutRegiones.setOnRefreshListener(() -> primeraLlamada());

        return v;
    }

    public void primeraLlamada() {
        swipeRefreshLayoutRegiones.setRefreshing(false);
        Call<Response<ArrayList<Region>>> call = regionAPI.get(prefs.getString(MyApplication.LAST_UPDATE_REGIONES, "0000-00-00 00:00:00"));

        call.enqueue(new Callback<Response<ArrayList<Region>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Region>>> call, retrofit2.Response<Response<ArrayList<Region>>> response) {
                tvEmptyRegion.setVisibility(View.GONE);
                if (response.body().success) {
                    List<Region> reg = response.body().data;


                    realm.beginTransaction();
                    for (Region r : reg) {
                        if(r.getDeletedAt() != null) {
                            Region re = realm.where(Region.class)
                                    .equalTo("idRegion", r.getIdRegion())
                                    .findFirst();
                            if(re != null) {
                                re.deleteFromRealm();
                            }
                        }
                        else {
                            realm.copyToRealmOrUpdate(r);
                        }
                    }
                    realm.commitTransaction();

                    if (reg.size() > 0) {
                        actualizarLista();
                    }

                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_REGIONES, lastUpdate).apply();

                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Region>>> call, Throwable t) {
                if (noHayDatosEnRealm()) {
                    AlertDialog.Builder mensajeError = new AlertDialog.Builder(getContext());
                    mensajeError.create();
                    mensajeError.setMessage("Necesitas estar conectado para poder ver las últimas regiones");
                    mensajeError.show();
                }
            }
        });
    }

    public void actualizarLista() {
        RealmResults<Region> result = realm.where(Region.class).findAll();
        regiones = realm.copyFromRealm(result);
        adapter = new RVRegionAdapter(getActivity(), regiones);
        rvRegion.setAdapter(adapter);
    }

    public boolean noHayDatosEnRealm() {
        return (realm.where(Region.class).findAll().isEmpty());
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        cToolbar.setVisibility(View.GONE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppBarLayout)cToolbar.getParent()).setExpanded(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView imagen = (ImageView)cToolbar.findViewById(R.id.image);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        cToolbar.setVisibility(View.VISIBLE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Regiones");
        cToolbar.setTitle("Regiones");
        imagen.setImageResource(R.drawable.registro_usuario);

    }
}
