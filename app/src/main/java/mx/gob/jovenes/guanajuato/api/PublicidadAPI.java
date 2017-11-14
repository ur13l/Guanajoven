package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Publicidad;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublicidadAPI {

    @GET("publicidad")
    Call<Response<ArrayList<Publicidad>>> get(
            @Query("timestamp") String timestamp
    );
}
