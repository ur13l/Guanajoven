package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itextpdf.text.Image;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.fragments.DetalleEventoFragment;
import mx.gob.jovenes.guanajuato.fragments.DetalleRegionFragment;
import mx.gob.jovenes.guanajuato.model.Evento;

/**
 * Created by uriel on 21/05/16.
 */
public class RVEventosAdapter extends RecyclerView.Adapter<RVEventosAdapter.EventosViewHolder> {
    private List<Evento> eventos;
    private Context context;
    private Realm realm;

    public RVEventosAdapter(Context context, List<Evento> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public EventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_evento, parent, false);
        EventosViewHolder pvh = new EventosViewHolder(v);

        realm = Realm.getDefaultInstance();
        return pvh;
    }

    @Override
    public void onBindViewHolder(EventosViewHolder holder, int position) {
        holder.tituloTv.setText(eventos.get(position).getTitulo());
        holder.descripcionTv.setText(eventos.get(position).getDescripcion());
        holder.fechaTv.setText("Inicio: " + getFechaCast(eventos.get(position).getFechaInicio()));

        verificarFecha(holder, eventos.get(position));

        holder.imageButtonEliminarEvento.setOnClickListener((View) -> {
            AlertDialog.Builder mensaje = new AlertDialog.Builder(context);
            mensaje.create();

            mensaje.setMessage("¿Estás seguro de eliminar este elemento?");

            mensaje.setPositiveButton("Aceptar", ((dialog, which) -> {
                realm.beginTransaction();
                Evento evento = realm.where(Evento.class).equalTo("idEvento", eventos.get(position).getIdEvento()).findFirst();
                evento.deleteFromRealm();
                eventos.remove(position);
                notifyDataSetChanged();
                realm.commitTransaction();
            }));

            mensaje.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            mensaje.show();

        });

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

    public void verificarFecha(EventosViewHolder holder, Evento evento) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInStringbegin = evento.getFechaInicio();
        String dateInStringend = evento.getFechaFin();
        String dateInStringToday = formatter.format(new Date());

        try {
            Date fechainicio = formatter.parse(dateInStringbegin);
            Date fechafin = formatter.parse(dateInStringend);
            Date today = formatter.parse(dateInStringToday);

            long timeStampBegin = fechainicio.getTime();
            long timeStampEnd = fechafin.getTime();
            long timeStampToday = today.getTime();

            boolean antesDeFecha = timeStampBegin > timeStampToday;
            boolean enFecha = timeStampBegin < timeStampToday && timeStampToday > timeStampEnd;
            boolean despuesDeFecha = timeStampEnd < timeStampToday;

            if (enFecha) {
                holder.estado.setText("Evento abierto");
                holder.imageButtonEliminarEvento.setVisibility(View.GONE);
            } else if (despuesDeFecha) {
                holder.estado.setText("Evento cerrado");
                holder.imageButtonEliminarEvento.setVisibility(View.VISIBLE);
            } else if (antesDeFecha) {
                holder.estado.setText("Evento próximo");
                holder.imageButtonEliminarEvento.setVisibility(View.GONE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class EventosViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTv;
        TextView descripcionTv;
        TextView fechaTv;
        TextView estado;
        ImageButton imageButtonEliminarEvento;

        EventosViewHolder(View itemView) {
            super(itemView);
            tituloTv = (TextView) itemView.findViewById(R.id.tv_titulo);
            descripcionTv = (TextView) itemView.findViewById(R.id.tv_descripcion);
            fechaTv = (TextView) itemView.findViewById(R.id.tv_fecha);
            estado = (TextView) itemView.findViewById(R.id.estado);
            imageButtonEliminarEvento = (ImageButton) itemView.findViewById(R.id.imagebutton_eliminar_evento);

            itemView.setOnClickListener((View) -> {
                DetalleEventoFragment f = DetalleEventoFragment.newInstance(eventos.get(getAdapterPosition()).getIdEvento());
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.segunda_fragment_container, f).addToBackStack(null).commit();
            });
        }

    }

}
