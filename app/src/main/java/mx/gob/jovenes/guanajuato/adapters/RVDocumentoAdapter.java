package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Documento;


/**
 * Created by esva on 19/04/17.
 */

public class RVDocumentoAdapter  extends RecyclerView.Adapter<RVDocumentoAdapter.DocumentoViewHolder>{
    public static List<Documento> documentos;
    public Context context;
    public String url;

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
        System.out.println(getItemCount());
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
        int tipoDocumento = documentos.get(position).getFormato().getIdFormato();

        switch(tipoDocumento) {
            case 1:
                holder.imagenFormato.setImageResource(R.mipmap.ic_pdf);
                break;
            case 2:
                holder.imagenFormato.setImageResource(R.mipmap.ic_doc);
                break;
            case 3:
                holder.imagenFormato.setImageResource(R.mipmap.ic_xls);
                break;
            default:
                holder.imagenFormato.setImageResource(R.mipmap.ic_unknow);
                break;
        }

        holder.tituloDocumento.setText(documentos.get(position).getTitulo());
        holder.url = documentos.get(position).getRutaDocumento();
        holder.context = context;
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
        String url;
        Context context;
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
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }
}
