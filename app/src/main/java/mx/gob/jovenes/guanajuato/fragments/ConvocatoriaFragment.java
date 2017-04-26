package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVConvocatoriaAdapter;
import mx.gob.jovenes.guanajuato.api.ConvocatoriaAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Juan José Estrada Valtierra on 12/04/17.
 */
public class ConvocatoriaFragment extends CustomFragment{
    private ConvocatoriaAPI convocatoriaAPI;
    private RecyclerView rvConvocatoria;
    private TextView tvEmptyConvocatoria;
    private RVConvocatoriaAdapter adapter;
    private Retrofit retrofit;
    private Realm realm;
    private List<Convocatoria> convocatorias;
    private SharedPreferences prefs;

    private AppCompatActivity activity;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instancias de la API
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        convocatoriaAPI = retrofit.create(ConvocatoriaAPI.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        realm = MyApplication.getRealmInstance();

        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolbar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_convocatorias, container, false);
        rvConvocatoria = (RecyclerView) v.findViewById(R.id.rv_convocatoria);
        tvEmptyConvocatoria = (TextView) v.findViewById(R.id.tv_empty_convocatoria);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvConvocatoria.setLayoutManager(llm);
        updateList();

        Call<Response<ArrayList<Convocatoria>>> call = convocatoriaAPI.get(prefs.getString(MyApplication.LAST_UPDATE_CONVOCATORIAS, "0000-00-00 00:00:00"));

        //Llamada a servidor caso de acertar o fallar
        call.enqueue(new Callback<Response<ArrayList<Convocatoria>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Convocatoria>>> call, retrofit2.Response<Response<ArrayList<Convocatoria>>> response) {
                if(response.body().success) {

                    List<Convocatoria> conv = response.body().data;

                    //Transacción de realm, se itera sobre las convocatorias obtenidas desde el servidor.
                    realm.beginTransaction();
                    for(Convocatoria c : conv) {
                        if(c.getDeletedAt() != null) {
                            Convocatoria cr = realm.where(Convocatoria.class)
                                    .equalTo("idConvocatoria", c.getIdConvocatoria())
                                    .findFirst();
                            if(cr != null) {
                                cr.deleteFromRealm();
                            }
                        }
                        else {
                            realm.copyToRealmOrUpdate(c);
                        }
                    }
                    realm.commitTransaction();

                    //La lista se actualiza cuando se carga al menos un registro desde el servidor.
                    if(conv.size() > 0) {
                        updateList();
                    }

                    //Actualizando el timestamp para no descargar el contenido ya existente.
<<<<<<< HEAD
                    long timestamp = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
                    prefs.edit().putLong(MyApplication.LAST_UPDATE_CONVOCATORIAS, timestamp).apply();
                    //que perron...
=======
                    String lastUpdate = DateUtilities.dateToString(new Date());
                    prefs.edit().putString(MyApplication.LAST_UPDATE_CONVOCATORIAS, lastUpdate).apply();
>>>>>>> 9561355a00c0413f5e6f0d0861484c1ff424d610
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Convocatoria>>> call, Throwable t) {

            }
        });

        return v;
    }

    /**
     * Método que permite la actualización de la lista de convocatorias a partir de los nuevos
     * resultados arrojados por el servidor.
     */
    public void updateList() {
        RealmResults<Convocatoria> result = realm.where(Convocatoria.class).findAll();
        convocatorias = realm.copyFromRealm(result);
        adapter = new RVConvocatoriaAdapter(getActivity(), convocatorias);
        rvConvocatoria.setAdapter(adapter);
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
        activity.getSupportActionBar().setTitle("Convocatorias");
        cToolbar.setTitle("Convocatorias");
        imagen.setImageResource(R.drawable.login_background);

    }
}
