package mx.gob.jovenes.guanajuato.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.model.Status;
import mx.gob.jovenes.guanajuato.model.TwitterResponse;

/**
 * Created by codigus on 26/05/2017.
 */

public class RVTweetAdapter extends RecyclerView.Adapter<RVTweetAdapter.TweetViewHolder>{
    private List<Status> statuses;
    private Context context;

    public RVTweetAdapter(Context context, List<Status> statuses) {
        this.context = context;
        this.statuses = statuses;
    }


    @Override
    public RVTweetAdapter.TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_tweet, parent, false);
        RVTweetAdapter.TweetViewHolder pvh  = new RVTweetAdapter.TweetViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        String name = statuses.get(position).getUser().getName();
        String screenName = statuses.get(position).getUser().getScreenName();
        String profileImageURL = statuses.get(position).getUser().getProfileImageUrl();

        Picasso.with(context).load(profileImageURL).into(holder.imagenUsuario);
        holder.tvUserName.setText(name);
        holder.tvScreenName.setText("@" + screenName);

        holder.tvTexto.setText(statuses.get(position).getText());
        holder.context = context;
        holder.url = "https://twitter.com/i/web/status/" + statuses.get(position).getIdStr();

    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenUsuario;
        TextView tvUserName;
        TextView tvScreenName;
        TextView tvTexto;
        String url;
        Context context;

        public TweetViewHolder(View itemView) {
            super(itemView);
            imagenUsuario = (ImageView) itemView.findViewById(R.id.imagen_usuario_tweet);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_nombre_usuario);
            tvScreenName = (TextView) itemView.findViewById(R.id.tv_screen_name_usuario);
            tvTexto = (TextView) itemView.findViewById(R.id.tv_texto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentTwitter = new Intent(Intent.ACTION_VIEW);
                    intentTwitter.setData(Uri.parse(url));
                    context.startActivity(intentTwitter);
                }
            });
        }

    }
}
