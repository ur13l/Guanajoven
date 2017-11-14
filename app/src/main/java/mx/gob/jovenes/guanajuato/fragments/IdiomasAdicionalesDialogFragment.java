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
        String[] idiomasArray = getResources().getStringArray(R.array.fragment_dialog_idiomas_adicionales_array_idiomas);
        IdiomaAdicional[] arregloIdiomas = new IdiomaAdicional[idiomasArray.length];

        for (int i = 0; i < idiomasArray.length; i++) {
            arregloIdiomas[i] = new IdiomaAdicional(i + 1, idiomasArray[i]);
            System.err.println(idiomasArray[i]);
            System.err.println(arregloIdiomas.length);
        }

        idiomas = new ArrayList<>(Arrays.asList(arregloIdiomas));

        recyclerViewIdiomaAdicionalAdapter = new RVIdiomaAdicionalAdapter(idiomas);
        recyclerViewIdiomas.setAdapter(recyclerViewIdiomaAdicionalAdapter);
    }

    public static void insertarIdiomas(DatosUsuarioIdioma datosUsuarioIdioma) {
        datosIdiomas.add(datosUsuarioIdioma);
    }

    public static int numeroDeIdiomas() {
        return (datosIdiomas == null) ? 0 : datosIdiomas.size();
    }

}
