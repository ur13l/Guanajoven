package mx.gob.jovenes.guanajuato.api;

import mx.gob.jovenes.guanajuato.model.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by code on 7/03/17.
 */

public interface UsuarioAPI {

    @POST("usuarios/login")
    Call<Response<Usuario>> login(
            @Query("email") String email,
            @Query("password") String password
    );
}
