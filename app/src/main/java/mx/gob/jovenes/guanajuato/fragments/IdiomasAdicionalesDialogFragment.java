package mx.gob.jovenes.guanajuato.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import mx.gob.jovenes.guanajuato.R;

/**
 * Created by Uriel on 25/05/2017.
 */

public class IdiomasAdicionalesDialogFragment  extends DialogFragment {
    CheckBox checkBoxAleman;
    CheckBox checkBoxArabe;
    CheckBox checkBoxChino;
    CheckBox checkBoxCoreano;
    CheckBox checkBoxFrances;
    CheckBox checkBoxIngles;
    CheckBox checkBoxItaliano;
    CheckBox checkBoxJapones;
    CheckBox checkBoxPolaco;
    CheckBox checkBoxPortugues;
    CheckBox checkBoxRuso;
    CheckBox checkBoxOtro;
    Button btnAceptar;
    Button btnCancelar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idiomas_adicionales, null);

        checkBoxAleman = (CheckBox) v.findViewById(R.id.checkbox_aleman);
        checkBoxArabe = (CheckBox) v.findViewById(R.id.checkbox_arabe);
        checkBoxChino = (CheckBox) v.findViewById(R.id.checkbox_chino);
        checkBoxCoreano = (CheckBox) v.findViewById(R.id.checkbox_coreano);
        checkBoxFrances = (CheckBox) v.findViewById(R.id.checkbox_frances);
        checkBoxIngles = (CheckBox) v.findViewById(R.id.checkbox_ingles);
        checkBoxItaliano = (CheckBox) v.findViewById(R.id.checkbox_italiano);
        checkBoxJapones = (CheckBox) v.findViewById(R.id.checkbox_japones);
        checkBoxPolaco = (CheckBox) v.findViewById(R.id.checkbox_polaco);
        checkBoxPortugues = (CheckBox) v.findViewById(R.id.checkbox_portugues);
        checkBoxRuso = (CheckBox) v.findViewById(R.id.checkbox_ruso);
        checkBoxOtro = (CheckBox) v.findViewById(R.id.checkbox_otro);

        btnAceptar = (Button) v.findViewById(R.id.btn_aceptar_idiomas);
        btnCancelar = (Button) v.findViewById(R.id.btn_cancelar_idiomas);



        btnCancelar.setOnClickListener((View) -> {
            this.dismiss();
        });

        return v;
    }
}
