package mx.gob.jovenes.guanajuato.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;


import io.realm.Realm;
import io.realm.RealmResults;
import mx.gob.jovenes.guanajuato.R;
import mx.gob.jovenes.guanajuato.application.MyApplication;
import mx.gob.jovenes.guanajuato.model.Publicidad;
import mx.gob.jovenes.guanajuato.model.models_tmp.Imagen;

/**
 * Created by Uriel on 10/04/2017.
 */

public class ImageHandler {
    private static int INTERVALO_PUBLICIDAD = 10000;
    private static List<Publicidad> publicidad;
    private static ImageView ivContainer;
    private static Context context;
    private static Handler handlerPublicidad;
    private static Realm realm;

    //Código que ejecuta el handler para cambiar la publicidad cada 10 segundos
    private static Runnable handlerPublicidadTask =  new Runnable(){
        @Override
        public void run() {

            Random rand = new Random();
            if(publicidad.size() != 0) {
                final int randInt = rand.nextInt(publicidad.size());
                if (ivContainer != null) {

                    Picasso.with(context)
                            .load(publicidad.get(randInt).getRutaImagen())
                            .into(ivContainer);


                    ivContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(publicidad.get(randInt).getUrl()));
                            context.startActivity(intent);
                        }
                    });


                }
            }
            handlerPublicidad.postDelayed(handlerPublicidadTask, INTERVALO_PUBLICIDAD);
        }
    };


    /**
     * Arranque para el cambio de publicidad.
     */
    public static void startCambioPublicidadTask()
    {
        handlerPublicidadTask.run();
    }

    /**
     * Se detiene el cambio de publicidad.
     */
    public static void stopCambioPublicidadTask()
    {
        if(handlerPublicidad != null) {
            handlerPublicidad.removeCallbacks(handlerPublicidadTask);
        }
    }


    public static void start(ViewGroup panel, Context ctx) {
        realm = MyApplication.getRealmInstance();
        RealmResults<Publicidad> results = realm.where(Publicidad.class)
                .findAll();
        publicidad = realm.copyFromRealm(results);

        //publicidad.removeIf(p -> DateUtilities.stringToDate(p.getFechaFin()).before(new Date()));

        ivContainer = (ImageView) panel.findViewById(R.id.img_container);
        context = ctx;

        handlerPublicidad = new Handler();
        startCambioPublicidadTask();
    }
}
