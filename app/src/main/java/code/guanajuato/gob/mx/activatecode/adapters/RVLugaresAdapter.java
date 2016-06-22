package code.guanajuato.gob.mx.activatecode.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.Lugar;

/**
 * Created by code on 21/06/16.
 */
public class RVLugaresAdapter extends RecyclerView.Adapter<RVLugaresAdapter.LugaresViewHolder> {
    public static List<Lugar> lugares;

    public RVLugaresAdapter(List<Lugar> lugares){
        this.lugares = lugares;
    }

    @Override
    public LugaresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_lugar, parent, false);
        LugaresViewHolder pvh = new LugaresViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final LugaresViewHolder holder, int position) {
        holder.nombreTV.setText(lugares.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return lugares.size();
    }


    public static class LugaresViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTV;


        LugaresViewHolder(View itemView) {
            super(itemView);
            nombreTV = (TextView) itemView.findViewById(R.id.tv_lugar);
        }
    }
}
