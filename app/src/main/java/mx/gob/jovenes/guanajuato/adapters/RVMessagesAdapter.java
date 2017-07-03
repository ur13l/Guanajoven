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

/**
 * Created by codigus on 26/06/2017.
 */

public class RVMessagesAdapter extends RecyclerView.Adapter<RVMessagesAdapter.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessages;

    public RVMessagesAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_message, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        String msg = chatMessage.getMessage();
        holder.textViewMessage.setText(msg);

        int color = fetchColor(R.attr.colorPrimary);
        int gravity = Gravity.RIGHT;

        if (!chatMessage.isSendByMe()) {
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
        return chatMessages.size();
    }

    public void add(ChatMessage msg) {
        if (!chatMessages.contains(msg)) {
            chatMessages.add(msg);
            notifyDataSetChanged();
        }
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
