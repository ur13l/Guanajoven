package mx.gob.jovenes.guanajuato.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVEventosAdapter;
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
public class EventoFragment extends CustomFragment /*implements View.OnClickListener*/ {
    private EventoAPI eventoAPI;
    private RecyclerView rvEvento;
    private TextView tvEmptyEvento;
    private RVEventosAdapter adapter;
    private Realm realm;
    private Retrofit retrofit;
    private List<Evento> eventos;
    private AppCompatActivity activity;
    private SharedPreferences prefs;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolBar;
    //private Context context;

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
        rvEvento = (RecyclerView) v.findViewById(R.id.rv_eventos);
        tvEmptyEvento = (TextView) v.findViewById(R.id.tv_empty_eventos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvEvento.setLayoutManager(linearLayoutManager);
        updateList();

        Call<Response<ArrayList<Evento>>> call = eventoAPI.obtenerEventos(prefs.getString(MyApplication.LAST_UPDATE_EVENTOS, "0000-00-00 00:00:00"));

        call.enqueue(new Callback<Response<ArrayList<Evento>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Evento>>> call, retrofit2.Response<Response<ArrayList<Evento>>> response) {

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

                    System.out.println(eventos.size());

                    if(evn.size() > 0) {
                        updateList();
                    }

                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_EVENTOS, lastUpdate).apply();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Evento>>> call, Throwable t) {
                System.err.println("Fallo");
            }
        });

        return v;
    }

    private void updateList() {
        RealmResults<Evento> result = realm.where(Evento.class).findAll();
        eventos = realm.copyFromRealm(result);
        adapter = new RVEventosAdapter(getActivity(), eventos);
        rvEvento.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        cToolBar.setVisibility(View.GONE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        imagen.setImageResource(R.drawable.background4);

    }

    /*
    @Override
    public void onClick(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DetalleEventoFragment f = DetalleEventoFragment.newInstance();
        ft.replace(R.id.segunda_fragment_container, f).commit();

    }*/


}
