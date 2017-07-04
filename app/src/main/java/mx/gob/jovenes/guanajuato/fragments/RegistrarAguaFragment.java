package mx.gob.jovenes.guanajuato.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import mx.gob.jovenes.guanajuato.R;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Agua;
import mx.gob.jovenes.guanajuato.model.Bitacora;
import mx.gob.jovenes.guanajuato.persistencia.AguaDBHelper;
import mx.gob.jovenes.guanajuato.persistencia.BitacoraDBHelper;
import mx.gob.jovenes.guanajuato.sesion.Sesion;
import mx.gob.jovenes.guanajuato.utils.MathFormat;

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
    private Sesion session;
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
        session = new Sesion(getActivity().getApplicationContext());
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
        String dialog_title = getResources().getString(R.string.agua);
        String dialog_message = getResources().getString(R.string.leyenda_hidratacion);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialog_title);
        builder.setMessage(dialog_message);

        String positiveText = "Aceptar";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

        return false;
    }

}