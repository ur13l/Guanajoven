package mx.gob.jovenes.guanajuato.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVNotificacionAdapter;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Notificacion;
import mx.gob.jovenes.guanajuato.notifications.FirebaseMessagingService;
import retrofit2.Retrofit;

/**
 * Created by code on 9/02/17.
 */

public class NotificacionesFragment extends CustomFragment {
    private RecyclerView rvNotificaciones;
    private TextView tvEmptyNotificaciones;
    public  RVNotificacionAdapter adapter;
    private Retrofit retrofit;
    private Realm realm;
    public  List<Notificacion> notificaciones;
    private SharedPreferences preferences;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        realm = MyApplication.getRealmInstance();

        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolbar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);

        IntentFilter intentFilter = new IntentFilter("mx.gob.jovenes.guanajuato.NOTIFICACION_RECIBIDA");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(notificacionRecibida, intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        rvNotificaciones = (RecyclerView) v.findViewById(R.id.rv_notificaciones);
        tvEmptyNotificaciones = (TextView) v.findViewById(R.id.tv_empty_notificacion);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvNotificaciones.setLayoutManager(llm);

        updateList();

        if (noHayDatosEnRealm()) {
            tvEmptyNotificaciones.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public boolean noHayDatosEnRealm() {
        return (realm.where(Notificacion.class).findAll().isEmpty());
    }


    private void updateList() {
        RealmResults<Notificacion> result = realm.where(Notificacion.class).findAll();
        notificaciones = realm.copyFromRealm(result);
        adapter = new RVNotificacionAdapter(getActivity(), notificaciones);
        rvNotificaciones.setAdapter(adapter);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        cToolbar.setVisibility(View.GONE);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        activity.getSupportActionBar().setTitle("Notificaciones");
        cToolbar.setTitle("Notificaciones");
        imagen.setImageResource(R.drawable.notificaciones);

    }

    private BroadcastReceiver notificacionRecibida = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notificacion notificacion = new Gson().fromJson(intent.getExtras().getString("notificacion"), Notificacion.class);
            notificaciones.add(notificacion);
            adapter.notifyDataSetChanged();

            Snackbar.make(getView(), "Nueva notificación: " + notificacion.getTitulo(), Snackbar.LENGTH_LONG).show();
        }
    };



}
