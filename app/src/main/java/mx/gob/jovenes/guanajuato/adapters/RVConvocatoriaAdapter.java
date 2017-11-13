package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.DetalleConvocatoriaFragment;
import mx.gob.jovenes.guanajuato.model.Convocatoria;

public class RVConvocatoriaAdapter extends RecyclerView.Adapter<RVConvocatoriaAdapter.ConvocatoriaViewHolder> {
    public static List<Convocatoria> convocatorias;
    public Context context;

    public RVConvocatoriaAdapter(Context context, List<Convocatoria> convocatorias) {
        this.context = context;
        this.convocatorias = convocatorias;
    }

    @Override
    public ConvocatoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_convocatoria, parent, false);
        ConvocatoriaViewHolder pvh = new ConvocatoriaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ConvocatoriaViewHolder holder, int position) {
       Picasso.with(context).load(convocatorias.get(position).getRutaImagen()).into(holder.imagenConvocatoria);

        holder.tituloConvocatoria.setText(convocatorias.get(position).getTitulo());

        holder.descripcionConvocatoria.setText(convocatorias.get(position).getDescripcion());

        switch(convocatorias.get(position).getEstatus()){
            case 1:
                holder.estatusConvocatoria.setText("Activa");
                break;
            case 2:
                holder.estatusConvocatoria.setText("Terminada");
                break;
            default:
                holder.estatusConvocatoria.setText("Sin asignar");
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return convocatorias.size();
    }

    class ConvocatoriaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenConvocatoria;
        TextView tituloConvocatoria;
        TextView descripcionConvocatoria;
        TextView estatusConvocatoria;

        ConvocatoriaViewHolder(View itemView) {
            super(itemView);
            imagenConvocatoria = (ImageView) itemView.findViewById(R.id.img_convocatoria);
            tituloConvocatoria = (TextView) itemView.findViewById(R.id.tv_titulo_convocatoria);
            descripcionConvocatoria = (TextView) itemView.findViewById(R.id.tv_descripcion_convocatoria);
            estatusConvocatoria = (TextView) itemView.findViewById(R.id.tv_estatus_convocatoria);

            itemView.setOnClickListener(v -> {
                DetalleConvocatoriaFragment f = DetalleConvocatoriaFragment.newInstance(convocatorias.get(getAdapterPosition()).getIdConvocatoria());
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.segunda_fragment_container, f).addToBackStack(null).commit();
            });
        }
    }
}
