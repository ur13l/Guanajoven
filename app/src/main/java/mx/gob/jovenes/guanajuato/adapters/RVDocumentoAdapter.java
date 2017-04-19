package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.Documento;

/**
 * Created by esva on 19/04/17.
 */

public class RVDocumentoAdapter  extends RecyclerView.Adapter<RVDocumentoAdapter.DocumentoViewHolder>{
    public static List<Documento> documentos;
    private Context context;

    /**
     * Constructor
     * @param context - establece el contexto que le pasa la activity
     * @param documentos - Lista de documentos
     */
    public RVDocumentoAdapter(Context context, List<Documento> documentos) {
        this.context = context;
        this.documentos = documentos;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RVDocumentoAdapter.DocumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_documento, parent, false);
        RVDocumentoAdapter.DocumentoViewHolder pvh = new RVDocumentoAdapter.DocumentoViewHolder(v);
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
    public void onBindViewHolder(RVDocumentoAdapter.DocumentoViewHolder holder, int position) {
        //TODO poner un getNombre en formato
        //holder.imagenFormato.setImageResource(documentos.get(position).getFormato());

        holder.tituloDocumento.setText(documentos.get(position).getTitulo());
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
        return documentos.size();
    }


    /**
     * Clase interna que define el holder que contiene los datos que le vamos a pasar a los
     * item de convocatorias
     */
    public static class DocumentoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenFormato;
        TextView tituloDocumento;

        /**
         * Constructor de clase interna
         * @param itemView - Le pasa un elemento de tipo itemView
         */
        DocumentoViewHolder(View itemView) {
            super(itemView);
            imagenFormato = (ImageView) itemView.findViewById(R.id.img_formato_documento);
            tituloDocumento = (TextView) itemView.findViewById(R.id.tv_titulo_documento);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Prueba","Si paso :D" + getAdapterPosition());
                }
            });
        }
    }
}
