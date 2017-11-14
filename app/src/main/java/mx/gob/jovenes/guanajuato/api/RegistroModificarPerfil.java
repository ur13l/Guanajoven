package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.DatosModificarPerfil;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistroModificarPerfil {

    @POST("profile/update")
    Call<Response<Boolean>> postModificarPerfil (
            @Body Object request
    );

    @POST("profile/get")
    Call<Response<DatosModificarPerfil>> getModificarPerfil (
            @Query("api_token") String apiToken
    );

}
