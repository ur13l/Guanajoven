package mx.gob.jovenes.guanajuato.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jovenes.guanajuato.model.models_tmp.Imagen;

/**
 * Created by Uriel on 28/01/2016.
 */
public class PublicidadSingleton {
    private static PublicidadSingleton instance = null;
    private ArrayList<Imagen> lista;
    private Context context;

    public PublicidadSingleton(Context context) {
        lista = new ArrayList<Imagen>();
        this.context = context;
        cargarImagenes();

    }

    public static PublicidadSingleton getInstance(Context context){
        if(instance == null){
            instance = new PublicidadSingleton(context);
        }
        return instance;
    }

    public void cargarImagenes(){
        Gson gson = new Gson();
        String data = FileUtils.readFromFile(context);
        ArrayList<Imagen> array = gson.fromJson(data, new TypeToken<List<Imagen>>(){
        }.getType());
        if(array != null) {
            for (int i = 0; i < array.size(); i++) {
                lista.add(array.get(i));
            }
        }
    }

    public Imagen getAt(int pos){
        return lista.get(pos);
    }

    public int length(){
        return lista.size();
    }
}
