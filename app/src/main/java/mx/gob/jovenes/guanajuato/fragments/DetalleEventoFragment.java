package mx.gob.jovenes.guanajuato.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.api.EventoAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.EventoResponse;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by uriel on 21/06/16.
 */
public class DetalleEventoFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private static String ID_EVENTO = "id_evento";
    private Evento evento;
    private MapFragment mapaEvento;
    private TextView tvNombreEvento;
    private TextView tvDireccionEvento;
    private TextView tvDescripcionEvento;
    private TextView tvFechaEvento;
    private Button botonEstoyEnEvento;
    private Button botonMeInteresa;
    private TextView textViewEventoCaducado;
    private Realm realm;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 321;
    private double latitud;
    private double longitud;
    private ProgressDialog progressDialog;
    private Retrofit retrofit;
    private EventoAPI eventoAPI;

    private static final String ERROR_YA_REGISTRADO = "Ya has sido registrado";
    private static final String ERROR_FUERA_DE_RANGO = "No te encuentras en el rango del evento";

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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        eventoAPI = retrofit.create(EventoAPI.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_detalle_evento, container, false);

        evento = realm.where(Evento.class).equalTo("idEvento", getArguments().getInt(ID_EVENTO)).findFirst();


        progressDialog = new ProgressDialog(getContext());

        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        mapaEvento.getMapAsync(this);

        tvNombreEvento = (TextView) v.findViewById(R.id.tv_nombre_evento);
        tvDireccionEvento = (TextView) v.findViewById(R.id.tv_direccion_evento);
        tvDescripcionEvento = (TextView) v.findViewById(R.id.tv_descripcion_evento);
        tvFechaEvento = (TextView) v.findViewById(R.id.tv_fechas_evento);
        botonEstoyEnEvento = (Button) v.findViewById(R.id.boton_estoy_en_el_evento);
        botonMeInteresa = (Button) v.findViewById(R.id.boton_me_interesa);
        textViewEventoCaducado = (TextView) v.findViewById(R.id.textview_evento_caducado);

        tvNombreEvento.setText(evento.getTitulo());
        tvDireccionEvento.setText(evento.getDireccion());
        tvDescripcionEvento.setText(evento.getDescripcion());
        tvFechaEvento.setText(getFechaCast(evento.getFechaInicio()) + " - " + getFechaCast(evento.getFechaFin()));
        checkAsist();

        botonEstoyEnEvento.setOnClickListener((View) -> {
                progressDialog = ProgressDialog.show(getContext(), "Cargando", "Obteniendo tu localización", true, true);

                Call<Response<EventoResponse>> call = eventoAPI.marcarEvento(evento.getIdEvento(), Sesion.getUsuario().getApiToken(), getLatitud(), getLongitud());

                call.enqueue(new Callback<Response<EventoResponse>>() {
                    @Override
                    public void onResponse(Call<Response<EventoResponse>> call, retrofit2.Response<Response<EventoResponse>> response) {

                        if (response.body() != null) {
                            if (response.body().errors.length == 0) {
                                int puntos = response.body().data.getPuntosOtorgados();
                                int puntosUsuario = Integer.parseInt(Sesion.getUsuario().getPuntaje());
                                String puntosFinal = String.valueOf(puntos + puntosUsuario);
                                Sesion.getUsuario().setPuntaje(puntosFinal);

                                Snackbar.make(getView(), "Registrado!", Snackbar.LENGTH_LONG).show();

                            } else if (response.body().errors[0].equals(ERROR_FUERA_DE_RANGO)) {
                                Snackbar.make(getView(), ERROR_FUERA_DE_RANGO, Snackbar.LENGTH_LONG).show();
                            } else if (response.body().errors[0].equals(ERROR_YA_REGISTRADO)) {
                                Snackbar.make(getView(), ERROR_YA_REGISTRADO, Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            Snackbar.make(getView(), "Error al obtener los datos", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<EventoResponse>> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Error de conexión");
                        builder.show();
                    }
                });

        });

        botonMeInteresa.setOnClickListener((View) -> {
            //Genera la llamada para poder enviar un correo
            Call<Response<Boolean>> enviarCorreo = eventoAPI.enviarCorreo(Sesion.getUsuario().getId(), evento.getIdEvento());

            enviarCorreo.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    Snackbar.make(getView(), "Fallo en enviar o ya se encuentra inscrito", 7000).show();
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), "Gracias por estar interesado en el evento, en breve te llegará un correo electrónico con más información.", 7000).show();
                }
            });

            //TODO Bloquear boton a lo largo de toda la app
            //Crea un contador para poder enviar correos cada cierto tiempo
            new CountDownTimer(10000, 1000) {

                public void onTick(long millisUntilFinished) {
                    MyApplication.ENVIAR_CORREOS_EVENTOS = false;
                    botonMeInteresa.setEnabled(false);
                }

                public void onFinish() {
                    MyApplication.ENVIAR_CORREOS_EVENTOS = true;
                }

            }.start();

        });

        return v;
    }

    public void checkAsist() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInStringbegin = evento.getFechaInicio();
        String dateInStringend = evento.getFechaFin();
        String dateInStringToday = formatter.format(new Date());

        try {
            Date fechainicio = formatter.parse(dateInStringbegin);
            Date fechafin = formatter.parse(dateInStringend);
            Date today = formatter.parse(dateInStringToday);

            long timeStampBegin = fechainicio.getTime();
            long timeStampEnd = fechafin.getTime();
            long timeStampToday = today.getTime();

            boolean antesDeFecha = timeStampBegin > timeStampToday;
            boolean enFecha = timeStampBegin < timeStampToday && timeStampToday > timeStampEnd;
            boolean despuesDeFecha = timeStampEnd < timeStampToday;

            if (antesDeFecha) {
                textViewEventoCaducado.setVisibility(View.VISIBLE);
            } else if (despuesDeFecha) {
                botonMeInteresa.setVisibility(View.VISIBLE);
            } else if (enFecha) {
                botonEstoyEnEvento.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pedirPermisos();
                } else {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

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
    public void onLocationChanged(Location location) {
        if (botonEstoyEnEvento.getVisibility() == View.VISIBLE) {
            progressDialog.dismiss();

            setLatitud(location.getLatitude());
            setLongitud(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (botonEstoyEnEvento.getVisibility() == View.VISIBLE) {
            if (getContext() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Necesitas el GPS");
                builder.show();
            }

            if (progressDialog != null) { progressDialog.dismiss(); }
        }
    }

    private void pedirPermisos() {
        String[] permisos = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permisos, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permitir = true;

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults) {
                    permitir = permitir && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                permitir = false;
                break;
        }

        if (permitir) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Snackbar.make(getActivity().findViewById(R.id.segunda_fragment_container), "Permiso denegado", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    private void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    private double getLatitud() {
        return this.latitud;
    }

    private double getLongitud() {
        return this.longitud;
    }

}
