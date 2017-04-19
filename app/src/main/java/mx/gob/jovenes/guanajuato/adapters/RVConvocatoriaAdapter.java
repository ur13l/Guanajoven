package mx.gob.jovenes.guanajuato.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.DetalleConvocatoriaFragment;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.StatusReporte;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.MathFormat;


/**
 * Created by Juan José Estrada Valtierra on 12/04/17.
 */
public class RVConvocatoriaAdapter extends RecyclerView.Adapter<RVConvocatoriaAdapter.ConvocatoriaViewHolder> {
    public static List<Convocatoria> convocatorias;
    public Context context;

    /**
     * Constructor
     * @param context - establece el contexto que le pasa la activity
     * @param convocatorias - Lista de convocatorias
     */
    public RVConvocatoriaAdapter(Context context, List<Convocatoria> convocatorias) {
        this.context = context;
        this.convocatorias = convocatorias;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ConvocatoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_convocatoria, parent, false);
        ConvocatoriaViewHolder pvh = new ConvocatoriaViewHolder(v);
        return pvh;
    }

    /**
     * Método para establecer a partir de los datos , la vista de los cardView dentro de los items
     * @param holder - objeto creado de la clase interna ConvocatoriaViewHolder, permite establecer
     *                 la imagen de la convocatoria y los textos correspondientes
     * @param position - establece en que posicion se encuentra cada elemento para devolver sus
     *                   respectivos valores
     */
    @Override
    public void onBindViewHolder(ConvocatoriaViewHolder holder, int position) {
        /*manda a llamar la imagen con la biblioteca de Picasso
          with - establece el contexto en el que pertenece la imagen en este caso es a nuestra activity
          load - carga la imagen pasandole una url con un get que lo obtiene directamente del modelo
          into - establece en donde lo va a cargar, osea en que elemento pondra la imagen*/
       Picasso.with(context)
        .load(convocatorias.get(position).getRutaImagen())
        .into(holder.imagenConvocatoria);

        holder.tituloConvocatoria.setText(convocatorias.get(position).getTitulo());

        holder.descripcionConvocatoria.setText(convocatorias.get(position).getDescripcion());

        holder.fechasConvocatoria.setText(convocatorias.get(position).getFechaInicio() + " - " +
                                          convocatorias.get(position).getFechaCierre());
    }


    /**
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Método que cuenta los elementos de la lista
     * @return - tamaño de la lista
     */
    @Override
    public int getItemCount() {
        return convocatorias.size();
    }


    /**
     * Clase interna que define el holder que contiene los datos que le vamos a pasar a los
     * item de convocatorias
     */
    public class ConvocatoriaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenConvocatoria;
        TextView tituloConvocatoria;
        TextView descripcionConvocatoria;
        TextView fechasConvocatoria;

        /**
         * Constructor de clase interna
         * @param itemView - Le pasa un elemento de tipo itemView
         */
        ConvocatoriaViewHolder(View itemView) {
            super(itemView);
            imagenConvocatoria = (ImageView) itemView.findViewById(R.id.img_convocatoria);
            tituloConvocatoria = (TextView) itemView.findViewById(R.id.tv_titulo_convocatoria);
            descripcionConvocatoria = (TextView) itemView.findViewById(R.id.tv_descripcion_convocatoria);
            fechasConvocatoria = (TextView) itemView.findViewById(R.id.tv_fechas_convocatoria);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetalleConvocatoriaFragment f = DetalleConvocatoriaFragment.newInstance(
                            convocatorias.get(getAdapterPosition())
                    );
                    FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.segunda_fragment_container, f).commit();
                }
            });
        }
    }
}
