package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.Realm;
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
    private RVNotificacionAdapter adapter;
    private Retrofit retrofit;
    private Realm realm;
    private List<Notificacion> notificaciones;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        rvNotificaciones = (RecyclerView) v.findViewById(R.id.rv_notificaciones);
        tvEmptyNotificaciones = (TextView) v.findViewById(R.id.tv_empty_notificacion);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvNotificaciones.setLayoutManager(llm);

        realm.beginTransaction();

        realm.commitTransaction();


        return v;
    }
}
