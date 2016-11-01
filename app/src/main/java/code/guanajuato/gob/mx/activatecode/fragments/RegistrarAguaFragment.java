package code.guanajuato.gob.mx.activatecode.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.model.Agua;
import code.guanajuato.gob.mx.activatecode.model.Bitacora;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.persistencia.AguaDBHelper;
import code.guanajuato.gob.mx.activatecode.persistencia.BitacoraDBHelper;
import code.guanajuato.gob.mx.activatecode.utils.MathFormat;

/**
 * Autor: Uriel Infante
 * Fragment para registrar Agua.
 * Fecha: 27/05/2016
 */
public class RegistrarAguaFragment extends CustomFragment {
    public final static String AGUA_COLUMN = "registro_agua";

    private ArrayList<Agua> aguas;
    private AguaDBHelper dbHelper;
    private BitacoraDBHelper bitacoraDBHelper;
    private LinearLayout aguasView;
    private Button registrarBtn;
    private TextView aguaIndicator;
    private ProgressBar aguaPb;
    private float progress;
    private float currentProgress;
    private Login session;
    private Bitacora bitacora;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        //Variables que determinan el progreso de las barras.
        progress = 0;
        currentProgress = 0;

        //Se despliega la vista.
        View v = inflater.inflate(R.layout.fragment_registrar_agua, parent, false);
        aguasView = (LinearLayout)v.findViewById(R.id.view_aguas);
        dbHelper = new AguaDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e("DB", e.getMessage());
        }
        aguaPb = (ProgressBar) v.findViewById(R.id.progress_bar_agua);
        registrarBtn = (Button) v.findViewById(R.id.button_registrar_agua);
        aguaIndicator = (TextView) v.findViewById(R.id.agua_indicator);
        aguas = dbHelper.getAguaList();

        //Función para mostrar los elementos en la lista.
        for(final Agua agua : aguas){
            LinearLayout aguaView = (LinearLayout)inflater.inflate(R.layout.item_agua, parent, false);
            aguasView.addView(aguaView);
            TextView nombreAguaTV = (TextView)aguaView.getChildAt(0);
            TextView cantidadTV = (TextView) aguaView.getChildAt(1);
            CheckBox aguaChk = (CheckBox) aguaView.getChildAt(2);
            nombreAguaTV.setText(agua.getNombre());
            cantidadTV.setText((int)agua.getCantidad() + agua.getUnidad());
            aguaChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        currentProgress += agua.getCantidad();
                    }
                    else{
                        currentProgress -= agua.getCantidad();
                    }
                }
            });
        }

        //Se obtienen los datos de la bitácora y se asignan a los valores de las barras.
        session = new Login(getActivity().getApplicationContext());
        bitacoraDBHelper = new BitacoraDBHelper(getActivity(), getActivity().getFilesDir().getAbsolutePath());
        bitacora = new Bitacora(getActivity().getApplicationContext());
        progress = bitacora.getRegistrAgua();
        aguaPb.setProgress((int) progress);
        aguaIndicator.setText(MathFormat.removeDots(progress/1000));

        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress += currentProgress;
                aguaPb.setProgress((int) progress);
                aguaIndicator.setText(MathFormat.removeDots(progress/1000));
                for(int i = 0 ; i < aguasView.getChildCount(); i++){
                    ((CheckBox)((LinearLayout) aguasView.getChildAt(i)).getChildAt(2)).setChecked(false);
                }
                currentProgress = 0;
                bitacora.setRegistrAgua(progress);
                if(bitacora.getId() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(AGUA_COLUMN, progress);
                    bitacoraDBHelper. update(values, bitacora.getIdUser(), bitacora.getFecha().toString());
                }
                else{
                    bitacoraDBHelper.insert(bitacora);
                }
            }
        });
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_agua, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Snackbar snackbar = Snackbar.make(getView(), getResources().getString(R.string.leyenda_ejercicio), Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setAlpha(0.8f);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(7);
        snackbar.show();
        return false;
    }
}