package code.guanajuato.gob.mx.activatecode.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.fragments.AlarmasActivacionFragment;
import code.guanajuato.gob.mx.activatecode.model.Alarma;
import code.guanajuato.gob.mx.activatecode.persistencia.AlarmasDBHelper;
import code.guanajuato.gob.mx.activatecode.receivers.AlarmasBroadcastReceiver;

/**
 * Created by Uriel on 19/02/2016.
 */
public class RVAlarmaAdapter extends RecyclerView.Adapter<RVAlarmaAdapter.AlarmaViewHolder> {
    public List<Alarma> alarma;
    public Alarma selectedAlarm;

    public RVAlarmaAdapter(List<Alarma> alarma){
        this.alarma = alarma;
    }

    @Override
    public AlarmaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_alarma, parent, false);
        AlarmaViewHolder pvh = new AlarmaViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(AlarmaViewHolder holder, final int position) {
        Alarma aa = alarma.get(position);
        holder.hora.setText(aa.getHora());
        holder.activo.setChecked(aa.isActivo());


        String dias = "";
        if(aa.isLunes()){
            dias += "Lun ";
            holder.lun.setChecked(true);
        }
        if(aa.isMartes()) {
            dias += "Mar ";
            holder.mar.setChecked(true);
        }
        if(aa.isMiercoles()) {
            dias += "Mie ";
            holder.mie.setChecked(true);
        }
        if(aa.isJueves()) {
            dias += "Jue ";
            holder.jue.setChecked(true);
        }
        if(aa.isViernes()) {
            holder.vie.setChecked(true);
            dias += "Vie ";
        }
        if(aa.isSabado()) {
            holder.sab.setChecked(true);
            dias += "Sab ";
        }
        if(aa.isDomingo()) {
            holder.dom.setChecked(true);
            dias += "Dom ";

        }
        holder.dias.setText(dias);
        holder.lun.setOnCheckedChangeListener(holder);
        holder.mar.setOnCheckedChangeListener(holder);
        holder.mie.setOnCheckedChangeListener(holder);
        holder.jue.setOnCheckedChangeListener(holder);
        holder.vie.setOnCheckedChangeListener(holder);
        holder.sab.setOnCheckedChangeListener(holder);
        holder.dom.setOnCheckedChangeListener(holder);
        holder.activo.setOnCheckedChangeListener(holder);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlarmasActivacionFragment.positionSel = position;
                return false;
            }
        });

    }

    public static String displayRepetir(int repetir) {
        return (repetir /60)+" hora(s)";
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return alarma.size();
    }


    public Alarma getItem(int pos){
        return alarma.get(pos);
    }


    public class AlarmaViewHolder extends RecyclerView.ViewHolder implements TimePickerDialog.OnTimeSetListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnCreateContextMenuListener {
        TextView hora;
        TextView dias;
        Switch activo;
        ToggleButton lun;
        ToggleButton mar;
        ToggleButton mie;
        ToggleButton jue;
        ToggleButton vie;
        ToggleButton sab;
        ToggleButton dom;
        AppCompatActivity a;
        AlarmasActivacionFragment f;
        Alarma aa;
        public int position;

        AlarmaViewHolder(View itemView) {
            super(itemView);
            hora = (TextView)itemView.findViewById(R.id.hora_tv);
            dias = (TextView)itemView.findViewById(R.id.dias_tv);
            activo = (Switch) itemView.findViewById(R.id.activo_sw);
            lun = (ToggleButton) itemView.findViewById(R.id.lunes_chk);
            mar = (ToggleButton) itemView.findViewById(R.id.martes_chk);
            mie = (ToggleButton) itemView.findViewById(R.id.miercoles_chk);
            jue = (ToggleButton) itemView.findViewById(R.id.jueves_chk);
            vie = (ToggleButton) itemView.findViewById(R.id.viernes_chk);
            sab = (ToggleButton) itemView.findViewById(R.id.sabado_chk);
            dom = (ToggleButton) itemView.findViewById(R.id.domingo_chk);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            View parent = (View)v.getParent().getParent().getParent();
            a = (AppCompatActivity)v.getContext();
            android.app.FragmentManager fm = a.getFragmentManager();
            hideDays((RecyclerView) v.getParent());
            v.findViewById(R.id.dias_ll).setVisibility(View.VISIBLE);
            v.findViewById(R.id.dias_tv).setVisibility(View.GONE);
            animateHeight(v.findViewById(R.id.dias_ll), 0, 100, 200);

            aa = alarma.get(getAdapterPosition());
            Calendar now = Calendar.getInstance();
            int hh = Integer.parseInt(hora.getText().toString().split(":")[0]);
            int mm = Integer.parseInt(hora.getText().toString().split(":")[1]);
            now.set(Calendar.HOUR_OF_DAY, hh);
            now.set(Calendar.MINUTE, mm);
            TimePickerDialog dpd = TimePickerDialog.newInstance(
                    AlarmaViewHolder.this,
                    hh,
                    mm,
                    false
            );
            f = (AlarmasActivacionFragment) a.getSupportFragmentManager().findFragmentById(R.id.segunda_fragment_container);
            dpd.show(fm, "Timepickerdialog");
            /**
            AlarmaDialogFragment dialogFragment = AlarmaDialogFragment.newInstance(alarma.get(this.getAdapterPosition()), this.getAdapterPosition());
            Fragment f = fm.findFragmentById(R.id.segunda_fragment_container);
            dialogFragment.setTargetFragment(f, Activity.RESULT_OK);
            dialogFragment.show(fm, "Info");
             */


        }

        public void hideDays(RecyclerView rv){
            for(int i = 0 ; i < rv.getChildCount(); i++){
                rv.getChildAt(i).findViewById(R.id.dias_ll).setVisibility(View.GONE);
                rv.getChildAt(i).findViewById(R.id.dias_tv).setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AlarmasActivacionFragment f;
            Alarma aa = alarma.get(this.getAdapterPosition());
            //Activity activity = (Activity)buttonView.getParent().getParent().getParent().getParent().getParent().getParent();
            AlarmasDBHelper dbHelper = new AlarmasDBHelper(
                    buttonView.getContext(), buttonView.getContext().getFilesDir().getAbsolutePath());
            ContentValues values = new ContentValues();

            try {
                dbHelper.prepareDatabase();
            } catch (IOException e) {
                Log.e("DB", e.getMessage());
            }

            switch (buttonView.getId()){
                case R.id.lunes_chk:
                    aa.setLunes(isChecked);
                    values.put("lunes", isChecked);
                    break;
                case R.id.martes_chk:
                    aa.setMartes(isChecked);
                    values.put("martes", isChecked);
                    break;
                case R.id.miercoles_chk:
                    aa.setMiercoles(isChecked);
                    values.put("miercoles", isChecked);
                    break;
                case R.id.jueves_chk:
                    aa.setJueves(isChecked);
                    values.put("jueves", isChecked);
                    break;
                case R.id.viernes_chk:
                    aa.setViernes(isChecked);
                    values.put("viernes", isChecked);
                    break;
                case R.id.sabado_chk:
                    aa.setSabado(isChecked);
                    values.put("sabado", isChecked);
                    break;
                case R.id.domingo_chk:
                    aa.setDomingo(isChecked);
                    values.put("domingo", isChecked);
                    break;
                case R.id.activo_sw:
                    aa.setActivo(isChecked);
                    values.put("estado", isChecked);
                    AlarmasBroadcastReceiver.cancelAlarmIfExists(buttonView.getContext().getApplicationContext(),
                            aa.getId(), new Intent(buttonView.getContext(),AlarmasBroadcastReceiver.class));
                    break;
            }
            setDaysOn(aa);


            if(aa.isActivo()) {
                AlarmasBroadcastReceiver.registerAlarm(buttonView.getContext().getApplicationContext(),
                        aa);
            }
            dbHelper.updateAlarma(values,aa.getId());

        }


        public void setDaysOn(Alarma aa){
            String diasStr = "";
            if(aa.isLunes()){
                diasStr += "Lun ";
            }
            if(aa.isMartes()) {
                diasStr += "Mar ";
            }
            if(aa.isMiercoles()) {
                diasStr += "Mie ";
            }
            if(aa.isJueves()) {
                diasStr += "Jue ";
            }
            if(aa.isViernes()) {
                diasStr += "Vie ";
            }
            if(aa.isSabado()) {
                diasStr += "Sab ";
            }
            if(aa.isDomingo()) {
                diasStr += "Dom ";

            }
            dias.setText(diasStr);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Seleccione una acción:");
            menu.add(0, 1, 0, "Eliminar");//groupId, itemId, order, title

        }


        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            Alarma alm = AlarmasActivacionFragment.setTime(a, hourOfDay, minute, aa, getAdapterPosition(), f);
            //alarma.add(alm);
            hora.setText(alm.getHora());
        }
    }

    public static final void animateHeight(final View view, int from, final int to, int duration) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        if(to != 0){
            view.setVisibility(View.VISIBLE);
        }
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);

            }

        });
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                if(to == 0){
                    view.setVisibility(View.GONE);
                }
            }
        });
        anim.setDuration(duration);
        anim.start();
    }



}
