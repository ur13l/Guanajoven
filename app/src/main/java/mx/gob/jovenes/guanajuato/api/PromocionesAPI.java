package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Empresa;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by codigus on 17/07/2017.
 */

public interface PromocionesAPI {

    //obtiene las promociones
    @GET("promociones")
    Call<Response<ArrayList<Empresa>>> getEmpresas (
            @Query("timestamp") String timeStamp
    );

    @GET("promociones")
    Call<Response<ArrayList<Empresa>>> getPromociones (
            @Query("timestamp") String timeStamp
    );

}
