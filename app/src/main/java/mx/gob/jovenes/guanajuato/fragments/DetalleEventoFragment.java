package mx.gob.jovenes.guanajuato.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DetalleEventoFragment extends Fragment implements OnMapReadyCallback {
    private static String ID_EVENTO = "id_evento";
    private Evento evento;
    private MapFragment mapaEvento;
    private TextView tvNombreEvento;
    private TextView tvDireccionEvento;
    private TextView tvDescripcionEvento;
    private TextView tvFechaEvento;
    private Button botonEstoyEnEvento;
    public static Button botonMeInteresa;
    private TextView textViewEventoCaducado;
    private TextView textViewYaHasSidoRegistrado;
    private Realm realm;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 321;
    private double latitud;
    private double longitud;
    private Retrofit retrofit;
    private EventoAPI eventoAPI;

    private Criteria criteria;

    public static DetalleEventoFragment newInstance(int idEvento) {
        DetalleEventoFragment detalleEventoFragment = new DetalleEventoFragment();
        Bundle args = new Bundle();
        args.putInt(ID_EVENTO, idEvento);
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

        evento = realm.where(Evento.class).equalTo(getString(R.string.fragment_detalle_evento_idevento), getArguments().getInt(ID_EVENTO)).findFirst();

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        mapaEvento.getMapAsync(this);

        tvNombreEvento = (TextView) v.findViewById(R.id.tv_nombre_evento);
        tvDireccionEvento = (TextView) v.findViewById(R.id.tv_direccion_evento);
        tvDescripcionEvento = (TextView) v.findViewById(R.id.tv_descripcion_evento);
        tvFechaEvento = (TextView) v.findViewById(R.id.tv_fechas_evento);
        botonEstoyEnEvento = (Button) v.findViewById(R.id.boton_estoy_en_el_evento);
        botonMeInteresa = (Button) v.findViewById(R.id.boton_me_interesa);
        textViewEventoCaducado = (TextView) v.findViewById(R.id.textview_evento_caducado);
        textViewYaHasSidoRegistrado = (TextView) v.findViewById(R.id.textview_registrado);

        tvNombreEvento.setText(evento.getTitulo());
        tvDireccionEvento.setText(evento.getDireccion());
        tvDescripcionEvento.setText(evento.getDescripcion());

        String fechaEvento = DateUtilities.getFechaCast(evento.getFechaInicio()) +
                getString(R.string.fragment_detalle_evento_guion) +
                DateUtilities.getFechaCast(evento.getFechaFin());

        tvFechaEvento.setText(fechaEvento);

        if (!usuarioRegistrado()) checkAsist();

        botonEstoyEnEvento.setOnClickListener((View) -> {
            ProgressDialog.show(getContext(),
                    getString(R.string.fragment_detalle_evento_cargando),
                    getString(R.string.fragment_detalle_evento_obteniendo_localizacion), true, true);

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

                            Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_registrado), Snackbar.LENGTH_LONG).show();

                            realm.beginTransaction();
                            evento.setEstaRegistrado(true);
                            realm.commitTransaction();

                        } else if (response.body().errors[0].equals(getString(R.string.fragment_detalle_evento_error_fuera_de_rango))) {
                            Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_error_fuera_de_rango), Snackbar.LENGTH_LONG).show();
                        } else if (response.body().errors[0].equals(getString(R.string.fragment_detalle_evento_error_ya_registrado))) {
                            Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_error_ya_registrado), Snackbar.LENGTH_LONG).show();

                            realm.beginTransaction();
                            evento.setEstaRegistrado(true);
                            realm.commitTransaction();
                        }
                    } else {
                        Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_intenta_mas_tarde), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response<EventoResponse>> call, Throwable t) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(getString(R.string.fragment_detalle_evento_error_conexion));
                    builder.show();
                }
            });

        });

        botonMeInteresa.setOnClickListener((View) -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.fragment_detalle_evento_enviando_correo));
            progressDialog.setMessage(getString(R.string.fragment_detalle_evento_espera));
            progressDialog.show();

            Call<Response<Boolean>> enviarCorreo = eventoAPI.enviarCorreo(Sesion.getUsuario().getApiToken(), evento.getIdEvento());

            enviarCorreo.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_error_envio), 7000).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), getString(R.string.fragment_detalle_evento_mensaje_enviado), 7000).show();
                    MyApplication.contadorCorreosEventos.start();
                    progressDialog.dismiss();
                }
            });

        });

        return v;
    }

    public void checkAsist() {
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.fragment_detalle_evento_date_format));
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
            boolean enFecha = timeStampBegin < timeStampToday && timeStampToday < timeStampEnd;
            boolean despuesDeFecha = timeStampEnd < timeStampToday;

            System.err.println(antesDeFecha);
            System.err.println(timeStampBegin < timeStampToday);
            System.err.println(despuesDeFecha);


            if (antesDeFecha) {
                botonMeInteresa.setVisibility(View.VISIBLE);
            } else if (despuesDeFecha) {
                textViewEventoCaducado.setVisibility(View.VISIBLE);
            } else if (enFecha) {
                botonEstoyEnEvento.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pedirPermisos();
                } else {
                    try {
                        locationManager.requestSingleUpdate(criteria, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                setLatitud(location.getLatitude());
                                setLongitud(location.getLongitude());
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
                        }, null);

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        float zoomLevel = (float) 16.0;
        LatLng coordenadas = new LatLng(evento.getLatitud(), evento.getLongitud());
        googleMap.addMarker(new MarkerOptions().position(coordenadas).title(evento.getTitulo()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, zoomLevel));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapaEvento = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_evento);
        if (mapaEvento != null)
            getActivity().getFragmentManager().beginTransaction().remove(mapaEvento).commit();
    }

    private void pedirPermisos() {
        String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
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
                locationManager.requestSingleUpdate(criteria, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        setLatitud(location.getLatitude());
                        setLongitud(location.getLongitude());
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
                }, null);

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Snackbar.make(getActivity().findViewById(R.id.segunda_fragment_container), getString(R.string.fragment_detalle_evento_permiso_denegado), Snackbar.LENGTH_LONG).show();
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

    private boolean usuarioRegistrado() {
        if (evento.getEstaRegistrado()) {
            botonEstoyEnEvento.setVisibility(View.GONE);
            botonMeInteresa.setVisibility(View.GONE);
            textViewEventoCaducado.setVisibility(View.GONE);
            textViewYaHasSidoRegistrado.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

}
