package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Empresa;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PromocionesAPI {

    @GET("promociones")
    Call<Response<ArrayList<Empresa>>> getEmpresas (
            @Query("timestamp") String timeStamp
    );

    @POST("promociones/registrar")
    Call<Response<Boolean>> registrar (
            @Query("token") String token,
            @Query("id_promocion") int idPromocion
    );

}
