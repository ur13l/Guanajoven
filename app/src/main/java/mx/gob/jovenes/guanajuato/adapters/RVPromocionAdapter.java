package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.DetallePromocionFragment;
import mx.gob.jovenes.guanajuato.model.Empresa;
import mx.gob.jovenes.guanajuato.model.Promocion;

/**
 * Created by codigus on 17/07/2017.
 */

public class RVPromocionAdapter  extends RecyclerView.Adapter<RVPromocionAdapter.PromocionViewHolder> {
    private Context context;
    private List<Promocion> promociones;
    private Empresa empresa;

    public RVPromocionAdapter(Context context, List<Promocion> promociones, Empresa empresa) {
        this.context = context;
        this.promociones = promociones;
        this.empresa = empresa;
    }

    public PromocionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_promocion, parent, false);
        PromocionViewHolder pvh = new PromocionViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PromocionViewHolder holder, int position) {
        holder.textViewNombrePromocion.setText(promociones.get(position).getTitulo());
        holder.textViewFechaInicio.setText("Inicio: " + getFechaCast(promociones.get(position).getFechaInicio()));
        holder.textViewFechaFin.setText("Fin: " + getFechaCast(promociones.get(position).getFechaFin()));
    }

    public int getItemCount() {
        return promociones.size();
    }

    private String getFechaCast(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/MM/yyyy");

        try {
            String reformato = miFormato.format(formato.parse(fecha));
            return reformato;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class PromocionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombrePromocion;
        TextView textViewFechaInicio;
        TextView textViewFechaFin;

        PromocionViewHolder(View item) {
            super(item);
            textViewNombrePromocion = (TextView) item.findViewById(R.id.textview_nombre_promocion);
            textViewFechaInicio = (TextView) item.findViewById(R.id.textview_fechainicio_promocion);
            textViewFechaFin = (TextView) item.findViewById(R.id.textview_fechafin_promocion);

            item.setOnClickListener((View) -> {
                try {
                    DetallePromocionFragment fragment = new DetallePromocionFragment();
                    fragment.setEmpresa(empresa);
                    fragment.setPromocion(promociones.get(getAdapterPosition()));
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.segunda_fragment_container, fragment).addToBackStack(null).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
