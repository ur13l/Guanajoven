package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import code.guanajuato.gob.mx.activatecode.R;

/**
 * Created by Uriel on 01/02/2016.
 */
public class ColaboradoresFragment extends CustomFragment {
    private Button btnColaboradores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_colaboradores, parent, false);
        btnColaboradores = (Button) v.findViewById(R.id.btn_colaboradores_code);
        btnColaboradores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirColaboradores();
            }
        });

        return v;
    }

    public void abrirColaboradores(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.codegto.gob.mx/index.php/directorio/"));
        startActivity(i);
    }
}
