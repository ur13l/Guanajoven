package mx.gob.jovenes.guanajuato.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by code on 6/03/17.
 */

public class StartFragment extends Fragment {

    //Declaración de variables (UI)
    private Button btnLogin;
    private Button btnRegistrar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        btnLogin = (Button) v.findViewById(R.id.btn_login);
        btnRegistrar = (Button) v.findViewById(R.id.btn_registrar);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment f = new LoginFragment();
                ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment f = new RegistrarFragment();
                ft.replace(R.id.login_fragment_container, f).addToBackStack(null).commit();
            }
        });

        return v;
    }
}
