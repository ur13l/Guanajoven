package mx.gob.jovenes.guanajuato.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by codigus on 12/06/2017.
 */

public interface EnviarCorreoAPI {

    @POST("notificaciones/convocatoria")
    Call<Response<Boolean>> enviarCorreo (
            @Query("id_usuario") int idUsuario,
            @Query("id_convocatoria") int idConvocatoria
    );

}
