package mx.gob.jovenes.guanajuato.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVDocumentoAdapter;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.Documento;

/**
 * Created by esva on 19/04/17.
 */

public class DetalleConvocatoriaFragment extends Fragment {
    private static String ID_CONVOCATORIA = "id_convocatoria";
    private Convocatoria convocatoria;
    private ImageView imgConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicioConvocatoria;
    private TextView tvFechaCierreConvocatoria;
    private TextView tvEmptyDocumentos;
    private RecyclerView rvDocumentosConvocatoria;
    private ArrayList<Documento> documentos;
    private RVDocumentoAdapter adapter;
    private Context context;
    private Realm realm;

    public static DetalleConvocatoriaFragment newInstance(int idConvocatoria) {
        DetalleConvocatoriaFragment detalleConvocatoriaFragment = new DetalleConvocatoriaFragment();

        //Guarda todos los datos del fragment anterior en una variable Bundle
        Bundle args = new Bundle();
        args.putInt(ID_CONVOCATORIA, idConvocatoria);
        detalleConvocatoriaFragment.setArguments(args);

        return detalleConvocatoriaFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        realm = MyApplication.getRealmInstance();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_convocatoria, container, false);
        Bundle args = getArguments();
       //convocatoria = args.getParcelable("convocatoria");
        convocatoria = realm.where(Convocatoria.class)
                .equalTo("idConvocatoria", getArguments().getInt(ID_CONVOCATORIA))
                .findFirst();

        imgConvocatoria = (ImageView) v.findViewById(R.id.img_convocatoria);
        tvDescripcionConvocatoria = (TextView) v.findViewById(R.id.tv_descripcion_convocatoria);
        tvFechaInicioConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_inicio_convocatoria);
        tvFechaCierreConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_cierre_convocatoria);
        tvEmptyDocumentos = (TextView) v.findViewById(R.id.tv_empty_documentos);
        rvDocumentosConvocatoria = (RecyclerView) v.findViewById(R.id.rv_documentos_convocatoria);
        adapter = new RVDocumentoAdapter(getActivity(), convocatoria.getDocumentos());

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);


        rvDocumentosConvocatoria.setLayoutManager(llm);


        Picasso.with(context)
                .load(convocatoria.getRutaImagen())
                .into(imgConvocatoria);

        tvDescripcionConvocatoria.setText(convocatoria.getDescripcion());


        tvFechaInicioConvocatoria.setText("Fecha inicio: " + getFechaCast(convocatoria.getFechaInicio()));
        tvFechaCierreConvocatoria.setText("Fecha cierre: " + getFechaCast(convocatoria.getFechaCierre()));
        rvDocumentosConvocatoria.setAdapter(adapter);

        if( convocatoria.getDocumentos().size() == 0) {
            rvDocumentosConvocatoria.setVisibility(View.GONE);
            tvEmptyDocumentos.setVisibility(View.VISIBLE);
        }


        return v;
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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(convocatoria.getTitulo());
    }
}
