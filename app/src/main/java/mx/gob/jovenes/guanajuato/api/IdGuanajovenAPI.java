package mx.gob.jovenes.guanajuato.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IdGuanajovenAPI {

    @GET("documentos/pdf/idguanajoven")
    Call<Response<Boolean>> enviarCorreoIDGuanajoven (
            @Query("api_token") String apiToken
    );
}
