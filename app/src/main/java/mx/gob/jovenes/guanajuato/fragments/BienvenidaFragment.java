package mx.gob.jovenes.guanajuato.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.activities.BienvenidaActivity;
import mx.gob.jovenes.guanajuato.activities.LoginActivity;

public class BienvenidaFragment extends Fragment {
    private Button btnComenzar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bienvenida, parent, false);
        btnComenzar = (Button) v.findViewById(R.id.btn_comenzar);

        btnComenzar.setOnClickListener(v1 -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(BienvenidaActivity.BIENVENIDA_KEY, true);
            editor.commit();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        return v;
    }
}
