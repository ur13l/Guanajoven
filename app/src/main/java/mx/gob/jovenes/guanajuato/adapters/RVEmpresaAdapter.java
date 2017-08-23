package mx.gob.jovenes.guanajuato.adapters;

import mx.gob.jovenes.guanajuato.fragments.PromocionFragment;
import mx.gob.jovenes.guanajuato.model.Empresa;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import android.content.Context;

import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by codigus on 17/07/2017.
 */

public class RVEmpresaAdapter extends RecyclerView.Adapter<RVEmpresaAdapter.EmpresasViewHolder>{
    private Context context;
    private Comparator<Empresa> comparator;
    private static final Comparator<Empresa> COMPARADOR_ALFABETICO = (o1, o2) -> o1.getEmpresa().compareTo(o2.getEmpresa());


    private final SortedList<Empresa> empresaSortedList = new SortedList<>(Empresa.class, new SortedList.Callback<Empresa>() {
       @Override
       public int compare(Empresa o1, Empresa o2) {
           return COMPARADOR_ALFABETICO.compare(o1, o2);
       }

       @Override
       public void onChanged(int position, int count) {
           notifyItemRangeChanged(position, count);
       }

       @Override
       public boolean areContentsTheSame(Empresa oldItem, Empresa newItem) {
           return oldItem.equals(newItem);
       }

       @Override
       public boolean areItemsTheSame(Empresa item1, Empresa item2) {
           return item1.getIdEmpresa() == item2.getIdEmpresa();
       }

       @Override
       public void onInserted(int position, int count) {
           notifyItemRangeInserted(position, count);
       }

       @Override
       public void onRemoved(int position, int count) {
           notifyItemRangeRemoved(position, count);
       }

       @Override
       public void onMoved(int fromPosition, int toPosition) {
           notifyItemRangeRemoved(fromPosition, toPosition);
       }
   });


    public RVEmpresaAdapter(Context context, Comparator<Empresa> comparator1) {
        this.context = context;
        this.comparator = comparator1;
    }

    public EmpresasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_empresa, parent, false);
        EmpresasViewHolder empresasViewHolder = new EmpresasViewHolder(view);
        return empresasViewHolder;
    }

    @Override
    public void onBindViewHolder(EmpresasViewHolder holder, int position) {
        Picasso.with(context).load(empresaSortedList.get(position).getLogo()).into(holder.imageViewLogoEmpresa);
        holder.textViewNombreEmpresa.setText(empresaSortedList.get(position).getEmpresa());
        holder.url = empresaSortedList.get(position).getUrlEmpresa();
    }

    public void add(Empresa empresa) {
        empresaSortedList.add(empresa);
    }

    public void remove(Empresa empresa) {
        empresaSortedList.remove(empresa);
    }

    public void add(List<Empresa> empresas1) {
        empresaSortedList.addAll(empresas1);
    }

    public void remove(List<Empresa> empresas1) {
        empresaSortedList.beginBatchedUpdates();
        for (Empresa empresa : empresas1) {
            empresaSortedList.remove(empresa);
        }
        empresaSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<Empresa> empresas1) {
        empresaSortedList.beginBatchedUpdates();
        for (int i = empresaSortedList.size() - 1; i >= 0; i--) {
            final Empresa empresa = empresaSortedList.get(i);
            if (!empresas1.contains(empresa)) {
                empresaSortedList.remove(empresa);
            }
        }
        empresaSortedList.addAll(empresas1);
        empresaSortedList.endBatchedUpdates();
    }

    public int getItemCount() {
        return empresaSortedList.size();
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
                    fragment.setEmpresa(empresaSortedList.get(getAdapterPosition()));
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
