package mx.gob.jovenes.guanajuato.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.StatusReporte;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import mx.gob.jovenes.guanajuato.utils.MathFormat;


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
        Calendar date = DateUtilities.dateToCalendar(reportes.get(position).getFecha());
        String fechaCorrecta = date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.MONTH) +"/" + date.get(Calendar.YEAR);
        holder.fechaTv.setText(fechaCorrecta);
        holder.ejercicioTv.setText(MathFormat.removeDots((float) MathFormat.round(reportes.get(position).getEjercicioMin(),2)) + " mins.");
        holder.aguaTv.setText(MathFormat.removeDots((float) MathFormat.round(reportes.get(position).getAguaLt() / 1000, 2)) + " lts.");
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
        TextView ejercicioTv;
        TextView aguaTv;

        ReporteViewHolder(View itemView) {
            super(itemView);
            fechaTv = (TextView) itemView.findViewById(R.id.tv_fecha);
            aguaImg = (ImageView) itemView.findViewById(R.id.img_agua);
            ejercicioImg = (ImageView) itemView.findViewById(R.id.img_ejercicio);
            ejercicioTv = (TextView) itemView.findViewById(R.id.tv_ejercicio);
            aguaTv = (TextView) itemView.findViewById(R.id.tv_agua);

        }


    }
}
