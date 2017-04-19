package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVConvocatoriaAdapter;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.Documento;
import retrofit2.Retrofit;

/**
 * Created by esva on 19/04/17.
 */

public class DetalleConvocatoriaFragment extends Fragment {
    private Convocatoria convocatoria;
    private Toolbar toolbar;
    private ImageView imgConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechasConvocatoria;
    private RecyclerView rvDocumentosConvocatoria;
    private ArrayList<Documento> documentos;

    AppCompatActivity activity;

    public static DetalleConvocatoriaFragment newInstance(Convocatoria convocatoria) {
        DetalleConvocatoriaFragment detalleConvocatoriaFragment = new DetalleConvocatoriaFragment();

        //Guarda todos los datos del fragment anterior en una variable Bundle
        Bundle args = new Bundle();
        args.putParcelable("convocatoria", convocatoria);
        /*
        args.putInt("idConvocatoria", convocatoria.getIdConvocatoria());
        args.putString("titulo", convocatoria.getTitulo());
        args.putString("descripcion", convocatoria.getDescripcion());
        args.putString("rutaImagen", convocatoria.getRutaImagen());
        args.putString("fechaInicio", convocatoria.getFechaInicio());
        args.putString("fechaCierre", convocatoria.getFechaCierre());
        args.putInt("estatus", convocatoria.getEstatus());
        args.putParcelableArrayList("documentos", convocatoria.getDocumentos());
        */
        detalleConvocatoriaFragment.setArguments(args);

        return detalleConvocatoriaFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_convocatoria, container, false);
        Bundle args = getArguments();
        convocatoria = args.getParcelable("convocatoria");

        toolbar = (Toolbar) v.findViewById(R.id.toolbar_titulo_convocatoria);
        imgConvocatoria = (ImageView) v.findViewById(R.id.img_convocatoria);
        tvDescripcionConvocatoria = (TextView) v.findViewById(R.id.tv_descripcion_convocatoria);
        tvFechasConvocatoria = (TextView) v.findViewById(R.id.tv_fechas_convocatoria);
        rvDocumentosConvocatoria = (RecyclerView) v.findViewById(R.id.rv_documentos_convocatoria);



        return v;
    }
}
