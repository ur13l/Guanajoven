package mx.gob.jovenes.guanajuato.api;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Evento;
import mx.gob.jovenes.guanajuato.model.EventoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by codigus on 11/5/2017.
 */

public interface EventoAPI {

    @GET("eventos")
    Call<Response<ArrayList<Evento>>> obtenerEventos(
            @Query("timestamp") String timestamp
    );

    @POST("eventos/marcar")
    Call<Response<EventoResponse>> marcarEvento (
            @Query("id_evento") int idEvento,
            @Query("api_token") String apiToken,
            @Query("latitud") double latitud,
            @Query("longitud") double longitud
    );

    @POST("notificaciones/evento")
    Call<Response<Boolean>> enviarCorreo (
            @Query("id_usuario") int idUsuario,
            @Query("id_evento") int idvento
    );
}
