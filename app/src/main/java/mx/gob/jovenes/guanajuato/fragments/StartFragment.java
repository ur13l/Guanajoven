package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.SegundaActivity;

/**
 * Created by code on 6/03/17.
 */

public class StartFragment extends Fragment {

    //Declaración de variables (UI)
    private Button btnLogin;
    private Button btnRegistrar;
    private ImageButton botonHelp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnRegistrar = (Button) v.findViewById(R.id.btn_registrar);
        botonHelp = (ImageButton) v.findViewById(R.id.boton_ayuda);

        btnLogin.setOnClickListener((View) -> {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment f = new LoginFragment();
                ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
        });

        btnRegistrar.setOnClickListener((View) -> {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment f = new RegistrarFragment();
                ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
        });

        botonHelp.setOnClickListener((View) -> {
            try {
                Fragment fragment = new AyudaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Cerrar el teclado cuando se abra el fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        return v;
    }
}
