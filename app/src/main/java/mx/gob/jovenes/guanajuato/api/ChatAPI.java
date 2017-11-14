package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.DatosMensajes;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatAPI {

    @POST("chat/enviar")
    Call<Response<Boolean>> enviarMensaje (
            @Query("api_token") String apiToken,
            @Query("mensaje") String mensaje
    );

    @POST("chat/mensajes")
    Call<Response<DatosMensajes>> obtenerMensajes (
            @Query("api_token") String apiToken,
            @Query("page") int page
    );
}
