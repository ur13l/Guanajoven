package mx.gob.jovenes.guanajuato.adapters;

import mx.gob.jovenes.guanajuato.fragments.PromocionFragment;
import mx.gob.jovenes.guanajuato.model.Empresa;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by codigus on 17/07/2017.
 */

public class RVEmpresaAdapter extends RecyclerView.Adapter<RVEmpresaAdapter.EmpresasViewHolder>{
    private Context context;
    private List<Empresa> empresas;

    public RVEmpresaAdapter(Context context, List<Empresa> empresas) {
        this.context = context;
        this.empresas = empresas;
    }

    public EmpresasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_empresa, parent, false);
        EmpresasViewHolder empresasViewHolder = new EmpresasViewHolder(view);
        return empresasViewHolder;
    }

    @Override
    public void onBindViewHolder(EmpresasViewHolder holder, int position) {
        Picasso.with(context).load(empresas.get(position).getLogo()).into(holder.imageViewLogoEmpresa);
        holder.textViewNombreEmpresa.setText(empresas.get(position).getEmpresa());
        holder.url = empresas.get(position).getUrlEmpresa();
    }

    public int getItemCount() {
        return empresas.size();
    }

    public class EmpresasViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewLogoEmpresa;
        TextView textViewNombreEmpresa;
        TextView textViewUrlEmpresa;
        String url;

        EmpresasViewHolder(View item) {
            super(item);

            imageViewLogoEmpresa = (ImageView) item.findViewById(R.id.imageview_logo_empresa);
            textViewNombreEmpresa = (TextView) item.findViewById(R.id.textview_nombre_empresa);
            textViewUrlEmpresa = (TextView) item.findViewById(R.id.textview_url_empresa);

            textViewUrlEmpresa.setOnClickListener((View) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            });

            item.setOnClickListener((View) -> {
                try {
                    PromocionFragment fragment = new PromocionFragment();
                    fragment.setEmpresa(empresas.get(getAdapterPosition()));
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
