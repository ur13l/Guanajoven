package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Mensaje;

public class RVMensajeAdapter extends RecyclerView.Adapter<RVMensajeAdapter.ViewHolder> {
    private Context context;
    private List<Mensaje> mensajes;

    public RVMensajeAdapter(Context context, List<Mensaje> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);

        String msg = mensaje.getMensaje();
        holder.textViewMessage.setText(msg);

        int color = fetchColor(R.attr.colorPrimary);
        int gravity = Gravity.RIGHT;

        if (mensaje.isEnviaUsuario() == 0) {
            color = fetchColor(R.attr.colorAccent);
            gravity = Gravity.LEFT;
        }

        holder.textViewMessage.setBackgroundColor(color);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textViewMessage.getLayoutParams();
        params.gravity = gravity;
        holder.textViewMessage.setLayoutParams(params);

    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void notifyData(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        notifyDataSetChanged();
    }

    public void add(Mensaje mensaje) {
        mensajes.add(mensaje);
        notifyDataSetChanged();
    }

    private int fetchColor(int color) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] {color});
        int returnColor = a.getColor(0,0);
        a.recycle();
        return  returnColor;
    }

    public void agregarMensajes(List<Mensaje> mensajes) {
        this.mensajes.addAll(mensajes);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMessage;

        ViewHolder(View itemView) {
            super(itemView);
            textViewMessage = (TextView) itemView.findViewById(R.id.textview_message);
        }
    }
}
