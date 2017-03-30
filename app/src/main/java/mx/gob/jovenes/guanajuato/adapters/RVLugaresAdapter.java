package mx.gob.jovenes.guanajuato.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.LugarFragment;
import mx.gob.jovenes.guanajuato.model.Lugar;

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


    public void setFilter(List<Lugar> lista) {
        lugares = new ArrayList<>();
        lugares.addAll(lista);
        notifyDataSetChanged();
    }

    public static class LugaresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombreTV;


        LugaresViewHolder(View itemView) {
            super(itemView);
            nombreTV = (TextView) itemView.findViewById(R.id.tv_lugar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity a = (AppCompatActivity)view.getContext();
            FragmentManager fm = a.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LugarFragment lf = LugarFragment.newInstance(lugares.get(getAdapterPosition()));
            ft.replace(R.id.segunda_fragment_container, lf).addToBackStack(null).commit();
        }
    }
}
