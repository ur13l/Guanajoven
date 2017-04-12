package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Convocatoria;
import mx.gob.jovenes.guanajuato.model.Publicidad;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by esva on 12/04/17.
 */

public interface ConvocatoriaAPI {

    @GET("convocatorias")
    Call<Response<ArrayList<Convocatoria>>> get(

    );
}
