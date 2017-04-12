package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Publicidad;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Uriel on 10/04/2017.
 */

public interface PublicidadAPI {

    @POST("publicidad/get")
    Call<Response<ArrayList<Publicidad>>> get(



    );
}
