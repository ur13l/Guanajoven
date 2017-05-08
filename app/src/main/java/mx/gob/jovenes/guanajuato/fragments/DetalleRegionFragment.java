package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Region;

/**
 * Created by esva on 8/05/17.
 */

public class DetalleRegionFragment extends Fragment implements OnMapReadyCallback{
    private static String ID_REGION = "id_region";
    private Region region;
    private MapFragment mapaRegion;
    private TextView tvNombreRegion;
    private TextView tvDireccionRegion;
    private TextView tvResponsableRegion;
    private TextView tvDescripcionRegion;
    private Realm realm;

    public static DetalleRegionFragment newInstance(int idRegion) {
        DetalleRegionFragment detalleRegionFragment = new DetalleRegionFragment();
        Bundle args = new Bundle();
        args.putInt(ID_REGION, idRegion);//cambia el valor de la variable por el id de la region seleccionada
        detalleRegionFragment.setArguments(args);
        return detalleRegionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = MyApplication.getRealmInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_region, container, false);

        region = realm.where(Region.class).equalTo("idRegion", getArguments().getInt(ID_REGION)).findFirst();

        mapaRegion = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapa_region);
        mapaRegion.getMapAsync(this);

        tvNombreRegion = (TextView) v.findViewById(R.id.tv_nombre_region);
        tvDireccionRegion = (TextView) v.findViewById(R.id.tv_direccion_region);
        tvResponsableRegion = (TextView) v.findViewById(R.id.tv_responsable_region);
        tvDescripcionRegion = (TextView) v.findViewById(R.id.tv_descripcion_region);

        tvNombreRegion.setText(region.getNombre());
        tvDireccionRegion.setText(region.getDireccion());
        tvResponsableRegion.setText(region.getResponsable());
        tvDireccionRegion.setText(region.getDescripcion());

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coordenadas = new LatLng(region.getLatitud() , region.getLongitud());
        googleMap.addMarker(new MarkerOptions().position(coordenadas).title(region.getNombre()));
    }
}
