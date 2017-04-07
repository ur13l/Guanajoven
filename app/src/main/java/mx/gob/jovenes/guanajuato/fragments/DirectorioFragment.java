package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVLugaresAdapter;
import mx.gob.jovenes.guanajuato.model.Lugar;
import mx.gob.jovenes.guanajuato.persistencia.LugarDBHelper;

/**
 * Created by Uriel on 11/03/2016.
 */
public class DirectorioFragment extends CustomFragment implements SearchView.OnQueryTextListener{
    private ArrayList<Lugar> lugares;
    private RecyclerView rv;
    private RVLugaresAdapter adapter;
    private SearchView searchView;
    private LugarDBHelper dbHelper;
    private TextView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_directorio, parent, false);

        rv = (RecyclerView) v.findViewById(R.id.rv_lugares);
        emptyView = (TextView) v.findViewById(R.id.empty_view);

        setDatos();

        adapter = new RVLugaresAdapter(lugares);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        setHasOptionsMenu(true);
        return v;
    }

    public void setDatos() {
        dbHelper = new LugarDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());

        try {
            dbHelper.prepareDatabase();
            lugares = dbHelper.getLugares();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_directorio, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Lugar> filteredList = filter(lugares, query);
        adapter.setFilter(filteredList);
        rv.scrollToPosition(0);
        adapter.notifyDataSetChanged();

        if(filteredList.isEmpty()){
            rv.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else {
            rv.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        return false;
    }

    private List<Lugar> filter(List<Lugar> lugares, String query) {
        query = query.toLowerCase();

        final List<Lugar> filteredList = new ArrayList<>();
        for (Lugar l : lugares) {
            final String porNombre = l.getNombre().toLowerCase();
            if (porNombre.contains(query)) {
                filteredList.add(l);
            }
        }
        return filteredList;
    }
}
