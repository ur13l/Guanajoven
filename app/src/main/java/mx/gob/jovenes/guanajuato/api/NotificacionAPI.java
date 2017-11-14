package mx.gob.jovenes.guanajuato.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificacionAPI {

    @POST("notificaciones/enviartoken")
    Call<Response<Boolean>> enviarToken (
            @Query("device_token") String deviceToken,
            @Query("id_usuario") int idUsuario,
            @Query("os") String os
    );

    @POST("notificaciones/cancelartoken")
    Call<Response<Boolean>> cancelarToken (
            @Query("id_usuario") int idUsuario
    );

}
