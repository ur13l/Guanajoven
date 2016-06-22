package code.guanajuato.gob.mx.activatecode.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.Lugar;

/**
 * Created by uriel on 21/06/16.
 */
public class LugarFragment extends Fragment implements OnMapReadyCallback{
    private Lugar lugar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lugar, parent, false);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        lugar = getLugar();
        LatLng latLng = new LatLng(lugar.getLatitud(),lugar.getLongitud());
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(lugar.getNombre()));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }


    public static LugarFragment newInstance(Lugar lugar){
        Bundle args = new Bundle();
        args.putInt("id_lugar", lugar.getId_lugar());
        args.putString("nombre", lugar.getNombre());
        args.putString("direccion", lugar.getDireccion());
        args.putString("colonia", lugar.getColonia());
        args.putString("municipio", lugar.getMunicipio());
        args.putInt("cp", lugar.getCp());
        args.putString("telefono", lugar.getTelefono());
        args.putString("tipo_instalacion", lugar.getTipo_instalacion());
        args.putString("administrador", lugar.getAdministrador());
        args.putString("email", lugar.getEmail());
        args.putFloat("latitud", lugar.getLatitud());
        args.putFloat("longitud", lugar.getLongitud());
        LugarFragment fragment = new LugarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Lugar getLugar(){
        Lugar lugar = new Lugar();
        Bundle args = getArguments();
        lugar.setId_lugar(args.getInt("id_lugar"));
        lugar.setNombre(args.getString("nombre"));
        lugar.setDireccion(args.getString("direccion"));
        lugar.setColonia(args.getString("colonia"));
        lugar.setMunicipio(args.getString("municipio"));
        lugar.setCp(args.getInt("cp"));
        lugar.setTelefono(args.getString("telefono"));
        lugar.setTipo_instalacion(args.getString("tipo_instalacion"));
        lugar.setAdministrador(args.getString("administrador"));
        lugar.setEmail(args.getString("email"));
        lugar.setLatitud(args.getFloat("latitud"));
        lugar.setLongitud(args.getFloat("longitud"));
        return lugar;
    }
}
