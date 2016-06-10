package code.guanajuato.gob.mx.activatecode.utilities;

import android.content.res.TypedArray;

import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.model.Publicidad;

/**
 * Created by Uriel on 28/01/2016.
 */
public class PublicidadSingleton {
    private static PublicidadSingleton instance = null;
    private ArrayList<Publicidad> lista;

    public PublicidadSingleton(TypedArray array, String[] stringArray) {
        lista = new ArrayList<Publicidad>();
        cargarImagenes(array, stringArray);

    }

    public static PublicidadSingleton getInstance(TypedArray array, String[] stringArray){
        if(instance == null){
            instance = new PublicidadSingleton(array, stringArray);
        }
        return instance;
    }

    public void cargarImagenes(TypedArray array, String[] stringArray){
        for(int i = 0 ; i < array.length(); i++){
            Publicidad anuncio = new Publicidad();
            anuncio.setResourceId(array.getResourceId(i, 0));
            anuncio.setLink(stringArray[i]);
            lista.add(anuncio);
        }
    }

    public Publicidad getAt(int pos){
        return lista.get(pos);
    }

    public int length(){
        return lista.size();
    }
}
