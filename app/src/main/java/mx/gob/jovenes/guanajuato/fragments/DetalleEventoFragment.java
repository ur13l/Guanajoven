package mx.gob.jovenes.guanajuato.fragments;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.Lugar;
import mx.gob.jovenes.guanajuato.model.Region;

/**
 * Created by uriel on 21/06/16.
 */
public class DetalleEventoFragment extends Fragment implements OnMapReadyCallback {
    private static String ID_EVENTO = "id_evento";
    private Evento evento;
    private MapFragment mapaEvento;
    private TextView tvNombreEvento;
    private TextView tvDireccionEvento;
    private TextView tvDescripcionEvento;
    private TextView tvFechaEvento;
    private Button btnAsistencia;
    private Realm realm;

    public static DetalleEventoFragment newInstance(int idEvento) {
        DetalleEventoFragment detalleEventoFragment = new DetalleEventoFragment();
        Bundle args = new Bundle();
        args.putInt(ID_EVENTO, idEvento);//cambia el valor de la variable por el id de la region seleccionada
        detalleEventoFragment.setArguments(args);
        return detalleEventoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = MyApplication.getRealmInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_evento, container, false);

        evento = realm.where(Evento.class).equalTo("idEvento", getArguments().getInt(ID_EVENTO)).findFirst();

        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        mapaEvento.getMapAsync(this);

        tvNombreEvento = (TextView) v.findViewById(R.id.tv_nombre_evento);
        tvDireccionEvento = (TextView) v.findViewById(R.id.tv_direccion_evento);
        tvDescripcionEvento = (TextView) v.findViewById(R.id.tv_descripcion_evento);
        tvFechaEvento = (TextView) v.findViewById(R.id.tv_fechas_evento);
        btnAsistencia = (Button) v.findViewById(R.id.btn_asistencia);

        tvNombreEvento.setText(evento.getTitulo());
        tvDireccionEvento.setText(evento.getDireccion());
        tvDescripcionEvento.setText(evento.getDescripcion());
        tvFechaEvento.setText(getFechaCast(evento.getFechaInicio()) + " - " + getFechaCast(evento.getFechaFin()));
        checkAsist();

        return v;
    }

    public void checkAsist(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInStringbegin = getFechaCast(evento.getFechaInicio());
        String dateInStringend = getFechaCast(evento.getFechaFin());
        try {
            Date fechainicio = formatter.parse(dateInStringbegin);
            Date fechafin = formatter.parse(dateInStringend);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date newFormat = formatter.parse(dateFormat.format(date));
            if(newFormat.after(fechainicio) && newFormat.before(fechafin)) {
                btnAsistencia.setText("Estoy en el evento");
                 } else if(newFormat.before(fechafin)){
                btnAsistencia.setText("Asistiré al evento");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        float zoomLevel = (float) 16.0;
        LatLng coordenadas = new LatLng(evento.getLatitud(), evento.getLongitud()); //coordenadas de la región
        googleMap.addMarker(new MarkerOptions().position(coordenadas).title(evento.getTitulo())); //pone el puntero en las coordenadas
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, zoomLevel)); //hace el zoom en el mapa
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        if (mapaEvento != null)
            getActivity().getFragmentManager().beginTransaction().remove(mapaEvento).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(evento.getTitulo());
    }

    private class GPSTracker extends Service implements LocationListener {
        String TAG = GPSTracker.class.getName();
        Context context;
        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        boolean isGPSTrackingEnabled = false;
        Location location;
        double altitude;
        double longitude;
        int geocoderMaxResults = 1;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        LocationManager locationManager;
        String providerInfo;

        public GPSTracker(Context context) {
            this.context = context;
            getLocation();
        }

        public void getLocation() {
            try {
                locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGPSEnabled) {
                    this.isGPSTrackingEnabled = true;
                    Log.d(TAG, "Aplication use GPS service");
                    providerInfo = LocationManager.GPS_PROVIDER;
                } else if (isNetworkEnabled) {
                    this.isGPSTrackingEnabled = true;
                    Log.d(TAG, "Application use Network state to get GPS coordinates");
                    providerInfo = LocationManager.NETWORK_PROVIDER;
                }

                if (!providerInfo.isEmpty()) {
                    locationManager.requestLocationUpdates(providerInfo, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, context.);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}
