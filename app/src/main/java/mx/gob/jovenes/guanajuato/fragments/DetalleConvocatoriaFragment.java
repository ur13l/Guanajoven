package mx.gob.jovenes.guanajuato.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVDocumentoAdapter;
import mx.gob.jovenes.guanajuato.api.ConvocatoriaAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class DetalleConvocatoriaFragment extends Fragment {
    private static String ID_CONVOCATORIA = "id_convocatoria";
    private Convocatoria convocatoria;
    private ImageView imgConvocatoria;
    private TextView tvDescripcionConvocatoria;
    private TextView tvFechaInicioConvocatoria;
    private TextView tvFechaCierreConvocatoria;
    private TextView tvEmptyDocumentos;
    private RecyclerView rvDocumentosConvocatoria;
    private RVDocumentoAdapter adapter;
    private Context context;
    private Realm realm;
    public static Button btnQuieroMasInformacion;
    private ConvocatoriaAPI convocatoriaAPI;
    private Retrofit retrofit;

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
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        convocatoriaAPI = retrofit.create(ConvocatoriaAPI.class);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalle_convocatoria, container, false);
        convocatoria = realm.where(Convocatoria.class).equalTo(getString(R.string.fragment_detalle_convocatoria_idconvocatoria), getArguments().getInt(ID_CONVOCATORIA)).findFirst();

        imgConvocatoria = (ImageView) v.findViewById(R.id.img_convocatoria);
        tvDescripcionConvocatoria = (TextView) v.findViewById(R.id.tv_descripcion_convocatoria);
        tvFechaInicioConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_inicio_convocatoria);
        tvFechaCierreConvocatoria = (TextView) v.findViewById(R.id.tv_fecha_cierre_convocatoria);
        tvEmptyDocumentos = (TextView) v.findViewById(R.id.tv_empty_documentos);
        rvDocumentosConvocatoria = (RecyclerView) v.findViewById(R.id.rv_documentos_convocatoria);
        btnQuieroMasInformacion = (Button) v.findViewById(R.id.btn_quiero_mas_informacion);
        adapter = new RVDocumentoAdapter(getActivity(), convocatoria.getDocumentos());

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvDocumentosConvocatoria.setLayoutManager(llm);

        Picasso.with(context)
                .load(convocatoria.getRutaImagen())
                .into(imgConvocatoria);

        tvDescripcionConvocatoria.setText(convocatoria.getDescripcion());

        String fechaInicio = getString(R.string.fragment_detalle_convocatoria_fecha_inicio) + DateUtilities.getFechaCast(convocatoria.getFechaInicio());
        String fechaFin = getString(R.string.fragment_detalle_convocatoria_fecha_fin) + DateUtilities.getFechaCast(convocatoria.getFechaCierre());

        tvFechaInicioConvocatoria.setText(fechaInicio);
        tvFechaCierreConvocatoria.setText(fechaFin);
        rvDocumentosConvocatoria.setAdapter(adapter);

        if( convocatoria.getDocumentos().size() == 0) {
            rvDocumentosConvocatoria.setVisibility(View.GONE);
            tvEmptyDocumentos.setVisibility(View.VISIBLE);
        }


        btnQuieroMasInformacion.setOnClickListener((View) -> {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.fragment_detalle_convocatoria_enviando_correo));
            progressDialog.setMessage(getString(R.string.fragment_detallle_convocatoria_espera));
            progressDialog.show();

            Call<Response<Boolean>> call = convocatoriaAPI.enviarCorreo(Sesion.getUsuario().getApiToken(), convocatoria.getIdConvocatoria());

            call.enqueue(new Callback<Response<Boolean>>() {
                @Override
                public void onResponse(Call<Response<Boolean>> call, retrofit2.Response<Response<Boolean>> response) {
                    Snackbar.make(getView(), getString(R.string.fragment_detalle_convocatoria_error_envio), 7000).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Response<Boolean>> call, Throwable t) {
                    Snackbar.make(getView(), getString(R.string.fragment_detalle_convocatoria_mensaje_enviado), 7000).show();
                    MyApplication.contadorCorreosConvocatorias.start();
                    progressDialog.dismiss();
                }
            });
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(convocatoria.getTitulo());
    }
}
