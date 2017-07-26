package mx.gob.jovenes.guanajuato.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by codigus on 10/5/2017.
 */

public interface NotificacionAPI {

    //registrar el token par que llegen las notificaciones
    @POST("notificaciones/enviartoken")
    Call<Response<Boolean>> enviarToken (
            @Query("device_token") String deviceToken,
            @Query("id_usuario") int idUsuario,
            @Query("os") String os
    );

    //método para cancelar el token y dejen de llegar notificaciones
    @POST("notificaciones/cancelartoken")
    Call<Response<Boolean>> cancelarToken (
            @Query("id_usuario") int idUsuario
    );

}
