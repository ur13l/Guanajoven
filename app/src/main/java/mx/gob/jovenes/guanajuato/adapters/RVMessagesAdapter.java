package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.ChatMessage;
import mx.gob.jovenes.guanajuato.model.Mensaje;

/**
 * Created by codigus on 26/06/2017.
 */

public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.ViewHolder> {
    private Context context;
    private List<Mensaje> mensajes;

    public RVMessagesAdapter(Context context, List<Mensaje> mensajes) {
        this.context = context;
        this.mensajes = mensajes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_message, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
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
        /*if (!mensajes.contains(mensaje)) {
            mensajes.add(mensaje);
            notifyDataSetChanged();
        }*/
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewMessage = (TextView) itemView.findViewById(R.id.textview_message);
        }
    }
}
