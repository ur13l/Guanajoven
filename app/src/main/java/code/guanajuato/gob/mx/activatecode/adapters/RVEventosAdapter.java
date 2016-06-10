package code.guanajuato.gob.mx.activatecode.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tyczj.extendedcalendarview.Event;

import java.util.ArrayList;
import java.util.List;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by uriel on 21/05/16.
 */
public class RVEventosAdapter extends RecyclerView.Adapter<RVEventosAdapter.EventosViewHolder> {
    public static List<Event> eventos;
    public Event selectedEvent;

    public RVEventosAdapter(List<Event> eventos){
        this.eventos = eventos;
    }

    public interface LongClickListener{
        void onItemLongClick(int position);
    }

    @Override
    public EventosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_evento, parent, false);
        EventosViewHolder pvh = new EventosViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final EventosViewHolder holder, int position) {
        holder.tituloTv.setText(eventos.get(position).getTitle());
        holder.descripcionTv.setText(eventos.get(position).getDescription());
        holder.fechaTv.setText(eventos.get(position).getStartDate("dd/MM/yyyy hh:mm")
            + " - " + eventos.get(position).getEndDate("dd/MM/yyyy hh:mm"));
        holder.setLongClickListener(new LongClickListener(){

            @Override
            public void onItemLongClick(int position) {
                selectedEvent = eventos.get(position);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }



    public void setFilter(List<Event> evs) {
        eventos = new ArrayList<>();
        eventos.addAll(evs);
        notifyDataSetChanged();
    }


    public Event getSelectedEvent(){
        return selectedEvent;
    }

    public static class EventosViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener{
        TextView tituloTv;
        TextView descripcionTv;
        TextView fechaTv;
        LongClickListener longClickListener;

        EventosViewHolder(View itemView) {
            super(itemView);
            tituloTv = (TextView) itemView.findViewById(R.id.tv_titulo);
            descripcionTv = (TextView) itemView.findViewById(R.id.tv_descripcion);
            fechaTv = (TextView) itemView.findViewById(R.id.tv_fecha);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        public void setLongClickListener(LongClickListener lc){
            this.longClickListener = lc;
        }

        @Override
        public boolean onLongClick(View v) {
            this.longClickListener.onItemLongClick(getLayoutPosition());
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle(R.string.selecciona_accion);
            menu.add(0, v.getId(), 0, R.string.eliminar);//groupId, itemId, order, title

        }

    }

}
