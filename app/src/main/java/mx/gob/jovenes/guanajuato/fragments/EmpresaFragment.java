package mx.gob.jovenes.guanajuato.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVEmpresaAdapter;
import mx.gob.jovenes.guanajuato.api.PromocionesAPI;
import mx.gob.jovenes.guanajuato.api.Response;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Empresa;
import mx.gob.jovenes.guanajuato.utils.DateUtilities;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by codigus on 17/07/2017.
 */

public class EmpresaFragment extends CustomFragment implements SearchView.OnQueryTextListener {
    private PromocionesAPI promocionesAPI;
    private RVEmpresaAdapter adapter;
    private TextView textViewEmptyEmpresas;
    private RecyclerView rvEmpresas;
    private Realm realm;
    private Retrofit retrofit;
    private List<Empresa> empresas;
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private CollapsingToolbarLayout cToolbar;
    private SwipeRefreshLayout swipeRefreshLayoutEmpresas;
    private SharedPreferences preferences;
    private static final Comparator<Empresa> COMPARADOR_ALFABETICO = (o1, o2) -> o1.getEmpresa().compareTo(o2.getEmpresa());


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = ((MyApplication) getActivity().getApplication()).getRetrofitInstance();
        promocionesAPI = retrofit.create(PromocionesAPI.class);
        realm = MyApplication.getRealmInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());

        activity = ((AppCompatActivity) getActivity());
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar2);
        cToolbar = (CollapsingToolbarLayout) activity.findViewById(R.id.collapsing_toolbar);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresas, container, false);
        swipeRefreshLayoutEmpresas = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout_empresas);
        rvEmpresas = (RecyclerView) view.findViewById(R.id.rv_empresas);
        textViewEmptyEmpresas = (TextView) view.findViewById(R.id.textview_empty_empresas);

        //actualiza la lista a partir de los datos en realm
        //actualizarLista();

        //añade mas elementos a partir del servicio
        //primeraLlamada();

        Call<Response<ArrayList<Empresa>>> call = promocionesAPI.getEmpresas(preferences.getString(MyApplication.LAST_UPDATE_EMPRESAS, "0000-00-00 00:00:00"));

        call.enqueue(new Callback<Response<ArrayList<Empresa>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Empresa>>> call, retrofit2.Response<Response<ArrayList<Empresa>>> response) {
                empresas = response.body().data;

                adapter = new RVEmpresaAdapter(getContext());
                adapter.add(empresas);

                StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvEmpresas.setAdapter(adapter);
                rvEmpresas.setLayoutManager(slm);

            }

            @Override
            public void onFailure(Call<Response<ArrayList<Empresa>>> call, Throwable t) {

            }
        });

        /*if (noHayDatosEnRealm()) {
            textViewEmptyEmpresas.setVisibility(View.VISIBLE);
        }*/

        swipeRefreshLayoutEmpresas.setOnRefreshListener(() -> primeraLlamada());

        return view;
    }

    private void primeraLlamada() {
        swipeRefreshLayoutEmpresas.setRefreshing(false);

        Call<Response<ArrayList<Empresa>>> call = promocionesAPI.getEmpresas(preferences.getString(MyApplication.LAST_UPDATE_EMPRESAS, "0000-00-00 00:00:00"));

        call.enqueue(new Callback<Response<ArrayList<Empresa>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Empresa>>> call, retrofit2.Response<Response<ArrayList<Empresa>>> response) {

                empresas = response.body().data;

                if (empresas != null) {
                    adapter.add(empresas);
                }

                StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvEmpresas.setAdapter(adapter);
                rvEmpresas.setLayoutManager(slm);

                /*if (response.body().success) {
                    List<Empresa> emp = response.body().data;

                    realm.beginTransaction();

                    for (Empresa empresa : emp) {
                        if (empresa.getDeletedAt() != null) {
                            Empresa e = realm.where(Empresa.class).equalTo("idEmpresa", empresa.getIdEmpresa()).findFirst();

                            if (e != null) {
                                e.deleteFromRealm();
                            }

                        } else {
                            realm.copyToRealmOrUpdate(empresa);
                        }
                    }

                    realm.commitTransaction();

                    if (emp.size() > 0) actualizarLista();

                    String lasUpdate = DateUtilities.dateToString(new Date());
                    preferences.edit().putString(MyApplication.LAST_UPDATE_EMPRESAS, lasUpdate).apply();

                }*/
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Empresa>>> call, Throwable t) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.create();
                b.setMessage("Error, intenta mas tarde");
                b.show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_empresas, menu);

        final MenuItem searchItem = menu.findItem(R.id.item_buscar_empresas);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
    }

    private void actualizarLista() {
        RealmResults<Empresa> result = realm.where(Empresa.class).findAll();

        empresas = realm.copyFromRealm(result);
        adapter = new RVEmpresaAdapter(getContext());
        StaggeredGridLayoutManager slm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvEmpresas.setAdapter(adapter);
        rvEmpresas.setLayoutManager(slm);

    }

    public boolean noHayDatosEnRealm() {
        return (realm.where(Empresa.class).findAll().isEmpty());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals("")) {
            final List<Empresa> filtro = filtro(empresas, newText);
            adapter.replaceAll(filtro);
            rvEmpresas.scrollToPosition(0);
        } else {
            primeraLlamada();
        }
        return true;
    }

    private List<Empresa> filtro(List<Empresa> empresas1, String query) {
        final String lowerCase = query.toLowerCase();
        final List<Empresa> filtro = new ArrayList<>();
        for (Empresa empresa : empresas1) {
            final String texto = empresa.getNombreComercial().toLowerCase();
            if (texto.contains(lowerCase)) {
                filtro.add(empresa);
            }
        }
        return filtro;
    }


}
