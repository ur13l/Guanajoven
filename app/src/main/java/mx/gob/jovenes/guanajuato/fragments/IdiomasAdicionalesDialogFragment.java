package mx.gob.jovenes.guanajuato.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.adapters.RVIdiomaAdicionalAdapter;
import mx.gob.jovenes.guanajuato.model.DatosUsuarioIdioma;
import mx.gob.jovenes.guanajuato.model.IdiomaAdicional;

/**
 * Created by Juan on 25/05/2017.
 */

public class IdiomasAdicionalesDialogFragment extends DialogFragment {
    private RecyclerView recyclerViewIdiomas;
    private static List<IdiomaAdicional> idiomas;
    private static RVIdiomaAdicionalAdapter recyclerViewIdiomaAdicionalAdapter;
    private Button btnAceptar;
    private Button btnCancelar;
    public static List<DatosUsuarioIdioma> datosIdiomas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idiomas_adicionales, null);

        datosIdiomas = new ArrayList<>();
        idiomas = new ArrayList<>();

        recyclerViewIdiomas = (RecyclerView) v.findViewById(R.id.rv_idiomas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewIdiomas.setLayoutManager(linearLayoutManager);

        llenarLista();

        btnAceptar = (Button) v.findViewById(R.id.btn_aceptar_idiomas);
        btnCancelar = (Button) v.findViewById(R.id.btn_cancelar_idiomas);

        btnAceptar.setOnClickListener((View) -> {
            int idiomasSeleccionados = recyclerViewIdiomaAdicionalAdapter.getSeleccionados().size();

            for (int i = 0; i < idiomasSeleccionados; i++) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                IdiomasAdicionalesPorcentajesFragment porcentajesFragment = new IdiomasAdicionalesPorcentajesFragment();
                porcentajesFragment.setIdiomaAdicional(recyclerViewIdiomaAdicionalAdapter.getSeleccionados().get(i));
                porcentajesFragment.show(fragmentManager, null);
            }

            this.getDialog().dismiss();

        });

        btnCancelar.setOnClickListener((View) -> {
            this.dismiss();
        });

        return v;
    }

    private void llenarLista() {
        IdiomaAdicional[] arregloIdiomas = new IdiomaAdicional[]{
                new IdiomaAdicional(1, "Alemán"),
                new IdiomaAdicional(2, "Árabe"), new IdiomaAdicional(3, "Chino"),
                new IdiomaAdicional(4, "Coreano"), new IdiomaAdicional(5, "Fránces"),
                new IdiomaAdicional(6, "Inglés"), new IdiomaAdicional(7, "Italiano"),
                new IdiomaAdicional(8, "Japonés"), new IdiomaAdicional(9, "Polaco"),
                new IdiomaAdicional(10, "Portugués"), new IdiomaAdicional(11, "Ruso"),
                new IdiomaAdicional(12, "Otro")
        };

        idiomas = new ArrayList<>(Arrays.asList(arregloIdiomas));

        recyclerViewIdiomaAdicionalAdapter = new RVIdiomaAdicionalAdapter(getActivity(), idiomas);
        recyclerViewIdiomas.setAdapter(recyclerViewIdiomaAdicionalAdapter);
    }

    public static void insertarIdiomas(DatosUsuarioIdioma datosUsuarioIdioma) {
        datosIdiomas.add(datosUsuarioIdioma);
        for (int i = 0; i < datosIdiomas.size(); i++) {
            System.out.println(datosIdiomas.get(i).getIdDatosUsuario() + "-" + datosIdiomas.get(i).getIdIdiomaAdicional() + "-" +
                    datosIdiomas.get(i).getConversacion() + "-" + datosIdiomas.get(i).getLectura() + "-" + datosIdiomas.get(i).getEscritura() + "\n");
        }
    }

    public static int numeroDeIdiomas() {
        if (datosIdiomas == null) {
            return 0;
        } else {
            return datosIdiomas.size();
        }
    }

    public static void quitarElementosDeAdapter(List<DatosUsuarioIdioma> idiomasAdicionales) {
            for (int i = 0; i < idiomasAdicionales.size(); i++) {
                for (int j = 0; j < idiomas.size(); j++) {
                    if (idiomasAdicionales.get(i).getIdIdiomaAdicional() == idiomas.get(j).getIdIdiomaAdicional()) {
                        idiomas.remove(j);
                    }
                }
            }
            recyclerViewIdiomaAdicionalAdapter.notifyDataSetChanged();
        }

}
