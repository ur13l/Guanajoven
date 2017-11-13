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
import mx.gob.jovenes.guanajuato.adapters.RVEventoAdapter;
import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by code on 31/01/17.
 */
public class EventoFragment extends CustomFragment {
    private EventoAPI eventoAPI;
    private RecyclerView rvEvento;
    private TextView tvEmptyEvento;
    private RVEventoAdapter adapter;
    private Realm realm;
    private Retrofit retrofit;
    private List<Evento> eventos;
    private AppCompatActivity activity;
    private SharedPreferences prefs;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolBar;
    private SwipeRefreshLayout swipeRefreshLayoutEventos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        realm = MyApplication.getRealmInstance();

        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolBar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_eventos, container, false);
        swipeRefreshLayoutEventos = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout_eventos);
        rvEvento = (RecyclerView) v.findViewById(R.id.rv_eventos);
        tvEmptyEvento = (TextView) v.findViewById(R.id.tv_empty_eventos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvEvento.setLayoutManager(linearLayoutManager);
        updateList();

        primeraLlamada();

        if (noHayDatosEnRealm()) {
            tvEmptyEvento.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayoutEventos.setOnRefreshListener(() -> primeraLlamada());

        return v;
    }

    private void primeraLlamada() {
        swipeRefreshLayoutEventos.setRefreshing(false);
        Call<Response<ArrayList<Evento>>> call = eventoAPI.obtenerEventos(prefs.getString(MyApplication.LAST_UPDATE_EVENTOS, "0000-00-00 00:00:00"));

        call.enqueue(new Callback<Response<ArrayList<Evento>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Evento>>> call, retrofit2.Response<Response<ArrayList<Evento>>> response) {

                tvEmptyEvento.setVisibility(View.GONE);

                if(response.body().success) {
                    List<Evento> evn = response.body().data;

                    realm.beginTransaction();
                    for(Evento e : evn) {
                        if(e.getDeletedAt() != null) {
                            Evento ev = realm.where(Evento.class).equalTo("idEvento", e.getIdEvento()).findFirst();
                            if(ev != null) {
                                ev.deleteFromRealm();
                            }
                        } else {
                            realm.copyToRealmOrUpdate(e);
                        }
                    }
                    realm.commitTransaction();

                    if(evn.size() > 0) {
                        updateList();
                    }

                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_EVENTOS, lastUpdate).apply();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Evento>>> call, Throwable t) {
                if (noHayDatosEnRealm()) {
                    AlertDialog.Builder mensajeError = new AlertDialog.Builder(getContext());
                    mensajeError.create();
                    mensajeError.setMessage("Necesitas estar conectado para poder ver los últimos eventos");
                    mensajeError.show();
                }
            }
        });
    }

    private void updateList() {
        RealmResults<Evento> result = realm.where(Evento.class).findAll();
        eventos = realm.copyFromRealm(result);
        adapter = new RVEventoAdapter(getActivity(), eventos);
        rvEvento.setAdapter(adapter);
    }

    public boolean noHayDatosEnRealm() {
        return (realm.where(Evento.class).findAll().isEmpty());
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        cToolBar.setVisibility(View.GONE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppBarLayout)cToolBar.getParent()).setExpanded(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView imagen = (ImageView)cToolBar.findViewById(R.id.image);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        cToolBar.setVisibility(View.VISIBLE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Eventos");
        cToolBar.setTitle("Eventos");
        imagen.setImageResource(R.drawable.registro_usuario);

    }

}
