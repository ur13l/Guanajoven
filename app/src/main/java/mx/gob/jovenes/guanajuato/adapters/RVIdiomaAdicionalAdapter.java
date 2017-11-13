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

public class RVIdiomaAdicionalAdapter extends RecyclerView.Adapter<RVIdiomaAdicionalAdapter.IdiomaAdicionalViewHolder> {
    private List<IdiomaAdicional> idiomas;
    private ArrayList<IdiomaAdicional> seleccionados;

    public RVIdiomaAdicionalAdapter(List<IdiomaAdicional> idiomas) {
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

        holder.checkBoxIdioma.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                seleccionados.add(idiomas.get(position));
            } else {
                seleccionados.remove(idiomas.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return idiomas.size();
    }

    public ArrayList<IdiomaAdicional> getSeleccionados() {
        return this.seleccionados;
    }

    class IdiomaAdicionalViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxIdioma;

        IdiomaAdicionalViewHolder(View itemView) {
            super(itemView);
            checkBoxIdioma = (CheckBox) itemView.findViewById(R.id.item_rv_idioma_adicional);
        }
    }
}
