package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Notificacion;
import retrofit2.Call;
import retrofit2.http.GET;
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
            @Query("so") String so
    );

    //método para cancelar el token y dejen de llegar notificaciones
    @GET("notificaciones/cancelartoken")
    Call<Response<Boolean>> cancelarToken (
            @Query("device_token") String deviceToken
    );

}
