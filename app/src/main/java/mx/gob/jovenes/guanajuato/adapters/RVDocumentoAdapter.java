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

public class RVDocumentoAdapter extends RecyclerView.Adapter<RVDocumentoAdapter.DocumentoViewHolder> {
    private Context context;
    private static List<Documento> documentos;

    public RVDocumentoAdapter(Context context, List<Documento> documentos) {
        this.context = context;
        this.documentos = documentos;
    }

    @Override
    public RVDocumentoAdapter.DocumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_documento, parent, false);
        RVDocumentoAdapter.DocumentoViewHolder pvh = new RVDocumentoAdapter.DocumentoViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVDocumentoAdapter.DocumentoViewHolder holder, int position) {
        int tipoDocumento = documentos.get(position).getFormato().getIdFormato();

        switch (tipoDocumento) {
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

    @Override
    public int getItemCount() {
        return documentos.size();
    }

    class DocumentoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenFormato;
        private TextView tituloDocumento;
        private String url;
        private Context context;

        DocumentoViewHolder(View itemView) {
            super(itemView);
            imagenFormato = (ImageView) itemView.findViewById(R.id.img_formato_documento);
            tituloDocumento = (TextView) itemView.findViewById(R.id.tv_titulo_documento);

            itemView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            });
        }
    }

}
