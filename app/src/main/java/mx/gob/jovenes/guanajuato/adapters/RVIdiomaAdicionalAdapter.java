package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.IdiomaAdicional;

/**
 * Created by codigus on 30/06/2017.
 */

public class RVIdiomaAdicionalAdapter extends RecyclerView.Adapter<RVIdiomaAdicionalAdapter.IdiomaAdicionalViewHolder> {
    private List<IdiomaAdicional> idiomas;
    private Context context;
    private ArrayList<IdiomaAdicional> seleccionados;

    public RVIdiomaAdicionalAdapter(Context context, List<IdiomaAdicional> idiomas) {
        this.context = context;
        this.idiomas = idiomas;
    }

    @Override
    public IdiomaAdicionalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_idioma_adicional, parent, false);
        seleccionados = new ArrayList<>();
        IdiomaAdicionalViewHolder idiomaAdicionalViewHolder = new IdiomaAdicionalViewHolder(v);
        return idiomaAdicionalViewHolder;
    }

    @Override
    public void onBindViewHolder(IdiomaAdicionalViewHolder holder, int position) {
        holder.checkBoxIdioma.setText(idiomas.get(position).getNombre());

        holder.checkBoxIdioma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seleccionados.add(idiomas.get(position));
                } else {
                    seleccionados.remove(idiomas.get(position));
                }
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return idiomas.size();
    }

    public ArrayList<IdiomaAdicional> getSeleccionados() {
        return this.seleccionados;
    }

    public class IdiomaAdicionalViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxIdioma;

        IdiomaAdicionalViewHolder(View itemView) {
            super(itemView);
            checkBoxIdioma = (CheckBox) itemView.findViewById(R.id.item_rv_idioma_adicional);
        }
    }
}
