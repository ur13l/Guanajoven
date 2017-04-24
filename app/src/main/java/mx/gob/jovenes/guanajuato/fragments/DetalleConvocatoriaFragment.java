package mx.gob.jovenes.guanajuato.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVConvocatoriaAdapter;
import mx.gob.jovenes.guanajuato.adapters.RVDocumentoAdapter;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.Documento;
import retrofit2.Retrofit;

/**
 * Created by esva on 19/04/17.
 */

public class DetalleConvocatoriaFragment extends Fragment {
    private Convocatoria convocatoria;
    private ImageView imgConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicioConvocatoria;
    private TextView tvFechaCierreConvocatoria;
    private RecyclerView rvDocumentosConvocatoria;
    private ArrayList<Documento> documentos;
    private RVDocumentoAdapter adapter;
    private Context context;
    private SimpleDateFormat sdf;
    private String fecha1;
    private String fecha2;

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
        context = getActivity();
        /*sdf = new SimpleDateFormat("DD-MM-YYYY");
        try {
            fecha1 = sdf.parse(convocatoria.getFechaInicio()).toString();
            fecha2 = sdf.parse(convocatoria.getFechaCierre()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_convocatoria, container, false);
        Bundle args = getArguments();
        convocatoria = args.getParcelable("convocatoria");

        imgConvocatoria = (ImageView) v.findViewById(R.id.img_convocatoria);
        tvDescripcionConvocatoria = (TextView) v.findViewById(R.id.tv_descripcion_convocatoria);
        tvFechaInicioConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_inicio_convocatoria);
        tvFechaCierreConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_cierre_convocatoria);
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


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Prueba");

        return v;
    }

    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try{
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
