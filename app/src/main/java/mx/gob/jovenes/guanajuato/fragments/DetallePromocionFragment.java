package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Empresa;
import mx.gob.jovenes.guanajuato.model.Promocion;

/**
 * Created by codigus on 18/07/2017.
 */

public class DetallePromocionFragment extends Fragment {
    private ImageView imageViewLogoEmpresa;
    private TextView textViewTituloPromocion;
    private TextView textViewCodigoPromocion;
    private TextView textViewDescripcionPromocion;
    private TextView textViewBasesPromocion;
    private TextView textViewFechaInicio;
    private TextView textViewFechaFin;
    private Button buttonURL;

    private Promocion promocion;
    private Empresa empresa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_promocion, container, false);
        imageViewLogoEmpresa = (ImageView) view.findViewById(R.id.imageview_logo_empresa);
        textViewTituloPromocion = (TextView) view.findViewById(R.id.textview_titulo_promocion);
        textViewCodigoPromocion = (TextView) view.findViewById(R.id.textview_codigo_promocion);
        textViewDescripcionPromocion = (TextView) view.findViewById(R.id.textview_descripcion_promocion);
        textViewBasesPromocion = (TextView) view.findViewById(R.id.textview_bases_promocion);
        textViewFechaInicio = (TextView) view.findViewById(R.id.textview_fechainicio_promocion);
        textViewFechaFin = (TextView) view.findViewById(R.id.textview_fechafin_promocion);
        buttonURL = (Button) view.findViewById(R.id.button_url_promocion);

        Picasso.with(getContext()).load(empresa.getLogo()).into(imageViewLogoEmpresa);
        textViewTituloPromocion.setText(promocion.getTitulo());
        textViewCodigoPromocion.setText(promocion.getCodigoPromocion());
        textViewDescripcionPromocion.setText(promocion.getDescripcion());
        textViewBasesPromocion.setText(promocion.getBases());
        textViewFechaInicio.setText(getFechaCast(promocion.getFechaInicio()));
        textViewFechaFin.setText(getFechaCast(promocion.getFechaFin()));

        buttonURL.setOnClickListener((View) -> { enlace(promocion.getUrlPromocion()); });

        return view;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void setPromocion(Promocion promocion) {
        this.promocion = promocion;
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

    public void enlace(String link){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
}
