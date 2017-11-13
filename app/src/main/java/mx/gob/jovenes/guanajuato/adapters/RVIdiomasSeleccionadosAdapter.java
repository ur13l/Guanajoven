package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.EditarDatosFragment;
import mx.gob.jovenes.guanajuato.model.DatosUsuarioIdioma;

public class RVIdiomasSeleccionadosAdapter extends RecyclerView.Adapter<RVIdiomasSeleccionadosAdapter.IdiomasSeleccionadosViewHolder> {
    private Context context;

    private static List<DatosUsuarioIdioma> idiomasSeleccionados;
    private Map<Integer, String> idiomas;

    public RVIdiomasSeleccionadosAdapter(Context context, List<DatosUsuarioIdioma> idiomasSeleccionados) {
        this.context = context;
        this.idiomasSeleccionados = idiomasSeleccionados;

        establecerIdiomas();
    }

    @Override
    public IdiomasSeleccionadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_idiomas_seleccionados, parent, false);
        IdiomasSeleccionadosViewHolder viewHolder = new IdiomasSeleccionadosViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IdiomasSeleccionadosViewHolder holder, int position) {
        holder.textViewNombreIdioma.setText(String.valueOf(idiomas.get(idiomasSeleccionados.get(position).getIdIdiomaAdicional())));
        holder.textViewPorcentajeConversacion.setText(String.valueOf(idiomasSeleccionados.get(position).getConversacion()));
        holder.textViewPorcentajeLectura.setText(String.valueOf(idiomasSeleccionados.get(position).getLectura()));
        holder.textViewPorcentajeEscritura.setText(String.valueOf(idiomasSeleccionados.get(position).getEscritura()));

        holder.btnEliminarIdioma.setOnClickListener((View) -> {
            idiomasSeleccionados.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, idiomasSeleccionados.size());

            if (idiomasSeleccionados.size() == 0) {
                EditarDatosFragment.textViewTituloIdiomasSeleccionados.setVisibility(View.GONE);
                EditarDatosFragment.layoutTablas.setVisibility(View.GONE);
                EditarDatosFragment.recyclerViewIdiomasSeleccionados.setVisibility(View.GONE);
            }

        });
    }

    @Override
    public int getItemCount() {
        return idiomasSeleccionados.size();
    }

    class IdiomasSeleccionadosViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNombreIdioma;
        private TextView textViewPorcentajeConversacion;
        private TextView textViewPorcentajeLectura;
        private TextView textViewPorcentajeEscritura;
        private ImageButton btnEliminarIdioma;

        IdiomasSeleccionadosViewHolder(View itemView) {
            super(itemView);
            textViewNombreIdioma = (TextView) itemView.findViewById(R.id.textview_nombre_idioma);
            textViewPorcentajeConversacion = (TextView) itemView.findViewById(R.id.textview_porcentaje_conversacion);
            textViewPorcentajeLectura = (TextView) itemView.findViewById(R.id.textview_porcentaje_lectura);
            textViewPorcentajeEscritura = (TextView) itemView.findViewById(R.id.textview_porcentaje_escritura);
            btnEliminarIdioma = (ImageButton) itemView.findViewById(R.id.btn_eliminar_idioma);
        }
    }

    private void establecerIdiomas() {
        idiomas = new HashMap<>();
        String[] arregloIdiomas = context.getResources().getStringArray(R.array.rv_idiomas_seleccionados_adapter_idiomas);

        for (int i = 0; i < arregloIdiomas.length; i++) {
            idiomas.put(i +1, arregloIdiomas[i]);
        }

    }
}
