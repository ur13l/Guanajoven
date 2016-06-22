package code.guanajuato.gob.mx.activatecode.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.StatusReporte;

/**
 * Created by code on 21/06/16.
 */
public class RVReporteAdapter extends RecyclerView.Adapter<RVReporteAdapter.ReporteViewHolder> {
    public static List<StatusReporte> reportes;

    public RVReporteAdapter(List<StatusReporte> reportes){
        this.reportes = reportes;
    }

    @Override
    public ReporteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_reporte, parent, false);
        ReporteViewHolder pvh = new ReporteViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final ReporteViewHolder holder, int position) {
        holder.fechaTv.setText(reportes.get(position).getFecha().toString());
        if (reportes.get(position).isEjercicio()){
            holder.ejercicioImg.setImageResource(R.drawable.feliz);
        }
        else{
            holder.ejercicioImg.setImageResource(R.drawable.triste);
        }

        if (reportes.get(position).isAgua()){
            holder.aguaImg.setImageResource(R.drawable.feliz);
        }
        else{
            holder.aguaImg.setImageResource(R.drawable.triste);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return reportes.size();
    }


    public static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView fechaTv;
        ImageView aguaImg;
        ImageView ejercicioImg;


        ReporteViewHolder(View itemView) {
            super(itemView);
            fechaTv = (TextView) itemView.findViewById(R.id.tv_fecha);
            aguaImg = (ImageView) itemView.findViewById(R.id.img_agua);
            ejercicioImg = (ImageView) itemView.findViewById(R.id.img_ejercicio);

        }


    }
}
