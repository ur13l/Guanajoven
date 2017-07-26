package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.Realm;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Notificacion;

/**
 * Created by codigus on 17/5/2017.
 */

public class RVNotificacionAdapter extends RecyclerView.Adapter<RVNotificacionAdapter.NotificacionViewHolder>{
    private List<Notificacion> notificaciones;
    private Context contexto;
    private Realm realm;

    public RVNotificacionAdapter(Context context, List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
        this.contexto = context;
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_notificacion, parent, false);
        NotificacionViewHolder nvh = new NotificacionViewHolder(v);

        realm = Realm.getDefaultInstance();

        return nvh;
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder holder, int position) {
        holder.tvNombreNotificacion.setText(notificaciones.get(position).getTitulo());
        holder.tvMensajeNotificacion.setText(notificaciones.get(position).getMensaje());
        holder.tvFechaNotificacion.setText(notificaciones.get(position).getFechaEmision());

        holder.imageViewEliminarNotificacion.setOnClickListener((View) -> {
            AlertDialog.Builder mensaje = new AlertDialog.Builder(contexto);

            mensaje.setMessage("¿Estás seguro de eliminar este elemento?");

            mensaje.setPositiveButton("Aceptar", (dialog, which) -> {
                realm.beginTransaction();
                Notificacion notificacion = realm.where(Notificacion.class).equalTo("idNotificacion", notificaciones.get(position).getIdNotificacion()).findFirst();
                notificacion.deleteFromRealm();
                notificaciones.remove(position);
                notifyDataSetChanged();
                realm.commitTransaction();
            });

            mensaje.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            mensaje.show();

        });
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreNotificacion;
        TextView tvMensajeNotificacion;
        TextView tvFechaNotificacion;
        ImageButton imageViewEliminarNotificacion;

        NotificacionViewHolder(View itemView) {
            super(itemView);
            tvNombreNotificacion = (TextView) itemView.findViewById(R.id.tv_nombre_notificacion);
            tvMensajeNotificacion = (TextView) itemView.findViewById(R.id.tv_mensaje_notificacion);
            tvFechaNotificacion = (TextView) itemView.findViewById(R.id.tv_fecha_notificacion);
            imageViewEliminarNotificacion = (ImageButton) itemView.findViewById(R.id.imagebutton_eliminar_notificacion);
        }
    }
}
